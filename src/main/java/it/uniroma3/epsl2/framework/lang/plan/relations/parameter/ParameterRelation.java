package it.uniroma3.epsl2.framework.lang.plan.relations.parameter;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterFactory;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ParameterRelation extends Relation 
{
	protected String referenceParameterLabel;
	protected String targetParameterLabel;
	protected ParameterFactory factory;
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 */
	protected ParameterRelation(RelationType type, Decision reference, Decision target) {
		super(type, reference, target);
		this.factory = ParameterFactory.getInstance();
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setReferenceParameterLabel(String label) {
		this.referenceParameterLabel = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getReferenceParameterLabel() {
		return this.referenceParameterLabel;
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setTargetParameterLabel(String label) {
		this.targetParameterLabel = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTargetParameterLabel() {
		return this.targetParameterLabel;
	}
	
	/**
	 * 
	 */
	@Override
	public ParameterConstraint getConstraint() {
		// cast constraint
		return (ParameterConstraint) this.constraint;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract ParameterConstraintType getConstraintType();
	
	/**
	 * 
	 */
	@Override
	public abstract ParameterConstraint create();
	
}
