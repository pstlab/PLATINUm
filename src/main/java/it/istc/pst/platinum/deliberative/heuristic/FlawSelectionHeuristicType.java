package it.istc.pst.platinum.deliberative.heuristic;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawSelectionHeuristicType 
{
	/**
	 * Hierarchical search & build flaw selection heuristics
	 */
	SEARCH_AND_BUILD(SearchAndBuildFlawSelectionHeuristic.class.getName(), 
			"Hierarchical Search & Build (S&B) flaw selection heuristics"),

	/**
	 * (optimized) Hierarchical flaw selection heuristic
	 */
	HIERARCHICAL(HierarchicalSearchHeuristic.class.getName(), 
			"Hierarchical Search flaw selection heuristics"),
	
	/**
	 * Pipeline-based Filter flaw selection heuristic
	 */
	PIPELINE(PipelineFlawSelectionHeuristic.class.getName(), 
			"General Pipeline-based flaw selection heuristics");
	
	private String cname;
	private String label;
	
	/**
	 * 
	 * @param cname
	 * @param label
	 */
	private FlawSelectionHeuristicType(String cname, String label) {
		this.cname = cname;
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHeuristicClassName() {
		return this.cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
}
