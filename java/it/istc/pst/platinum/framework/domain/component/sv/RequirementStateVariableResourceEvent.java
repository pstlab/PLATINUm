package it.istc.pst.platinum.framework.domain.component.sv;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceEvent;

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
