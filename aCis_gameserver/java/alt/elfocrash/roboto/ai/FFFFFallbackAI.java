package alt.elfocrash.roboto.ai;

import java.util.Collections;
import java.util.List;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.model.HHHHHealingSpell;
import alt.elfocrash.roboto.model.OOOOOffensiveSpell;
import alt.elfocrash.roboto.model.SSSSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class FFFFFallbackAI extends CCCCCombatAI
{

	public FFFFFallbackAI(FFFFFakePlayer character)
	{
		super(character);
	}
	
	@Override
	public void thinkAndAct()
	{
		
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.SOULSHOT;
	}
	
	@Override
	protected List<OOOOOffensiveSpell> getOffensiveSpells()
	{
		return Collections.emptyList();
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return new int[0][0];
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
