package alt.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.ai.addon.IIIIIConsumableSpender;
import alt.elfocrash.roboto.helpers.FFFFFakeHelpers;
import alt.elfocrash.roboto.model.HHHHHealingSpell;
import alt.elfocrash.roboto.model.OOOOOffensiveSpell;
import alt.elfocrash.roboto.model.SSSSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class GGGGGhostSentinelAI extends CCCCCombatAI implements IIIIIConsumableSpender
{

	public GGGGGhostSentinelAI(FFFFFakePlayer character)
	{
		super(character);
	}
	
	@Override
	public void thinkAndAct()
	{
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();
		handleConsumable(_fffffakePlayer, getArrowId());
		selfSupportBuffs();
		handleShots();		
		tryTargetRandomCreatureByTypeInRadius(FFFFFakeHelpers.getTestTargetClass(), FFFFFakeHelpers.getTestTargetRange());		
		tryAttackingUsingFighterOffensiveSkill();
		setBusyThinking(false);
	}
	
	@Override
	protected double changeOfUsingSkill() {
		return 0.2;
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.SOULSHOT;
	}
	
	@Override
	protected List<OOOOOffensiveSpell> getOffensiveSpells()
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
		List<SSSSSupportSpell> _selfSupportSpells = new ArrayList<>();
		_selfSupportSpells.add(new SSSSSupportSpell(99, 1));
		return _selfSupportSpells;
	}
}
