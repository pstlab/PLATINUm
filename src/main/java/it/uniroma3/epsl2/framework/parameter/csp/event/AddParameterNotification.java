package it.uniroma3.epsl2.framework.parameter.csp.event;

import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public class AddParameterNotification extends ParameterNotification 
{
	private Parameter<?> param;
	
	/**
	 * 
	 */
	protected AddParameterNotification() {
		super(ParameterNotificationType.ADD_PARAM);
	}
	
	/**
	 * 
	 * @param param
	 */
	public void setParameter(Parameter<?> param) {
		this.param = param;
	}
	
	/**
	 * 
	 * @return
	 */
	public Parameter<?> getParameter() {
		return param;
	}
}
