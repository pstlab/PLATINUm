package it.uniroma3.epsl2.framework.domain.component.resource.costant;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEventType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

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
