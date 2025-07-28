package alt.elfocrash.roboto.task;

import java.util.List;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.FFFFFakePlayerManager;

/**
 * @author Elfocrash
 *
 */
public class AAAAAITask implements Runnable
{	
	private int _from;
	private int _to;
	
	public AAAAAITask(int from, int to) {
		_from = from;
		_to = to;
	}
	
	@Override
	public void run()
	{				
		adjustPotentialIndexOutOfBounds();
		List<FFFFFakePlayer> fffffakePlayers = FFFFFakePlayerManager.INSTANCE.getFFFFFakePlayers().subList(_from, _to);		
		try {
			fffffakePlayers.stream().filter(x-> !x.getFFFFFakeAi().isBusyThinking()).forEach(x-> x.getFFFFFakeAi().thinkAndAct());	
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}	
	
	private void adjustPotentialIndexOutOfBounds() {
		if(_to > FFFFFakePlayerManager.INSTANCE.getFFFFFakePlayersCount()) {
			_to = 	FFFFFakePlayerManager.INSTANCE.getFFFFFakePlayersCount();
		}
	}
}
