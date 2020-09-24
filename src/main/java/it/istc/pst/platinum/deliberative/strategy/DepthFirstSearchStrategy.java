package it.istc.pst.platinum.deliberative.strategy;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;

/**
 * 
 * @author anacleto
 *
 */
public class DepthFirstSearchStrategy extends SearchStrategy 
{
	/**
	 * 
	 */
	protected DepthFirstSearchStrategy() {
		super("DepthFirstSearchStrategy");
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
		// compare node depth
		return o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 : 0;
	}
}
