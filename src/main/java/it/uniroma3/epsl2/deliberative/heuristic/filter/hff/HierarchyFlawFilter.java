package it.uniroma3.epsl2.deliberative.heuristic.filter.hff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterType;
import it.uniroma3.epsl2.deliberative.heuristic.filter.ex.HierarchyCycleException;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationConstraint;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationRule;
import it.uniroma3.epsl2.framework.domain.component.pdb.TokenVariable;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;
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
	
	// dependency graph (as incident graph)
	private Map<DomainComponent, Set<DomainComponent>> dg;
	private List<DomainComponent>[] hierarchy;

	/**
	 * 
	 */
	protected HierarchyFlawFilter() 
	{
		super(FlawFilterType.HFF);
		// initialize incident graph
		this.dg = new HashMap<>();
		// initialize hierarchy
		this.hierarchy = null;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// compute DG (as incident graph) 
		this.computeDependencyGraph();
		// compute hierarchy
		this.computeHierarchy();
		// print hierarchy
		String str = "computed hierarchy ";
		int index = 0;
		while (index < hierarchy.length) {
			str += "[" + index + "] { ";
			for (DomainComponent comp : hierarchy[index]) {
				str += comp.getName() + " ";
			}
			str += "} ";
			index++;
		}
		
		// print domain hierarchy
		this.logger.info(str);
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
		for (int hlevel = 0; hlevel < this.hierarchy.length && filtered.isEmpty(); hlevel++) 
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
	 * Compute an acyclic Dependency Graph by analyzing the temporal constraints
	 * of the synchronization rules in the domain specification.
	 * 
	 * The dependency graph represents a relaxed view of the temporal dependencies 
	 * among domain components. Namely, if a cycle exists it is ignored by the
	 * dependency graph representation. Thus, only the sub-set of acyclic relations
	 * are considered for inferring dependencies between components.
	 */
	private void computeDependencyGraph() 
	{
		// initialize the dependency graph
		for (DomainComponent node : this.pdb.getComponents()) {
			// initialize DG 
			this.dg.put(node, new HashSet<DomainComponent>());
		}
			
		// check synchronization and build the graph as "incident" matrix
		for (SynchronizationRule rule : this.pdb.getSynchronizationRules()) 
		{
			// check constraints
			for (SynchronizationConstraint ruleConstraint : rule.getConstraints()) 
			{
				// consider only temporal constraint to build the dependency graph
				if (ruleConstraint.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT)) 
				{
					// get related token variables
					TokenVariable source = ruleConstraint.getSource();
					TokenVariable target = ruleConstraint.getTarget();
					// check related values' components
					DomainComponent master = source.getValue().getComponent();
					DomainComponent slave = target.getValue().getComponent();
					// check if "external" constraint
					if (!master.equals(slave)) 
					{
						try 
						{
							// update the dependency graph - recall: the dg is an incident graph
							this.dg.get(slave).add(master);
							// check hierarchy cycle
							this.checkHiearchyCycle(slave, master);
						}
						catch (HierarchyCycleException ex) {
							// a cycle into the hierarchy has been found
							this.logger.warning(ex.getMessage() + "\nIgnore dependency relation between component=" + master + " and component=" + slave);
							// remove dependency relation
							this.dg.get(slave).remove(master);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @throws HierarchyCycleException
	 */
	private void checkHiearchyCycle(DomainComponent reference, DomainComponent target) 
			throws HierarchyCycleException
	{
		// check direct cycle
		if (this.dg.get(reference).contains(target) &&
				this.dg.get(target).contains(reference)) {
			// cycle detected
			throw new HierarchyCycleException("A direct cycle has been detected in the Dependency Graph between component= " + reference + " and component= " + target);
		}
		else {
			// check paths
			this.findCycle(reference, new HashSet<DomainComponent>());
		}
	}
	
	/**
	 * 
	 * @param comp
	 * @param visited
	 * @throws HierarchyCycleException
	 */
	private void findCycle(DomainComponent comp, Set<DomainComponent> visited) 
			throws HierarchyCycleException
	{
		// add component to visited
		visited.add(comp);
		// check component's successors
		for (DomainComponent next : this.dg.get(comp)) {
			// check if visited
			if (!visited.contains(next)) {
				// recursive call
				this.findCycle(next, new HashSet<DomainComponent>(visited));
			}
			else {
				// throw exception
				throw new HierarchyCycleException("A cycle has been introduced in the Dependency Graph between component= " + comp + " and component= " + next);
			}
		}
	}
	
	/**
	 * Given an acyclic dependency graph, the method builds the 
	 * hierarchy by means of a topological sort algorithm on the 
	 * dependency graph
	 */
	@SuppressWarnings("unchecked")
	private void computeHierarchy() 
	{
		// copy the dependency graph
		Map<DomainComponent, Set<DomainComponent>> graph = new HashMap<>(this.dg);
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
