package it.uniroma3.epsl2.deliberative.heuristic.fsh.filter;

import it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.hff.HierarchyFlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.tkff.TemporalKnowledgeFlawFilter;

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
	TKFF(TemporalKnowledgeFlawFilter.class.getName());
	
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
