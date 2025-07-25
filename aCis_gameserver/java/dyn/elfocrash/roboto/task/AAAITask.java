package dyn.elfocrash.roboto.task;

import java.util.List;

import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.FFFakePlayerManager;

/**
 * @author Elfocrash
 *
 */
public class AAAITask implements Runnable
{	
	private int _from;
	private int _to;
	
	public AAAITask(int from, int to) {
		_from = from;
		_to = to;
	}
	
	@Override
	public void run()
	{				
		adjustPotentialIndexOutOfBounds();
		List<FFFakePlayer> fffakePlayers = FFFakePlayerManager.INSTANCE.getFFFakePlayers().subList(_from, _to);		
		try {
			fffakePlayers.stream().filter(x-> !x.getFFFakeAi().isBusyThinking()).forEach(x-> x.getFFFakeAi().thinkAndAct());	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}	
	
	private void adjustPotentialIndexOutOfBounds() {
		if(_to > FFFakePlayerManager.INSTANCE.getFFFakePlayersCount()) {
			_to = 	FFFakePlayerManager.INSTANCE.getFFFakePlayersCount();
		}
	}
}
