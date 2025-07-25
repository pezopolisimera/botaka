package dyn.elfocrash.roboto;

import java.util.ArrayList;
import java.util.List;

import dyn.elfocrash.roboto.task.AAAITask;
import dyn.elfocrash.roboto.task.AAAITaskRunner;

import net.sf.l2j.commons.concurrent.ThreadPool;

/**
 * @author Elfocrash
 *
 */
public enum FFFakePlayerTaskManager
{
	INSTANCE;
	
	private final int aiTaskRunnerInterval = 700;
	private final int _playerCountPerTask = 2000;
	private List<AAAITask> _aiTasks;
	
	private FFFakePlayerTaskManager(){
		
	}
	
	public void initialise() {
		ThreadPool.scheduleAtFixedRate(new AAAITaskRunner(), aiTaskRunnerInterval, aiTaskRunnerInterval);
		_aiTasks = new ArrayList<>();
	}
	
	public void adjustTaskSize() {
		int fffakePlayerCount = FFFakePlayerManager.INSTANCE.getFFFakePlayersCount();
		int tasksNeeded = calculateTasksNeeded(fffakePlayerCount);
		_aiTasks.clear();
		
		for(int i = 0; i < tasksNeeded; i++ ) {
			int from = i * _playerCountPerTask;
			int to = (i + 1) * _playerCountPerTask;
			_aiTasks.add(new AAAITask(from, to));
		}
	}
	
	private int calculateTasksNeeded(int fffakePlayerCount)
	{
		return fffakePlayerCount == 0 ? 0 : fffakePlayerCount > 0 && fffakePlayerCount < _playerCountPerTask ? 1 : (fffakePlayerCount +_playerCountPerTask) / _playerCountPerTask;
	}
	
	public int getPlayerCountPerTask() {
		return _playerCountPerTask;
	}
	
	public int getTaskCount() {
		return _aiTasks.size();
	}
	
	public List<AAAITask> getAITasks(){
		return _aiTasks;
	}
}
