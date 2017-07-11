package it.istc.pst.platinum.deliberative.app;

import it.istc.pst.platinum.framework.domain.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.PlanDataBasePlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 */
public abstract class Planner extends ApplicationFrameworkObject 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_DELIBERATIVE_LOGGER)
	protected FrameworkLogger loggger;
	
	@PlanDataBasePlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE)
	protected PlanDataBase pdb;
	
	/**
	 * 
	 */
	protected Planner() {}
	
	/**
	 * Display the current plan
	 */
	public void display() {
		// display the current plan
		this.pdb.display();
	}
	
	/**
	 * 
	 * @return
	 * @throws NoSolutionFoundException
	 */
	public abstract SolutionPlan plan() throws NoSolutionFoundException;
}
