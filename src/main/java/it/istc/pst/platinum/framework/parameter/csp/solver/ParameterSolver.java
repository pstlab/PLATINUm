package it.istc.pst.platinum.framework.parameter.csp.solver;

import it.istc.pst.platinum.framework.microkernel.FrameworkObject;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotification;
import it.istc.pst.platinum.framework.parameter.csp.event.ParameterNotificationObserver;
import it.istc.pst.platinum.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ParameterSolver extends FrameworkObject implements ParameterNotificationObserver 
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
