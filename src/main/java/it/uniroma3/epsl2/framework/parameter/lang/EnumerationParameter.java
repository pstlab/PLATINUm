package it.uniroma3.epsl2.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public class EnumerationParameter extends Parameter<EnumerationParameterDomain> 
{
	private int value;
	
	/**
	 * 
	 * @param label
	 * @param domain
	 */
	protected EnumerationParameter(String label, EnumerationParameterDomain domain) {
		super(label, ParameterType.ENUMERATION_PARAMETER_TYPE, domain);
	}
	
	/**
	 * 
	 * @param dom
	 */
	protected EnumerationParameter(EnumerationParameterDomain dom) {
		super(ParameterType.ENUMERATION_PARAMETER_TYPE, dom);
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getDomainValues() {
		return this.domain.getValues();
	}

	/**
	 * 
	 * @return
	 */
	public String getValue() {
		// get label from value
		return this.domain.getValue(this.value);
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getValues() 
	{
		// get labels
		String[] vals = this.domain.getValues();
		// value indexes
		int[] indexes = new int[vals.length];
		for (int i = 0; i < vals.length; i++) {
			indexes[i] = i;
		}
		// get indexes
		return indexes;
	}
	
	/**
	 * 
	 * @param values
	 */
	public void setValue(String value) {
		int index = this.domain.getIndex(value);
		this.value = index;
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
		return "[EnumerationParameter label= " + this.getLabel() + " "
				+ "value=" + this.value + " domain= " + this.domain + "]";
	}
}
