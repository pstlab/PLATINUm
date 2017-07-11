package it.istc.pst.platinum.framework.domain.component.resource;

import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEventType;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class ConsumptionResourceEvent extends ResourceEvent 
{
	/**
	 * 
	 * @param activity
	 * @param event
	 * @param amount
	 */
	protected ConsumptionResourceEvent(Decision activity, TimePoint event, int amount) {
		super(ResourceEventType.CONSUMPTION, event, amount, activity);
	}
}
