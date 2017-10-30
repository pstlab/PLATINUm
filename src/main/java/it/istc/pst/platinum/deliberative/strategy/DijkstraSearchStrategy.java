package it.istc.pst.platinum.deliberative.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;

/**
 * 
 * @author anacleto
 *
 */
public class DijkstraSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> 
{
	private List<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected DijkstraSearchStrategy() {
		super(SearchStrategyType.DIJKSTRA.getLabel());
		// java7 compliant constructor
		this.fringe = new ArrayList<>();
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
		
		// sort fringe
		Collections.sort(this.fringe, this);
		// remove the first element of the queue
		return this.fringe.remove(0);
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
		return o1.getCost() <= o2.getCost() ? -1 : 1;
	}
}
