package it.uniroma3.epsl2.deliberative.search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.search.ex.EmptyFringeException;
import it.uniroma3.epsl2.deliberative.solver.SearchSpaceNode;
import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
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
	private List<DomainComponent>[] hierarchy;
	
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
		// get dependency graph
		Map<DomainComponent, Set<DomainComponent>> dg = this.pdb.getDependencyGraph();
		// compute the resulting hierarchy 
		this.computeHierarchy(dg);
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
				double level = this.getHierarchicalLevelValue(component);
				// get values
				distance += ((1.0 / level) * comp2value.get(component).size());
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
	
	/**
	 * Given an acyclic dependency graph, the method builds the 
	 * hierarchy by means of a topological sort algorithm on the 
	 * dependency graph
	 * 
	 * @param graph
	 */
	@SuppressWarnings("unchecked")
	private void computeHierarchy(Map<DomainComponent, Set<DomainComponent>> graph) 
	{
		// compute hierarchy by means of topological sort algorithm
		List<DomainComponent> S = new ArrayList<>();	// root components
		for (DomainComponent c : graph.keySet()) 
		{
			// check if root
			if (graph.get(c).isEmpty()) {
				S.add(c);
			}
 		}

		// initialize hierarchy
		this.hierarchy = (List<DomainComponent>[]) new ArrayList[this.pdb.getComponents().size()];
		// initialize hierarchy
		for (int index = 0; index < this.hierarchy.length; index++) {
			this.hierarchy[index] = new ArrayList<>();
		}
		
		// top hierarchy level
		int hlevel = 0;
		// start building hierarchy
		while (!S.isEmpty()) 
		{
			// get all root components
			for (DomainComponent root : S) 
			{
				// add component
				this.hierarchy[hlevel].add(root);
				// remove current root from the graph
				graph.remove(root);
				// remove "edges" to 
				for (DomainComponent other : graph.keySet())
				{
					if (graph.get(other).contains(root)) {
						graph.get(other).remove(root);
					}
				}
			}
			
			// clear the set of root node
			S.clear();
			// update hierarchy degree
			hlevel++;
			
			// look for new roots
			for (DomainComponent c : graph.keySet()) 
			{
				// check if root
				if (graph.get(c).isEmpty() && !S.contains(c)) {
					// add root
					S.add(c);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param component
	 * @return
	 */
	private int getHierarchicalLevelValue(DomainComponent component)
	{
		boolean found = false;
		int level = 0;
		for (level = 0; level < this.hierarchy.length && !found; level++) {
			// get components
			found = this.hierarchy[level].contains(component);
		}
		
		// get level
		return level + 1;
	}
}
