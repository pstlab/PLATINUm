package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.plan;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;

/**
 * 
 * @author alessandro
 *
 */
public class GoalSchedule {
	
	private Decision left; 
	private Decision right;
	private long lb;
	private long ub;
	
	/**
	 * 
	 * @param left
	 * @param right
	 * @param lb
	 * @param ub
	 */
	protected GoalSchedule(Decision left, Decision right, long lb, long ub) {
		this.left = left;
		this.right = right;
		this.lb = lb;
		this.ub = ub;
	}

	/**
	 * 
	 * @return
	 */
	public Decision getLeft() {
		return left;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getRight() {
		return right;
	}
	
	public long getLowerBound() {
		return lb;
	}
	
	public long getUpperBound() {
		return ub;
	}
}
