package it.istc.pst.platinum.framework.microkernel.lang.relations.parameter;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.parameter.lang.constraints.NotEqualParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintFactory;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintType;

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
	 * @param id
	 * @param reference
	 * @param target
	 */
	protected NotEqualParameterRelation(int id, Decision reference, Decision target) {
		super(id, RelationType.NOT_EQUAL_PARAMETER, reference, target);
		this.factory = new ParameterConstraintFactory();
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
		return "{ \"type\": \"" + this.getType() +"\", "
				+ "\"refId\": " + this.reference.getId() + ", "
				+ "\"refParameter: \"" + this.referenceParameterLabel + "\", "
				+ "\"targetId\": " +  this.target.getId() +  ", "
				+ "\"targetParameter\": \"" + this.targetParameterLabel + "\" }";
	}
}
