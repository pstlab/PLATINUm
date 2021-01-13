package it.cnr.istc.pst.platinum.ai.framework.parameter.lang;

import java.util.Arrays;

/**
 * 
 * @author anacleto
 *
 */
public final class EnumerationParameterDomain extends ParameterDomain 
{
	private String[] values;
	
	/**
	 * 
	 * @param name
	 */
	protected EnumerationParameterDomain(String name) {
		super(name, ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
		this.values = null;
	}
	
	/**
	 * 
	 * @param values
	 */
	public void setValues(String[] values) {
		this.values = values;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getValues() {
		return this.values;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getValue(int index) {
		// check index
		if (index >= this.values.length) {
			throw new RuntimeException("Index out of bound for EnumerationParameterDomain [index= " + index + "]");
		}
		
		// get value
		return this.values[index];
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public int getIndex(String value) {
		int index = 0;
		boolean found = false;
		while (index < this.values.length && !found) {
			// check current value
			if (this.values[index].equals(value)) {
				found = true;
			}
			else {
				index++;
			}
		}
		
		// get index
		return index;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[EnumerationParameterDomain name= " + this.getName() + " values= " + Arrays.asList(this.values) + "]";
	}
}
