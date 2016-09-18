package it.uniroma3.epsl2.deliberative.heuristic.fsh.filter;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawFilterType 
{
	/**
	 * Type-based Flaw Filter
	 */
	TFF(TypeFlawFilter.class.getName()),
	
	/**
	 * Hierarchy-based Flaw Filter
	 */
	HFF(HierarchyFlawFilter.class.getName()),
	
	/**
	 * Temporal Semantic-based Flaw Filter
	 */
	TSFF(TemporalSemanticFlawFilter.class.getName());
	
	private String cname;

	/**
	 * 
	 * @param cname
	 */
	private FlawFilterType(String cname) {
		this.cname = cname;
	}
	
	/*
	 * 
	 */
	public String getFilterClassName() {
		return cname;
	}
}
