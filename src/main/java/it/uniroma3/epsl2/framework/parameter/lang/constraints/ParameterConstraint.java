package it.uniroma3.epsl2.framework.parameter.lang.constraints;

import it.uniroma3.epsl2.framework.domain.component.Constraint;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ParameterConstraint extends Constraint
{
	protected ParameterConstraintType type;
	protected Parameter reference;
	protected Parameter target;
	
	/**
	 * 
	 * @param type
	 */
	protected ParameterConstraint(ParameterConstraintType type) {
		super(type.getSymbol(), type.getCategory());
		this.type = type;
	}
	
	/**
	 * 
	 * @param param
	 */
	public void setReference(Parameter param) {
		this.reference = param;
	}
	
	/**
	 * 
	 * @return
	 */
	public Parameter getReference() {
		return this.reference;
	}
			
	
	/**
	 * 
	 * @param param
	 */
	public void setTarget(Parameter param) {
		this.target = param;
	}
			
	
	/**
	 * 
	 * @return
	 */
	public Parameter getTarget() {
		return this.target;
	}
	
	/**
	 * 
	 * @return
	 */
	public ParameterConstraintType getType() {
		return type;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[ParameterConstraint label= \"" + this.label + "\" reference= " + this.reference.getLabel()  + " target= " + this.target.getLabel() + "]";
	}
}
