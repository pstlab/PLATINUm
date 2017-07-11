package it.istc.pst.platinum.deliberative.strategy;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;

/**
 * 
 * @author anacleto
 *
 */
public class DijkstraSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> 
{
	private Queue<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected DijkstraSearchStrategy() {
		super(SearchStrategyType.DIJKSTRA.getLabel());
		// java7 compliant constructor
		this.fringe = new PriorityQueue<SearchSpaceNode>(11, this);
	}
	
	/**
	 * 
	 */
	@Override
	public int getFringeSize() {
		return this.fringe.size();
	}
	
	/**
	 * 
	 */
	@Override
	public SearchSpaceNode dequeue() 
			throws EmptyFringeException 
	{
		// check the fringe
		if (this.fringe.isEmpty()) {
			throw new EmptyFringeException("No more nodes in the fringe");
		}
		
		// remove the first element of the queue
		return this.fringe.poll();
	}
	
	/**
	 * 
	 */
	@Override
	public void enqueue(SearchSpaceNode node) {
		// add the node to the priority queue
		this.fringe.add(node);
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) {
		// compare the costs of the nodes
		return o1.getCost() < o2.getCost() ? -1 : 
			o1.getCost() == o2.getCost() && o1.getDepth() >= o2.getDepth() ? -1 : 1;
	}
}
