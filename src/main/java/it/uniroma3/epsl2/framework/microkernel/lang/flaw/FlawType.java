package it.uniroma3.epsl2.framework.microkernel.lang.flaw;

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
	PLAN_REFINEMENT(FlawCategoryType.PLANNING),
	
	/**
	 * State Variable scheduling threat
	 */
	SV_SCHEDULING(FlawCategoryType.SCHEDULING),
	
	/**
	 * State Variable gap threat
	 */
	SV_GAP(FlawCategoryType.PLANNING),
	
	/**
	 * Issue concerning the temporal behavior of a component. It represents 
	 * an inconsistency which generates an unsolvable flaw
	 */
	INVALID_BEHAVIOR(FlawCategoryType.UNSOLVABLE),
	
	/**
	 * Resource peak threat
	 */
	RESOURCE_PEAK(FlawCategoryType.SCHEDULING);
	
	private FlawCategoryType category;
	
	private FlawType(FlawCategoryType category) {
		this.category = category;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawCategoryType getCategory() {
		return category;
	}
}
