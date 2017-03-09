package it.uniroma3.epsl2.deliberative.heuristic;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawSelectionHeuristicType 
{
	/**
	 * Semantic and Hierarchy-based Flaw Selection Heuristic
	 */
	SHFSH(SemanticHierarchyFLawSelectionHeuristic.class.getName()),
	
	/**
	 * Hierarchy-based Flaw Selection Heuristic
	 */
	HFSH(HierarchicalFlawSelectionHeuristic.class.getName());
	
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
