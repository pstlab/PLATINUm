package it.istc.pst.platinum.control.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.control.platform.lang.PlatformCommand;
import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlatformProxy 
{
	protected final List<PlatformObserver> observers;							// list of platform observers
	protected Map<ExecutionNode, PlatformCommand> trace;						// cache of executed commands
	
	/**
	 * 
	 */
	protected PlatformProxy() {
		// initialize data structures
		this.observers = new ArrayList<>();
		this.trace = new HashMap<>();
	}
	
	/**
	 * 
	 * @throws PlatformException
	 */
	public abstract void initialize() 
			throws PlatformException;
	
	/**
	 * 
	 * @throws PlatformException
	 */
	public abstract void start() 
			throws PlatformException;
	
	/**
	 * 
	 * @throws PlatformException
	 */
	public abstract void stop() 
			throws PlatformException;
	
	/**
	 * 
	 * @param cfgFile
	 * @throws PlatformException
	 */
	public abstract void initialize(String cfgFile) 
			throws PlatformException;
	
	/**
	 * This method registers an observer to a particular (physical) platform.
	 * 
	 *  The interactions between the executive and the physical platform are 
	 *  managed through a dedicated PROXY which must implement this interface
	 * 
	 * @param executive
	 */
	public void register(PlatformObserver observer) {
		// register a new observer
		synchronized (this.observers) {
			// add the observer
			this.observers.add(observer);
			// send signal
			this.observers.notifyAll();
		}
	}
	
	/**
	 * Unregister a (previously) registered observer to a particular (physical) platform.
	 * 
	 * 
	 * @param observer
	 */
	public void unregister(PlatformObserver observer) {
		// unregister observers
		synchronized (this.observers) {
			// remove observer
			this.observers.remove(observer);
			// send signal
			this.observers.notifyAll();
		}
	}
	
	
	/**
	 * This method sends an asynchronous request to the real/simulated platform to execute a command.
	 * 
	 *  A notification about the execution of the command (execution feedback) will be received through 
	 *  the PlatformObserver interface 
	 * 
	 * @param cmd
	 * @return
	 * @throws PlatformException
	 */
	public PlatformCommand executedCommand(ExecutionNode node) 
			throws PlatformException
	{
		// platform command
		PlatformCommand cmd = this.doExecuteCommand(node);
		// add entry to the cache
		this.trace.put(node, cmd);
		// return executed command
		return cmd;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * @throws PlatformException
	 */
	protected abstract PlatformCommand doExecuteCommand(ExecutionNode node) 
			throws PlatformException;
	
	/**
	 * 
	 * This method sends a synchronous request to the real/simulated platform to start the execute a command
	 * 
	 * @param node
	 * @return
	 * @throws PlatformException
	 */
	public PlatformCommand startCommand(ExecutionNode node) 
			throws PlatformException 
	{
		// start command execution
		PlatformCommand cmd = this.doStartCommand(node);
		// add entry to the execution trace
		this.trace.put(node, cmd);
		// return started command
		return cmd;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * @throws PlatformException
	 */
	protected abstract PlatformCommand doStartCommand(ExecutionNode node) 
			throws PlatformException;

	/**
	 * This method sends a synchronous request to the real/simulated platform to stop the execution of a command
	 * 
	 * @param cmd
	 * @throws PlatformException
	 */
	public void stopCommand(ExecutionNode node) 
			throws PlatformException 
	{
		// check execution trace
		if (!this.trace.containsKey(node)) {
			throw new PlatformException("No command associated to the requested node:\n\t- node: " + node + "\n");
		}
		
		// get associated command
		PlatformCommand cmd = this.trace.get(node);
		// stop command execution
		this.doStopCommand(cmd);
	}
	
	/**
	 * 
	 * @param cmd
	 * @throws PlatformException
	 */
	protected abstract void doStopCommand(PlatformCommand cmd) 
			throws PlatformException;
}
