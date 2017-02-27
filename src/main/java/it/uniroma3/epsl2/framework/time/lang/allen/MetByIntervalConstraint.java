package it.uniroma3.epsl2.framework.time.lang.allen;

import it.uniroma3.epsl2.framework.time.TemporalInterval;
import it.uniroma3.epsl2.framework.time.lang.BinaryTemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public final class MetByIntervalConstraint extends BinaryTemporalConstraint<TemporalInterval, TemporalInterval> 
{
	private long lb;
	private long ub;
	
	/**
	 * 
	 */
	protected MetByIntervalConstraint() {
		super(TemporalConstraintType.MET_BY);
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
