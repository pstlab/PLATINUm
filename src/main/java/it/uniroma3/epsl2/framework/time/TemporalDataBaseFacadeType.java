package it.uniroma3.epsl2.framework.time;

import it.uniroma3.epsl2.framework.time.tn.uncertainty.UncertaintyTemporalDataBaseFacade;

/**
 * 
 * @author anacleto
 *
 */
public enum TemporalDataBaseFacadeType {
	
	/**
	 * 
	 */
	UNCERTAINTY_TEMPORAL_FACADE(UncertaintyTemporalDataBaseFacade.class.getName());
	
	private String className;
	
	/**
	 * 
	 * @param className
	 */
	private TemporalDataBaseFacadeType(String className) {
		this.className = className;
	}
	
	/**
	 * 
	 * @return
	 */
	protected String getFacadeClassName() {
		return this.className;
	}

}