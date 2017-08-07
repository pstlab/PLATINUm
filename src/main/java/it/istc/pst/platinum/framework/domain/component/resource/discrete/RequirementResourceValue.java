package it.istc.pst.platinum.framework.domain.component.resource.discrete;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.ComponentValueType;

/**
 * 
 * @author anacleto
 *
 */
public class RequirementResourceValue extends ComponentValue<DiscreteResource>
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
