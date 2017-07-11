package it.uniroma3.epsl2.deliberative.app;

import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkContainer;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.deliberative.PlanDataBasePlaceholder;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

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
