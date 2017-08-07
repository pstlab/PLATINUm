package it.istc.pst.platinum.framework.domain.component.resource.discrete;

import it.istc.pst.platinum.framework.domain.component.resource.ResourceProfileSample;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class RequirementResourceProfileSample extends ResourceProfileSample
{
	private RequirementResourceEvent event;
	private long start;
	private long end;
	
	/**
	 * 
	 * @param event
	 * @param start
	 * @param end
	 */
	protected RequirementResourceProfileSample(RequirementResourceEvent event, long start, long end) {
		super();
		this.event = event;
		this.start = start;
		this.end = end;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getDecision() {
		return this.event.getDecision();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getAmount() {
		return this.event.getAmount();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getStart() {
		return start;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getEnd() {
		return end;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ResourceProfileSample other) {
		// cast to requirement sample
		RequirementResourceProfileSample o = (RequirementResourceProfileSample) other;
		// compare the start time of requirement and the end time if necessary
		return this.start < o.start ? -1 : 
			this.start == o.start && this.end <= o.end ? -1 : 1;
	}
}
