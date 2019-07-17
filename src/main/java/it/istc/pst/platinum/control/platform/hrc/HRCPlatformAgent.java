package it.istc.pst.platinum.control.platform.hrc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import it.istc.pst.platinum.control.platform.lang.PlatformCommand;
import it.istc.pst.platinum.control.platform.lang.PlatformCommandDescription;


/**
 * 
 * @author anacleto
 *
 */
public class HRCPlatformAgent 
{
	private static final AtomicLong CmdIdCounter = new AtomicLong(0);
	private String id;													// agent id
	private String label;												// agent label
	private final Object lock = new Object();							// agent status lock
	private HRCPlatformAgentStatus status;									// agent status
	
	// agent configuration information 
	private long uncertainty;											// uncertainty about the execution of a command
	private Map<String, PlatformCommandDescription> commands;			// descriptions of commands the agent can perform 
	
	private List<HRCPlatformAgentObserver> observers;						// list of agent observers
	private PlatformCommand cmd;										// command currently in execution
	private Thread handler;												// asynchronous command execution handler
	
	/**
	 * 
	 * @param id
	 * @param label
	 * @param uncertainty
	 */
	protected HRCPlatformAgent(String id, String label, long uncertainty) 
	{
		// set id 
		this.id = id;
		// set agent label
		this.label = label;
		// set uncertainty
		this.uncertainty = uncertainty;
		// set of commands the agent can execute
		this.commands = new HashMap<>();
		// set command
		this.cmd = null;
		// initialize observers
		this.observers = new ArrayList<>();
		// set initial status
		this.status = HRCPlatformAgentStatus.OFFLINE;
		// create asynchronous command handler
		this.handler = new Thread(new Runnable() {
			
			/**
			 * 
			 */
			@Override
			public void run() 
			{
				boolean run = true;
				while (run)
				{
					try
					{
						// wait a command to execute asynchronously
						synchronized (lock) {
							while (!status.equals(HRCPlatformAgentStatus.COMMAND_EXECUTION_REQUEST)) {
								lock.wait();
							}
							
							// set status
							status = HRCPlatformAgentStatus.COMMAND_EXECUTION_HANDLING;
							// send signal
							lock.notifyAll();
						}
						
						// execution result flag
						boolean success = true;
						try
						{
							// asynchronously execute command
							System.out.println("[" + HRCPlatformAgent.this.label + "] Start command execution:\n\t- " + cmd + "\n");
							
							// get command expected duration
							float execTime = cmd.getExecutionTime();
							// check agent uncertainty and set minimum execution time (avoid execution times lower than 0)
							int min = Math.round(Math.max(1, execTime - HRCPlatformAgent.this.uncertainty));
							// check agent uncertainty and set maximum execution time
							int max = Math.round(execTime + uncertainty);
							
							// get random value 
							Random rand = new Random(System.currentTimeMillis());
							// get a random time within the expected bounds
							long time = rand.nextInt((max - min) + 1) + min;
							
							// wait for a random amount of seconds 
							Thread.sleep(time * 1000);
							System.out.println("[" + HRCPlatformAgent.this.label + "] Stop command execution:\n\t- " + cmd + "\n");
						}
						catch (Exception ex) {
							// command execution failure
							success = false;
						}
						finally 
						{
							// check execution result
							if (success) 
							{
								// notify observers
								for (HRCPlatformAgentObserver obs : observers) {
									// notify success to observers
									obs.notifySuccess(cmd);
								}
								
								
								// successful execution
								synchronized (lock) {
									// update status
									status = HRCPlatformAgentStatus.IDLE;
									// send signal
									lock.notifyAll();
								}
							}
							else 
							{
								// notify observers
								for (HRCPlatformAgentObserver obs : observers) {
									// notify success to observers
									obs.notifyFailure(cmd);
								}
								
								
								// execution failure
								synchronized (lock) {
									// update status
									status = HRCPlatformAgentStatus.FAILURE;
									// send signal
									lock.notifyAll();
								}
							}
						}
					}
					catch (InterruptedException ex) {
						run = false;
					}
				}
			}
		});
	}
	
	/**
	 * 
	 * @param obs
	 */
	public void register(HRCPlatformAgentObserver obs) { 
		synchronized (this.observers) {
			this.observers.add(obs);
		}
	}

