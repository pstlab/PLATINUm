package it.uniroma3.epsl2.planner.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.uniroma3.epsl2.planner.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.planner.solver.SearchSpaceNode;

/**
 * 
 * @author anacleto
 *
 */
public class DepthFirstDegreeSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> {

	private List<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected DepthFirstDegreeSearchStrategy() {
		super(SearchStrategyType.DFD);
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
			throws EmptyFringeException {
		
		// check the fringe
		if (this.fringe.isEmpty()) {
			throw new EmptyFringeException("No more node in the fringe");
		}
		
		// sort nodes in the fringe
		Collections.sort(this.fringe, this);
		
		// get the head of the stack
		return this.fringe.remove(0);
	}
	
	/**
	 * 
	 */
	@Override
	public void enqueue(SearchSpaceNode node) {
		// add the node at the head of the stack
		this.fringe.add(node);
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) {
		// compare depth
		if (o1.getDepth() != o2.getDepth()) {
			// prefer deepest nodes
			return o1.getDepth() > o2.getDepth() ? -1 : 1;
		}
		else { 
			// compare generator flaw degrees
			return o1.getFlaw().getDegree() <= o2.getFlaw().getDegree() ? -1 : 1;
		}
	}
}
