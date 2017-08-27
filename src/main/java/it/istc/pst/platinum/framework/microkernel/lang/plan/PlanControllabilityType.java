package it.istc.pst.platinum.framework.microkernel.lang.plan;

/**
 * 
 * @author anacleto
 *
 */
public enum PlanControllabilityType 
{
	/**
	 * No information is given about the controllability property of the plan
	 */
	UNKNOWN,
	
	/**
	 * The plan is pseudo-controllable 
	 */
	PSEUDO_CONTROLLABILITY,
	
	/**
	 * 
	 */
	WEAK_CONTROLLABILITY,
	
	/**
	 * 
	 */
	STRONG_CONTROLLABILITY,
	
	/**
	 * 
	 */
	DYNAMIC_CONTROLLABILITY
}
