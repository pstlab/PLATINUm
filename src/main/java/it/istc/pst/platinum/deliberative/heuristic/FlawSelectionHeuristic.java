package it.istc.pst.platinum.deliberative.heuristic;

import java.util.Set;

import it.istc.pst.platinum.framework.domain.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.PlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FlawSelectionHeuristic extends ApplicationFrameworkObject 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_DELIBERATIVE_LOGGER)
	protected FrameworkLogger logger;
	
	@PlanDataBasePlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE)
	protected PlanDataBase pdb;
	
	private String label;
	
	/**
	 * 
	 * @param label
	 */
	protected FlawSelectionHeuristic(String label) {
		super();
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Return a set of equivalent flaws to solve for plan refinement. Each solution of a 
	 * flaw determines a branch in the resulting search space of the planner.
	 * 
	 * @return
	 * @throws UnsolvableFlawFoundException
	 * @throws NoFlawFoundException
	 */
	public abstract Set<Flaw> choose() 
			throws UnsolvableFlawFoundException, NoFlawFoundException; 
	
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[FlawSelectionHeuristic label= " + this.label + "]";
	}
}
