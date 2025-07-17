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
public class SSSSSoultakerAI extends CCCCCombatAI implements IIIIIConsumableSpender
{
	private final int boneId = 2508;
	
	public SSSSSoultakerAI(FFFFFakePlayer character)
	{
		super(character);		
	}
	
	@Override
	public void thinkAndAct()
	{
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();
		handleConsumable(_fffffakePlayer, boneId);		
		handleShots();		
		tryTargetRandomCreatureByTypeInRadius(FFFFFakeHelpers.getTestTargetClass(), FFFFFakeHelpers.getTestTargetRange());		
		tryAttackingUsingMageOffensiveSkill();
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
		List<OOOOOffensiveSpell> _offensiveSpells = new ArrayList<>();
		_offensiveSpells.add(new OOOOOffensiveSpell(1234, 1));
		_offensiveSpells.add(new OOOOOffensiveSpell(1148, 2));
		_offensiveSpells.add(new OOOOOffensiveSpell(1343, 3));
		return _offensiveSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFFFFakeHelpers.getMageBuffs();
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
