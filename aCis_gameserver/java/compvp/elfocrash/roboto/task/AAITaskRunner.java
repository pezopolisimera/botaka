package compvp.elfocrash.roboto.task;

import java.util.List;

import compvp.elfocrash.roboto.FFakePlayerTaskManager;

import net.sf.l2j.commons.concurrent.ThreadPool;

/**
 * @author Elfocrash
 *
 */
public class AAITaskRunner implements Runnable
{	
	@Override
	public void run()
	{		
		FFakePlayerTaskManager.INSTANCE.adjustTaskSize();
		List<AAITask> aiTasks = FFakePlayerTaskManager.INSTANCE.getAITasks();		
		aiTasks.forEach(aiTask -> ThreadPool.execute(aiTask));
	}	
}
