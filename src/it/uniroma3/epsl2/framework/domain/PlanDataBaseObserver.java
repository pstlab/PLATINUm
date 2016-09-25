package it.uniroma3.epsl2.framework.domain;

import it.uniroma3.epsl2.framework.domain.component.pdb.PlanDataBaseEvent;

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
	public void notify(PlanDataBaseEvent event);
}
