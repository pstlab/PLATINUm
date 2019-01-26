package it.istc.pst.platinum.control.platform;

import it.istc.pst.platinum.control.platform.lang.PlatformCommand;
import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public interface PlatformProxy {

	/**
	 * This method registers an observer to a particular (physical) platform.
	 * 
	 *  The interactions between the executive and the physical platform are 
	 *  managed through a dedicated PROXY which must implement this interface
	 * 
	 * @param executive
	 */
	public void register(PlatformObserver observer);
	
	/**
	 * Unregister a (previously) registered observer to a particular (physical) platform.
	 * 
	 * 
	 * @param observer
	 */
	public void unregister(PlatformObserver observer);
	
	
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
			throws PlatformException;
	
	/**
	 * This method sends a synchronous request to the real/simulated platform to stop the execution of a command
	 * 
	 * @param cmd
	 * @return
	 * @throws PlatformException
	 */
	public PlatformCommand stopCommand(ExecutionNode node) 
			throws PlatformException;
}
