package it.istc.pst.platinum.framework.domain.component.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.ComponentValueType;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceConsumptionValue extends ResourceUsageValue 
{
	/**
	 * 
	 * @param label
	 * @param duration
	 * @param controllable
	 * @param resource
	 */
	protected ResourceConsumptionValue(String label, long[] duration, boolean controllable, ReservoirResource resource) {
		super(ComponentValueType.RESOURCE_CONSUMPTION, label,  duration, controllable, resource);
	}
}