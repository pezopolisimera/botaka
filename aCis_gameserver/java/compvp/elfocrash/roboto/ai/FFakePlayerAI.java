package compvp.elfocrash.roboto.ai;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import compvp.elfocrash.roboto.FFakePlayer;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.xml.MapRegionData;
import net.sf.l2j.gameserver.data.xml.MapRegionData.TeleportType;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.serverpackets.MoveToLocation;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.StopMove;
import net.sf.l2j.gameserver.network.serverpackets.StopRotation;
import net.sf.l2j.gameserver.network.serverpackets.TeleportToLocation;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author Elfocrash
 *
 */
public abstract class FFakePlayerAI
{
	protected final FFakePlayer _ffakePlayer;		
	protected volatile boolean _clientMoving;
	protected volatile boolean _clientAutoAttacking;
	private long _moveToPawnTimeout;
	protected int _clientMovingToPawnOffset;	
	protected boolean _isBusyThinking = false;
	protected int iterationsOnDeath = 0;
	private final int toVillageIterationsOnDeath = 5;
	
	public FFakePlayerAI(FFakePlayer character)
	{
		_ffakePlayer = character;
		setup();
		applyDefaultBuffs();
	}
	
	public void setup() {
		_ffakePlayer.setIsRunning(true);
	}
	
