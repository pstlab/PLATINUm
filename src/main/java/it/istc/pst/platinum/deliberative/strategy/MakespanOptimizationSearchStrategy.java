package it.istc.pst.platinum.deliberative.strategy;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.PartialPlan;

/**
 * 
 * @author anacleto
 *
 */
public class MakespanOptimizationSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> 
{
	private Queue<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected MakespanOptimizationSearchStrategy() {
		super("SearchStrategy:MakespanOptimization");
		this.fringe = new PriorityQueue<>(this);
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
		SearchSpaceNode best = this.fringe.poll();
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
		this.fringe.offer(node);
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
		
		// compare node depth and cycle times of the partial plans
		return o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 : 
			p1.estimateMakespan() < p2.estimateMakespan() ? -1 : p1.estimateMakespan() > p2.estimateMakespan() ? 1 : 0;
	}
}
