package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.EqualParameterConstraint;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraint;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraintFactory;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public class EqualParameterRelation extends ParameterRelation 
{
	private String targetParameterLabel;
	private ParameterConstraintFactory factory;
	
	/**
	 * 
	 * @param id
	 * @param reference
	 * @param target
	 */
	protected EqualParameterRelation(int id, Decision reference, Decision target) {
		super(id, RelationType.EQUAL_PARAMETER, reference, target);
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
	 * @param label
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
		return ParameterConstraintType.EQUAL;
	}
	
	/**
	 * 
	 */
	@Override
	public ParameterConstraint create() 
	{
		// create constraint
		EqualParameterConstraint constraint = this.factory.
				createParameterConstraint(ParameterConstraintType.EQUAL);
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
		// return constraint
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
