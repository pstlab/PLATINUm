package it.istc.pst.platinum.framework.parameter.csp.event;

import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class DelConstraintParameterNotification extends ParameterNotification 
{
	private ParameterConstraint constraint;
	
	/**
	 * 
	 */
	protected DelConstraintParameterNotification() {
		super(ParameterNotificationType.DEL_CONSTRAINT);
	}
	
	/**
	 * 
	 * @param constraint
	 */
	public void setConstraint(ParameterConstraint constraint) {
		this.constraint = constraint;
	}
	
	/**
	 * 
	 * @return
	 */
	public ParameterConstraint getParameterConstraint() {
		return this.constraint;
	}
}
