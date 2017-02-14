package it.uniroma3.epsl2.framework.lang.flaw;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawType 
{
	/**
	 * Plan refinement goal
	 */
	PLAN_REFINEMENT(0.8),
	
	/**
	 * State Variable scheduling threat
	 */
	SV_SCHEDULING(0.3),
	
	/**
	 * State Variable gap threat
	 */
	SV_GAP(0.1),
	
	/**
	 * Issue concerning the temporal behavior of a component. It represents 
	 * an inconsistency which generates an unsolvable flaw
	 */
	INVALID_BEHAVIOR(1.0),
	
	/**
	 * Resource peak threat
	 */
	RESOURCE_PEAK(1.0);
	
	double cost;
	
	/**
	 * 
	 * @param cost
	 */
	private FlawType(double cost) {
		this.cost = cost;
	}
}
