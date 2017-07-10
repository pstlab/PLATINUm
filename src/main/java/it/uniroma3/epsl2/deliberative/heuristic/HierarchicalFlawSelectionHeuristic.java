package it.uniroma3.epsl2.deliberative.heuristic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.NoFlawFoundException;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
public class HierarchicalFlawSelectionHeuristic extends FlawSelectionHeuristic 
{
	// domain hierarchy
	private List<DomainComponent>[] hierarchy;
	private FlawType[] preferences;
	
	/**
	 * 
	 */
	protected HierarchicalFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.HFS.getLabel());
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// load preferences
		this.preferences = new FlawType[] {
				FlawType.PLAN_REFINEMENT,
				FlawType.RESOURCE_PEAK,
				FlawType.SV_SCHEDULING,
				FlawType.SV_GAP
		};
		
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
	public Set<Flaw> choose() 
			throws UnsolvableFlawFoundException, NoFlawFoundException 
	{
		// set of detected flaws
		Set<Flaw> flaws = new HashSet<>();
		// check components according to the hierarchy
		for (int index = 0; index < this.hierarchy.length && flaws.isEmpty(); index++)
		{
			// get component at the current level of the hierarchy
			List<DomainComponent> components = this.hierarchy[index];
			// find flaws on components according to preference
			for (int jndex = 0; jndex < this.preferences.length && flaws.isEmpty(); jndex++)
			{
				// get preference 
				FlawType type = this.preferences[jndex];
				// check flaw type
				if (type.equals(FlawType.PLAN_REFINEMENT))
				{
					// filter goals according to the hierarchy
					for (Flaw goal : this.pdb.detectFlaws(type)) {
						// check goal component
						if (components.contains(goal.getComponent())) {
							// add flaw 
							flaws.add(goal);
						}
					}
				}
				else 
				{
					// check other type of flaws by querying the component directly
					for (DomainComponent component : components) {
						// detect flaws by type
						flaws.addAll(component.detectFlaws(type));
					}
				}
			}
		}
		
		// check flaws found
		if (flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		
		// get "equivalent" flaws to solve
		return flaws;
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
