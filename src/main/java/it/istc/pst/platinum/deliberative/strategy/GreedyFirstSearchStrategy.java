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
public class GreedyFirstSearchStrategy extends SearchStrategy implements Comparator<SearchSpaceNode> 
{
	private List<SearchSpaceNode> fringe;
	
	/**
	 * 
	 */
	protected GreedyFirstSearchStrategy() {
		super(SearchStrategyType.GREEDY.getLabel());
		// java7 compliant constructor
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
	public int compare(SearchSpaceNode o1, SearchSpaceNode o2) 
	{
		// compute heuristic distance from solution node
		double h1 = this.computeHeuristicDistance(o1);
		double h2 = this.computeHeuristicDistance(o2);
		
		// compare the costs of the nodes
		return o1.getDepth() > o2.getDepth() ? -1 :
			o1.getDepth() == o2.getDepth() && h1 <= h2 ? -1 : 1;
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
