package it.istc.pst.platinum.control.platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import it.istc.pst.platinum.control.platform.ex.PlatformException;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlatformProxy 
{
	private static final AtomicLong CmdIdCounter = new AtomicLong(0);			// command ID counter
	protected final List<PlatformObserver> observers;							// list of platform observers
	
	/**
	 * 
	 */
	protected PlatformProxy() {
		// set data structures
		this.observers = new ArrayList<>();
	}
	
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
	 * 
	 * @param name
	 * @param params
	 * @return
	 */
	protected PlatformCommand create(String name, String[] params) {
		// return a platform command
		return new PlatformCommand("CMD" + CmdIdCounter.getAndIncrement(), name, params);
	}
	
	
	/**
	 * 
	 * @param cmd
	 */
	public void success(PlatformCommand cmd) 
	{
		// check platform observers
		synchronized (this.observers) {
			for (PlatformObserver obs : this.observers) {
				// notify success
				obs.success(cmd);
			}

			// send signals before releasing monitor
			this.observers.notifyAll();
		}
	}
	
	/**
	 * 
	 * @param cmd
	 */
	public void failure(PlatformCommand cmd) {
		// check platform observers
		synchronized (this.observers) {
			for (PlatformObserver obs : this.observers) {
				// notify failure
				obs.failure(cmd);
			}
			
			// send signals before releasing monitor
			this.observers.notifyAll();
		}
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	protected String extractCommandName(ExecutionNode node) { 
		// extract command name from node to execute
		String[] splits = node.getGroundSignature().split("-");
		// command name
		String name = splits[0];
		
		// clear command name from controllability tags
		if (name.startsWith("_")) {
			name = name.replaceFirst("_", "");
		}
		
		// get command name
		return name.toLowerCase();
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	protected String[] extractCommandParameters(ExecutionNode node) {
		// extract command parameter from node to execute
		String[] splits = node.getGroundSignature().split("-");
		// get parameters
		String[] parameters = Arrays.copyOfRange(splits, 1, splits.length);
		// get parameters		
		return parameters;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public abstract boolean isPlatformCommand(ExecutionNode node);
	
	/**
	 * 
	 * This method sends a request to the real/simulated platform to execute a command.
	 * 
	 * The platform will then send a notification about the execution result
	 * 
	 * @param node
	 * @return
	 * @throws PlatformException
	 */
	public abstract PlatformCommand executeNode(ExecutionNode node) 
			throws PlatformException;
	
	/**
	 * This method sends a request to the real/simulated platform to start the execution of a command.
	 * 
	 *  A notification about the execution of the command (execution feedback) will be received through 
	 *  the PlatformObserver interface 
	 * 
	 * @param node
	 * @return
	 * @throws PlatformException
	 */
	public abstract PlatformCommand startNode(ExecutionNode node) 
			throws PlatformException;
	

	/**
	 * This method sends a request to the real/simulated platform to stop the execution of a command
	 * 
	 * @param cmd
	 * @throws PlatformException
	 */
	public abstract void stopNode(ExecutionNode node) 
			throws PlatformException; 
}
