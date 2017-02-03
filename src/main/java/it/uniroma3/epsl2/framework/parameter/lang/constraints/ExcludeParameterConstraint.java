package it.uniroma3.epsl2.framework.parameter.lang.constraints;

/**
 * 
 * @author anacleto
 *
 */
public class ExcludeParameterConstraint extends ParameterConstraint 
{
	private Object value;
	
	/**
	 * 
	 */
	protected ExcludeParameterConstraint() {
		super(ParameterConstraintType.EXCLUDE);
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
		return "[ExcludeParameterConstraint symbol= " + this.type.getSymbol() + "\n- reference= " + this.reference.getLabel() + "\n- value= " + this.value + "\n]";
	}
}
