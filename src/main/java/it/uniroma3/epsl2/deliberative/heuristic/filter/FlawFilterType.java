package it.uniroma3.epsl2.deliberative.heuristic.filter;

import it.uniroma3.epsl2.deliberative.heuristic.filter.hff.HierarchyFlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.filter.sff.SemanticFlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.filter.tff.TypeFlawFilter;

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
	 * Dependency Graph Flaw Filter
	 */
	HFF(HierarchyFlawFilter.class.getName()),
	
	/**
	 * Temporal Knowledge-based Flaw Filter
	 */
	SFF(SemanticFlawFilter.class.getName());
	
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
