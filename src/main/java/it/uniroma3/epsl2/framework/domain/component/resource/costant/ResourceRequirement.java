package it.uniroma3.epsl2.framework.domain.component.resource.costant;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.ComponentValueType;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceRequirement extends ComponentValue 
{
	/**
	 * 
	 * @param value
	 * @param duration
	 * @param component
	 */
	protected ResourceRequirement(long[] duration, DomainComponent component) {
		super("REQUIREMENT", ComponentValueType.RESOURCE_REQUIREMENT, duration, true, component);
	}
}
