package it.istc.pst.platinum.framework.domain.component.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.resource.ResourceEvent;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceProfileSample;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceUsageProfileSample extends ResourceProfileSample
{
	private ResourceEvent<?> event;
	private long schedule;
	private double amount;
	
	/**
	 * The amount is a number specifying the amount of resource consumed or produced by the event.
	 * 
	 * Note that a positive number of amount represents a resource production while a negative number 
	 * represents a resource consumption 
	 * 
	 * @param event
	 * @param schedule
	 * @param amount
	 */
	protected ResourceUsageProfileSample(ResourceEvent<?> event, long schedule, double amount) {
		super();
		this.event = event;			// the resource usage event
		this.schedule = schedule;	// the scheduled time of the event
		this.amount = amount;		// a negative amount implies a resource consumption, a resource production otherwise
	}
	
	/**
	 * Get the amount of resource produced or consumed by the event.
	 * 
	 * A positive number represents a resource production while a negative number represents a resource consumption
	 * 
	 * @return
	 */
	public double getAmount() {
		return amount;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResourceEvent<?> getEvent() {
		return event;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getSchedule() {
		return schedule;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ResourceProfileSample other) {
		// cast to usage sample
		ResourceUsageProfileSample o = (ResourceUsageProfileSample) other;
		// compare the time instant and the required amount (absolute values)
		return this.schedule < o.schedule ? -1 :
			this.schedule == o.schedule && this.amount < 0 && o.amount > 0 ? -1 :
				this.schedule == o.schedule && this.amount > 0 && o.amount < 0 ? 1 :
					this.schedule == o.schedule && (this.amount < 0 && o.amount < 0 || this.amount > 0 && o.amount > 0) && Math.abs(this.amount) > Math.abs(o.amount) ? -1 : 1;
	}
}
