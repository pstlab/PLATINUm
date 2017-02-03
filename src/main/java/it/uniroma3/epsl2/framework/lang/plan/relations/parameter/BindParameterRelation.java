package it.uniroma3.epsl2.framework.lang.plan.relations.parameter;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.BindParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintFactory;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public class BindParameterRelation extends ParameterRelation 
{
	private String value;
	private ParameterConstraintFactory factory;

	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected BindParameterRelation(Decision reference, Decision target) {
		super(RelationType.BIND_PARAMETER, reference, target);
		// reflexive relation
		this.target = reference;
		this.factory = ParameterConstraintFactory.getInstance();
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public ParameterConstraintType getConstraintType() {
		return ParameterConstraintType.BIND;
	}
	
	/**
	 * 
	 */
	@Override
	public ParameterConstraint create() 
	{
		// create constraint
		BindParameterConstraint constraint = this.factory.
				createParameterConstraint(ParameterConstraintType.BIND);
		
		// get index
		int index = this.reference.getParameterIndexByLabel(this.referenceParameterLabel);
		// set reference parameter
		constraint.setReference(this.reference.getParameterByIndex(index));
		// set the value
		constraint.setValue(this.value);
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
				+ "referenceParameter= " + this.referenceParameterLabel + " value= " +  this.value + "]";
	}
}
