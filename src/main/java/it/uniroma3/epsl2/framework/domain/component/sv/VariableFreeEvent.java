package it.uniroma3.epsl2.framework.domain.component.sv;

import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.resource.ResourceEventType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

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
