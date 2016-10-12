package it.uniroma3.epsl2.deliberative.search;

/**
 * 
 * @author anacleto
 *
 */
public enum SearchStrategyType {

	/**
	 * "Classical" Depth First search strategy
	 */
	DFS(DepthFirstSearchStrategy.class.getName()),
	
	/**
	 * 
	 */
	DFCF(DepthFirstCostFirstStrategy.class.getName()),
	
	/**
	 * 
	 */
	DIJKSTRA(DijkstraSearchStrategy.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private SearchStrategyType(String cname) {
		this.cname = cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStrategyClassName() {
		return this.cname;
	}
}
