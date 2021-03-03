package it.cnr.istc.pst.platinum.ai.executive.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.executive.Executive;
import it.cnr.istc.pst.platinum.ai.executive.lang.ExecutionFeedback;
import it.cnr.istc.pst.platinum.ai.executive.lang.ex.ExecutionException;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;
import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Monitor<T extends Executive> extends FrameworkObject
{
	@ExecutivePlaceholder
	protected T executive;
	
	protected List<ExecutionFeedback> observations;
	
	/**
	 * 
	 */
	protected Monitor() {
		super();
		// set list of observations
		this.observations = new ArrayList<ExecutionFeedback>();
	}
	
	/**
	 * 
	 * @param feedback
	 */
	public void addExecutionFeedback(ExecutionFeedback feedback) {
		// protected access to the list of observations
		synchronized (this.observations) {
			// add received feedback
			this.observations.add(feedback);
			this.observations.notifyAll();
		}
	}

	/**
	 * 
	 */
	public void clear() {
		// clear internal structures
		synchronized (this.observations) {
			this.observations.clear();
		}
	}

	/**
	 * 
	 * @return
	 */
	protected boolean hasObservations()
	{
		// content flag
		boolean hasObservations = true;
		// protect access to the list of observations
		synchronized (this.observations) {
			// check if empty
			hasObservations = !this.observations.isEmpty();
		}
		
		// get result
		return hasObservations;
	}
	
	/**
	*
	* @return
	*/
	protected List<ExecutionFeedback> getObservations()
	{
		//list of observations
		List<ExecutionFeedback> list = new ArrayList<>();
		//protect access to the list of observations
		synchronized (this.observations) {
			//list of received observations
			list = new ArrayList<>(this.observations);
			//sort observations
			Collections.sort(list);
			//clear observations queue
			this.observations.clear();
			//notify
			this.observations.notifyAll();
		}
		
		//get received observations
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	protected ExecutionFeedback next() {
		// next execution feedback
		ExecutionFeedback feedback = null;
		// protect access to the list of observations
		synchronized (this.observations) {
			// remove the first element of the queue
			feedback = this.observations.remove(0);
		}
		
		// get next feedback to handle
		return feedback;
	}
	
	/**
	 * 
	 * @param tick
	 * @throws ExecutionException
	 * @throws PlatformException
	 */
	public abstract void handleTick(long tick) 
			throws ExecutionException, PlatformException;
	
	/**
	 * 
	 * @param tick
	 * @param cause
	 * @throws PlatformException
	 */
	public abstract void handleExecutionFailure(long tick, ExecutionFailureCause cause) 
			throws PlatformException;
}
