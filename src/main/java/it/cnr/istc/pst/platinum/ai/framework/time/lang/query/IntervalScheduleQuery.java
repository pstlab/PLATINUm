package it.cnr.istc.pst.platinum.ai.framework.time.lang.query;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public final class IntervalScheduleQuery extends TemporalIntervalQuery {

	private TemporalInterval interval;
	
	/**
	 * 
	 */
	protected IntervalScheduleQuery() {
		super(TemporalQueryType.INTERVAL_SCHEDULE);
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
	
//	/**
//	 * 
//	 * @param duration
//	 */
//	public void setDuration(long[] duration) {
//		this.interval.setDurationLowerBound(duration[0]);
//		this.interval.setDurationUpperBound(duration[1]);
//	}
//	
//	/**
//	 * 
//	 * @param start
//	 */
//	public void setStartTimeSchedule(long[] start) {
//		this.interval.getStartTime().setLowerBound(start[0]);
//		this.interval.getStartTime().setUpperBound(start[1]);
//	}
//	
//	/**
//	 * 
//	 * @param end
//	 */
//	public void setEndTimeSchedule(long[] end) {
//		this.interval.getEndTime().setLowerBound(end[0]);
//		this.interval.getEndTime().setUpperBound(end[1]);
//	}
}
