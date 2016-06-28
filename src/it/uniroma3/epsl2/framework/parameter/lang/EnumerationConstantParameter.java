package it.uniroma3.epsl2.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public class EnumerationConstantParameter extends Parameter 
{
	private String value;
	
	/**
	 * 
	 * @param label
	 * @param domain
	 */
	protected EnumerationConstantParameter(String label, EnumerationParameterDomain domain) {
		super(label, ParameterType.CONSTANT_ENUMERATION_PARAMETER_TYPE, domain);
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
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[EnumerationConstantParameter label= " + this.getLabel() + " value= " + this.value + " domain= " + this.domain + "]";
	}
}
