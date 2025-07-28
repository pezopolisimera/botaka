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
public class SSoultakerAI extends CCombatAI implements IIConsumableSpender
{
	private final int boneId = 2508;
	
	public SSoultakerAI(FFakePlayer character)
	{
		super(character);		
	}
	
	@Override
	public void thinkAndAct()
	{
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();
		handleConsumable(_ffakePlayer, boneId);		
		handleShots();		
		tryTargetRandomCreatureByTypeInRadius(FFakeHelpers.getTestTargetClass(), FFakeHelpers.getTestTargetRange());		
		tryAttackingUsingMageOffensiveSkill();
		setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.BLESSED_SPIRITSHOT;
	}
	
	@Override
	protected List<OOffensiveSpell> getOffensiveSpells()
	{
		List<OOffensiveSpell> _offensiveSpells = new ArrayList<>();
		_offensiveSpells.add(new OOffensiveSpell(1234, 1));
		_offensiveSpells.add(new OOffensiveSpell(1148, 2));
		_offensiveSpells.add(new OOffensiveSpell(1343, 3));
		return _offensiveSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFakeHelpers.getMageBuffs();
	}

	@Override
	protected List<HHealingSpell> getHealingSpells()
	{		
		return Collections.emptyList();
	}
	
	@Override
	protected List<SSupportSpell> getSelfSupportSpells() {
		return Collections.emptyList();
	}
}
