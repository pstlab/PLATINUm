package it.istc.pst.platinum.deliberative.strategy.fbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.SearchStrategy;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.ComponentBehavior;

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
		long o1HLoad = 0;
		// estimate the number of tasks assigned to the robot
		long o1RLoad = 0;
		
		// compute minimum load of the human operator
		for (ComponentBehavior b : o1.getPartialPlan().getBehaviors(this.pdb.getComponentByName("Human"))) {
			// check behavior 
			if (!b.getValue().getLabel().contains("Idle")) {
				// update load with task duration lower bound
				o1HLoad++;
			}
		}
		
		// take into account also the agenda
		for (ComponentBehavior b : o1.getAgenda().getGoals(this.pdb.getComponentByName("Human"))) {
			// check behavior 
			if (!b.getValue().getLabel().contains("Idle")) {
				// update load with task duration lower bound
				o1HLoad++;
			}
		}
		
		// compute minimum load of the robot
		for (ComponentBehavior b : o1.getPartialPlan().getBehaviors(this.pdb.getComponentByName("Robot"))) {
			// check behavior 
			if (!b.getValue().getLabel().contains("Idle")) {
				// update load with task duration lower bound
				o1RLoad++;
			}
		}
		
		// take into account also the agenda
		for (ComponentBehavior b : o1.getAgenda().getGoals(this.pdb.getComponentByName("Robot"))) {
			// check behavior 
			if (!b.getValue().getLabel().contains("Idle")) {
				// update load with task duration lower bound
				o1RLoad++;
			}
		}
		
		// compute HRC balance
		double o1HRCBalance = Math.abs(o1HLoad - o1RLoad);
		
		
		// estimate the number of tasks assigned to the human 
		long o2HLoad = 0;
		// estimate the number of tasks assigned to the robot
		long o2RLoad = 0;
		
		// compute minimum load of the human operator
		for (ComponentBehavior b : o2.getPartialPlan().getBehaviors(this.pdb.getComponentByName("Human"))) {
			// check behavior 
			if (!b.getValue().getLabel().contains("Idle")) {
				// update load with task duration lower bound
				o2HLoad++;
			}
		}
		
		// take into account also the agenda
		for (ComponentBehavior b : o2.getAgenda().getGoals(this.pdb.getComponentByName("Human"))) {
			// check behavior 
			if (!b.getValue().getLabel().contains("Idle")) {
				// update load with task duration lower bound
				o2HLoad++;
			}
		}
		
		// compute minimum load of the robot
		for (ComponentBehavior b : o2.getPartialPlan().getBehaviors(this.pdb.getComponentByName("Robot"))) {
			// check behavior 
			if (!b.getValue().getLabel().contains("Idle")) {
				// update load with task duration lower bound
				o2RLoad++;
			}
		}
		
		// take into account also the agenda
		for (ComponentBehavior b : o2.getAgenda().getGoals(this.pdb.getComponentByName("Robot"))) {
			// check behavior 
			if (!b.getValue().getLabel().contains("Idle")) {
				// update load with task duration lower bound
				o2RLoad++;
			}
		}
		
		// compute HRC balance
		double o2HRCBalance = Math.abs(o2HLoad - o2RLoad);
		
		// compare node depth and HR balancing of the partial plans
		int result = o1.getDepth() > o2.getDepth() ? -1 : o1.getDepth() < o2.getDepth() ? 1 : 
			o1HRCBalance < o2HRCBalance ? -1 : o1HRCBalance > o2HRCBalance ? 1 : 
				o1RLoad > o2RLoad ? -1 : o1RLoad < o2RLoad ? 1 : 0;
//		// print data 
//		System.out.println("\n"
//				+ "o1(depth: " + o1.getDepth() + "): R-Load: " + o1RLoad + " H-Load: " + o1HLoad + " HRC-Balance: " + o1HRCBalance + "\n"
//				+ "o2(depth: " + o2.getDepth() + "): R-Load: " + o2RLoad + " H-Load: " + o2HLoad + " HRC-Balance: " + o2HRCBalance + "\n"
//				+ "result: " + result + "\n"
//				+ "\n");
		
		// return comparison result		
		return result;
	}
}
