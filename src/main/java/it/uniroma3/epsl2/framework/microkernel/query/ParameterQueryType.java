package it.uniroma3.epsl2.framework.microkernel.query;

import it.uniroma3.epsl2.framework.parameter.lang.query.CheckValuesParameterQuery;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterQueryType {

	/**
	 * Check the current values of a parameter
	 */
	CHECK_PARAMETER_VALUES(CheckValuesParameterQuery.class.getName());
	
	private String cname;
	
	private ParameterQueryType(String cname) {
		this.cname = cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getQueryClassName() {
		return cname;
	}
}
