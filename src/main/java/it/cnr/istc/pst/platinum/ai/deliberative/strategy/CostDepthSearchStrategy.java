package it.cnr.istc.pst.platinum.ai.deliberative.strategy;

import it.cnr.istc.pst.platinum.ai.deliberative.solver.SearchSpaceNode;

/**
 * 
 * @author alessandro
 *
 */
public class CostDepthSearchStrategy extends SearchStrategy {
	
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
		return o1.getPlanCost() < o2.getPlanCost() ? -1 : o1.getPlanCost() > o2.getPlanCost() ? 1 :
			o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 : 0;
	}
}
