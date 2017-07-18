package it.istc.pst.platinum.deliberative.heuristic.filter;

import it.istc.pst.platinum.deliberative.heuristic.filter.semantic.SemanticFlawFilter;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawFilterType 
{
	/**
	 * Temporal Knowledge-based Flaw Filter
	 */
	SFF(SemanticFlawFilter.class.getName(), "Temporal Semantic Flaw Filter"),
	
	/**
	 * Dependency Graph Flaw Filter
	 */
	HFF(HierarchyFlawFilter.class.getName(), "Dependecy Graph Flaw Filter"),
	
	/**
	 * The filter selects first the hardest-flaws to solve. Namely, the filter select the flaws
	 * with the lowest number of available solutions (see the Fail-First principle in CSP).
	 */
	DFF(DegreeFlawFilter.class.getName(), "Degree-based Flaw Filter"),
	
	/**
	 * Type-based Flaw Filter
	 */
	TFF(TypeFlawFilter.class.getName(), "Type-based Flaw Filter");
	
	
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
