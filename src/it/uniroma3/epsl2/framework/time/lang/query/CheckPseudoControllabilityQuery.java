package it.uniroma3.epsl2.framework.time.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public class CheckPseudoControllabilityQuery extends TemporalIntervalQuery {

	private TemporalInterval interval;
	
	/**
	 * 
	 */
	protected CheckPseudoControllabilityQuery() {
		super(TemporalQueryType.CHECK_PSEUDO_CONTROLLABILITY);
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
	public void setDuration(long[] duration) {
		this.interval.setDurationLowerBound(duration[0]);
		this.interval.setDurationUpperBound(duration[1]);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isPseudoControllable() {
		// check interval duration w.r.t. to domain specification
		return this.interval.getDurationLowerBound() == this.interval.getNominalDurationLowerBound() &&
				this.interval.getDurationUpperBound() == this.interval.getNominalDurationUpperBound();
	}
}
