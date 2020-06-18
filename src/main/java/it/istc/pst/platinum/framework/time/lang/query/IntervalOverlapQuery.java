package it.istc.pst.platinum.framework.time.lang.query;

import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public final class IntervalOverlapQuery extends TemporalIntervalQuery 
{
	private TemporalInterval reference;
	private TemporalInterval target;
	boolean canOverlap;
	
	/**
	 * 
	 */
	protected IntervalOverlapQuery() {
		super(TemporalQueryType.INTERVAL_OVERLAP);
		this.canOverlap = false;
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
	public boolean canOverlap() {
		return canOverlap;
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
	public void setCanOverlap(boolean overlaps) {
		this.canOverlap = overlaps;
	}
}
