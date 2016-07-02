package it.uniroma3.epsl2.framework.time.lang;

import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public class FixEndTimeIntervalConstraint extends TemporalConstraint {

	private long end;
	
	/**
	 * 
	 */
	protected FixEndTimeIntervalConstraint() {
		super(TemporalConstraintType.FIX_END_TIME);
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
		this.end = bounds[0][0];
	}
	
	/**
	 * 
	 * @param end
	 */
	public void setEnd(long end) {
		this.end = end;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getEnd() {
		return end;
	}
}
