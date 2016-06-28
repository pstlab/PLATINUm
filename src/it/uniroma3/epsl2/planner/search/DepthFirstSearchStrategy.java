package it.uniroma3.epsl2.planner.search;

import java.util.Stack;

import it.uniroma3.epsl2.planner.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.planner.solver.SearchSpaceNode;

/**
 * 
 * @author anacleto
 *
 */
public class DepthFirstSearchStrategy extends SearchStrategy {

	private Stack<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected DepthFirstSearchStrategy() {
		super(SearchStrategyType.DFS);
		this.fringe = new Stack<>();
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
			throws EmptyFringeException {
		// check the fringe
		if (this.fringe.isEmpty()) {
			throw new EmptyFringeException("No more node in the fringe");
		}
		// get the head of the stack
		return this.fringe.pop();
	}
	
	/**
	 * 
	 */
	@Override
	public void enqueue(SearchSpaceNode node) {
		// add the node at the head of the stack
		this.fringe.push(node);
	}
}
