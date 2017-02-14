package it.uniroma3.epsl2.framework.domain.component.resource;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.ComponentValueType;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceConsumption extends ComponentValue 
{
	
	/**
	 * 
	 * @param value
	 * @param duration
	 * @param component
	 */
	protected ResourceConsumption(long[] duration, DomainComponent component) {
		super("CONSUMPTION", ComponentValueType.RESOURCE_CONSUMPTION, duration, true, component);
	}
}
