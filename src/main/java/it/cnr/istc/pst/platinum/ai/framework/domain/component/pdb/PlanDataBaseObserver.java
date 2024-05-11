package it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb;

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
