package dyn.elfocrash.roboto.ai;

import java.util.Collections;
import java.util.List;

import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.model.HHHealingSpell;
import dyn.elfocrash.roboto.model.OOOffensiveSpell;
import dyn.elfocrash.roboto.model.SSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class FFFallbackAI extends CCCombatAI
{

	public FFFallbackAI(FFFakePlayer character)
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
	protected List<OOOffensiveSpell> getOffensiveSpells()
	{
		return Collections.emptyList();
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return new int[0][0];
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
