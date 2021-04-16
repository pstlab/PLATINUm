package it.cnr.istc.pst.platinum.control.platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;
import it.cnr.istc.pst.platinum.control.lang.PlatformCommand;
import it.cnr.istc.pst.platinum.control.lang.PlatformFeedback;
import it.cnr.istc.pst.platinum.control.lang.PlatformMessage;
import it.cnr.istc.pst.platinum.control.lang.PlatformObservation;
import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class PlatformProxy 
{
	protected static final AtomicInteger obsIdCounter = new AtomicInteger(0);
	protected static final AtomicInteger cmdIdCounter = new AtomicInteger(0);
	
	protected final List<PlatformObserver> observers;							// list of platform observers
	
	protected Map<Long, PlatformCommand> dispatchedIndex;				// index of dispatched commands by command ID
	
	/**
	 * 
	 */
	protected PlatformProxy() {
		// setup data structures
		this.observers = new ArrayList<>();
		this.dispatchedIndex = new HashMap<>();
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
	 * @param cmdId
	 * @return
	 */
	public synchronized PlatformCommand getDispatchedCommand(long cmdId) {
		// get command from cache
		return this.dispatchedIndex.get(cmdId);
	}
	
	/**
	 * 
	 * This method sends a request to the real/simulated platform to execute a command.
	 * 
	 * This method is usually meant for partially controllable commands. The platform/environment is 
	 * in charge of the execution of the related command and therefore it will then notify the 
	 * acting agent about the successful execution or failures.
	 * 
	 * Feedbacks about the execution of the command will be then received through the PlatformObserver 
	 * interface
	 * 
	 * @param node
	 * @return
	 * @throws PlatformException
	 */
	public abstract PlatformCommand executeNode(ExecutionNode node) 
			throws PlatformException;
	
	/**
	 * 
	 * This method sends a request to the real/simulated platform to start the execution of a command.
	 * 
	 * This method is usually meant for controllable commands. The acting agent is in charge of 
	 * deciding when to start the execution of a command.
	 * 
	 * @param node
	 * @return
	 * @throws PlatformException
	 */
	public abstract PlatformCommand startNode(ExecutionNode node) 
			throws PlatformException;
	

	/**
	 * 
	 * This method sends a request to the real/simulated platform to stop the execution of a command
	 * 
	 * This method is usually meant for controllable commands. The acting agent is in charge of 
	 * deciding when to stop the execution of a previously started command.  
	 * 
	 * @param cmd
	 * @throws PlatformException
	 */
	public abstract void stopNode(ExecutionNode node) 
			throws PlatformException;
	
	/**
	 * This method check whether an execution node represents a platform command or not. 
	 * 
	 *  Namely, the method checks if a particular token of a plan actually represents a 
	 *  command to be executed on some platform. 
	 *  
	 *  Clearly, the implementation of this method strongly depends on the functional layer 
	 *  provided by the hardware or software platform considered for the execution of 
	 *  timeline-based plan.
	 * 
	 * @param node
	 * @return
	 */
	public abstract boolean isPlatformCommand(ExecutionNode node);
	
	
	/**
	 * 
	 * @param observation
	 */
	public void notify(PlatformObservation<? extends Object> observation) 
	{
		// get platform observers
		synchronized (observers) {
			// notify observers
			for (PlatformObserver observer : observers) {
				// notify observation to platform observer
				observer.observation(observation);
			}
		}
	}
	
	/**
	 * 
	 * @param feedback
	 */
	public void notify(PlatformFeedback feedback) 
	{
		// get platform observers
		synchronized (observers) {
			// notify observers
			for (PlatformObserver observer : observers) {
				// notify observation to platform observer
				observer.feedback(feedback);
				
			}
		}
	}
	
	/**
	 * 
	 * @param msg
	 */
	public void notify(PlatformMessage msg)
	{
		// check message internal types
		if (msg instanceof PlatformFeedback) {
			// handle execution feedback
			this.notify((PlatformFeedback) msg);
		}
		else {
			// handle observation
			this.notify((PlatformObservation<?>) msg);
		}
	}
	
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public static String extractCommandName(ExecutionNode node) 
	{ 
		// get signature
		String name = node.getGroundSignature();
		// "clear" command name from "control tags"
		if (name.startsWith("_")) {
			// remove partial controllability tag
			name = name.replaceFirst("_", "");
		}
		
		// discard parameters if any 
		String[] splits = name.split("-");
		// set the first element as command name
		name = splits[0];
		
		// get cleared command name 
		return name.toLowerCase();
	}
	
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public static String[] extractCommandParameters(ExecutionNode node) {
		// extract command parameter from node to execute
		String[] splits = node.getGroundSignature().split("-");
		// get parameters
		String[] parameters = Arrays.copyOfRange(splits, 1, splits.length);
		// get parameters		
		return parameters;
	}
}
