package it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;

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
