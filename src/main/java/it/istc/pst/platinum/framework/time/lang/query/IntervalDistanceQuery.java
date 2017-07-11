package it.istc.pst.platinum.framework.time.lang.query;

import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public final class IntervalDistanceQuery extends TemporalIntervalQuery {

	private TemporalInterval source;
	private TemporalInterval target;
	private long[] distance;
	
	/**
	 * 
	 */
	protected IntervalDistanceQuery() {
		super(TemporalQueryType.INTERVAL_DISTANCE);
		this.distance = new long[2];
	}
	
	/**
	 * 
	 * @param source
	 */
	public void setSource(TemporalInterval source) {
		this.source = source;
	}
	
	/**
	 * 
	 * @param target
	 */
	public void setTarget(TemporalInterval target) {
		this.target = target;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalInterval getSource() {
		return source;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalInterval getTarget() {
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
	 * @return
	 */
	public long getDistanceLowerBound() {
		return this.distance[0];
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setDistanceUpperBOund(long value) {
		this.distance[1] = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDistanceUpperBound() {
		return this.distance[1];
	}
}
