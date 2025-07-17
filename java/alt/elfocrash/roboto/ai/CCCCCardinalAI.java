package alt.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.ai.addon.IIIIIHealer;
import alt.elfocrash.roboto.helpers.FFFFFakeHelpers;
import alt.elfocrash.roboto.model.HHHHHealingSpell;
import alt.elfocrash.roboto.model.OOOOOffensiveSpell;
import alt.elfocrash.roboto.model.SSSSSupportSpell;

import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class CCCCCardinalAI extends CCCCCombatAI implements IIIIIHealer
{
	public CCCCCardinalAI(FFFFFakePlayer character)
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
		tryTargetingLowestHpTargetInRadius(_fffffakePlayer, FFFFFakePlayer.class, FFFFFakeHelpers.getTestTargetRange());
		tryHealingTarget(_fffffakePlayer);
		setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.BLESSED_SPIRITSHOT;
	}
	
	@Override
	protected List<OOOOOffensiveSpell> getOffensiveSpells()
	{		
		return Collections.emptyList();
	}
	
	@Override
	protected List<HHHHHealingSpell> getHealingSpells()
	{		
		List<HHHHHealingSpell> _healingSpells = new ArrayList<>();
		_healingSpells.add(new HHHHHealingSpell(1218, SkillTargetType.TARGET_ONE, 70, 1));		
		_healingSpells.add(new HHHHHealingSpell(1217, SkillTargetType.TARGET_ONE, 60, 3));
		return _healingSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFFFFakeHelpers.getMageBuffs();
	}	

	@Override
	protected List<SSSSSupportSpell> getSelfSupportSpells() {
		return Collections.emptyList();
	}
}
