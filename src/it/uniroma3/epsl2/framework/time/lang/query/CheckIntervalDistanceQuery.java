package it.uniroma3.epsl2.framework.time.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.TemporalInterval;

/**
 * 
 * @author anacleto
 *
 */
public final class CheckIntervalDistanceQuery extends TemporalIntervalQuery {

	private TemporalInterval source;
	private TemporalInterval target;
	private long[] distance;
	
	/**
	 * 
	 */
	protected CheckIntervalDistanceQuery() {
		super(TemporalQueryType.CHECK_INTERVAL_DISTANCE);
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
	 * @param distance
	 */
	public void setIntervalDistance(long[] distance) {
		this.distance = distance;
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
