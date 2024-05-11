package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.ConstraintCategory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterProblemConstraint extends ProblemConstraint 
{
	private String referenceParameterLabel;
	private String targetParameterLabel;
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 * @param referenceLabel
	 * @param targetLabel
	 */
	protected ParameterProblemConstraint(RelationType type, ProblemFluent reference, ProblemFluent target, String referenceLabel, String targetLabel) {
		super(type, reference, target);
		// check type
		if (type.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT)) {
			throw new RuntimeException("Invalid relation type for parameter constraints " + type);
		}
		
		// set labels
		this.referenceParameterLabel = referenceLabel;
		this.targetParameterLabel = targetLabel;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getReferenceParameterLabel() {
		return referenceParameterLabel;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTargetParameterLabel() {
		return targetParameterLabel;
	}
}
