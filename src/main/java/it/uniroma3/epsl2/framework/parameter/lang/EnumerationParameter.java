package it.uniroma3.epsl2.framework.parameter.lang;

import java.util.Arrays;

/**
 * 
 * @author anacleto
 *
 */
public class EnumerationParameter extends Parameter<EnumerationParameterDomain> 
{
	private int[] values;
	
	/**
	 * 
	 * @param label
	 * @param domain
	 */
	protected EnumerationParameter(String label, EnumerationParameterDomain domain) {
		super(label, ParameterType.ENUMERATION_PARAMETER_TYPE, domain);
		// initialize values from the domain
		this.values = new int[this.domain.getValues().length];
		for (int index= 0; index < this.values.length; index++) {
			this.values[index] = index;
		}
	}
	
	/**
	 * 
	 * @param dom
	 */
	protected EnumerationParameter(EnumerationParameterDomain dom) {
		super(ParameterType.ENUMERATION_PARAMETER_TYPE, dom);
		// initialize values from the domain
		this.values = new int[this.domain.getValues().length];
		for (int index= 0; index < this.values.length; index++) {
			this.values[index] = index;
		}
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
	public String[] getValues() 
	{
		// get label from value
		String[] list = new String[this.values.length];
		for (int i= 0; i < this.values.length; i++) {
			// get value label
			String val = this.domain.getValue(this.values[i]);
			list[i] = val;
		}
		// get the list
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getValueIndexes() {
		return this.values;
	}
	
	/**
	 * 
	 * @param values
	 */
	public void setValues(String[] vals) 
	{
		// list of indexes
		this.values = new int[vals.length];
		for (int i= 0; i < vals.length; i++) {
			// get value index
			int index = this.domain.getIndex(vals[i]);
			this.values[i] = index;
		}
	}
	
	/**
	 * 
	 * @param vals
	 */
	public void setValues(int[] vals) {
		this.values = vals;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[EnumerationParameter label= " + this.getLabel() + " "
				+ "value=" + Arrays.asList(this.values) + " domain= " + this.domain + "]";
	}
}
