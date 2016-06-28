package it.uniroma3.epsl2.framework.time.tn.solver.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public final class TimePointDistanceQuery extends TimePointQuery {
	
	private TimePoint source;
	private TimePoint target;
	private long[] distance;
	
	/**
	 * 
	 */
	protected TimePointDistanceQuery() {
		super(TemporalQueryType.TP_DISTANCE);
		this.distance = new long[2];
	}
	
	/**
	 * 
	 * @param source
	 */
	public void setSource(TimePoint source) {
		this.source = source;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getSource() {
		return source;
	}
	
	/**
	 * 
	 * @param target
	 */
	public void setTarget(TimePoint target) {
		this.target = target;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getTarget() {
		return target;
	}
	
	public long[] getDistance() {
		return distance;
	}
	
	public void setDistance(long[] distance) {
		this.distance = distance;
	}
}
