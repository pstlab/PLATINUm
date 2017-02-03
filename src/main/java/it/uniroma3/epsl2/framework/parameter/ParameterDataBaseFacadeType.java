package it.uniroma3.epsl2.framework.parameter;

import it.uniroma3.epsl2.framework.parameter.csp.CSPParameterDataBaseFacade;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterDataBaseFacadeType {

	/**
	 * 
	 */
	CSP_PARAMETER_FACADE(CSPParameterDataBaseFacade.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private ParameterDataBaseFacadeType(String cname) {
		this.cname = cname;
	}
	
	public String getFacadeClassName() {
		return this.cname;
	}
}
