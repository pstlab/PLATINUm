package it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.tkff;

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
	ORDERING("ordering"),
	
	/**
	 * 
	 */
	CAUSAL_RELATION("CausalRelation");
	
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

}
