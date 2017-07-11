package it.istc.pst.platinum.framework.time.lang.allen;

import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.lang.BinaryTemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class EndsDuringIntervalConstraint extends BinaryTemporalConstraint<TemporalInterval, TemporalInterval> 
{
	private long[] firstTimeBounds;
	private long[] secondTimeBounds;
	
	/**
	 * 
	 */
	protected EndsDuringIntervalConstraint() {
		super(TemporalConstraintType.ENDS_DURING);
		this.firstTimeBounds = new long[2];
		this.secondTimeBounds = new long[2];
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public void setFirstBound(long[] bound) {
		this.firstTimeBounds[0] = bound[0];
		this.firstTimeBounds[1] = bound[1];
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public void setSecondBound(long[] bound) {
		this.secondTimeBounds[0] = bound[0];
		this.secondTimeBounds[1] = bound[1];
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getFirstTimeBound() {
		return this.firstTimeBounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getSecondTimeBound() {
		return this.secondTimeBounds;
	}
}
