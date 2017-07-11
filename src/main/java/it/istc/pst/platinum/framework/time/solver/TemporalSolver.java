package it.istc.pst.platinum.framework.time.solver;

import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.microkernel.query.Query;
import it.istc.pst.platinum.framework.microkernel.query.QueryManager;
import it.istc.pst.platinum.framework.time.tn.TemporalNetwork;
import it.istc.pst.platinum.framework.time.tn.lang.event.TemporalNetworkNotification;
import it.istc.pst.platinum.framework.time.tn.lang.event.TemporalNetworkObserver;
import it.istc.pst.platinum.framework.time.tn.lang.event.ex.NotificationPropagationFailureException;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalSolver<T extends Query> extends ApplicationFrameworkObject implements TemporalNetworkObserver, QueryManager<T> 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE_LOGGER)
	protected FrameworkLogger logger;
	
	protected TemporalNetwork tn;
	
	/**
	 * 
	 */
	protected TemporalSolver() {
		this.tn = null;
	}
	
	/**
	 * 
	 * @param tn
	 */
	public void setTemporalNetwork(TemporalNetwork tn) {
		this.tn = tn;
		// subscribe
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
