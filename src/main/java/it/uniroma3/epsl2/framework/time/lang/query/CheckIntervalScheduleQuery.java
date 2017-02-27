package it.uniroma3.epsl2.framework.time.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public final class CheckIntervalScheduleQuery extends TemporalIntervalQuery {

	private TemporalInterval interval;
	
	/**
	 * 
	 */
	protected CheckIntervalScheduleQuery() {
		super(TemporalQueryType.CHECK_SCHEDULE);
	}
	
	/**
	 * 
	 * @param i
	 */
	public void setInterval(TemporalInterval i) {
		this.interval = i;
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
	 * @param start
	 */
	public void setStartTimeSchedule(long[] start) {
		this.interval.getStartTime().setLowerBound(start[0]);
		this.interval.getStartTime().setUpperBound(start[1]);
	}
	
	/**
	 * 
	 * @param end
	 */
	public void setEndTimeSchedule(long[] end) {
		this.interval.getEndTime().setLowerBound(end[0]);
		this.interval.getEndTime().setUpperBound(end[1]);
	}
}