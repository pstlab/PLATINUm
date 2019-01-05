package it.istc.pst.platinum.deliberative.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.istc.pst.platinum.deliberative.solver.PartialPlan;
import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;

/**
 * 
 * @author anacleto
 *
 */
public class MakespanOptimizationSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> 
{
	private List<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected MakespanOptimizationSearchStrategy() {
		super("SearchStrategy:MakespanOptimization");
		this.fringe = new ArrayList<SearchSpaceNode>();
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
		
		// sort elements of the list
		Collections.sort(this.fringe, this);
		// remove the first element of the queue
		SearchSpaceNode best = this.fringe.remove(0);
		// debug information
		debug("[" + this.label + "] Selected node from the fringe:\n"
				+ "- node: " + best + "\n"
				+ "- partial plan: " + best.getPartialPlan() + "\n"
				+ "- estimated makespan: " + best.getPartialPlan().estimateMakespan() + "\n");
		// get the best selected node
		return best;
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
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) 
	{
		// get partial plans
		PartialPlan p1 = o1.getPartialPlan();
		PartialPlan p2 = o2.getPartialPlan();
		// compare the cycle times of the partial plans
		return p1.estimateMakespan() < p2.estimateMakespan() ? -1 : p1.estimateMakespan() > p2.estimateMakespan() ? 1 : 0;
	}
}
