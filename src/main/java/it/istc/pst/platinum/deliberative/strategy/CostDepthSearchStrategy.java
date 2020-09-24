package it.istc.pst.platinum.deliberative.strategy;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;

/**
 * 
 * @author anacleto
 *
 */
public class CostDepthSearchStrategy extends SearchStrategy
{
	/**
	 * 
	 */
	protected CostDepthSearchStrategy() {
		super("CostDepthSearchStrategy");
	}
	
	/**
	 * 
	 */
	@Override
	public void enqueue(SearchSpaceNode node) {
		// add the node to the priority queue
		this.fringe.offer(node);
	}
	
	
	/**
	 * 
	 */
	@Override
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) {
		// compare node depths and costs
		return o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 : 
			o1.getCost() < o2.getCost() ? -1 : o1.getCost() > o2.getCost() ? 1 : 0;
	}
}
