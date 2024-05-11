package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;

/**
 * 
 * @author alessandro
 *
 */
public class PrecedenceConstraint implements Comparable<PrecedenceConstraint>
{
	private Decision reference;				// reference decision of the precedence constraint
	private Decision target;				// target decision of the precedence constraint
	private double preserved;				// value computed by means of heuristics introduced in [Laborie 2005]
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected PrecedenceConstraint(Decision reference, Decision target) {
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
				+ "\"target\": " + this.target + ", "
				+ "\"preserved-space-heuristic\": " + this.preserved + " }";
	}

	/**
	 * 
	 */
	@Override
	public int compareTo(PrecedenceConstraint pc) {
		// compare preserved space heuristic
		return this.preserved < pc.preserved ? -1 : this.preserved > pc.preserved ? 1 : 0;
	}
}
