package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw;

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
	PLAN_REFINEMENT(FlawCategoryType.PLANNING, "plan-goal"),
	
	/**
	 * A set of temporally overlapping tokens on a timeline
	 */
	TIMELINE_OVERFLOW(FlawCategoryType.SCHEDULING, "timeline-peak"),
	
	/**
	 * An unbounded temporal interval that entail planning decisions to define the behavior of a particular component of the domain 
	 */
	TIMELINE_BEHAVIOR_PLANNING(FlawCategoryType.PLANNING, "timeline-planning"),
	
	
	/**
	 * A set of temporally overlapping activities that require an amount of resource exceeding the maximum capacity
	 */
	DISCRETE_OVERFLOW(FlawCategoryType.SCHEDULING, "discrete-overflow"),
	
	/**
	 * 
	 */
	RESERVOIR_OVERFLOW(FlawCategoryType.SCHEDULING, "reservoir-overflow"),
	
	/**
	 * 
	 */
	RESERVOIR_PLANNING(FlawCategoryType.PLANNING, "reservoir-planning"),
	
	
	/**
	 * 
	 */
	RESERVOIR_PROFILE_UPDATE(FlawCategoryType.PLANNING, "reservoir-profile-update"),
	
	/**
	 * Issue concerning the temporal behavior of a component. It represents 
	 * an inconsistency which generates an unsolvable flaw
	 */
	TIMELINE_BEHAVIOR_CHECKING(FlawCategoryType.UNSOLVABLE, "timeline-check");
	
	
	
	private FlawCategoryType category;
	private String label;
	
	/**
	 * 
	 * @param category
	 * @param label
	 */
	private FlawType(FlawCategoryType category, String label) {
		this.category = category;
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawCategoryType getCategory() {
		return category;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @param label
	 * @return
	 */
	public static FlawType getFlawTypeFromLabel(String label) 
	{
		// check flaws
		for (FlawType type : FlawType.values()) {
			// check label
			if (type.getLabel().equalsIgnoreCase(label.trim())) {
				// get the type
				return type;
			}
		}
		
		// no type of flaw correspondes to the label
		throw new RuntimeException("Unknown flaw type label \"" + label + "\"");
	}
}
