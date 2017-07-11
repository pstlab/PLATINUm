package it.istc.pst.platinum.framework.microkernel.lang.plan.resource;

import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ResourceEvent implements Comparable<ResourceEvent>
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	private int id;
	private Decision activity;
	private TimePoint event;
	private ResourceEventType type;
	private int amount;
	
	/**
	 * 
	 * @param type
	 * @param event
	 * @param amount
	 * @param activity
	 */
	protected ResourceEvent(ResourceEventType type, TimePoint event, int amount, Decision activity) 
	{
		// set id
		this.id = ID_COUNTER.getAndIncrement();
		this.activity = activity;
		this.event = event;
		this.type = type;
		this.amount = amount;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getDecision() {
		return activity;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getEvent() {
		return event;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResourceEventType getType() {
		return type;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ResourceEvent o) {
		return this.event.getLowerBound() <= o.event.getLowerBound() ? -1 : 1;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceEvent other = (ResourceEvent) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() 
	{
		// get event description
		return "[ResourceEvent id= " + this.id +  " type= " + this.type + " amount= " + this.amount + "\n"
				+ "- activity= " + this.activity.getValue().getLabel() + "\n- event= " + this.event + "\n]";
	}
}
