package it.uniroma3.epsl2.planner.heuristic.fsh.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationConstraint;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationRule;
import it.uniroma3.epsl2.framework.domain.component.pdb.TokenVariable;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifcycle.PostConstruct;
import it.uniroma3.epsl2.planner.heuristic.fsh.filter.ex.HierarchyCycleException;

/**
 * 
 * @author anacleto
 *
 */
public class DependencyGraphFlawFilter extends FlawFilter 
{
	@PlanDataBaseReference
	private PlanDataBase pdb;
	
	// dependency graph (as incident graph)
	private Map<DomainComponent, Set<DomainComponent>> dg;
	private List<DomainComponent>[] hierarchy;

	/**
	 * 
	 */
	protected DependencyGraphFlawFilter() {
		super(FlawFilterType.DgF);
		// initialize incident graph
		this.dg = new HashMap<>();
		// initialize hierarchy
		this.hierarchy = null;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		
		try {
			
			// compute DG (as incident graph) 
			this.computeDependencyGraph();
			// compute hierarchy
			this.computeHierarchy();
		}
		catch (HierarchyCycleException ex) {
			
			// cycle in the hierarchy
			this.logger.error(ex.getMessage());
			// compute flat hierarchy
			this.computeFlatHierarchy();
		}
		finally {
			
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
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) {
		
		// filtered set
		Set<Flaw> filtered = new HashSet<>();
		// filter flaws according to the hierarchy of the related component
		for (int hlevel = 0; hlevel < this.hierarchy.length && filtered.isEmpty(); hlevel++) {
			
			// extract flaws of equivalent components
			for (DomainComponent c : this.hierarchy[hlevel]) {
				
				// check component's flaws
				for (Flaw flaw : flaws) {
					
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
	 * 
	 * @throws HierarchyCycleException
	 */
	private void computeDependencyGraph() 
			throws HierarchyCycleException {
		
		// initialize the dependency graph
		for (DomainComponent node : this.pdb.getComponents()) {
			// initialize DG 
			this.dg.put(node, new HashSet<DomainComponent>());
		}
			
		// check synchronization and build the graph as "incident" matrix
		for (SynchronizationRule rule : this.pdb.getSynchronizationRules()) {
			
			// check constraints
			for (SynchronizationConstraint ruleConstraint : rule.getConstraints()) {
				
				// consider only temporal constraint to build the dependency graph
				if (ruleConstraint.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT)) {
					
					// get related token variables
					TokenVariable source = ruleConstraint.getSource();
					TokenVariable target = ruleConstraint.getTarget();
					
					// check related values' components
					DomainComponent master = source.getValue().getComponent();
					DomainComponent slave = target.getValue().getComponent();
					// check if "external" constraint
					if (!master.equals(slave)) {
						
						// update DG
						this.dg.get(slave).add(master);
						// check cycle
						if (this.dg.get(master).contains(slave)) {
							
							// cycle detected
							throw new HierarchyCycleException("A cycle has been detected in the Dependency Graph");
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void computeFlatHierarchy() {
	
		// set all component at the same hierarchy level
		this.hierarchy = (List<DomainComponent>[]) new ArrayList[1]; 
		// add all components
		this.hierarchy[0] = new ArrayList<>(this.pdb.getComponents());
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void computeHierarchy() {
	
		// copy the dependency graph
		Map<DomainComponent, Set<DomainComponent>> graph = new HashMap<>(this.dg);
		// compute hierarchy by means of topological sort algorithm
		List<DomainComponent> S = new ArrayList<>();	// root components
		for (DomainComponent c : graph.keySet()) {
			
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
//		// check if root element has been found
//		if (S.isEmpty()) {
//			// all the component are at the same hierarchy level
//			for (DomainComponent comp : this.pdb.getComponents()) {
//				this.hierarchy[hlevel].add(comp);
//			}
//			// flat hierarchy
//			this.logger.warning("Flat hierarchy found... all the component are at the same hierarcical level " + hlevel);
//		}
//		else {
		// start building hierarchy
		while (!S.isEmpty()) {
			// get current root component
			DomainComponent root = S.remove(0);
			// add component
			this.hierarchy[hlevel].add(root);
				
			// update hierarchy degree
			hlevel++;
			
			// update the graph and look for new roots
			graph.remove(root);
			for (DomainComponent c : graph.keySet()) {
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
//			}
		}
	}
}
