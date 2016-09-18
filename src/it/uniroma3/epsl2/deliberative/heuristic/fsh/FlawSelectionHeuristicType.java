package it.uniroma3.epsl2.deliberative.heuristic.fsh;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawSelectionHeuristicType 
{
	/**
	 * Flaw type-based selection heuristic
	 */
	TFSH(TypeFlawSelectionHeuristic.class.getName()),
	
	/**
	 * Flaw dependency-based selection heuristic 
	 */
	HFSH(HierarchyFlawSelectionHeuristic.class.getName()),
	
	/**
	 * Flaw hierarchy-based selection heuristic, it combines TFSH
	 * and DgFSH evaluation criteria 
	 */
	HTFSH(HierarchyTypeFlawSelectionHeuristic.class.getName());
	
	private String cname;
	
	private FlawSelectionHeuristicType(String cname) {
		this.cname = cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHeuristicClassName() {
		return this.cname;
	}
}
