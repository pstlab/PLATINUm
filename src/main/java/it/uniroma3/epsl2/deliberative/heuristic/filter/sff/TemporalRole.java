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
	DURING("During"),
	
	/**
	 * 
	 */
	CONTAINS("Contains"),
	
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
