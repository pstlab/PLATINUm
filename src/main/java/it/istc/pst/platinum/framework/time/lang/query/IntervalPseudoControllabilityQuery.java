package it.istc.pst.platinum.framework.time.lang.query;

import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public class IntervalPseudoControllabilityQuery extends TemporalIntervalQuery 
{
	private TemporalInterval interval;
	private boolean pseudoControllable;
	
	/**
	 * 
	 */
	protected IntervalPseudoControllabilityQuery() {
		super(TemporalQueryType.INTERVAL_PSEUDO_CONTROLLABILITY);
		this.pseudoControllable = true;
	}
	
	/**
	 * 
	 * @param interval
	 */
	public void setInterval(TemporalInterval interval) {
		this.interval = interval;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalInterval getInterval() {
		return interval;
	}
	
	/**
	 * 
	 * @param duration
	 */
//	public void setDuration(long[] duration) {
//		this.interval.setDurationLowerBound(duration[0]);
//		this.interval.setDurationUpperBound(duration[1]);
//	}

	/**
	 * 
	 * @param pseudoControllable
	 */
	public void setPseudoControllable(boolean pseudoControllable) {
		this.pseudoControllable = pseudoControllable;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isPseudoControllable() {
		// check interval duration w.r.t. to domain specification
		return this.pseudoControllable;
	}
}
