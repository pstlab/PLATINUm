package it.uniroma3.epsl2.deliberative.heuristic.fsh;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawSelectionHeuristicType {
	
	/**
	 * No criteria flaw selection
	 */
	BLIND(BlindFlawSelectionHeuristic.class.getName()),

	/**
	 * Flaw type-based selection heuristic
	 */
	TFSH(TypePreferencesFlawSelectionHeuristic.class.getName()),
	
	/**
	 * Flaw dependency-based selection heuristic 
	 */
	DgFSH(DependencyGraphFlawSelectionHeuristic.class.getName()),
	
	/**
	 * Flaw hierarchy-based selection heuristic, it combines TFSH
	 * and DgFSH evaluation criteria 
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
