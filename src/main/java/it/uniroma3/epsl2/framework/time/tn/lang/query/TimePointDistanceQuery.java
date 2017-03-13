package it.uniroma3.epsl2.framework.time.tn.lang.query;

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
	
	/**
	 * 
	 * @param value
	 */
	public void setDistanceLowerBound(long value) {
		this.distance[0] = value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setDistanceUpperBound(long value) {
		this.distance[1] = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDistanceLowerBound() { 
		return this.distance[0];
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDistanceUpperBound() {
		return this.distance[1];
	}
}
