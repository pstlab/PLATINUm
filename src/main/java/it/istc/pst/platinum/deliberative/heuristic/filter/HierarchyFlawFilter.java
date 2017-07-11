package it.istc.pst.platinum.deliberative.heuristic.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
public class HierarchyFlawFilter extends FlawFilter 
{
	// domain hierarchy
	private List<DomainComponent>[] hierarchy;

	/**
	 * 
	 */
	protected HierarchyFlawFilter() {
		super(FlawFilterType.HFF.getLabel());
		// initialize hierarchy
		this.hierarchy = null;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// get dependency graph
		Map<DomainComponent, Set<DomainComponent>> dg = this.pdb.getDependencyGraph();
		// compute the resulting hierarchy 
		this.computeHierarchy(dg);
		// print hierarchy information
		String str = "Computed hierarchy:\n";
		for (int index = 0; index < this.hierarchy.length; index++)
		{
			// check if the current level contains elements
			if (!this.hierarchy[index].isEmpty())
			{
				// print the current hierarchical level
				str += "[" + index + "] -> {";
				for (DomainComponent component : this.hierarchy[index]) {
					str += " " + component.getName() + " ";
				}
				str += "}\n";
			}
		}
		str += "\n";
		// print computed hierarchy
		this.logger.info(str);
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter() 
			throws UnsolvableFlawFoundException 
	{
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// get goals 
		List<Flaw> goals = this.pdb.detectFlaws(FlawType.PLAN_REFINEMENT);
		// detect flaws according to the computed hierarchy of the domain
		for (int index = 0; index < this.hierarchy.length && set.isEmpty(); index++)
		{
			// extract flaws of equivalent components
			for (DomainComponent component : this.hierarchy[index])
			{
				// add related goals
				for (Flaw goal : goals) {
					if (goal.getComponent().equals(component)) {
						set.add(goal);
					}
				}
				// add flaws of the specific component
				set.addAll(component.detectFlaws());
			}
		}
		
		// get flaws
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) 
	{
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// filter flaws according to the hierarchy of the related component
		for (int hlevel = 0; hlevel < this.hierarchy.length && set.isEmpty(); hlevel++)
		{
			// extract flaws of equivalent components
			for (DomainComponent c : this.hierarchy[hlevel]) 
			{
				// check component's flaws
				for (Flaw flaw : flaws) 
				{
					// check flaw's component
					if (flaw.getComponent().equals(c)) {
						// add flaw
						set.add(flaw);
					}
				}
			}
		}
		
		// get filtered set
		return set;
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
}
