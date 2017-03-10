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
	HFS(HierarchicalFlawSelectionHeuristic.class.getName()),
	
	/**
	 * Pipeline-based Filter flaw selection heuristic
	 */
	PIPELINE(PipelineFlawSelectionHeuristic.class.getName());
	
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
