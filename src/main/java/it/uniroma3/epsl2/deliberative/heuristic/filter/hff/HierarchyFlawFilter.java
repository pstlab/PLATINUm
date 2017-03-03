package it.uniroma3.epsl2.deliberative.heuristic.filter.hff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;

/**
 * 
 * @author anacleto
 *
 */
public class HierarchyFlawFilter extends FlawFilter 
{
	@PlanDataBaseReference
	private PlanDataBase pdb;

	// domain hierarchy
	private List<DomainComponent>[] hierarchy;

	/**
	 * 
	 */
	protected HierarchyFlawFilter() {
		super(FlawFilterType.HFF);
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
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) 
	{
		// filtered set
		Set<Flaw> filtered = new HashSet<>();
		// filter flaws according to the hierarchy of the related component
		for (int hlevel = 0; hlevel < this.hierarchy.length; hlevel++)
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
						filtered.add(flaw);
					}
				}
			}
		}
		
		// get filtered set
		return filtered;
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
			// get current root component
			DomainComponent root = S.remove(0);
			// add component
			this.hierarchy[hlevel].add(root);
			// update hierarchy degree
			hlevel++;
			
			// update the graph and look for new roots
			graph.remove(root);
			for (DomainComponent c : graph.keySet()) 
			{
				// remove node
				if (graph.get(c).contains(root)) {
					graph.get(c).remove(root);
				}
				
				// check if root
				if (graph.get(c).isEmpty() && !S.contains(c)) {
					// add root
					S.add(c);
				}
			}
		}
	}
}
