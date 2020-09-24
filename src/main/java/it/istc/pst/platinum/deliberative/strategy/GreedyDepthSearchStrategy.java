package it.istc.pst.platinum.deliberative.strategy;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;

/**
 * 
 * @author anacleto
 *
 */
public class GreedyDepthSearchStrategy extends SearchStrategy 
{
	/**
	 * 
	 */
	protected GreedyDepthSearchStrategy() {
		super("GreedyDepthSearchStrategy");
	}
	
	/**
	 * 
	 */
	@Override
	public void enqueue(SearchSpaceNode node) 
	{
		// compute a pessimistic estimation of flaw resolution cost
		double hValue = this.computePlanningHeuristics(node);
		// set heuristic estimation
		node.setHeuristic(hValue);
		// add the node to the priority queue
		this.fringe.offer(node);
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) {
		// compare heuristics and makespan of nodes and use depth as last selection criterion
		return o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 : 
			o1.getHeuristic() < o2.getHeuristic() ? -1 : o1.getHeuristic() > o2.getHeuristic() ? 1 : 0; 
			 
	}
}
