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
	protected Parameter<?> reference;
	
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
	public void setReference(Parameter<?> param) {
		this.reference = param;
	}
	
	/**
	 * 
	 * @return
	 */
	public Parameter<?> getReference() {
		return this.reference;
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
	public abstract String toString();
}
