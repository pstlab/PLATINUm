package it.istc.pst.platinum.deliberative.heuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
public class SearchAndBuildFlawSelectionHeuristic extends FlawSelectionHeuristic implements Comparator<Flaw>
{
	// domain hierarchy
	private List<DomainComponent>[] hierarchy;
	private FlawType[] searchPreferences;
	private FlawType[] buildPreferences;
	
	/**
	 * 
	 */
	protected SearchAndBuildFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.SEARCH_AND_BUILD.getLabel());
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// set search phase preferences
		this.searchPreferences = new FlawType[] {
				FlawType.RESOURCE_PEAK,
				FlawType.SV_SCHEDULING,
				FlawType.PLAN_REFINEMENT,
		};
		
		// set build phase preferences
		this.buildPreferences = new FlawType[] {
			FlawType.SV_GAP,
			FlawType.INVALID_BEHAVIOR,
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
		List<Flaw> flaws = new ArrayList<>();
		// check components according to the hierarchy
		for (int index = 0; index < this.hierarchy.length && flaws.isEmpty(); index++)
		{
			// get component at the current level of the hierarchy
			List<DomainComponent> components = this.hierarchy[index];
			// find flaws on components according to preference
			for (int jndex = 0; jndex < this.searchPreferences.length && flaws.isEmpty(); jndex++)
			{		
				// get type of flaw 
				FlawType type = this.searchPreferences[jndex];			
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
	
		// check flaws
		if (flaws.isEmpty())
		{
			// check components according to the (inverse) hierarchy
			for (int index = this.hierarchy.length - 1; index >= 0 && flaws.isEmpty(); index--)
			{
				// get component at the current level of the hierarchy
				List<DomainComponent> components = this.hierarchy[index];
				// find flaws on components according to preferences
				for (int jndex = 0; jndex < this.buildPreferences.length && flaws.isEmpty(); jndex++)
				{
					// get type 
					FlawType type = this.buildPreferences[jndex];
					// check components
					for (DomainComponent component : components) {
						flaws.addAll(component.detectFlaws(type));
					}
				}
			}
		}
		
		// prepare the equivalent set
		Set<Flaw> set = new HashSet<>();
		// check flaws found
		if (flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		else {
			// get the most difficult flaw to solve
			Collections.sort(flaws, this);
			// get the first flaw
			set.add(flaws.get(0));
		}
		
		// get "equivalent" flaws to solve
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
	
	/**
	 * 
	 */
	@Override
	public int compare(Flaw o1, Flaw o2) {
		// compare the number of available solutions
		return o1.getSolutions().size() <= o2.getSolutions().size() ? -1 : 1; 
	}
}
