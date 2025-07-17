package compvp.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.ai.addon.IIHealer;
import compvp.elfocrash.roboto.helpers.FFakeHelpers;
import compvp.elfocrash.roboto.model.HHealingSpell;
import compvp.elfocrash.roboto.model.OOffensiveSpell;
import compvp.elfocrash.roboto.model.SSupportSpell;

import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class CCardinalAI extends CCombatAI implements IIHealer
{
	public CCardinalAI(FFakePlayer character)
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
		tryTargetingLowestHpTargetInRadius(_ffakePlayer, FFakePlayer.class, FFakeHelpers.getTestTargetRange());
		tryHealingTarget(_ffakePlayer);
		setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.BLESSED_SPIRITSHOT;
	}
	
	@Override
	protected List<OOffensiveSpell> getOffensiveSpells()
	{		
		return Collections.emptyList();
	}
	
	@Override
	protected List<HHealingSpell> getHealingSpells()
	{		
		List<HHealingSpell> _healingSpells = new ArrayList<>();
		_healingSpells.add(new HHealingSpell(1218, SkillTargetType.TARGET_ONE, 70, 1));		
		_healingSpells.add(new HHealingSpell(1217, SkillTargetType.TARGET_ONE, 60, 3));
		return _healingSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFakeHelpers.getMageBuffs();
	}	

	@Override
	protected List<SSupportSpell> getSelfSupportSpells() {
		return Collections.emptyList();
	}
}
