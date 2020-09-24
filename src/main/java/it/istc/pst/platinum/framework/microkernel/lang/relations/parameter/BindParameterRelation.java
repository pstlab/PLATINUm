package it.istc.pst.platinum.framework.microkernel.lang.relations.parameter;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.parameter.lang.constraints.BindParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintFactory;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintType;

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
	 * @param id
	 * @param reference
	 * @param target
	 */
	protected BindParameterRelation(int id, Decision reference, Decision target) {
		super(id, RelationType.BIND_PARAMETER, reference, target);
		// reflexive relation
		this.target = reference;
		this.factory = new ParameterConstraintFactory();
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
		return "{ \"type\": \"" + this.getType() + "\", \"id\": " + this.reference.getId() + ", \"parameter\": \"" + this.referenceParameterLabel +"\", \"value\": \"" + this.value+ "\" }";
	}
}