	/**
	 * 
	 * @param obs
	 */
	public void unregister(HRCPlatformAgentObserver obs) {
		synchronized (this.observers) {
			this.observers.remove(obs);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param parameters
	 * @param duration
	 * @return
	 */
	public PlatformCommandDescription addCommandDescription(String name, String[] parameters, float duration) {
		// return command description
		PlatformCommandDescription cmd = new PlatformCommandDescription(name, parameters, duration);
		// add command
		this.commands.put(cmd.getName(), cmd);
		// get created command description
		return cmd;
	}
	
	
	/**
	 * 
	 * @param desc
	 * @return
	 */
	protected PlatformCommand create(PlatformCommandDescription desc, String[] params) 
	{
		// set command index
		String cmdId = desc.getName() + "-" + CmdIdCounter.getAndIncrement() + "@" + this.id;
		// return a platform command
		return new PlatformCommand(cmdId, desc, params);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PlatformCommandDescription> getCommandDescriptions() {
		return new ArrayList<>(this.commands.values());
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public PlatformCommandDescription getCommandDescription(String name) {
		return this.commands.get(name);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getUncertainty() {
		return uncertainty;
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void start() 
			throws InterruptedException 
	{
		synchronized (lock) {
			// check status
			while (!this.status.equals(HRCPlatformAgentStatus.OFFLINE)) {
				lock.wait();
			}
			
			// update status and send signal
			this.status = HRCPlatformAgentStatus.IDLE;
			lock.notifyAll();
		}
		
		System.out.println("[" + this.label + "] Starting agent simulator... ");
		// start asynchronous command handler
		this.handler.start();
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void stop() 
			throws InterruptedException
	{
		synchronized (lock) {
			// check status
			while (!this.status.equals(HRCPlatformAgentStatus.IDLE)) {
				lock.wait();
			}
			
			// update status and send signal
			this.status = HRCPlatformAgentStatus.OFFLINE;
			lock.notifyAll();
		}
		
		// interrupt handler
		this.handler.interrupt();
		this.handler.join();
	}
	
	/**
	 * Asynchronous execution of a command
	 * 
	 * @param cmd
	 * @throws InterruptedException
	 */
	public void execute(PlatformCommand cmd) 
			throws InterruptedException 
	{
		synchronized (lock) {
			while (!this.status.equals(HRCPlatformAgentStatus.IDLE)) {
				lock.wait();
			}
			
			// set the command to execute
			this.cmd = cmd;
			// set the status 
			this.status = HRCPlatformAgentStatus.COMMAND_EXECUTION_REQUEST;
			// notify 
			lock.notifyAll();
		}
	}

	/**
	 * Start and synchronously manage the execution of a command
	 * 
	 * @param cmd
	 * @throws InterruptedException
	 */
	public void startCommand(PlatformCommand cmd) 
			throws InterruptedException 
	{
		synchronized (lock) {
			while (!this.status.equals(HRCPlatformAgentStatus.IDLE)) {
				lock.wait();
			}
		
			// set the command to execute
			this.cmd = cmd;
			System.out.println("[" + this.label + "] START : " + this.cmd + "\n");
			// set the status 
			this.status = HRCPlatformAgentStatus.COMMAND_START_REQUEST;
			// notify 
			lock.notifyAll();
		}
	}
	
	/**
	 * Synchronous stop a command whose execution has been started previously.
	 * 
	 * It is supposed that only controllable commands can be stopped synchronously. Therefore,
	 * there is not need to send a notification to the executive in this case. This call is 
	 * managed as a "blocking call". If the returned object is not null the operation succeded
	 * 
	 * @param cmd
	 * @throws InterruptedException
	 */
	public PlatformCommand stopCommand() 
			throws InterruptedException 
	{
		synchronized (lock) {
			while (!this.status.equals(HRCPlatformAgentStatus.COMMAND_START_REQUEST)) {
				lock.wait();
			}
			
			// set the status 
			this.status = HRCPlatformAgentStatus.COMMAND_EXECUTION_HANDLING;
			// notify 
			lock.notifyAll();
		}
		
		// get stopped command
		PlatformCommand stopped = this.cmd;
		System.out.println("[" + this.label + "] STOP : " + this.cmd + "\n");
		// execution of "controllable" commands succeed always 
		synchronized (lock) {
			// change status 
			this.status = HRCPlatformAgentStatus.IDLE;
			// send signal
			lock.notifyAll();
		}
	
		// get stopped command
		return stopped;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HRCPlatformAgent other = (HRCPlatformAgent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[AgentSimulator id: " + this.id + ", label: " + this.label + "]";
	}
}
