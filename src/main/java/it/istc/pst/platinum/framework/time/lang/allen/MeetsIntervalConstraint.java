package it.istc.pst.platinum.framework.time.lang.allen;

import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.lang.BinaryTemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public final class MeetsIntervalConstraint extends BinaryTemporalConstraint<TemporalInterval, TemporalInterval> 
{
	private long lb;
	private long ub;
	
	/**
	 * 
	 */
	protected MeetsIntervalConstraint() {
		super(TemporalConstraintType.MEETS);
		this.lb = 0;
		this.ub = 0;
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
	 * @return
	 */
	public long getUpperBound() {
		return ub;
	}
}
