package com.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.elfocrash.roboto.FakePlayer;
import com.elfocrash.roboto.ai.addon.IHealer;
import com.elfocrash.roboto.helpers.FakeHelpers;
import com.elfocrash.roboto.model.HealingSpell;
import com.elfocrash.roboto.model.OffensiveSpell;
import com.elfocrash.roboto.model.SupportSpell;

import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class CardinalAI extends CombatAI implements IHealer
{
	public CardinalAI(FakePlayer character)
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
		tryTargetingLowestHpTargetInRadius(_fakePlayer, FakePlayer.class, FakeHelpers.getTestTargetRange());
		tryHealingTarget(_fakePlayer);
		setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.BLESSED_SPIRITSHOT;
	}
	
	@Override
	protected List<OffensiveSpell> getOffensiveSpells()
	{		
		return Collections.emptyList();
	}
	
	@Override
	protected List<HealingSpell> getHealingSpells()
	{		
		List<HealingSpell> _healingSpells = new ArrayList<>();
		_healingSpells.add(new HealingSpell(1218, SkillTargetType.TARGET_ONE, 70, 1));		
		_healingSpells.add(new HealingSpell(1217, SkillTargetType.TARGET_ONE, 60, 3));
		return _healingSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FakeHelpers.getMageBuffs();
	}	

	@Override
	protected List<SupportSpell> getSelfSupportSpells() {
		return Collections.emptyList();
	}
}
