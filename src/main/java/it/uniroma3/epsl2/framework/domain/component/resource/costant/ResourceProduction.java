package it.uniroma3.epsl2.framework.domain.component.resource.costant;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.ComponentValueType;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceProduction extends ComponentValue 
{
	/**
	 * 
	 * @param value
	 * @param duration
	 * @param component
	 */
	protected ResourceProduction(long[] duration, DomainComponent component) {
		super("PRODUCTION", ComponentValueType.RESOURCE_PRODUCTION, duration, true, component);
	}
}
