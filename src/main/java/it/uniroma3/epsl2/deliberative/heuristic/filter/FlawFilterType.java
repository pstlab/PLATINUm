package it.uniroma3.epsl2.deliberative.heuristic.filter;

import it.uniroma3.epsl2.deliberative.heuristic.filter.semantic.SemanticFlawFilter;

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
	TFF(TypeFlawFilter.class.getName(), "Type-based Flaw Filter"),
	
	/**
	 * Dependency Graph Flaw Filter
	 */
	HFF(HierarchyFlawFilter.class.getName(), "Dependecy Graph Flaw Filter"),
	
	/**
	 * Temporal Knowledge-based Flaw Filter
	 */
	SFF(SemanticFlawFilter.class.getName(), "Temporal Semantic Flaw Filter");
	
	private String cname;
	private String label;

	/**
	 * 
	 * @param cname
	 * @param label
	 */
	private FlawFilterType(String cname, String label) {
		this.cname = cname;
		this.label = label;
	}
	
	/*
	 * 
	 */
	public String getFilterClassName() {
		return cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
}
