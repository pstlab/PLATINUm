package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class DecisionPrecedenceConstraint extends FlawSolution 
{
	private Decision reference;				// reference decision of the precedence constraint
	private Decision target;				// target decision of the precedence constraint
	
	/**
	 * 
	 * @param conflict
	 * @param reference
	 * @param target
	 * @param cost
	 */
	protected DecisionPrecedenceConstraint(BinaryDecisionConflict conflict, Decision reference, Decision target, double cost) {
		super(conflict, cost);
		this.reference = reference;
		this.target = target;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Decision getReference() {
		return reference;
	}

	/**
	 * 
	 * @return
	 */
	public Decision getTarget() {
		return target;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"type\": \"PRECEDENCE_CONSTRAINT\", "
				+ "\"reference\": " + this.reference + ", "
				+ "\"target\": " + this.target + " }";
	}
}
