package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.ConstraintCategory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ProblemConstraint 
{
	private RelationType type;
	private ProblemFluent reference;
	private ProblemFluent target;
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 * @param bounds
	 */
	protected ProblemConstraint(RelationType type, ProblemFluent reference, ProblemFluent target) {
		this.type = type;
		this.reference = reference;
		this.target = target;
	}
	
	/**
	 * 
	 * @return
	 */
	public RelationType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public ConstraintCategory getCategory() {
		return this.type.getCategory();
	}
	
	/**
	 * 
	 * @return
	 */
	public ProblemFluent getReference() {
		return reference;
	}
	
	/**
	 * 
	 * @return
	 */
	public ProblemFluent getTarget() {
		return target;
	}
}
