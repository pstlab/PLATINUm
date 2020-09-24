package it.istc.pst.platinum.framework.microkernel.resolver.timeline.scheduling;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class PrecedenceConstraint extends FlawSolution 
{
	private Decision reference;			// reference decision of the precedence constraint
	private Decision target;			// target decision of the precedence constraint
	private double preserved;			// value computed by means of heuristics introduced in [Laborie 2005] 
	
	/**
	 * 
	 * @param set
	 * @param reference
	 * @param target
	 * @param cost
	 */
	protected PrecedenceConstraint(OverlappingSet set, Decision reference, Decision target, double cost) {
		super(set, cost);
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
		// JSON style object description
		return "{ \"type\": \"PRECEDENCE_CONSTRAINT\", "
		+ "\"reference\": " + this.reference + ", "
		+ "\"target\": " + this.target + " }";
	}

	/**
	 * 
	 */
	@Override
	public int compareTo(FlawSolution o) {
		PrecedenceConstraint pc = (PrecedenceConstraint) o;
		return this.preserved < pc.preserved ? -1 : this.preserved > pc.preserved ? 1 : 0;
	}
}
