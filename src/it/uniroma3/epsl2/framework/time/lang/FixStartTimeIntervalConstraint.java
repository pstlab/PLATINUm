package it.uniroma3.epsl2.framework.time.lang;

import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public class FixStartTimeIntervalConstraint extends TemporalConstraint {

	private long start;
	
	/**
	 * 
	 */
	protected FixStartTimeIntervalConstraint() {
		super(TemporalConstraintType.FIX_START_TIME);
	}
	
	/**
	 * 
	 */
	@Override
	public void setReference(TemporalInterval reference) {
		this.reference = reference;
	}
	
	/**
	 * 
	 */
	@Override
	public void setTarget(TemporalInterval target) {
		this.reference = target;
	}
	
	/**
	 * 
	 */
	@Override
	public TemporalInterval getReference() {
		return this.reference;
	}
	
	/**
	 * 
	 */
	@Override
	public TemporalInterval getTarget() {
		return this.reference;
	}
	
	/**
	 * 
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.start = bounds[0][0];
	}
	
	/**
	 * 
	 * @param start
	 */
	public void setStart(long start) {
		this.start = start;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getStart() {
		return start;
	}
}
