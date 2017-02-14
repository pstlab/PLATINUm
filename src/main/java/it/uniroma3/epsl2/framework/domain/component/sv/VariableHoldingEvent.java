package it.uniroma3.epsl2.framework.domain.component.sv;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEventType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class VariableHoldingEvent extends ResourceEvent 
{
	/**
	 * 
	 * @param activity
	 * @param event
	 */
	protected VariableHoldingEvent(Decision activity, TimePoint event) {
		super(activity, event, ResourceEventType.CONSUMPTION, 1);
	}
}
