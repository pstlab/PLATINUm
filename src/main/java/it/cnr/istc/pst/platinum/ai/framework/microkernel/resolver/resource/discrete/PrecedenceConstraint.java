package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.resource.discrete;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class PrecedenceConstraint extends FlawSolution 
{
	private Decision reference;				// reference decision of the precedence constraint
	private Decision target;				// target decision of the precedence constraint
	private double preserved;				// value computed by means of heuristics introduced in [Laborie 2005]
	
	/**
	 * 
	 * @param cs
	 * @param reference
	 * @param target
	 * @param preserved
	 */
	protected PrecedenceConstraint(CriticalSet cs, Decision reference, Decision target, double cost) {
		super(cs, cost);
		this.reference = reference;
		this.target = target;
	}
	
	/**
	 * 
	 * @param preserved
	 */
	public void setPreservedSpace(double preserved) {
		this.preserved = preserved;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPreservedValue() {
		return this.preserved;
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
				+ "\"reference\": " + this.reference +", "
				+ "\"target\": " + this.target + ", "
				+ "\"preserved:\": " + this.preserved + " }";
	}
	
}
