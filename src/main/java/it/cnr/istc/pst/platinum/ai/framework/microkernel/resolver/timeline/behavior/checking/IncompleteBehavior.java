package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.checking;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;

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
	 * @param id
	 * @param sv
	 * @param left
	 * @param right
	 */
	protected IncompleteBehavior(int id, StateVariable sv, Decision left, Decision right) {
		super(id, sv, FlawType.TIMELINE_BEHAVIOR_CHECKING);
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
