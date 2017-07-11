package it.istc.pst.platinum.framework.parameter.csp.event;

import it.istc.pst.platinum.framework.parameter.lang.Parameter;

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
