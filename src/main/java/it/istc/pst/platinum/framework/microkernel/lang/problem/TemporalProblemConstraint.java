package it.istc.pst.platinum.framework.microkernel.lang.problem;

import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalProblemConstraint extends ProblemConstraint 
{
	private long[][] bounds;
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 * @param bounds
	 */
	protected TemporalProblemConstraint(RelationType type, ProblemFluent reference, ProblemFluent target, long[][] bounds) {
		super(type, reference, target);
		// check type
		if (type.getCategory().equals(ConstraintCategory.PARAMETER_CONSTRAINT)) {
			throw new RuntimeException("Invalid relation type for temporal constraints " + type);
		}
		// set temporal bounds
		this.bounds = bounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[][] getBounds() {
		return bounds;
	}
}
