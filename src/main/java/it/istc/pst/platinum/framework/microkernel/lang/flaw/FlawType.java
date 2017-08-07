package it.istc.pst.platinum.framework.microkernel.lang.flaw;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawType 
{
	/**
	 * A condition or state that constrain the behavior of the component of the domain and that must be achieved in order to complete a plan 
	 * 
	 */
	PLAN_REFINEMENT(FlawCategoryType.PLANNING),
	
	/**
	 * A set of temporally overlapping activities that require an amount of resource exceeding the maximum capacity
	 */
	RESOURCE_OVERFLOW(FlawCategoryType.SCHEDULING),
	
	/**
	 * An unbounded temporal interval that entail planning decisions to define the behavior of a particular component of the domain 
	 */
	INCOMPLETE_BEHAVIOR(FlawCategoryType.PLANNING),
	
	/**
	 *  
	 */
	RESOURCE_PLANNING(FlawCategoryType.PLANNING_SCHEDULING),
	
	/**
	 * Issue concerning the temporal behavior of a component. It represents 
	 * an inconsistency which generates an unsolvable flaw
	 */
	INVALID_BEHAVIOR(FlawCategoryType.UNSOLVABLE);
	
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
