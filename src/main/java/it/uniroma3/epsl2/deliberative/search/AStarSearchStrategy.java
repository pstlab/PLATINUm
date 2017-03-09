package it.uniroma3.epsl2.deliberative.search;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.deliberative.solver.SearchSpaceNode;
import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;

/**
 * 
 * @author anacleto
 *
 */
public class AStarSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> 
{
	private Queue<SearchSpaceNode> fringe;
	private Map<ComponentValue, Set<ComponentValue>> tree;
	
	/**
	 * 
	 */
	protected AStarSearchStrategy() {
		super(SearchStrategyType.ASTAR);
		// initialize the fringe
		this.fringe = new PriorityQueue<SearchSpaceNode>(11, this);
		this.tree = new HashMap<>();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		// get the decomposition graph
		this.tree = this.pdb.getDecompositionTree();
	}

	/**
	 * 
	 */
	@Override
	public int getFringeSize() {
		// get fringe size
		return this.fringe.size();
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
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) 
	{
		// compute heuristic distance from solution node
		double h1 = this.computeHeuristicDistance(o1);
		double h2 = this.computeHeuristicDistance(o2);
		
		// compute (absolute) costs
		double g1 = Math.abs(o1.getCost() + o1.getMakespan());
		double g2 = Math.abs(o2.getCost() + o2.getMakespan());
		
		// compute (absolute) values
		double f1 = Math.abs(g1 + h1);
		double f2 = Math.abs(g2 + h2);
		
		// compare computed values
		return f1 <= f2 ? -1 : 1;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	private double computeHeuristicDistance(SearchSpaceNode node)
	{
		// initialize distance
		double distance = 0.0;
		// check goals of the agenda
		for (Decision goal : node.getAgenda().getGoals())
		{
			// check reachable sub-tree from the decomposition graph
			Set<ComponentValue> subtree = this.computeReachableSubTree(this.tree, goal.getValue());
			// update distance
			distance += subtree.size();
		}
		
		// get computed distance
		return distance;
	}
	
	/**
	 * 
	 * @param graph
	 * @param goal
	 * @return
	 */
	private Set<ComponentValue> computeReachableSubTree(Map<ComponentValue, Set<ComponentValue>> graph, ComponentValue goal)
	{
		// list of reachable nodes
		Set<ComponentValue> set = new HashSet<>();
		// initialization step
		set.add(goal);
		// search reachable nodes
		this.doComputeReachableSubTree(graph, goal, set);
		// get list of reachable nodes
		return set;
	}
	
	/**
	 * 
	 * @param graph
	 * @param node
	 * @param set
	 */
	private void doComputeReachableSubTree(Map<ComponentValue, Set<ComponentValue>> graph, ComponentValue node, Set<ComponentValue> set)
	{
		// check child nodes
		for (ComponentValue child : graph.get(node)) 
		{
			// check if visited
			if (!set.contains(child)) 
			{
				// add to list and go deeper with the search
				set.add(child);
				// recursive call
				this.doComputeReachableSubTree(graph, child, set);
			}
		}
	}
}
