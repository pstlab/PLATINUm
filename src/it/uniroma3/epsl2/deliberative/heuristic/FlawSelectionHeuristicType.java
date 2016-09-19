package it.uniroma3.epsl2.deliberative.heuristic;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawSelectionHeuristicType 
{
	/**
	 * 
	 */
	H1(H1SelectionHeuristic.class.getName()),
	
	/**
	 * 
	 */
	H2(H2SelectionHeuristic.class.getName()),
	
	/**
	 * 
	 */
	HFH(HierarchicalFlawSelectionHeuristic.class.getName());
	
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
