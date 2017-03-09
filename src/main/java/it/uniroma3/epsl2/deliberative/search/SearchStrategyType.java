package it.uniroma3.epsl2.deliberative.search;

/**
 * 
 * @author anacleto
 *
 */
public enum SearchStrategyType 
{
	/**
	 * 
	 */
	ASTAR(AStarSearchStrategy.class.getName()),
	
	/**
	 * Dijkstra search strategy. 
	 * 
	 * The strategy selects the nodes with the lowest generation cost. If two 
	 * nodes have the same cost then the strategy selects the node which is 
	 * deeper in the search space.
	 */
	DIJKSTRA(DijkstraSearchStrategy.class.getName()),
	
	/**
	 * Depth First search strategy. 
	 * 
	 * The strategy selects the nodes at the higher depth in the search space 
	 */
	DFS(DepthFirstSearchStrategy.class.getName());;
	
	private String className;
	
	/**
	 * 
	 * @param cname
	 */
	private SearchStrategyType(String cname) {
		this.className = cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getStrategyClassName() {
		return this.className;
	}
}
