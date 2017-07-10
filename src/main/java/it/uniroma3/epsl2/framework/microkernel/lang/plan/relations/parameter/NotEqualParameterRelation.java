package it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.parameter;

import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.NotEqualParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintFactory;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public class NotEqualParameterRelation extends ParameterRelation 
{
	private String targetParameterLabel;
	private ParameterConstraintFactory factory;
	
	/**
	 * 
	 * @param refrence
	 * @param target
	 */
	protected NotEqualParameterRelation(Decision reference, Decision target) {
		super(RelationType.NOT_EQUAL_PARAMETER, reference, target);
		this.factory = ParameterConstraintFactory.getInstance();
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
	public ParameterConstraintType getConstraintType() {
		return ParameterConstraintType.NOT_EQUAL;
	}
	
	/**
	 * 
	 */
	@Override
	public ParameterConstraint create() 
	{
		// create constraint
		NotEqualParameterConstraint constraint = this.factory.
				createParameterConstraint(ParameterConstraintType.NOT_EQUAL);
		// get index
		int index = this.reference.getParameterIndexByLabel(this.referenceParameterLabel);
		// set reference parameter
		constraint.setReference(this.reference.getParameterByIndex(index));
		// get index
		index = this.target.getParameterIndexByLabel(this.targetParameterLabel);
		// set target parameter
		constraint.setTarget(this.target.getParameterByIndex(index));
		// set and get constraint
		this.constraint = constraint;
		return constraint;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Relation type= " + this.getType() +" reference= " + this.reference.getId() + ":" + this.reference.getValue().getLabel() +" "
				+ "referenceParameter= " + this.referenceParameterLabel + " target= " +  this.target.getId() + ":" + this.target.getValue().getLabel() + " "
						+ "targetParameter= " + this.targetParameterLabel + "]";
	}
}
