package it.istc.pst.platinum.deliberative.strategy;

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
	ASTAR(AStarSearchStrategy.class.getName(), "A* search strategy"),
	
	/**
	 * Dijkstra search strategy. 
	 * 
	 * The strategy selects the nodes with the lowest generation cost. If two 
	 * nodes have the same cost then the strategy selects the node which is 
	 * deeper in the search space.
	 */
	DIJKSTRA(DijkstraSearchStrategy.class.getName(), "Dijkstra search strategy"),
	
	/**
	 * Depth First Greedy strategy.
	 * 
	 * The strategy selects the nodes at the higher depth in the search space and 
	 * with the lowest cost function. 
	 */
	GREEDY(GreedyFirstSearchStrategy.class.getName(), "Depth First Greedy search strategy"),
	
	/**
	 * Depth First search strategy. 
	 * 
	 * The strategy selects the nodes at the higher depth in the search space 
	 */
	DFS(DepthFirstSearchStrategy.class.getName(), "Depth First search strategy");
	
	private String className;
	private String label;
	
	/**
	 * 
	 * @param cname
	 * @param label
	 */
	private SearchStrategyType(String cname, String label) {
		this.className = cname;
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClassName() {
		return this.className;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
}
