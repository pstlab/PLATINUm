package it.istc.pst.platinum.framework.microkernel.resolver.resource.discrete;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

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
	 * @param cs
	 * @param reference
	 * @param target
	 * @param preserved
	 */
	protected PrecedenceConstraint(CriticalSet cs, Decision reference, Decision target) {
		super(cs);
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
	public double getCost() {
		// get property file 
		FilePropertyReader property = FilePropertyReader.getDeliberativePropertyFile();
		// read property
		String cost = property.getProperty("scheduling-cost");
		// parse and get double value
		return Double.parseDouble(cost);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[PrecedenceConstraint reference= " + this.reference +", target= " + this.target + ", preserved= " + this.preserved + "]";
	}
	
}
