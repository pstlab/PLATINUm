package it.istc.pst.platinum.deliberative.heuristic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
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
public class SearchAndBuildFlawSelectionHeuristic extends FlawSelectionHeuristic implements Comparator<Flaw>
{
	// domain hierarchy
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
				FlawType.PLAN_REFINEMENT,
				FlawType.RESOURCE_OVERFLOW,
				FlawType.RESOURCE_PLANNING,
				FlawType.TIMELINE_OVERFLOW,
		};
		
		// set build phase preferences
		this.buildPreferences = new FlawType[] {
			FlawType.TIMELINE_BEHAVIOR_PLANNING,
			FlawType.TIMELINE_BEHAVIOR_CHECKING,
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
		List<Flaw> flaws = new ArrayList<>();
		// get the hierarchy
		List<DomainComponent>[] hierarchy = this.knowledge.getDomainHierarchy();
		// check components according to the hierarchy
		for (int index = hierarchy.length - 1; index >= 0 && flaws.isEmpty(); index--)
		{
			// get component at the current level of the hierarchy
			List<DomainComponent> components = hierarchy[index];
			// find flaws on components according to preference
			for (int jndex = 0; jndex < this.searchPreferences.length && flaws.isEmpty(); jndex++)
			{		
				// get type of flaw 
				FlawType type = this.searchPreferences[jndex];			
				// check flaw type on components of the same hierarchical level
				for (DomainComponent component : components) {
					// detect flaws by type
					flaws.addAll(component.detectFlaws(type));
				}
			}
		}
	
		// check flaws
		if (flaws.isEmpty())
		{
			// plan behaviors of components according to the hierarchy
			for (int index = hierarchy.length - 1; index >= 0 && flaws.isEmpty(); index--)
			{
				// get component at the current level of the hierarchy
				List<DomainComponent> components = hierarchy[index];
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
		
		// check flaws found
		if (flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		
		// prepare the equivalent set
		Set<Flaw> set = new HashSet<>();
		// randomly select a flaw to solve
		Random rand = new Random(System.currentTimeMillis());
		int pos = rand.nextInt(flaws.size());
		set.add(flaws.get(pos));
		// get "equivalent" flaws to solve
		return set;
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
