package it.istc.pst.platinum.framework.domain.component.resource;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.ComponentValueType;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceRequirementValue extends ComponentValue 
{
	/**
	 * 
	 * @param value
	 * @param duration
	 * @param component
	 */
	protected ResourceRequirementValue(String value, long[] duration, DomainComponent component) {
		super(value, ComponentValueType.RESOURCE_REQUIREMENT, duration, true, component);
	}
}
