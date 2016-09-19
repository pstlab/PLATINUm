package it.uniroma3.epsl2.framework.domain;

import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public interface PlanDataBaseObserver 
{
	/**
	 * 
	 * @param solution
	 */
	public void propagated(FlawSolution solution);

	/**
	 * 
	 * @param solution
	 */
	public void retracted(FlawSolution solution);
}
