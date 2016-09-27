package it.uniroma3.epsl2.framework.time.tn.solver.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public final class TimePointBoundQuery extends TimePointQuery {

	protected TimePoint point;
	
	/**
	 * 
	 */
	protected TimePointBoundQuery() {
		super(TemporalQueryType.TP_BOUND);
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
	
	/**
	 * 
	 * @param lb
	 */
	public void setLb(long lb) {
		this.point.setLowerBound(lb);
	}
	
	/**
	 * 
	 * @param ub
	 */
	public void setUb(long ub) {
		this.point.setUpperBound(ub);
	}
}
