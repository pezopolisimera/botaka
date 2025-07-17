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

import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;

/**
 * @author Elfocrash
 *
 */
public class CCCCardinalAI extends CCCCombatAI implements IIIIHealer {
	    private boolean performClanHealingLogic() {
		        List<Player> clanmates = _ffffakePlayer.getKnownType(Player.class).stream()
		            .filter(p -> p != null && p.getClan() == _ffffakePlayer.getClan() && !p.isDead())
		            .filter(p -> p.getCurrentHp() / p.getMaxHp() < 0.5)
		            .toList();
		
		        for (Player ally : clanmates) {
		            for (HHHHealingSpell healSpell : getHealingSpells()) {
		                L2Skill skill = SkillTable.getInstance().getInfo(healSpell.getSkillId(), healSpell.getLevel());
		                if (skill != null && !_ffffakePlayer.isSkillDisabled(skill) && _ffffakePlayer.canCast(skill, ally)) {
		                    _ffffakePlayer.setTarget(ally);
		                    if (_ffffakePlayer.getFFFFakeAi().maybeMoveToPawn(ally, skill.getCastRange())) return true;
		                    _ffffakePlayer.getFFFFakeAi().castSpell(skill);
		                    return true; // Heal μόνο έναν κάθε κύκλο
		                }
		            }
		        }
		        return false; // Δεν έγινε κανένα heal
		    }

		    private void performEscortLogic() {
		        List<Player> clanmates = _ffffakePlayer.getKnownType(Player.class).stream()
		            .filter(p -> p != null && p != _ffffakePlayer && p.getClan() == _ffffakePlayer.getClan() && !p.isDead())
		            .toList();

		        if (clanmates.isEmpty())
		            return;

		        Player escortTarget = clanmates.get(0); // Προς το παρόν, απλός επιλογή πρώτου

		        _ffffakePlayer.setTarget(escortTarget);
		        _ffffakePlayer.getFFFFakeAi().maybeMoveToPawn(escortTarget, 100); // Κρατιέται κοντά του
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
		           if (!performClanHealingLogic()) {
		    	          performEscortLogic();
		    	      }
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
