package it.istc.pst.platinum.framework.microkernel.resolver.timeline.checking;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public class InvalidTransition extends Flaw {
	
	private Decision left;
	private Decision right;

	/**
	 * 
	 * @param sv
	 * @param left
	 * @param right
	 */
	protected InvalidTransition(StateVariable sv, Decision left, Decision right) {
		super(sv, FlawType.TIMELINE_BEHAVIOR_CHECKING);
		this.left = left;
		this.right = right;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isSolvable() {
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
		return "[InvalidTransition from= " + this.left + " to= " + this.right +"]";
	}
}
