package compvp.elfocrash.roboto.ai;

import java.util.Collections;
import java.util.List;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.model.HHealingSpell;
import compvp.elfocrash.roboto.model.OOffensiveSpell;
import compvp.elfocrash.roboto.model.SSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class FFallbackAI extends CCombatAI
{

	public FFallbackAI(FFakePlayer character)
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
	protected List<OOffensiveSpell> getOffensiveSpells()
	{
		return Collections.emptyList();
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return new int[0][0];
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
