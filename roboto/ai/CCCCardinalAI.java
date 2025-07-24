package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.ai.addon.IIIIHealer;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.instance.Player;

/**
 * @author Elfocrash
 *
 */
public class CCCCardinalAI extends CCCCombatAI implements IIIIHealer
{
	    /**
	     * Called when this bot receives a private message from a player.
	     */
	    public void onPrivateMessage(String fromPlayerName, String messageText) {
	        _ffffakePlayer.onPrivateMessageReceived(fromPlayerName, messageText);
	    }

	public CCCCardinalAI(FFFFakePlayer character)
	{
		super(character);
	}
	
	@Override
	public void thinkAndAct()
	{
	    super.thinkAndAct();
	    setBusyThinking(true);
	    applyDefaultBuffs();
	    handleShots();

	    long thinkStart = System.currentTimeMillis();

	    Player ally = _ffffakePlayer.getKnownType(Player.class).stream()
	        .filter(p -> !p.isDead())
	        .filter(p -> isSameClanOrAlliance(p))
	        .filter(p -> _ffffakePlayer.isInsideRadius(p, 1200, true, false))
	        .findFirst().orElse(null);

	    if (ally != null) {
	        _ffffakePlayer.setTarget(ally);
	       
	        while (System.currentTimeMillis() - thinkStart < 2000) {
	        	    if (!_ffffakePlayer.isInsideRadius(ally, 150, false, false)) {
	        	         _ffffakePlayer.getFFFFakeAi().moveTo(ally.getX(), ally.getY(), ally.getZ());
	        	     }
	        	    if (ally.getCurrentHp() < ally.getMaxHp() * 0.6) {
	        	         _ffffakePlayer.setTarget(ally);
	        	         for (HHHHealingSpell heal : getHealingSpells()) {
	        	        	 L2Skill skill = SkillTable.getInstance().getInfo(heal.getSkillId(), heal.getSkillLevel());

	        	        	    if (skill != null && !skill.isOffensive()) {
	        	        	        castSpell(skill);
	        	        	        break;
	        	        	    }
	        	        	}

	        	     }
	        	 }
	        	 } // <-- Κλείσιμο του if (ally != null)

	        	 tryTargetingLowestHpTargetInRadius(_ffffakePlayer, FFFFakePlayer.class, FFFFakeHelpers.getTestTargetRange());
	        	 tryHealingTarget(_ffffakePlayer);
	        	 setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.BLESSED_SPIRITSHOT;
	}
	
	@Override
	protected List<OOOOffensiveSpell> getOffensiveSpells()
	{		
		return Collections.emptyList();
	}
	
	@Override
	protected List<HHHHealingSpell> getHealingSpells()
	{		
		List<HHHHealingSpell> _healingSpells = new ArrayList<>();
		_healingSpells.add(new HHHHealingSpell(1218, SkillTargetType.TARGET_ONE, 60, 1));		
		_healingSpells.add(new HHHHealingSpell(1217, SkillTargetType.TARGET_ONE, 60, 3));
		return _healingSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFFFakeHelpers.getMageBuffs();
	}	

	@Override
	protected List<SSSSupportSpell> getSelfSupportSpells() {
		return Collections.emptyList();
	}
}
