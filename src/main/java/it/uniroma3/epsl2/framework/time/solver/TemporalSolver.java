package it.uniroma3.epsl2.framework.time.solver;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkContainer;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.uniroma3.epsl2.framework.microkernel.query.Query;
import it.uniroma3.epsl2.framework.microkernel.query.QueryManager;
import it.uniroma3.epsl2.framework.time.tn.TemporalNetwork;
import it.uniroma3.epsl2.framework.time.tn.lang.event.TemporalNetworkNotification;
import it.uniroma3.epsl2.framework.time.tn.lang.event.TemporalNetworkObserver;
import it.uniroma3.epsl2.framework.time.tn.lang.event.ex.NotificationPropagationFailureException;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

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
