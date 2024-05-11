package it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.discrete;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValueType;

/**
 * 
 * @author anacleto
 *
 */
public class RequirementResourceValue extends ComponentValue
{
	/**
	 * 
	 * @param label
	 * @param duration
	 * @param component
	 */
	protected RequirementResourceValue(String label, long[] duration, DiscreteResource component) {
		super(label, ComponentValueType.RESOURCE_REQUIREMENT, duration, true, component);
	}
}
