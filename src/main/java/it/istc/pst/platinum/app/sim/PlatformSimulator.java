package it.istc.pst.platinum.app.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.platinum.executive.ExecutivePlatformObserver;
import it.istc.pst.platinum.executive.ExecutivePlatformProxy;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlatformSimulator implements ExecutivePlatformProxy 
{
	private static final AtomicInteger OpCounter = new AtomicInteger(0);		// operation counter
	private final List<ExecutivePlatformObserver> observers;					// list of observers

	private Map<String, String> cmd2index;										// command to index mapping  
	
	/**
	 * 
	 */
	public PlatformSimulator() {
		// initialize the list of observers
		this.observers = new ArrayList<>();
		this.cmd2index = new HashMap<>();
	}
	
	/**
	 * 
	 */
	@Override
	public void register(ExecutivePlatformObserver observer) {
		// register a new observer
		synchronized (this.observers) {
			// add the observer
			this.observers.add(observer);
			// send signal
			this.observers.notifyAll();
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void unregister(ExecutivePlatformObserver observer) {
		// unregister observers
		synchronized (this.observers) {
			// remove observer
			this.observers.remove(observer);
			// send signal
			this.observers.notifyAll();
		}
	}

	/**
	 * 
	 */
	@Override
	public String sendCommand(String cmd) 
	{
		// get operation ID
		String opId = "OP-" + OpCounter.getAndIncrement();
		// add entry to the index
		this.cmd2index.put(cmd, opId);
		// actually send command to the agent
		this.doSendCommand(cmd);
		// get operation id
		return opId;
	}
	
	/**
	 * 
	 * @param cmd
	 */
	protected abstract void doSendCommand(String cmd);
	
	/**
	 * 
	 * @param cmd
	 */
	public void success(String cmd) 
	{
		// check if command has been actually dispatched
		if (this.cmd2index.containsKey(cmd))
		{
			// get operation ID
			String opId = this.cmd2index.get(cmd);
			// notify success to all observers
			for (ExecutivePlatformObserver obs : this.observers) {
				// set success execution notification
				obs.success(opId, cmd);
			}
		}
	}

	/**
	 * Start platform simulator execution
	 */
	public abstract void start();
	
	/**
	 * Stop platform simulator execution
	 */
	public abstract void stop() 
			throws InterruptedException;
}
