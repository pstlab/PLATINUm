package it.istc.pst.platinum.framework.microkernel.resolver.timeline.behavior;

import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class IncompleteBehavior extends Flaw {

	private Decision left;
	private Decision right;
	
	/**
	 * 
	 * @param sv
	 * @param left
	 * @param right
	 */
	protected IncompleteBehavior(StateVariable sv, Decision left, Decision right) {
		super(sv, FlawType.INVALID_BEHAVIOR);
		this.left = left;
		this.right = right;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isSolvable() {
		// unsolvable flaw
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getLeftDecision() {
		return left;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getRightDecision() {
		return right;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[IncompleteBehavior sv= " + this.getComponent() + " left= " + this.left +" right= " + this.right + "]";
	}
}
