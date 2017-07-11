package it.istc.pst.platinum.framework.domain.component.resource;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.ComponentValueType;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceConsumptionValue extends ComponentValue 
{
	
	/**
	 * 
	 * @param value
	 * @param duration
	 * @param component
	 */
	protected ResourceConsumptionValue(String value, long[] duration, DomainComponent component) {
		super(value, ComponentValueType.RESOURCE_CONSUMPTION, duration, true, component);
	}
}
