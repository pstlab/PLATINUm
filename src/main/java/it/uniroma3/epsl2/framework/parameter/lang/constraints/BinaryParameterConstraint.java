package it.uniroma3.epsl2.framework.parameter.lang.constraints;

import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public abstract class BinaryParameterConstraint extends ParameterConstraint 
{
	protected Parameter<?> target;
	
	/**
	 * 
	 * @param type
	 */
	protected BinaryParameterConstraint(ParameterConstraintType type) {
		super(type);
	}
	
	/**
	 * 
	 * @param param
	 */
	public void setTarget(Parameter<?> param) {
		this.target = param;
	}
			
	
	/**
	 * 
	 * @return
	 */
	public Parameter<?> getTarget() {
		return this.target;
	}
}
