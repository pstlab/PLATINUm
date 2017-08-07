package it.istc.pst.platinum.framework.domain.component.sv;

import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceEvent;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class RequirementStateVariableResourceEvent extends RequirementResourceEvent 
{
	/**
	 * 
	 * @param activity
	 */
	protected RequirementStateVariableResourceEvent(Decision activity) {
		super(activity, 1);
	}
}
