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
public class ProductionResourceEvent extends ResourceEvent 
{
	/**
	 * 
	 * @param activity
	 * @param event
	 * @param amount
	 */
	protected ProductionResourceEvent(Decision activity, TimePoint event, int amount) {
		super(ResourceEventType.PRODUCTION, event, amount, activity);
	}
}
