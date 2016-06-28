package it.uniroma3.epsl2.framework.domain.component.pdb;

import it.uniroma3.epsl2.framework.lang.plan.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public class ParameterSynchronizationConstraint extends SynchronizationConstraint
{
	private String referenceLabel;
	private String targetLabel;
	
	/**
	 * 
	 * @param type
	 * @param source
	 * @param referenceLabel
	 * @param target
	 * @param targetLabel
	 */
	protected ParameterSynchronizationConstraint(RelationType type, TokenVariable source, String referenceLabel, TokenVariable target, String targetLabel) {
		super(type, source, target);
		this.referenceLabel = referenceLabel;
		this.targetLabel = targetLabel;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getReferenceLabel() {
		return this.referenceLabel;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTargetLabel() {
		return this.targetLabel;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ParameterSynchronizationConstraint type= " + this.type + " source= " + source + " target= " + target + "]";
	}
}
