package it.uniroma3.epsl2.framework.time.lang;

import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public class FixDurationIntervalConstraint extends TemporalConstraint {

	private long duration;
	
	/**
	 * 
	 */
	protected FixDurationIntervalConstraint() {
		super(TemporalConstraintType.FIX_DURATION);
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
		this.duration = bounds[0][0];
	}
	
	/**
	 * 
	 * @param duration
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDuration() {
		return duration;
	}
}
