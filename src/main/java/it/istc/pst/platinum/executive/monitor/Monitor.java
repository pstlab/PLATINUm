package it.istc.pst.platinum.executive.monitor;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.lang.ExecutionFeedback;
import it.istc.pst.platinum.executive.lang.ex.ExecutionException;
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
	
	protected final List<ExecutionFeedback> observations;
	
	/**
	 * 
	 */
	protected Monitor() {
		super();
		// initialize list of observations
		this.observations = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param feedback
	 */
	public void addExecutionFeedback(ExecutionFeedback feedback) {
		// synchronize access to shared resource
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
		// list of received observations
		List<ExecutionFeedback> list = new ArrayList<>();
		// access shared resource
		synchronized (this.observations) {
			// get all received observations
			list.addAll(this.observations);
			// clear observation queue
			this.observations.clear();
			// notify all
			this.observations.notifyAll();
		}
		
		// get observations
		return list;
	}
	
	/**
	 * 
	 * @param tick
	 * @throws ExecutionException
	 */
	public abstract void handleTick(long tick) 
			throws ExecutionException;
}
