package it.istc.pst.platinum.framework.parameter.csp.solver;

import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotificationObserver;
import it.istc.pst.platinum.framework.parameter.lang.Parameter;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ParameterSolver extends ApplicationFrameworkObject implements ParameterNotificationObserver 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE_LOGGER)
	protected FrameworkLogger logger;
	
	/**
	 * 
	 * @param type
	 */
	protected ParameterSolver() {
		super();
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isConsistent();
	
	/**
	 * 
	 */
	public abstract void computeSolution();
	
	/**
	 * 
	 * @param param
	 */
	public abstract void computeValues(Parameter<?> param);
	
	/**
	 * 
	 */
	@Override
	public abstract void update(ParameterNotification info);
}
