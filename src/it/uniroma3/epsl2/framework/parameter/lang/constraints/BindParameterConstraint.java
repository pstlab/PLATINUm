package it.uniroma3.epsl2.framework.parameter.lang.constraints;

/**
 * 
 * @author anacleto
 *
 */
public class BindParameterConstraint extends ParameterConstraint 
{
	private String value;
	
	/**
	 * 
	 */
	protected BindParameterConstraint() {
		super(ParameterConstraintType.BIND);
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
		return value;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[BindParameterConstraint label= \"" + this.label + "\" reference= " + this.reference.getLabel() + " value= " + this.value + "]";
	}
}
