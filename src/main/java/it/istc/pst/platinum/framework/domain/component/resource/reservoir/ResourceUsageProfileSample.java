package it.istc.pst.platinum.framework.domain.component.resource.reservoir;

import it.istc.pst.platinum.framework.domain.component.resource.ResourceProfileSample;

/**
 * 
 * @author anacleto
 *
 */
public class ResourceUsageProfileSample extends ResourceProfileSample
{
	private long amount;
	private long time;
	
	/**
	 * The amount is a number specifying the amount of resource consumed or produced by the event.
	 * 
	 * Note that a positive number of amount represents a resource production while a negative number 
	 * represents a resource consumption 
	 * 
	 * @param time
	 * @param amount
	 */
	protected ResourceUsageProfileSample(long time, long amount) {
		super();
		this.amount = amount;		// a negative amount implies a resource consumption, a resource production otherwise
		this.time = time;
	}
	
	/**
	 * Get the amount of resource produced or consumed by the event.
	 * 
	 * A positive number represents a resource production while a negative number represents a resource consumption
	 * 
	 * @return
	 */
	public long getAmount() {
		return amount;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ResourceProfileSample other) {
		// cast to usage sample
		ResourceUsageProfileSample o = (ResourceUsageProfileSample) other;
		// compare the time instant and the required amount (absolute values)
		return this.time < o.time ? -1 :
			this.time == o.time && Math.abs(this.amount) >= Math.abs(o.amount) ? -1 : 1;
	}
}
