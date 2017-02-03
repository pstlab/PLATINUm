package it.uniroma3.epsl2.framework.parameter.lang.constraints;

/**
 * 
 * @author anacleto
 *
 */
public class BindParameterConstraint extends ParameterConstraint 
{
	private Object value;
	
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
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[BindParameterConstraint symbol= " + this.type.getSymbol() + "\n- reference= " + this.reference.getLabel() + "\n- value= " + this.value + "\n]";
	}
}
