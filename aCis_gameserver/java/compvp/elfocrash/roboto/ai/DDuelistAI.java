package compvp.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.ai.addon.IIConsumableSpender;
import compvp.elfocrash.roboto.helpers.FFakeHelpers;
import compvp.elfocrash.roboto.model.HHealingSpell;
import compvp.elfocrash.roboto.model.OOffensiveSpell;
import compvp.elfocrash.roboto.model.SSpellUsageCondition;
import compvp.elfocrash.roboto.model.SSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

public class DDuelistAI extends CCombatAI implements IIConsumableSpender {
	
	public DDuelistAI(FFakePlayer character) {
		super(character);
	}

	@Override
	public void thinkAndAct() {
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();
		handleShots();
		selfSupportBuffs();
		tryTargetRandomCreatureByTypeInRadius(FFakeHelpers.getTestTargetClass(), FFakeHelpers.getTestTargetRange());		
		tryAttackingUsingFighterOffensiveSkill();
		setBusyThinking(false);
	}

	@Override
	protected ShotType getShotType() {
		return ShotType.SOULSHOT;
	}	
	
	@Override
	protected double changeOfUsingSkill() {
		return 0.2;
	}

	@Override
	protected List<OOffensiveSpell> getOffensiveSpells() {
		List<OOffensiveSpell> _offensiveSpells = new ArrayList<>();
		return _offensiveSpells;
	}
	
	@Override
	protected List<SSupportSpell> getSelfSupportSpells() {
		List<SSupportSpell> _selfSupportSpells = new ArrayList<>();
		_selfSupportSpells.add(new SSupportSpell(139, 1));
		_selfSupportSpells.add(new SSupportSpell(297, 2));
		_selfSupportSpells.add(new SSupportSpell(440, SSpellUsageCondition.MISSINGCP, 1000, 3));
		return _selfSupportSpells;
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFakeHelpers.getFighterBuffs();
	}
	
	@Override
	protected List<HHealingSpell> getHealingSpells()
	{		
		return Collections.emptyList();
	}

}
