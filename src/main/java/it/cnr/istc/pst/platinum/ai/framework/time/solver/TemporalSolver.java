package it.cnr.istc.pst.platinum.ai.framework.time.solver;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.Query;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.QueryManager;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetwork;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.TemporalNetworkNotification;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.TemporalNetworkObserver;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.event.ex.NotificationPropagationFailureException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalSolver<T extends Query> extends FrameworkObject implements TemporalNetworkObserver, QueryManager<T> 
{
	protected TemporalNetwork tn;
	
	/**
	 * 
	 */
	protected TemporalSolver(TemporalNetwork tn) {
		super();
		this.tn = tn;
		// subscribe to the network
		this.tn.subscribe(this);
	}
	
	/**
	 * 
	 */
	@Override
	public abstract void process(T query);
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isConsistent();
	
	/**
	 * 
	 */
	@Override
	public abstract void notify(TemporalNetworkNotification info) 
			throws NotificationPropagationFailureException;
}
