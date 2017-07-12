package it.istc.pst.platinum.framework.time;

/**
 * 
 * @author anacleto
 *
 */
public enum TemporalFacadeType {
	
	/**
	 * 
	 */
	UNCERTAINTY_TEMPORAL_FACADE(UncertaintyTemporalFacade.class.getName());
	
	private String className;
	
	/**
	 * 
	 * @param className
	 */
	private TemporalFacadeType(String className) {
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