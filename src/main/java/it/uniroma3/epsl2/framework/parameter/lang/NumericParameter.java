package it.uniroma3.epsl2.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public class NumericParameter extends Parameter<NumericParameterDomain>
{
	private int value;
	
	/**
	 * 
	 * @param label
	 * @param domain
	 */
	protected NumericParameter(String label, NumericParameterDomain domain) {
		super(label, ParameterType.NUMERIC_PARAMETER_TYPE, domain);
	}
	
	/**
	 * 
	 * @param dom
	 */
	protected NumericParameter(NumericParameterDomain dom) {
		super(ParameterType.NUMERIC_PARAMETER_TYPE, dom);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLowerBound() {
		return this.domain.getLowerBound();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getUpperBound() {
		return this.domain.getUpperBound();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[NumericParameter label= " + this.getLabel() + " value= "
				+ "" + this.value + " domain= " + this.domain + "]";
	}
}
