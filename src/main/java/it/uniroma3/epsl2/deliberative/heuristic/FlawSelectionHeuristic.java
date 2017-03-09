package it.uniroma3.epsl2.deliberative.heuristic;

import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilter;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.lang.ex.NoFlawFoundException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.inject.FilterPipelineReference;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FlawSelectionHeuristic extends ApplicationFrameworkObject 
{
	private FlawSelectionHeuristicType type;
	
	@PlanDataBaseReference
	private PlanDataBase pdb;
	
	@FilterPipelineReference
	private List<FlawFilter> filters;
	
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	/**
	 * 
	 * @param type
	 */
	protected FlawSelectionHeuristic(FlawSelectionHeuristicType type) {
		super();
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawSelectionHeuristicType getType() {
		return type;
	}

	/**
	 * Return a set of equivalent flaws to solve for plan refinement. Each solution of a 
	 * flaw determines a branch in the resulting search space of the planner.
	 * 
	 * @return
	 * @throws UnsolvableFlawFoundException
	 * @throws NoFlawFoundException
	 */
	public final Set<Flaw> choose() 
			throws UnsolvableFlawFoundException, NoFlawFoundException 
	{
		// set of detected flaws
		Set<Flaw> flaws = null;
		// iteratively find and filter flaws
		for (FlawFilter ff : this.filters) 
		{
			// check if first filter to be applied
			if (flaws == null) {
				// apply filter
				flaws = ff.filter();
			}
			else {
				// apply filter
				flaws = ff.filter(flaws);
			}
		}
		
		// check if any flaw has been found
		if (flaws == null || flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		else {
			// print the size of equivalent flaws found
			this.logger.debug("The heuristic has found/filtered " + flaws.size() + " flaws to solve");
		}
		
		// get "equivalent" flaws to solve
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[FlawSelectionHeuristic type= " + this.type + "]";
	}
}
