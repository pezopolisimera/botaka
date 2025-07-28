package dyn.elfocrash.roboto.ai;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dyn.elfocrash.roboto.FFFakePlayer;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
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
public abstract class FFFakePlayerAI
{
	protected final FFFakePlayer _fffakePlayer;		
	protected volatile boolean _clientMoving;
	protected volatile boolean _clientAutoAttacking;
	private long _moveToPawnTimeout;
	protected int _clientMovingToPawnOffset;	
	protected boolean _isBusyThinking = false;
	protected int iterationsOnDeath = 0;
	private final int toVillageIterationsOnDeath = 5;
	
	public FFFakePlayerAI(FFFakePlayer character)
	{
		_fffakePlayer = character;
		setup();
		applyDefaultBuffs();
	}
	
	public void setup() {
		_fffakePlayer.setIsRunning(true);
	}
	
	protected void applyDefaultBuffs() {
		for(int[] buff : getBuffs()){
			try {
				Map<Integer, L2Effect> activeEffects = Arrays.stream(_fffakePlayer.getAllEffects())
						.filter(x->x.getEffectType() == L2EffectType.BUFF)
					.collect(Collectors.toMap(x-> x.getSkill().getId(), x->x));
			
			if(!activeEffects.containsKey(buff[0]))
				SkillTable.getInstance().getInfo(buff[0], buff[1]).getEffects(_fffakePlayer, _fffakePlayer);
			else {
				if((activeEffects.get(buff[0]).getPeriod() - activeEffects.get(buff[0]).getTime()) <= 20) {
					SkillTable.getInstance().getInfo(buff[0], buff[1]).getEffects(_fffakePlayer, _fffakePlayer);
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	protected void handleDeath() {
		if(_fffakePlayer.isDead()) {
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
		_fffakePlayer.stopMove(null);
		_fffakePlayer.abortAttack();
		_fffakePlayer.abortCast();		
		_fffakePlayer.setIsTeleporting(true);
		_fffakePlayer.setTarget(null);		
		_fffakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);		
		if (randomOffset > 0)
		{
			x += Rnd.get(-randomOffset, randomOffset);
			y += Rnd.get(-randomOffset, randomOffset);
		}		
		z += 5;
		_fffakePlayer.broadcastPacket(new TeleportToLocation(_fffakePlayer, x, y, z));
		_fffakePlayer.decayMe();		
		_fffakePlayer.setXYZ(x, y, z);
		_fffakePlayer.onTeleported();		
		_fffakePlayer.revalidateZone(true);
	}
	
	protected void tryTargetRandomCreatureByTypeInRadius(Class<? extends Creature> creatureClass, int radius)
	{
		if(_fffakePlayer.getTarget() == null) {
			List<Creature> targets = _fffakePlayer.getKnownTypeInRadius(creatureClass, radius).stream().filter(x->!x.isDead()).collect(Collectors.toList());
			if(!targets.isEmpty()) {
				Creature target = targets.get(Rnd.get(0, targets.size() -1 ));
				_fffakePlayer.setTarget(target);				
			}
		}else {
			if(((Creature)_fffakePlayer.getTarget()).isDead())
			_fffakePlayer.setTarget(null);
		}	
	}	
		
	public void castSpell(L2Skill skill) {
		if(!_fffakePlayer.isCastingNow()) {		
			
			if (skill.getTargetType() == SkillTargetType.TARGET_GROUND)
			{
				if (maybeMoveToPosition((_fffakePlayer).getCurrentSkillWorldPosition(), skill.getCastRange()))
				{
					_fffakePlayer.setIsCastingNow(false);
					return;
				}
			}
			else
			{
				if (checkTargetLost(_fffakePlayer.getTarget()))
				{
					if (skill.isOffensive() && _fffakePlayer.getTarget() != null)
						_fffakePlayer.setTarget(null);
					
					_fffakePlayer.setIsCastingNow(false);
					return;
				}
				
				if (_fffakePlayer.getTarget() != null)
				{
					if(maybeMoveToPawn(_fffakePlayer.getTarget(), skill.getCastRange())) {
						return;
					}
				}
				
				if (_fffakePlayer.isSkillDisabled(skill)) {
					return;
				}					
			}
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_fffakePlayer.doCast(skill);
		}else {
			_fffakePlayer.forceAutoAttack((Creature)_fffakePlayer.getTarget());
		}
	}
	
	protected void castSelfSpell(L2Skill skill) {
		if(!_fffakePlayer.isCastingNow() && !_fffakePlayer.isSkillDisabled(skill)) {		
			
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_fffakePlayer.doCast(skill);
		}
	}
	
	protected void toVillageOnDeath() {
		if (_fffakePlayer.isDead())
			_fffakePlayer.doRevive();
		
		_fffakePlayer.getFFFakeAi().teleportToLocation(82653, 148621, -3472, 400);
	}
	
	protected void clientStopMoving(SpawnLocation loc)
	{
		if (_fffakePlayer.isMoving())
			_fffakePlayer.stopMove(loc);
		
		_clientMovingToPawnOffset = 0;
		
		if (_clientMoving || loc != null)
		{
			_clientMoving = false;
			
			_fffakePlayer.broadcastPacket(new StopMove(_fffakePlayer));
			
			if (loc != null)
				_fffakePlayer.broadcastPacket(new StopRotation(_fffakePlayer.getObjectId(), loc.getHeading(), 0));
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
			_fffakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);
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
			
		if (!_fffakePlayer.isInsideRadius(worldPosition.getX(), worldPosition.getY(), (int) (offset + _fffakePlayer.getCollisionRadius()), false))
		{
			if (_fffakePlayer.isMovementDisabled())
				return true;
			
			int x = _fffakePlayer.getX();
			int y = _fffakePlayer.getY();
			
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
		if (!_fffakePlayer.isMovementDisabled())
		{
			if (offset < 10)
				offset = 10;
			
			boolean sendPacket = true;
			if (_clientMoving && (_fffakePlayer.getTarget() == pawn))
			{
				if (_clientMovingToPawnOffset == offset)
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout)
						return;
					
					sendPacket = false;
				}
				else if (_fffakePlayer.isOnGeodataPath())
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout + 1000)
						return;
				}
			}
			
			_clientMoving = true;
			_clientMovingToPawnOffset = offset;
			_fffakePlayer.setTarget(pawn);
			_moveToPawnTimeout = System.currentTimeMillis() + 1000;
			
			if (pawn == null)
				return;
			
			_fffakePlayer.moveToLocation(pawn.getX(), pawn.getY(), pawn.getZ(), offset);
			
			if (!_fffakePlayer.isMoving())
			{
				return;
			}
			
			if (pawn instanceof Creature)
			{
				if (_fffakePlayer.isOnGeodataPath())
				{
					_fffakePlayer.broadcastPacket(new MoveToLocation(_fffakePlayer));
					_clientMovingToPawnOffset = 0;
				}
				else if (sendPacket)
					_fffakePlayer.broadcastPacket(new MoveToPawn(_fffakePlayer, pawn, offset));
			}
			else
				_fffakePlayer.broadcastPacket(new MoveToLocation(_fffakePlayer));
		}
	}
	
	public void moveTo(int x, int y, int z)	{
		
		if (!_fffakePlayer.isMovementDisabled())
		{
			_clientMoving = true;
			_clientMovingToPawnOffset = 0;
			_fffakePlayer.moveToLocation(x, y, z, 0);
			
			_fffakePlayer.broadcastPacket(new MoveToLocation(_fffakePlayer));
			
		}
	}
	
	protected boolean maybeMoveToPawn(WorldObject target, int offset) {
		
		if (target == null || offset < 0)
			return false;
		
		offset += _fffakePlayer.getCollisionRadius();
		if (target instanceof Creature)
			offset += ((Creature) target).getCollisionRadius();
		
		if (!_fffakePlayer.isInsideRadius(target, offset, false, false))
		{			
			if (_fffakePlayer.isMovementDisabled())
			{
				if (_fffakePlayer.getAI().getIntention() == CtrlIntention.ATTACK)
					_fffakePlayer.getAI().setIntention(CtrlIntention.IDLE);				
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
		
		if(!GeoEngine.getInstance().canSeeTarget(_fffakePlayer, _fffakePlayer.getTarget())){
			_fffakePlayer.setIsCastingNow(false);
			moveToPawn(target, 50);			
			return true;
		}
		
		
		return false;
	}	
	
	public abstract void thinkAndAct(); 
	protected abstract int[][] getBuffs();
}
