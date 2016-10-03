package it.uniroma3.epsl2.deliberative.heuristic.filter.sff;

/**
 * 
 * @author anacleto
 *
 */
enum TemporalRole 
{
	/**
	 * 
	 */
	BEFORE("Before"),

	/**
	 * 
	 */
	AFTER("After"),
	
	/**
	 * 
	 */
	DURING("During"),
	
	/**
	 * 
	 */
	CONTAINS("Contains"),
	
	/**
	 * 
	 */
	STARTS_DURING("StartsDuring"),
	
	/**
	 * 
	 */
	ENDS_DURING("EndsDuring"),
	
	/**
	 * 
	 */
	CAUSAL_LINK("CausalLink"),
	
	/**
	 * 
	 */
	ORDERING("ordering");

	private String label;
	
	/**
	 * 
	 * @param label
	 */
	private TemporalRole(String label) {
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.getLabel();
	}

}
