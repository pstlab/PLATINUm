package it.istc.pst.platinum.framework.parameter;

import it.istc.pst.platinum.framework.parameter.csp.CSPParameterDataBaseFacade;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterFacadeType {

	/**
	 * 
	 */
	CSP_PARAMETER_FACADE(CSPParameterDataBaseFacade.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private ParameterFacadeType(String cname) {
		this.cname = cname;
	}
	
	public String getFacadeClassName() {
		return this.cname;
	}
}
