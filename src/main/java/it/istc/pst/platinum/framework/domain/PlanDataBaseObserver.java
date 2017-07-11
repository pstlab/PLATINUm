package it.istc.pst.platinum.framework.domain;

import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBaseEvent;

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
