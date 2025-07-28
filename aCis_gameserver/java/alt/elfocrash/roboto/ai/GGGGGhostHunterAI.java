package alt.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.helpers.FFFFFakeHelpers;
import alt.elfocrash.roboto.model.HHHHHealingSpell;
import alt.elfocrash.roboto.model.OOOOOffensiveSpell;
import alt.elfocrash.roboto.model.SSSSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class GGGGGhostHunterAI extends CCCCCombatAI
{
	public GGGGGhostHunterAI(FFFFFakePlayer character)
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
		tryTargetRandomCreatureByTypeInRadius(FFFFFakeHelpers.getTestTargetClass(), FFFFFakeHelpers.getTestTargetRange());		
		tryAttackingUsingFighterOffensiveSkill();
		setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.SOULSHOT;
	}
	
	@Override
	public List<OOOOOffensiveSpell> getOffensiveSpells()
	{
		List<OOOOOffensiveSpell> _offensiveSpells = new ArrayList<>();	
		return _offensiveSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFFFFakeHelpers.getFighterBuffs();
	}
	
	@Override
	protected List<HHHHHealingSpell> getHealingSpells()
	{		
		return Collections.emptyList();
	}

	@Override
	protected List<SSSSSupportSpell> getSelfSupportSpells() {
		return Collections.emptyList();
	}
}
