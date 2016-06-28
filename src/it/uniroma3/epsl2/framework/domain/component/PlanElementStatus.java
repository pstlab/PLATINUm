package it.uniroma3.epsl2.framework.domain.component;

/**
 * 
 * @author anacleto
 *
 */
public enum PlanElementStatus {
	
	/**
	 * A decision not yet justified, i.e. 
	 * a decision which does not belongs to the plan yet
	 */
	PENDING,
	
	/**
	 * A decision of the plan
	 */
	ACTIVE;
}
