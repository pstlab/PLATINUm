package it.cnr.istc.pst.platinum.ai.framework.time.lang.allen;

import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.BinaryTemporalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class DuringIntervalConstraint extends BinaryTemporalConstraint<TemporalInterval, TemporalInterval> 
{
	private long[] startTimeBounds;
	private long[] endTimeBounds;
	
	/**
	 * 
	 */
	protected DuringIntervalConstraint() {
		super(TemporalConstraintType.DURING);
		this.startTimeBounds = new long[2];
		this.endTimeBounds = new long[2];
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public void setStartTimeBound(long[] bounds) {
		this.startTimeBounds[0] = bounds[0];
		this.startTimeBounds[1] = bounds[1];
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public void setEndTimeBound(long[] bounds) {
		this.endTimeBounds[0] = bounds[0];
		this.endTimeBounds[1] = bounds[1];
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getFirstBound() {
		return startTimeBounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getSecondBound() {
		return endTimeBounds;
	}
}
