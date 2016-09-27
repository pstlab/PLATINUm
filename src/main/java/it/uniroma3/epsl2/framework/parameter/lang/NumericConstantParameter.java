package it.uniroma3.epsl2.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public class NumericConstantParameter extends Parameter 
{
	private int value;
	
	/**
	 * 
	 * @param label
	 * @param domain
	 */
	protected NumericConstantParameter(String label, NumericParameterDomain domain) {
		super(label, ParameterType.CONSTANT_NUMERIC_PARAMETER_TYPE, domain);
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
	 * @return
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[NumericConstantParameter label= " + this.getLabel() + " "
				+ "value= " + this.value + " domain= " + this.domain + "]";
	}
}
