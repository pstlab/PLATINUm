package it.uniroma3.epsl2.framework.time.tn.solver.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
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
