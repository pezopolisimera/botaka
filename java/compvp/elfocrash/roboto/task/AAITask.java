package compvp.elfocrash.roboto.task;

import java.util.List;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.FFakePlayerManager;

/**
 * @author Elfocrash
 *
 */
public class AAITask implements Runnable
{	
	private int _from;
	private int _to;
	
	public AAITask(int from, int to) {
		_from = from;
		_to = to;
	}
	
	@Override
	public void run()
	{				
		adjustPotentialIndexOutOfBounds();
		List<FFakePlayer> ffakePlayers = FFakePlayerManager.INSTANCE.getFFakePlayers().subList(_from, _to);		
		try {
			ffakePlayers.stream().filter(x-> !x.getFFakeAi().isBusyThinking()).forEach(x-> x.getFFakeAi().thinkAndAct());	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}	
	
	private void adjustPotentialIndexOutOfBounds() {
		if(_to > FFakePlayerManager.INSTANCE.getFFakePlayersCount()) {
			_to = 	FFakePlayerManager.INSTANCE.getFFakePlayersCount();
		}
	}
}