	protected void applyDefaultBuffs() {
		for(int[] buff : getBuffs()){
			try {
				Map<Integer, L2Effect> activeEffects = Arrays.stream(_ffakePlayer.getAllEffects())
						.filter(x->x.getEffectType() == L2EffectType.BUFF)
					.collect(Collectors.toMap(x-> x.getSkill().getId(), x->x));
			
			if(!activeEffects.containsKey(buff[0]))
				SkillTable.getInstance().getInfo(buff[0], buff[1]).getEffects(_ffakePlayer, _ffakePlayer);
			else {
				if((activeEffects.get(buff[0]).getPeriod() - activeEffects.get(buff[0]).getTime()) <= 20) {
					SkillTable.getInstance().getInfo(buff[0], buff[1]).getEffects(_ffakePlayer, _ffakePlayer);
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	protected void handleDeath() {
		if(_ffakePlayer.isDead()) {
			if(iterationsOnDeath >= toVillageIterationsOnDeath) {
				toVillageOnDeath();
			}
			iterationsOnDeath++;
			return;
		}
		
		iterationsOnDeath = 0;		
	}
	
	public void setBusyThinking(boolean thinking) {
		_isBusyThinking = thinking;
	}
	
	public boolean isBusyThinking() {
		return _isBusyThinking;
	}
	
	public void teleportToLocation(int x, int y, int z, int randomOffset) {
		_ffakePlayer.stopMove(null);
		_ffakePlayer.abortAttack();
		_ffakePlayer.abortCast();		
		_ffakePlayer.setIsTeleporting(true);
		_ffakePlayer.setTarget(null);		
		_ffakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);		
		if (randomOffset > 0)
		{
			x += Rnd.get(-randomOffset, randomOffset);
			y += Rnd.get(-randomOffset, randomOffset);
		}		
		z += 5;
		_ffakePlayer.broadcastPacket(new TeleportToLocation(_ffakePlayer, x, y, z));
		_ffakePlayer.decayMe();		
		_ffakePlayer.setXYZ(x, y, z);
		_ffakePlayer.onTeleported();		
		_ffakePlayer.revalidateZone(true);
	}
	
	protected void tryTargetRandomCreatureByTypeInRadius(Class<? extends Creature> creatureClass, int radius)
	{
		if(_ffakePlayer.getTarget() == null) {
			List<Creature> targets = _ffakePlayer.getKnownTypeInRadius(creatureClass, radius).stream().filter(x->!x.isDead()
					/*
					 * cia atsifiltruosim zaidejus kurie turi karmos arba flagai
					 */
					&& x instanceof Player
					&& (((Player)x).getPvpFlag() != 0 || ((Player)x).getKarma() > 0)
					&& !Creature.isInsidePeaceZone(_ffakePlayer, x)
					&& !((Player) x).isGM()
					).collect(Collectors.toList());
			if(!targets.isEmpty()) {
				Creature target = targets.get(Rnd.get(0, targets.size() -1 ));
				_ffakePlayer.setTarget(target);				
			}
		}else {
			if ( ((Creature)_ffakePlayer.getTarget()).isDead() ||
				(_ffakePlayer.getTarget() instanceof Player ? ((Player) _ffakePlayer.getTarget()).getPvpFlag() <= 0 : false) )
			_ffakePlayer.setTarget(null);
		}	
	}	
		
	public void castSpell(L2Skill skill) {
		if(!_ffakePlayer.isCastingNow()) {		
			
			if (skill.getTargetType() == SkillTargetType.TARGET_GROUND)
			{
				if (maybeMoveToPosition((_ffakePlayer).getCurrentSkillWorldPosition(), skill.getCastRange()))
				{
					_ffakePlayer.setIsCastingNow(false);
					return;
				}
			}
			else
			{
				if (checkTargetLost(_ffakePlayer.getTarget()))
				{
					if (skill.isOffensive() && _ffakePlayer.getTarget() != null)
						_ffakePlayer.setTarget(null);
					
					_ffakePlayer.setIsCastingNow(false);
					return;
				}
				
				if (_ffakePlayer.getTarget() != null)
				{
					if(maybeMoveToPawn(_ffakePlayer.getTarget(), skill.getCastRange())) {
						return;
					}
				}
				
				if (_ffakePlayer.isSkillDisabled(skill)) {
					return;
				}					
			}
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_ffakePlayer.doCast(skill);
		}else {
			_ffakePlayer.forceAutoAttack((Creature)_ffakePlayer.getTarget());
		}
	}
	
	protected void castSelfSpell(L2Skill skill) {
		if(!_ffakePlayer.isCastingNow() && !_ffakePlayer.isSkillDisabled(skill)) {		
			
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_ffakePlayer.doCast(skill);
		}
	}
	
	protected void toVillageOnDeath() {
		Location location = MapRegionData.getInstance().getLocationToTeleport(_ffakePlayer, TeleportType.TOWN);
		
		if (_ffakePlayer.isDead())
			_ffakePlayer.doRevive();
		
		_ffakePlayer.getFFakeAi().teleportToLocation(location.getX(), location.getY(), location.getZ(), 0);
	}
	
	protected void clientStopMoving(SpawnLocation loc)
	{
		if (_ffakePlayer.isMoving())
			_ffakePlayer.stopMove(loc);
		
		_clientMovingToPawnOffset = 0;
		
		if (_clientMoving || loc != null)
		{
			_clientMoving = false;
			
			_ffakePlayer.broadcastPacket(new StopMove(_ffakePlayer));
			
			if (loc != null)
				_ffakePlayer.broadcastPacket(new StopRotation(_ffakePlayer.getObjectId(), loc.getHeading(), 0));
		}
	}
	
	protected boolean checkTargetLost(WorldObject target)
	{
		if (target instanceof Player)
		{
			final Player victim = (Player) target;
			if (victim.isFakeDeath())
			{
				victim.stopFakeDeath(true);
				return false;
			}
		}
		
		if (target == null)
		{
			_ffakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);
			return true;
		}
		return false;
	}
	
	protected boolean maybeMoveToPosition(Location worldPosition, int offset)
	{
		if (worldPosition == null)
		{
			return false;
		}
		
		if (offset < 0)
			return false;
			
		if (!_ffakePlayer.isInsideRadius(worldPosition.getX(), worldPosition.getY(), (int) (offset + _ffakePlayer.getCollisionRadius()), false))
		{
			if (_ffakePlayer.isMovementDisabled())
				return true;
			
			int x = _ffakePlayer.getX();
			int y = _ffakePlayer.getY();
			
			double dx = worldPosition.getX() - x;
			double dy = worldPosition.getY() - y;
			
			double dist = Math.sqrt(dx * dx + dy * dy);
			
			double sin = dy / dist;
			double cos = dx / dist;
			
			dist -= offset - 5;
			
			x += (int) (dist * cos);
			y += (int) (dist * sin);
			
			moveTo(x, y, worldPosition.getZ());
			return true;
		}

		return false;
	}	
	
	protected void moveToPawn(WorldObject pawn, int offset)
	{
		if (!_ffakePlayer.isMovementDisabled())
		{
			if (offset < 10)
				offset = 10;
			
			boolean sendPacket = true;
			if (_clientMoving && (_ffakePlayer.getTarget() == pawn))
			{
				if (_clientMovingToPawnOffset == offset)
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout)
						return;
					
					sendPacket = false;
				}
				else if (_ffakePlayer.isOnGeodataPath())
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout + 1000)
						return;
				}
			}
			
			_clientMoving = true;
			_clientMovingToPawnOffset = offset;
			_ffakePlayer.setTarget(pawn);
			_moveToPawnTimeout = System.currentTimeMillis() + 1000;
			
			if (pawn == null)
				return;
			
			_ffakePlayer.moveToLocation(pawn.getX(), pawn.getY(), pawn.getZ(), offset);
			
			if (!_ffakePlayer.isMoving())
			{
				return;
			}
			
			if (pawn instanceof Creature)
			{
				if (_ffakePlayer.isOnGeodataPath())
				{
					_ffakePlayer.broadcastPacket(new MoveToLocation(_ffakePlayer));
					_clientMovingToPawnOffset = 0;
				}
				else if (sendPacket)
					_ffakePlayer.broadcastPacket(new MoveToPawn(_ffakePlayer, pawn, offset));
			}
			else
				_ffakePlayer.broadcastPacket(new MoveToLocation(_ffakePlayer));
		}
	}
	
	public void moveTo(int x, int y, int z)	{
		
		if (!_ffakePlayer.isMovementDisabled())
		{
			_clientMoving = true;
			_clientMovingToPawnOffset = 0;
			_ffakePlayer.moveToLocation(x, y, z, 0);
			
			_ffakePlayer.broadcastPacket(new MoveToLocation(_ffakePlayer));
			
		}
	}
	
	protected boolean maybeMoveToPawn(WorldObject target, int offset) {
		
		if (target == null || offset < 0)
			return false;
		
		offset += _ffakePlayer.getCollisionRadius();
		if (target instanceof Creature)
			offset += ((Creature) target).getCollisionRadius();
		
		if (!_ffakePlayer.isInsideRadius(target, offset, false, false))
		{			
			if (_ffakePlayer.isMovementDisabled())
			{
				if (_ffakePlayer.getAI().getIntention() == CtrlIntention.ATTACK)
					_ffakePlayer.getAI().setIntention(CtrlIntention.IDLE);				
				return true;
			}
			
			if (target instanceof Creature && !(target instanceof Door))
			{
				if (((Creature) target).isMoving())
					offset -= 30;
				
				if (offset < 5)
					offset = 5;
			}
			
			moveToPawn(target, offset);
			
			return true;
		}
		
		if(!GeoEngine.getInstance().canSeeTarget(_ffakePlayer, _ffakePlayer.getTarget())){
			_ffakePlayer.setIsCastingNow(false);
			moveToPawn(target, 50);			
			return true;
		}
		
		
		return false;
	}	
	
	public abstract void thinkAndAct(); 
	protected abstract int[][] getBuffs();
}
