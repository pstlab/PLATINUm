package it.cnr.istc.pst.platinum.ai.deliberative.heuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.knowledge.DomainKnowledge;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.NoFlawFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class ReverseHierarchicalFlawSelectionHeuristic extends FlawSelectionHeuristic implements Comparator<Flaw>
{
	// domain hierarchy
	private FlawType[] preferences;
	private List<DomainComponent>[] hierarchy;
	
	/**
	 * 
	 */
	protected ReverseHierarchicalFlawSelectionHeuristic() {
		super("ReverseHierarchicalFlawSelectionHeuristic");
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(
				FRAMEWORK_HOME + FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		// get preference property
		String[] prefs = properties.getProperty("preferences").trim().split(",");
		// set prefernces
		this.preferences = new FlawType[prefs.length];
		for (int i = 0; i < prefs.length; i++) {
			// set preference
			this.preferences[i] = FlawType.getFlawTypeFromLabel(prefs[i]);
		}
		
		// get domain knowledge
		DomainKnowledge knowledge = this.pdb.getDomainKnowledge();
		// get the hierarchy
		this.hierarchy = knowledge.getDomainHierarchy();
	}
	
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Set<Flaw> flaws) 
			throws NoFlawFoundException 
	{
		// check flaws found
		if (flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		
		
		// set of detected flaws
		Set<Flaw> filtered = new HashSet<>();
		// check components according to the hierarchy
		for (int index = this.hierarchy.length - 1; index >= 0 && filtered.isEmpty(); index--)
		{
			// get component at the current level of the hierarchy
			List<DomainComponent> components = this.hierarchy[index];
			// find flaws on components according to preference
			for (int jndex = 0; jndex < this.preferences.length && filtered.isEmpty(); jndex++)
			{
				// get preference 
				FlawType type = this.preferences[jndex];
				// filter flaws accordingly
				for (Flaw flaw : flaws) {
					// check type and flaw component
					if (components.contains(flaw.getComponent()) && 
							flaw.getType().equals(type)) {
						// add flaw to the filtered list
						filtered.add(flaw);
					}
				}
			}
		}
		
		// further filter the remaining flaws according to the number of available solutions
		if (!filtered.isEmpty()) 
		{
			// sort flaws
			List<Flaw> list = new ArrayList<>(filtered);
			// sort flaws
			Collections.sort(list, this);
			// get the first flaw
			Flaw flaw = list.remove(0);
			// reset filtered set
			filtered = new HashSet<>();
			filtered.add(flaw);
			// check remaining flaws from the list
			boolean stop = false;
			for (int index = 0; index < list.size() && !stop; index++) {
				// get flaw
				Flaw current = list.get(index);
				// compare available solutions
				if (flaw.getSolutions().size() == current.getSolutions().size()) {
					// add flaw to the set
					filtered.add(current);
				}
				else {
					// stop, the rest of flaws have an higher number of solutions available
					stop = true;
				}
			}
		}
		
		// check flaws found
		if (filtered.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		
		// get "equivalent" flaws to solve
		return filtered;
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
		// check components according to the hierarchy
		for (int index = this.hierarchy.length - 1; index >= 0 && flaws.isEmpty(); index--)
		{
			// get component at the current level of the hierarchy
			List<DomainComponent> components = this.hierarchy[index];
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
	public Set<Flaw> check() {
		// set of detected flaws
		Set<Flaw> flaws = new HashSet<>();
		// check components according to the hierarchy
		for (int index = this.hierarchy.length - 1; index >= 0 && flaws.isEmpty(); index--)
		{
			// get component at the current level of the hierarchy
			List<DomainComponent> components = this.hierarchy[index];
			// find flaws on components according to preference
			for (int jndex = 0; jndex < this.preferences.length && flaws.isEmpty(); jndex++)
			{
				// get preference 
				FlawType type = this.preferences[jndex];
				// check other type of flaws by querying the component directly
				for (DomainComponent component : components) {
					// detect flaws by type
					flaws.addAll(component.checkFlaws(new FlawType[] {
							type
					}));
				}
			}
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
