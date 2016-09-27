package it.uniroma3.epsl2.framework.time.lang.allen;

import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public final class AfterIntervalConstraint extends TemporalConstraint 
{
	private long lb;
	private long ub;
	
	/**
	 * 
	 */
	protected AfterIntervalConstraint() {
		super(TemporalConstraintType.AFTER);
	}
	
	/**
	 * 
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.lb = bounds[0][0];
		this.ub = bounds[0][1];
	}
	
	/**
	 * 
	 * @param lb
	 */
	public void setLb(long lb) {
		this.lb = lb;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getLb() {
		return lb;
	}
	
	
	/**
	 * 
	 * @param ub
	 */
	public void setUb(long ub) {
		this.ub = ub;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getUb() {
		return ub;
	}
}
