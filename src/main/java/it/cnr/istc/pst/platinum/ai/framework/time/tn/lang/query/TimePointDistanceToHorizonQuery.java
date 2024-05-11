package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;

/**
 * 
 * @author alessandro
 *
 */
public final class TimePointDistanceToHorizonQuery extends TimePointQuery {
	
	private TimePoint tp;		// time pint
	private long[] distance;	// distance
	
	/**
	 * 
	 */
	protected TimePointDistanceToHorizonQuery() {
		super(TemporalQueryType.TP_DISTANCE_TO_HORIZON);
		this.distance = new long[2];
	}
	
	/**
	 * 
	 * @param tp
	 */
	public void setTimePoint(TimePoint tp) {
		this.tp = tp;
	}

	/**
	 * 
	 * @return
	 */
	public TimePoint getTimePoint() {
		return this.tp;
	}
	
	/**
	 * 
	 * @param dmin
	 */
	public void setDistance(long[] distance) {
		this.distance = distance;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getDistance() {
		return distance;
	}
}
