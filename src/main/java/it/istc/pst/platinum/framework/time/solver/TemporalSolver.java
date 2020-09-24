package it.istc.pst.platinum.framework.time.solver;

import it.istc.pst.platinum.framework.microkernel.FrameworkObject;
import it.istc.pst.platinum.framework.microkernel.query.Query;
import it.istc.pst.platinum.framework.microkernel.query.QueryManager;
import it.istc.pst.platinum.framework.time.tn.TemporalNetwork;
import it.istc.pst.platinum.framework.time.tn.lang.event.TemporalNetworkNotification;
import it.istc.pst.platinum.framework.time.tn.lang.event.TemporalNetworkObserver;
import it.istc.pst.platinum.framework.time.tn.lang.event.ex.NotificationPropagationFailureException;

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
