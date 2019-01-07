package it.istc.pst.platinum.deliberative.strategy.fbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.SearchStrategy;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;

/**
 * 
 * @author anacleto
 *
 */
public class HRCBalancingSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> 
{
	private List<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected HRCBalancingSearchStrategy() {
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
		// estimate the number of tasks assigned to the human 
		long o1HTasks = 0;
		// estimate the number of tasks assigned to the robot
		long o1RTasks = 0;
		
		// get tasks currently assigned to the human
		o1HTasks += o1.getPartialPlan().getBehaviors(this.pdb.getComponentByName("Human")).size();
		// get tasks currently assigned to the robot
		o1RTasks += o1.getPartialPlan().getBehaviors(this.pdb.getComponentByName("Robot")).size();
		// get pending tasks for the human
		o1HTasks += o1.getAgenda().getGoalsByComponent(this.pdb.getComponentByName("Human")).size();
		// get pending task for the robot
		o1RTasks += o1.getAgenda().getGoalsByComponent(this.pdb.getComponentByName("Robot")).size();
		
		
		
		// estimate the number of tasks assigned to the human 
		long o2HTasks = 0;
		// estimate the number of tasks assigned to the robot
		long o2RTasks = 0;
		
		// get tasks currently assigned to the human
		o2HTasks += o2.getPartialPlan().getBehaviors(this.pdb.getComponentByName("Human")).size();
		// get tasks currently assigned to the robot
		o2RTasks += o2.getPartialPlan().getBehaviors(this.pdb.getComponentByName("Robot")).size();
		// get pending tasks for the human
		o2HTasks += o2.getAgenda().getGoalsByComponent(this.pdb.getComponentByName("Human")).size();
		// get pending task for the robot
		o2RTasks += o2.getAgenda().getGoalsByComponent(this.pdb.getComponentByName("Robot")).size();
		
		// compute balancing 
		long o1Balancing = Math.abs(o1HTasks - o1RTasks);
		long o2Balancing = Math.abs(o2HTasks - o2RTasks);
		
		// compare node depth and HR balancing of the partial plans
		return o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 : 
			o1Balancing < o2Balancing ? -1 : o1Balancing > o2Balancing ? 1 : 0;
	}
}
