package it.istc.pst.platinum.app.sim.hrc;

import it.istc.pst.platinum.app.sim.PlatformSimulator;
import it.istc.pst.platinum.app.sim.SerialAgentSimulator;
import it.istc.pst.platinum.executive.ExecutivePlatformObserver;

/**
 * 
 * @author anacleto
 *
 */
public class HRCPlatformSimulator extends PlatformSimulator 
{
	private SerialAgentSimulator human;						// human operator agent simulator
	private SerialAgentSimulator robot;						// robot agent simulator
	
	private Thread hProcess;
	private Thread rProcess;
	
	/**
	 * 
	 */
	public HRCPlatformSimulator() {
		super();
		// initialize human agent simulator
		this.human = new SerialAgentSimulator("HUMAN", this);
		// initialize robot agent simulator
		this.robot = new SerialAgentSimulator("ROBOT", this);
		// initialize process threads
		hProcess = null;
		rProcess = null;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doSendCommand(String cmd) 
	{
		// check component
		String component = cmd.split("@")[1];
		
		// check start 
		if (component.contains("Human")) {
			// set the task to the human
			this.human.setTask(cmd);
		}
 		
		if (component.contains("Robot") || 
				component.contains("Arm") || 
				component.contains("Tool")) 
		{
			// set the task to the robot
			this.robot.setTask(cmd);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void start() {
		// set human process
		this.hProcess = new Thread(this.human);
		// set robot process
		this.rProcess = new Thread(this.robot);
		// start human process
		this.hProcess.start();
		// start robot process
		this.rProcess.start();
	}
	
	/**
	 * 
	 */
	@Override
	public void stop() 
			throws InterruptedException 
	{
		// stop human process
		this.hProcess.interrupt();
		// wait completion
		this.hProcess.join();

		// stop robot process
		this.rProcess.interrupt();
		// wait completion
		this.rProcess.join();
	}
	
	
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// create simulator
			HRCPlatformSimulator sim = new HRCPlatformSimulator();
			// register an anonymous observer
			sim.register(new ExecutivePlatformObserver() {
				
				@Override
				public void success(String opId, Object data) {
					System.out.println("SUCCESS opId: " + opId + "\n");
				}
				
				@Override
				public void failure(String opId, Object data) {
					System.out.println("FAILURE opId: " + opId + "\n");
				}
			});
		
			System.out.println("Starting simulator");
			// start simulator
			sim.start();
			// execute simulator for 60 seconds
			for (int i = 1; i <= 60; i++)
			{
				System.out.println("[SIMULATOR] Interation #" + i + " .... ");
				// wait a bit
				Thread.sleep(1000);

				// decide whether human or robot task
				boolean human = i % 2 == 0;
				if (human) {
					// send a command
					sim.sendCommand("Task-" + i + "@Human");
				}
				else {
					// send a command
					sim.sendCommand("Task-" + i + "@Robot");
				}
				
			}
			
			// stop simulator
			sim.stop();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
