package alt.elfocrash.roboto.ai;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import alt.elfocrash.roboto.FFFFFakePlayer;

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
public abstract class FFFFFakePlayerAI
{
	protected final FFFFFakePlayer _fffffakePlayer;		
	protected volatile boolean _clientMoving;
	protected volatile boolean _clientAutoAttacking;
	private long _moveToPawnTimeout;
	protected int _clientMovingToPawnOffset;	
	protected boolean _isBusyThinking = false;
	protected int iterationsOnDeath = 0;
	private final int toVillageIterationsOnDeath = 5;
	
	public FFFFFakePlayerAI(FFFFFakePlayer character)
	{
		_fffffakePlayer = character;
		setup();
		applyDefaultBuffs();
	}
	
	public void setup() {
		_fffffakePlayer.setIsRunning(true);
	}
	
	protected void applyDefaultBuffs() {
		for(int[] buff : getBuffs()){
			try {
				Map<Integer, L2Effect> activeEffects = Arrays.stream(_fffffakePlayer.getAllEffects())
						.filter(x->x.getEffectType() == L2EffectType.BUFF)
					.collect(Collectors.toMap(x-> x.getSkill().getId(), x->x));
			
			if(!activeEffects.containsKey(buff[0]))
				SkillTable.getInstance().getInfo(buff[0], buff[1]).getEffects(_fffffakePlayer, _fffffakePlayer);
			else {
				if((activeEffects.get(buff[0]).getPeriod() - activeEffects.get(buff[0]).getTime()) <= 20) {
					SkillTable.getInstance().getInfo(buff[0], buff[1]).getEffects(_fffffakePlayer, _fffffakePlayer);
				}
			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	protected void handleDeath() {
		if(_fffffakePlayer.isDead()) {
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
		_fffffakePlayer.stopMove(null);
		_fffffakePlayer.abortAttack();
		_fffffakePlayer.abortCast();		
		_fffffakePlayer.setIsTeleporting(true);
		_fffffakePlayer.setTarget(null);		
		_fffffakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);		
		if (randomOffset > 0)
		{
			x += Rnd.get(-randomOffset, randomOffset);
			y += Rnd.get(-randomOffset, randomOffset);
		}		
		z += 5;
		_fffffakePlayer.broadcastPacket(new TeleportToLocation(_fffffakePlayer, x, y, z));
		_fffffakePlayer.decayMe();		
		_fffffakePlayer.setXYZ(x, y, z);
		_fffffakePlayer.onTeleported();		
		_fffffakePlayer.revalidateZone(true);
	}
	
	protected void tryTargetRandomCreatureByTypeInRadius(Class<? extends Creature> creatureClass, int radius)
	{
		
		if(_fffffakePlayer.getTarget() == null) {
			List<Creature> targets = _fffffakePlayer.getKnownTypeInRadius(creatureClass, radius).stream().filter(x->!x.isDead()).collect(Collectors.toList());
			if(!targets.isEmpty()) {
				Creature target = targets.get(Rnd.get(0, targets.size() -1 ));
				_fffffakePlayer.setTarget(target);				
			}
		}else {
			if(((Creature)_fffffakePlayer.getTarget()).isDead())
			_fffffakePlayer.setTarget(null);
		}	
	}	
		
	public void castSpell(L2Skill skill) {
		if(!_fffffakePlayer.isCastingNow()) {		
			
			if (skill.getTargetType() == SkillTargetType.TARGET_GROUND)
			{
				if (maybeMoveToPosition((_fffffakePlayer).getCurrentSkillWorldPosition(), skill.getCastRange()))
				{
					_fffffakePlayer.setIsCastingNow(false);
					return;
				}
			}
			else
			{
				if (checkTargetLost(_fffffakePlayer.getTarget()))
				{
					if (skill.isOffensive() && _fffffakePlayer.getTarget() != null)
						_fffffakePlayer.setTarget(null);
					
					_fffffakePlayer.setIsCastingNow(false);
					return;
				}
				
				if (_fffffakePlayer.getTarget() != null)
				{
					if(maybeMoveToPawn(_fffffakePlayer.getTarget(), skill.getCastRange())) {
						return;
					}
				}
				
				if (_fffffakePlayer.isSkillDisabled(skill)) {
					return;
				}					
			}
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_fffffakePlayer.doCast(skill);
		}else {
			_fffffakePlayer.forceAutoAttack((Creature)_fffffakePlayer.getTarget());
		}
	}
	
	protected void castSelfSpell(L2Skill skill) {
		if(!_fffffakePlayer.isCastingNow() && !_fffffakePlayer.isSkillDisabled(skill)) {		
			
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_fffffakePlayer.doCast(skill);
		}
	}
	
	protected void toVillageOnDeath() {
		if (_fffffakePlayer.isDead())
			_fffffakePlayer.doRevive();
		
		_fffffakePlayer.getFFFFFakeAi().teleportToLocation(82599, 148115, -3470, 400);
	}
	
	protected void clientStopMoving(SpawnLocation loc)
	{
		if (_fffffakePlayer.isMoving())
			_fffffakePlayer.stopMove(loc);
		
		_clientMovingToPawnOffset = 0;
		
		if (_clientMoving || loc != null)
		{
			_clientMoving = false;
			
			_fffffakePlayer.broadcastPacket(new StopMove(_fffffakePlayer));
			
			if (loc != null)
				_fffffakePlayer.broadcastPacket(new StopRotation(_fffffakePlayer.getObjectId(), loc.getHeading(), 0));
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
			_fffffakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);
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
			
		if (!_fffffakePlayer.isInsideRadius(worldPosition.getX(), worldPosition.getY(), (int) (offset + _fffffakePlayer.getCollisionRadius()), false))
		{
			if (_fffffakePlayer.isMovementDisabled())
				return true;
			
			int x = _fffffakePlayer.getX();
			int y = _fffffakePlayer.getY();
			
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
		if (!_fffffakePlayer.isMovementDisabled())
		{
			if (offset < 10)
				offset = 10;
			
			boolean sendPacket = true;
			if (_clientMoving && (_fffffakePlayer.getTarget() == pawn))
			{
				if (_clientMovingToPawnOffset == offset)
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout)
						return;
					
					sendPacket = false;
				}
				else if (_fffffakePlayer.isOnGeodataPath())
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout + 1000)
						return;
				}
			}
			
			_clientMoving = true;
			_clientMovingToPawnOffset = offset;
			_fffffakePlayer.setTarget(pawn);
			_moveToPawnTimeout = System.currentTimeMillis() + 1000;
			
			if (pawn == null)
				return;
			
			_fffffakePlayer.moveToLocation(pawn.getX(), pawn.getY(), pawn.getZ(), offset);
			
			if (!_fffffakePlayer.isMoving())
			{
				return;
			}
			
			if (pawn instanceof Creature)
			{
				if (_fffffakePlayer.isOnGeodataPath())
				{
					_fffffakePlayer.broadcastPacket(new MoveToLocation(_fffffakePlayer));
					_clientMovingToPawnOffset = 0;
				}
				else if (sendPacket)
					_fffffakePlayer.broadcastPacket(new MoveToPawn(_fffffakePlayer, pawn, offset));
			}
			else
				_fffffakePlayer.broadcastPacket(new MoveToLocation(_fffffakePlayer));
		}
	}
	
	public void moveTo(int x, int y, int z)	{
		
		if (!_fffffakePlayer.isMovementDisabled())
		{
			_clientMoving = true;
			_clientMovingToPawnOffset = 0;
			_fffffakePlayer.moveToLocation(x, y, z, 0);
			
			_fffffakePlayer.broadcastPacket(new MoveToLocation(_fffffakePlayer));
			
		}
	}
	
	protected boolean maybeMoveToPawn(WorldObject target, int offset) {
		
		if (target == null || offset < 0)
			return false;
		
		offset += _fffffakePlayer.getCollisionRadius();
		if (target instanceof Creature)
			offset += ((Creature) target).getCollisionRadius();
		
		if (!_fffffakePlayer.isInsideRadius(target, offset, false, false))
		{			
			if (_fffffakePlayer.isMovementDisabled())
			{
				if (_fffffakePlayer.getAI().getIntention() == CtrlIntention.ATTACK)
					_fffffakePlayer.getAI().setIntention(CtrlIntention.IDLE);				
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
		
		if(!GeoEngine.getInstance().canSeeTarget(_fffffakePlayer, _fffffakePlayer.getTarget())){
			_fffffakePlayer.setIsCastingNow(false);
			moveToPawn(target, 50);			
			return true;
		}
		
		
		return false;
	}	
	
	public abstract void thinkAndAct(); 
	protected abstract int[][] getBuffs();
}
