package it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.reservoir;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEvent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEventType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;

/**
 * 
 * @author alessandro
 *
 */
public class ConsumptionResourceEvent extends ResourceEvent<TimePoint>
{
	/**
	 * 
	 * @param activity
	 * @param amount
	 */
	protected ConsumptionResourceEvent(Decision activity, int amount) {
		super(ResourceEventType.CONSUMPTION, activity, amount, activity.getToken().getInterval().getStartTime());
	}
}
