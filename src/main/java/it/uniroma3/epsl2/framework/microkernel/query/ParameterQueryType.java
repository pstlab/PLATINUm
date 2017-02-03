package it.uniroma3.epsl2.framework.microkernel.query;

import it.uniroma3.epsl2.framework.parameter.lang.query.CheckValuesParameterQuery;
import it.uniroma3.epsl2.framework.parameter.lang.query.ComputeSolutionParameterQuery;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterQueryType {

	/**
	 * Check the current values of a parameter
	 */
	CHECK_PARAMETER_VALUES(CheckValuesParameterQuery.class.getName()),
	
	/**
	 * Compute possible assignments of all parameters
	 */
	COMPUTE_SOLUTION(ComputeSolutionParameterQuery.class.getName());
	
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
