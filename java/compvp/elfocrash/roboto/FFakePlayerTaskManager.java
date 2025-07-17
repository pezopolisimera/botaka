package compvp.elfocrash.roboto;

import java.util.ArrayList;
import java.util.List;

import compvp.elfocrash.roboto.task.AAITask;
import compvp.elfocrash.roboto.task.AAITaskRunner;

import net.sf.l2j.commons.concurrent.ThreadPool;

/**
 * @author Elfocrash
 *
 */
public enum FFakePlayerTaskManager
{
	INSTANCE;
	
	private final int aiTaskRunnerInterval = 700;
	private final int _playerCountPerTask = 2000;
	private List<AAITask> _aiTasks;
	
	private FFakePlayerTaskManager(){
		
	}
	
	public void initialise() {
		ThreadPool.scheduleAtFixedRate(new AAITaskRunner(), aiTaskRunnerInterval, aiTaskRunnerInterval);
		_aiTasks = new ArrayList<>();
	}
	
	public void adjustTaskSize() {
		int ffakePlayerCount = FFakePlayerManager.INSTANCE.getFFakePlayersCount();
		int tasksNeeded = calculateTasksNeeded(ffakePlayerCount);
		_aiTasks.clear();
		
		for(int i = 0; i < tasksNeeded; i++ ) {
			int from = i * _playerCountPerTask;
			int to = (i + 1) * _playerCountPerTask;
			_aiTasks.add(new AAITask(from, to));
		}
	}
	
	private int calculateTasksNeeded(int ffakePlayerCount)
	{
		return ffakePlayerCount == 0 ? 0 : ffakePlayerCount > 0 && ffakePlayerCount < _playerCountPerTask ? 1 : (ffakePlayerCount +_playerCountPerTask) / _playerCountPerTask;
	}
	
	public int getPlayerCountPerTask() {
		return _playerCountPerTask;
	}
	
	public int getTaskCount() {
		return _aiTasks.size();
	}
	
	public List<AAITask> getAITasks(){
		return _aiTasks;
	}
}
