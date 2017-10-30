package it.istc.pst.platinum.deliberative.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.deliberative.solver.SearchSpaceNode;
import it.istc.pst.platinum.deliberative.strategy.ex.EmptyFringeException;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class AStarSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> 
{
	private List<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected AStarSearchStrategy() {
		super(SearchStrategyType.ASTAR.getLabel());
		// initialize the fringe
		this.fringe = new ArrayList<>();
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
		
		// sort elements of the fringe
		Collections.sort(this.fringe, this);
		// remove the first element of the queue
		return this.fringe.remove(0);
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
		double g1 = o1.getCost();
		double g2 = o2.getCost();
		
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
		for (ComponentValue goal : node.getAgenda().getGoals())
		{
			// check reachable sub-tree from the decomposition graph
			Set<ComponentValue> subtree = this.computeReachableSubTree(this.knowledge.getDecompositionTree(), goal);
			// organize values by components
			Map<DomainComponent, Set<ComponentValue>> comp2value = new HashMap<>();
			// check hierarchy of the reachable values
			for (ComponentValue value : subtree) 
			{
				// get related component
				DomainComponent component = value.getComponent();
				if (!comp2value.containsKey(component)) {
					comp2value.put(component, new HashSet<>());
				}
	
				// add value
				comp2value.get(component).add(value);
			}
			
			// update distance
			for (DomainComponent component : comp2value.keySet()) {
				// get hierarchical value of the component
				double level = this.knowledge.getHierarchicalLevelValue(component);
				// get values
				distance += Math.abs(level * comp2value.get(component).size());
			}
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
