package it.istc.pst.platinum.framework.domain.component.resource;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.ComponentValueType;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;

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
