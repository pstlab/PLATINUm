package it.uniroma3.epsl2.framework.parameter.csp.solver;

import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotification;
import it.uniroma3.epsl2.framework.parameter.csp.event.ParameterNotificationObserver;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ParameterSolver extends ApplicationFrameworkObject implements ParameterNotificationObserver 
{
	
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
	 * @param param
	 */
	public abstract void computeValues(Parameter<?> param);
	
	/**
	 * 
	 */
	@Override
	public abstract void update(ParameterNotification info);
}
