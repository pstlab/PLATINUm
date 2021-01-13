package it.cnr.istc.pst.platinum.ai.framework.time.lang.allen;

import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.BinaryTemporalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class StartsDuringIntervalConstraint extends BinaryTemporalConstraint<TemporalInterval, TemporalInterval> 
{
	private long[] firstTimeBounds;
	private long[] secondTimeBounds;
	
	/**
	 * 
	 */
	protected StartsDuringIntervalConstraint() {
		super(TemporalConstraintType.STARTS_DURING);
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
