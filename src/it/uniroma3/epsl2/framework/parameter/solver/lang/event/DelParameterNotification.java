package it.uniroma3.epsl2.framework.parameter.solver.lang.event;

import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public class DelParameterNotification extends ParameterNotification 
{
	private Parameter param;
	
	/**
	 * 
	 */
	protected DelParameterNotification() {
		super(ParameterNotificationType.DEL_PARAM);
	}
	
	/**
	 * 
	 * @param param
	 */
	public void setParameter(Parameter param) {
		this.param = param;
	}
	
	/**
	 * 
	 * @return
	 */
	public Parameter getParameter() {
		return param;
	}
}
