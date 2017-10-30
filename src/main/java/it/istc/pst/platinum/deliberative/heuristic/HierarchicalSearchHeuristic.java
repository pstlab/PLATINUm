package it.istc.pst.platinum.deliberative.heuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;

/**
 * 
 * @author anacleto
 *
 */
public class HierarchicalSearchHeuristic extends FlawSelectionHeuristic implements Comparator<Flaw>
{
	// domain hierarchy
	private FlawType[] preferences;
	
	/**
	 * 
	 */
	protected HierarchicalSearchHeuristic() {
		super(FlawSelectionHeuristicType.HIERARCHICAL.getLabel());
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// set preferences
		this.preferences = new FlawType[] {
				FlawType.PLAN_REFINEMENT,
				FlawType.RESOURCE_PLANNING,
				FlawType.RESOURCE_OVERFLOW,
				FlawType.TIMELINE_OVERFLOW,
		};
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> choose() 
			throws UnsolvableFlawException, NoFlawFoundException 
	{
		// set of detected flaws
		Set<Flaw> flaws = new HashSet<>();
		// get the hierarchy
		List<DomainComponent>[] hierarchy = this.knowledge.getDomainHierarchy();
		// check components according to the hierarchy
		for (int index = hierarchy.length - 1; index >= 0 && flaws.isEmpty(); index--)
		{
			// get component at the current level of the hierarchy
			List<DomainComponent> components = hierarchy[index];
			// find flaws on components according to preference
			for (int jndex = 0; jndex < this.preferences.length && flaws.isEmpty(); jndex++)
			{
				// get preference 
				FlawType type = this.preferences[jndex];
				// check other type of flaws by querying the component directly
				for (DomainComponent component : components) {
					// detect flaws by type
					flaws.addAll(component.detectFlaws(type));
				}
			}
		}
		
		// further filter the remaining flaws according to the number of available solutions
		if (!flaws.isEmpty()) 
		{
			// sort flaws
			List<Flaw> list = new ArrayList<>(flaws);
			// sort flaws
			Collections.sort(list, this);
			// get the first flaw
			Flaw flaw = list.remove(0);
			// reset filtered set
			flaws = new HashSet<>();
			flaws.add(flaw);
			// check remaining flaws from the list
			boolean stop = false;
			for (int index = 0; index < list.size() && !stop; index++) {
				// get flaw
				Flaw current = list.get(index);
				// compare available solutions
				if (flaw.getSolutions().size() == current.getSolutions().size()) {
					// add flaw to the set
					flaws.add(current);
				}
				else {
					// stop, the rest of flaws have an higher number of solutions available
					stop = true;
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
	 * 
	 */
	@Override
	public int compare(Flaw o1, Flaw o2) {
		// compare the number of available solutions
		return o1.getSolutions().size() < o2.getSolutions().size() ? -1 : 
			o1.getSolutions().size() > o2.getSolutions().size() ? 1 : 0;
	}
}
