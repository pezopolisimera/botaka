package dyn.elfocrash.roboto.task;

import java.util.List;

import dyn.elfocrash.roboto.FFFakePlayerTaskManager;

import net.sf.l2j.commons.concurrent.ThreadPool;

/**
 * @author Elfocrash
 *
 */
public class AAAITaskRunner implements Runnable
{	
	@Override
	public void run()
	{		
		FFFakePlayerTaskManager.INSTANCE.adjustTaskSize();
		List<AAAITask> aiTasks = FFFakePlayerTaskManager.INSTANCE.getAITasks();		
		aiTasks.forEach(aiTask -> ThreadPool.execute(aiTask));
	}	
}
