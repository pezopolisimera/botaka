package dyn.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.ai.addon.IIIConsumableSpender;
import dyn.elfocrash.roboto.helpers.FFFakeHelpers;
import dyn.elfocrash.roboto.model.HHHealingSpell;
import dyn.elfocrash.roboto.model.OOOffensiveSpell;
import dyn.elfocrash.roboto.model.SSSpellUsageCondition;
import dyn.elfocrash.roboto.model.SSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

public class DDDreadnoughtAI extends CCCombatAI implements IIIConsumableSpender {
	
	public DDDreadnoughtAI(FFFakePlayer character) {
		super(character);
	}

	@Override
	public void thinkAndAct() {		
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();
		handleShots();
		selfSupportBuffs();
		tryTargetRandomCreatureByTypeInRadius(FFFakeHelpers.getTestTargetClass(), FFFakeHelpers.getTestTargetRange());		
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
	protected List<OOOffensiveSpell> getOffensiveSpells() {
		List<OOOffensiveSpell> _offensiveSpells = new ArrayList<>();		
		return _offensiveSpells;
	}
	
	@Override
	protected List<SSSupportSpell> getSelfSupportSpells() {
		List<SSSupportSpell> _selfSupportSpells = new ArrayList<>();
		_selfSupportSpells.add(new SSSupportSpell(440, SSSpellUsageCondition.MISSINGCP, 1000, 1));
		return _selfSupportSpells;
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFFakeHelpers.getFighterBuffs();
	}
	
	@Override
	protected List<HHHealingSpell> getHealingSpells()
	{		
		return Collections.emptyList();
	}

}
