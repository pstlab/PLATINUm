package it.istc.pst.platinum.deliberative.heuristic.filter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;

/**
 * 
 * @author anacleto
 *
 */
public class HierarchyFlawFilter extends FlawFilter 
{
	/**
	 * 
	 */
	protected HierarchyFlawFilter() {
		super(FlawFilterType.HFF.getLabel());
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter() 
			throws UnsolvableFlawException 
	{
		// filtered set
		Set<Flaw> set = new HashSet<>();
		// get goals 
		List<Flaw> goals = this.pdb.detectFlaws(FlawType.PLAN_REFINEMENT);
		// get the hierarchy
		List<DomainComponent>[] hierarchy = this.knowledge.getDomainHierarchy();
		// detect flaws according to the computed hierarchy of the domain
		for (int index = 0; index < hierarchy.length && set.isEmpty(); index++)
		{
			// extract flaws of equivalent components
			for (DomainComponent component : hierarchy[index])
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
		// get the hierarchy
		List<DomainComponent>[] hierarchy = this.knowledge.getDomainHierarchy();
		// filter flaws according to the hierarchy of the related component
		for (int hlevel = 0; hlevel < hierarchy.length && set.isEmpty(); hlevel++)
		{
			// extract flaws of equivalent components
			for (DomainComponent c : hierarchy[hlevel]) 
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
}
