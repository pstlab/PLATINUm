package it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.ParameterNotification;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event.ParameterNotificationObserver;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.Parameter;

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
