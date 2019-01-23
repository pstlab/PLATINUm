package it.istc.pst.platinum.app.control.sim;

import java.util.Random;

/**
 * 
 * @author anacleto
 *
 */
public class SerialAgentSimulator implements Runnable 
{
	private String label;								// agent label
	private PlatformSimulator platform;					// platform simulator
	private static final Object lock = new Object();	// agent lock status
	private String task;								// task to execute

	/**
	 * 
	 * @param label
	 * @param platform
	 */
	public SerialAgentSimulator(String label, PlatformSimulator platform) {
		this.label = label;
		this.task = null;
		this.platform = platform;
	}
	
	/**
	 * 
	 * @param task
	 */
	public void setTask(String task) {
		synchronized (lock) {
			// update the current task
			this.task = task;
			// notify 
			lock.notifyAll();
		}
	}
	
	
	/**
	 * 
	 */
	@Override
	public void run() 
	{
		// start running the 
		boolean run = true;
		System.out.println("[" + this.label + "] Starting agent...\n");
		while (run)
		{
			try
			{
				// task to perform
				String cmd = null;
				// wait a task to perform 
				synchronized (lock) {
					while (this.task == null) {
						lock.wait();
					}
					
					// get the task to perform
					cmd = this.task;
					// clear current task
					this.task = null;
					// notify all 
					lock.notifyAll();
				}
				
				
				System.out.println("[" + this.label + "] Start performing task " + cmd + "\n");
				// get random value 
				Random rand = new Random(System.currentTimeMillis());
				// get a random integer within 1 and 10
				int val = rand.nextInt(10);
				// wait for a random amount of seconds 
				Thread.sleep(val * 1000);
				System.out.println("[" + this.label + "] ends performing task" + cmd + "\n");
				// notify successful execution
				this.platform.success(cmd);
				System.out.println("[" + this.label + "] Notifying successful execution of task" + cmd + "\n");
			}
			catch (InterruptedException ex) {
				run = false;
			}
		}

		System.out.println("[" + this.label + "] Stopping agent...\n");
	}
	
}
