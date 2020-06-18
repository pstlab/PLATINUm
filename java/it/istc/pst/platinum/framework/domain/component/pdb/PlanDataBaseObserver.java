package it.istc.pst.platinum.framework.domain.component.pdb;

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
