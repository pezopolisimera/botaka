package dre.elfocrash.roboto;

import java.util.ArrayList;
import java.util.List;

import dre.elfocrash.roboto.task.AAAAITask;
import dre.elfocrash.roboto.task.AAAAITaskRunner;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.actor.instance.Player;

/**
 * @author Elfocrash
 *
 */
public enum FFFFakePlayerTaskManager
{
	INSTANCE;
	
	private final int aiTaskRunnerInterval = 700;
	private final int _playerCountPerTask = 2000;
	private List<AAAAITask> _aiTasks;
	
	private FFFFakePlayerTaskManager(){
		
	}
	
	public void initialise() {
		ThreadPool.scheduleAtFixedRate(new AAAAITaskRunner(), aiTaskRunnerInterval, aiTaskRunnerInterval);
		_aiTasks = new ArrayList<>();
	}
	
	public void adjustTaskSize() {
		int ffffakePlayerCount = FFFFakePlayerManager.INSTANCE.getFFFFakePlayersCount();
		int tasksNeeded = calculateTasksNeeded(ffffakePlayerCount);
		_aiTasks.clear();
		
		for(int i = 0; i < tasksNeeded; i++ ) {
			int from = i * _playerCountPerTask;
			int to = (i + 1) * _playerCountPerTask;
			_aiTasks.add(new AAAAITask(from, to));
		}
	}
	
	private int calculateTasksNeeded(int count) {
	    if (count <= 0)
	        return 0;

	    return (count + _playerCountPerTask - 1) / _playerCountPerTask;
	}
	
	public int getPlayerCountPerTask() {
		return _playerCountPerTask;
	}
	
	public int getTaskCount() {
		return _aiTasks.size();
	}
	
	public List<AAAAITask> getAITasks(){
		return _aiTasks;
	}
    public void startTask(FFFFakePlayer bot) {
        // Ενημέρωση tasks για να καλύπτουν όλους τους bots
        adjustTaskSize();

        // Πάρε τη λίστα με τους active fake players από το manager
        List<Player> activeFakePlayers = FFFFakePlayerManager.INSTANCE.getActiveFakePlayers();

        // Βρες το index του bot στη λίστα
        int index = activeFakePlayers.indexOf(bot);

        if (index == -1) {
            // Αν ο bot δεν υπάρχει στη λίστα, πρόσθεσέ τον
            activeFakePlayers.add(bot);
            index = activeFakePlayers.size() - 1;
        }

        // Υπολογισμός σε ποιο task πρέπει να μπει
        int taskIndex = index / _playerCountPerTask;
        if (taskIndex >= _aiTasks.size()) {
            taskIndex = _aiTasks.size() - 1; // fallback
        }

        // Πάρε το task και πρόσθεσε το bot
        AAAAITask task = _aiTasks.get(taskIndex);
        task.addBot(bot);
    }
}
