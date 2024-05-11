package it.cnr.istc.pst.platinum.ai.framework.time.lang.allen;

import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.BinaryTemporalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public final class BeforeIntervalConstraint extends BinaryTemporalConstraint<TemporalInterval, TemporalInterval> 
{
	private long lb;
	private long ub;
	
	/**
	 * 
	 */
	protected BeforeIntervalConstraint() {
		super(TemporalConstraintType.BEFORE);
	}
	
	
	/**
	 * 
	 * @param lb
	 */
	public void setLowerBound(long lb) {
		this.lb = lb;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getLowerBound() {
		return lb;
	}
	
	
	/**
	 * 
	 * @param ub
	 */
	public void setUpperBound(long ub) {
		this.ub = ub;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getUpperBound() {
		return ub;
	}
}
