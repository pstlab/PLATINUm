package it.istc.pst.platinum.framework.domain.component.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.ComponentValueType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ResourceUsageValue extends ComponentValue<ReservoirResource> 
{
	/**
	 * 
	 * @param type
	 * @param label
	 * @param duration
	 * @param controllable
	 * @param component
	 */
	protected ResourceUsageValue(ComponentValueType type, String label, long[] duration, boolean controllable, ReservoirResource component) {
		super(label, type, duration, controllable, component);
	}
}

/**
 * 
 * @author anacleto
 *
 */
class ResourceConsumptionValue extends ResourceUsageValue 
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

/**
 * 
 * @author anacleto
 *
 */
class ResourceProductionValue extends ResourceUsageValue
{
	/**
	 * 
	 * @param label
	 * @param duration
	 * @param controllable
	 * @param resource
	 */
	protected ResourceProductionValue(String label, long[] duration, boolean controllable, ReservoirResource resource) {
		super(ComponentValueType.RESOURCE_PRODUCTION, label,  duration, controllable, resource);
	}
}

