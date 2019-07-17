package it.istc.pst.platinum.control.platform;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * 
	 */
	protected PlatformProxy() {
		// initialize data structures
		this.observers = new ArrayList<>();
	}
	
	/**
	 * 
	 * @throws PlatformException
	 */
	public abstract void initialize() 
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
