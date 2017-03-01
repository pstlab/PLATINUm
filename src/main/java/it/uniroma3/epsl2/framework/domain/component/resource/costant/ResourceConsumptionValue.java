package it.uniroma3.epsl2.framework.domain.component.resource.costant;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.ComponentValueType;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;

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
