package it.uniroma3.epsl2.deliberative.heuristic;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawSelectionHeuristicType 
{
	/**
	 * (optimized) Hierarchical flaw selection heuristic
	 */
	HFS(HierarchicalFlawSelectionHeuristic.class.getName(), "Hierarchical flaw selection heuristics"),
	
	/**
	 * Pipeline-based Filter flaw selection heuristic
	 */
	PIPELINE(PipelineFlawSelectionHeuristic.class.getName(), "General Pipeline-based flaw selection heuristics");
	
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
