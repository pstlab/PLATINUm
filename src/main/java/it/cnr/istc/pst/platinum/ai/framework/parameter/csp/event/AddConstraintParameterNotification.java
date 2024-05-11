package it.cnr.istc.pst.platinum.ai.framework.parameter.csp.event;

import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class AddConstraintParameterNotification extends ParameterNotification 
{
	private ParameterConstraint constraint;
	
	/**
	 * 
	 */
	protected AddConstraintParameterNotification() {
		super(ParameterNotificationType.ADD_CONSTRAINT);
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
