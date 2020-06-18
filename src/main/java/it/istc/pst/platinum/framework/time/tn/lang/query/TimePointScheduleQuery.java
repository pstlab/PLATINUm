package it.istc.pst.platinum.framework.time.tn.lang.query;

import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public final class TimePointScheduleQuery extends TimePointQuery {

	protected TimePoint point;
	
	/**
	 * 
	 */
	protected TimePointScheduleQuery() {
		super(TemporalQueryType.TP_SCHEDULE);
	}
	
	/**
	 * 
	 * @param point
	 */
	public void setTimePoint(TimePoint point) {
		this.point = point;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getTimePoint() {
		return point;
	}
//	
//	/**
//	 * 
//	 * @param lb
//	 */
//	public void setLb(long lb) {
//		this.point.setLowerBound(lb);
//	}
//	
//	/**
//	 * 
//	 * @param ub
//	 */
//	public void setUb(long ub) {
//		this.point.setUpperBound(ub);
//	}
}
