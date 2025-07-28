package alt.elfocrash.roboto.task;

import java.util.List;

import alt.elfocrash.roboto.FFFFFakePlayerTaskManager;

import net.sf.l2j.commons.concurrent.ThreadPool;

/**
 * @author Elfocrash
 *
 */
public class AAAAAITaskRunner implements Runnable
{	
	@Override
	public void run()
	{		
		FFFFFakePlayerTaskManager.INSTANCE.adjustTaskSize();
		List<AAAAAITask> aiTasks = FFFFFakePlayerTaskManager.INSTANCE.getAITasks();		
		aiTasks.forEach(aiTask -> ThreadPool.execute(aiTask));
	}	
}
