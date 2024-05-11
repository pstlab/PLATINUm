package it.cnr.istc.pst.platinum.ai.framework.time.lang;

import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;

/**
 * 
 * @author alessandro
 *
 */
public class FixIntervalDurationConstraint extends UnaryTemporalConstraint<TemporalInterval>
{
	private long duration;
	
	/**
	 * 
	 */
	protected FixIntervalDurationConstraint() {
		super(TemporalConstraintType.FIX_INTERVAL_DURATION);
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
