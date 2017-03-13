package it.uniroma3.epsl2.framework.time.tn.solver;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalNetworkReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;
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
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	@TemporalNetworkReference
	protected TemporalNetwork tn;
	
	/**
	 * 
	 */
	protected TemporalSolver() {}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// subscribe reasoner to temporal network updates
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
