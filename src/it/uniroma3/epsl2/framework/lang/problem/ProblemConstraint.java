package it.uniroma3.epsl2.framework.lang.problem;

import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;

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
