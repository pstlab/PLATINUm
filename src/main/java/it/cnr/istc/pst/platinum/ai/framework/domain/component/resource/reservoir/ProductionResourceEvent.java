package it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.reservoir;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEvent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEventType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class ProductionResourceEvent extends ResourceEvent<TimePoint>
{
	/**
	 * 
	 * @param activity
	 * @param amount
	 */
	protected ProductionResourceEvent(Decision activity, int amount) {
		super(ResourceEventType.PRODUCTION, activity, amount, activity.getToken().getInterval().getEndTime());
	}
}
