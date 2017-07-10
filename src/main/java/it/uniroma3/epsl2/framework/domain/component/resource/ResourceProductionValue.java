package it.uniroma3.epsl2.framework.domain.component.resource;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.ComponentValueType;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceProductionValue extends ComponentValue 
{
	/**
	 * 
	 * @param value
	 * @param duration
	 * @param component
	 */
	protected ResourceProductionValue(String value, long[] duration, DomainComponent component) {
		super(value, ComponentValueType.RESOURCE_PRODUCTION, duration, true, component);
	}
}
