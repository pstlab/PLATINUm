package it.istc.pst.platinum.framework.domain.component.resource.discrete;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceEvent;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceEventType;
import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class RequirementResourceEvent extends ResourceEvent<TemporalInterval>
{
	/**
	 * 
	 * @param activity
	 * @param amount
	 */
	protected RequirementResourceEvent(Decision activity, int amount) {
		super(ResourceEventType.REQUIREMENT, activity, amount, activity.getToken().getInterval());
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getStart() {
		// check activity start time
		return this.event.getStartTime();
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getEnd() {
		// get activity end time
		return this.event.getEndTime();
	}
	
	/**
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(ResourceEvent<?> o) {
		// get the other event
		RequirementResourceEvent other = (RequirementResourceEvent) o;
		// compare the amount required by the events
		return this.amount > other.amount ? -1 : this.amount < other.amount ? 1 : 0;
	}
}
