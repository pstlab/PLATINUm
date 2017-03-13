package it.uniroma3.epsl2.framework.time.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public final class IntervalOverlapQuery extends TemporalIntervalQuery 
{
	private TemporalInterval reference;
	private TemporalInterval target;
	boolean overlapping;
	
	/**
	 * 
	 */
	protected IntervalOverlapQuery() {
		super(TemporalQueryType.INTERVAL_OVERLAP);
		this.overlapping = false;
	}
	
	/**
	 * 
	 * @param reference
	 */
	public void setReference(TemporalInterval reference) {
		this.reference = reference;
	}
	
	/**
	 * 
	 * @param target
	 */
	public void setTarget(TemporalInterval target) {
		this.target = target;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isOverlapping() {
		return overlapping;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalInterval getReference() {
		return reference;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalInterval getTarget() {
		return target;
	}
	
	/**
	 * 
	 * @param overlaps
	 */
	public void setOverlapping(boolean overlaps) {
		this.overlapping = overlaps;
	}
}
