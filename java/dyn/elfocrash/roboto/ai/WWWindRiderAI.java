package dyn.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.helpers.FFFakeHelpers;
import dyn.elfocrash.roboto.model.HHHealingSpell;
import dyn.elfocrash.roboto.model.OOOffensiveSpell;
import dyn.elfocrash.roboto.model.SSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class WWWindRiderAI extends CCCombatAI
{
	public WWWindRiderAI(FFFakePlayer character)
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
		tryTargetRandomCreatureByTypeInRadius(FFFakeHelpers.getTestTargetClass(), FFFakeHelpers.getTestTargetRange());		
		tryAttackingUsingFighterOffensiveSkill();
		setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.SOULSHOT;
	}
	
	@Override
	protected List<OOOffensiveSpell> getOffensiveSpells()
	{
		List<OOOffensiveSpell> _offensiveSpells = new ArrayList<>();

		return _offensiveSpells; 
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
	
	@Override
	protected List<SSSupportSpell> getSelfSupportSpells() {
		return Collections.emptyList();
	}
}
