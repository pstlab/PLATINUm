package it.uniroma3.epsl2.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public class EnumerationParameter extends Parameter 
{
	private String[] value;
	
	/**
	 * 
	 * @param label
	 * @param domain
	 */
	protected EnumerationParameter(String label, EnumerationParameterDomain domain) {
		super(label, ParameterType.ENUMERATION_PARAMETER_TYPE, domain);
		this.value = domain.getValues();
	}

	/**
	 * 
	 * @return
	 */
	public String[] getValue() {
		return this.value;
	}
	
	/**
	 * 
	 * @param values
	 */
	public void setValue(String[] value) {
		this.value = value;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[EnumerationParameter label= " + this.getLabel() + " values=" + this.value + " domain= " + this.domain + "]";
	}
}
