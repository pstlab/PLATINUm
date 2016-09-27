package it.uniroma3.epsl2.deliberative.heuristic;

import java.util.HashSet;
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
	 * 
	 * @return
	 * @throws UnsolvableFlawFoundException
	 * @throws NoFlawFoundException
	 */
	public final Set<Flaw> choose() 
			throws UnsolvableFlawFoundException, NoFlawFoundException 
	{
		// initial set of flaws
		Set<Flaw> flaws = new HashSet<>(this.pdb.detectFlaws());
		
		// check flaws
		if (flaws.isEmpty()) {
			throw new NoFlawFoundException("No flaw to solve on the current plan");
		}

		// initial size
		int initSize = flaws.size();
		// iteratively filter flaws
		for(FlawFilter f : this.filters) {
			// filter flaws
			flaws = f.filter(flaws);
		}
		
		// print the size of equivalent flaws found
		this.logger.debug("The heuristic has found " + flaws.size() + " equivalent flaws to solve from the "
				+ "initial set of " + initSize + " flaws");
		
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
