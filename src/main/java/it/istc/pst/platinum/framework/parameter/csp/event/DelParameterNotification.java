package it.istc.pst.platinum.framework.parameter.csp.event;

import it.istc.pst.platinum.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public class DelParameterNotification extends ParameterNotification 
{
	private Parameter<?> param;
	
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
