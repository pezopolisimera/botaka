package compvp.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.ai.addon.IIConsumableSpender;
import compvp.elfocrash.roboto.helpers.FFakeHelpers;
import compvp.elfocrash.roboto.model.HHealingSpell;
import compvp.elfocrash.roboto.model.OOffensiveSpell;
import compvp.elfocrash.roboto.model.SSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class SSaggitariusAI extends CCombatAI implements IIConsumableSpender
{

	public SSaggitariusAI(FFakePlayer character)
	{
		super(character);
	}
	
	@Override
	public void thinkAndAct()
	{		
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();
		selfSupportBuffs();
		handleConsumable(_ffakePlayer, getArrowId());
		handleShots();		
		tryTargetRandomCreatureByTypeInRadius(FFakeHelpers.getTestTargetClass(), FFakeHelpers.getTestTargetRange());
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
	protected List<OOffensiveSpell> getOffensiveSpells()
	{
		List<OOffensiveSpell> _offensiveSpells = new ArrayList<>();
		return _offensiveSpells;
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

	@Override
	protected List<SSupportSpell> getSelfSupportSpells() {
		List<SSupportSpell> _selfSupportSpells = new ArrayList<>();
		_selfSupportSpells.add(new SSupportSpell(99, 1));
		return _selfSupportSpells;
	}
}
