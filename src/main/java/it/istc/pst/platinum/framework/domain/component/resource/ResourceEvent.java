package it.istc.pst.platinum.framework.domain.component.resource;

import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.time.tn.TemporalData;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ResourceEvent<T extends TemporalData> implements Comparable<ResourceEvent<?>>
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	protected int id;
	protected Decision activity;
	protected ResourceEventType type;
	protected int amount;
	protected T event;
	
	/**
	 * 
	 * @param type
	 * @param activity
	 * @param amount
	 */
	protected ResourceEvent(ResourceEventType type, Decision activity, int amount, T event) 
	{
		// set id
		this.id = ID_COUNTER.getAndIncrement();
		this.activity = activity;
		this.type = type;
		this.amount = amount;
		this.event = event;
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
	public int getAmount() {
		return amount;
	}
	
	/**
	 * 
	 * @return
	 */
	public T getEvent() {
		return event;
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
	public int compareTo(ResourceEvent<?> o) {
		// compare the related temporal events
		return this.event.compareTo(o.event);
	}
	
	/**
	 * 
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
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceEvent<?> other = (ResourceEvent<?>) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		// get event description
		return "[ResourceEvent id= " + this.id +  " type= " + this.type + " amount= " + this.amount + "]";
	}
}
