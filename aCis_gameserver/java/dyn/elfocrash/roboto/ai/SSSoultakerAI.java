package dyn.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.ai.addon.IIIConsumableSpender;
import dyn.elfocrash.roboto.helpers.FFFakeHelpers;
import dyn.elfocrash.roboto.model.HHHealingSpell;
import dyn.elfocrash.roboto.model.OOOffensiveSpell;
import dyn.elfocrash.roboto.model.SSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class SSSoultakerAI extends CCCombatAI implements IIIConsumableSpender
{
	private final int boneId = 2508;
	
	public SSSoultakerAI(FFFakePlayer character)
	{
		super(character);		
	}
	
	@Override
	public void thinkAndAct()
	{
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();
		handleConsumable(_fffakePlayer, boneId);		
		handleShots();		
		tryTargetRandomCreatureByTypeInRadius(FFFakeHelpers.getTestTargetClass(), FFFakeHelpers.getTestTargetRange());		
		tryAttackingUsingMageOffensiveSkill();
		setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.BLESSED_SPIRITSHOT;
	}
	
	@Override
	protected List<OOOffensiveSpell> getOffensiveSpells()
	{
		List<OOOffensiveSpell> _offensiveSpells = new ArrayList<>();
		_offensiveSpells.add(new OOOffensiveSpell(1234, 1));
		_offensiveSpells.add(new OOOffensiveSpell(1148, 2));
		_offensiveSpells.add(new OOOffensiveSpell(1343, 3));
		return _offensiveSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFFakeHelpers.getMageBuffs();
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
