package it.istc.pst.platinum.framework.domain.component.sv;

import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEventType;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class VariableFreeEvent extends ResourceEvent 
{
	/**
	 * 
	 * @param activity
	 * @param event
	 */
	protected VariableFreeEvent(Decision activity, TimePoint event) {
		super(ResourceEventType.PRODUCTION, event, 1, activity);
	}
}
