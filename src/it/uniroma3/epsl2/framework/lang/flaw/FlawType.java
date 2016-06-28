package it.uniroma3.epsl2.framework.lang.flaw;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawType {

	/**
	 * Plan refinement goal
	 */
	PLAN_REFINEMENT,
	
	/**
	 * State Variable scheduling threat
	 */
	SV_SCHEDULING,
	
	/**
	 * State Variable gap threat
	 */
	SV_GAP,
	
	/**
	 * Issue concerning the temporal behavior of a component. It represents 
	 * an inconsistency which generates an unsolvable flaw
	 */
	INVALID_BEHAVIOR,
	
	/**
	 * Resource peak threat
	 */
	RESOURCE_PEAK
}
