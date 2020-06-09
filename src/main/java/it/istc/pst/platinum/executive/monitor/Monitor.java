package it.istc.pst.platinum.executive.monitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.istc.pst.platinum.control.platform.ex.PlatformException;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ExecutionFeedback;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
import it.istc.pst.platinum.executive.lang.failure.ExecutionFailureCause;
import it.istc.pst.platinum.framework.microkernel.ExecutiveObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.executive.ExecutivePlaceholder;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Monitor<T extends Executive> extends ExecutiveObject
{
	@ExecutivePlaceholder
	protected T executive;
	
	protected List<ExecutionFeedback> observations;
	
	/**
	 * 
	 */
	protected Monitor() {
		super();
		// initialize list of observations
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
	 * @return
	 */
	protected List<ExecutionFeedback> getObservations() 
	{
		// list of observations
		List<ExecutionFeedback> list = new ArrayList<>();
		// protect access to the list of observations
		synchronized (this.observations) {
			// list of received observations
			list = new ArrayList<>(this.observations);
			// sort observations
			Collections.sort(list);
			// clear observation queue
			this.observations.clear();
			// notify
			this.observations.notifyAll();
		}
		
		// get received observations
		return list;
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
