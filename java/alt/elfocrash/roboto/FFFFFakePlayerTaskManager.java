package alt.elfocrash.roboto;

import java.util.ArrayList;
import java.util.List;

import alt.elfocrash.roboto.task.AAAAAITask;
import alt.elfocrash.roboto.task.AAAAAITaskRunner;

import net.sf.l2j.commons.concurrent.ThreadPool;

/**
 * @author Elfocrash
 *
 */
public enum FFFFFakePlayerTaskManager
{
	INSTANCE;
	
	private final int aiTaskRunnerInterval = 700;
	private final int _playerCountPerTask = 2000;
	private List<AAAAAITask> _aiTasks;
	
	private FFFFFakePlayerTaskManager(){
		
	}
	
	public void initialise() {
		ThreadPool.scheduleAtFixedRate(new AAAAAITaskRunner(), aiTaskRunnerInterval, aiTaskRunnerInterval);
		_aiTasks = new ArrayList<>();
	}
	
	public void adjustTaskSize() {
		int fffffakePlayerCount = FFFFFakePlayerManager.INSTANCE.getFFFFFakePlayersCount();
		int tasksNeeded = calculateTasksNeeded(fffffakePlayerCount);
		_aiTasks.clear();
		
		for(int i = 0; i < tasksNeeded; i++ ) {
			int from = i * _playerCountPerTask;
			int to = (i + 1) * _playerCountPerTask;
			_aiTasks.add(new AAAAAITask(from, to));
		}
	}
	
	private int calculateTasksNeeded(int fffffakePlayerCount)
	{
		return fffffakePlayerCount == 0 ? 0 : fffffakePlayerCount > 0 && fffffakePlayerCount < _playerCountPerTask ? 1 : (fffffakePlayerCount +_playerCountPerTask) / _playerCountPerTask;
	}
	
	public int getPlayerCountPerTask() {
		return _playerCountPerTask;
	}
	
	public int getTaskCount() {
		return _aiTasks.size();
	}
	
	public List<AAAAAITask> getAITasks(){
		return _aiTasks;
	}
}
