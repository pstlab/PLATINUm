package it.cnr.istc.pst.platinum.ai.framework.domain.component.sv;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.discrete.RequirementResourceEvent;

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
