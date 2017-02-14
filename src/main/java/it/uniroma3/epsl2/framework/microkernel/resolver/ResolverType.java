package it.uniroma3.epsl2.framework.microkernel.resolver;

import it.uniroma3.epsl2.framework.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.microkernel.resolver.planning.PlanRefinementResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.pcp.ResourceSchedulingResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.timeline.behavior.BehaviorCheckingResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.timeline.behavior.ObservationBehaviorCheckingResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.timeline.gap.StateVariableGapResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.timeline.scheduling.StateVariableSchedulingResolver;

/**
 * 
 * @author anacleto
 *
 */
public enum ResolverType 
{
	/**
	 * Special type of resolver responsible for managing 
	 * synchronization rules and plan refinement
	 */
	PLAN_REFINEMENT(PlanRefinementResolver.class.getName(), 
			FlawType.PLAN_REFINEMENT),
	
	/**
	 * 
	 */
	RESOURCE_SCHEDULING_RESOLVER(ResourceSchedulingResolver.class.getName(), 
			FlawType.RESOURCE_PEAK),

	/**
	 * 
	 */
	SV_SCHEDULING_RESOLVER(StateVariableSchedulingResolver.class.getName(), 
			FlawType.SV_SCHEDULING),
	
	/**
	 * This resolver looks for gaps and complete the behavior of a 
	 * component by checking the related state variable
	 */
	SV_GAP_RESOLVER(StateVariableGapResolver.class.getName(), 
			FlawType.SV_GAP),
	
	/**
	 * This resolver checks if the behavior of the external variable contains some
	 * gaps. It generates an unsolvable flaw if a gap is found.
	 * 
	 * Typically such a resolver is used to check the behavior of external
	 * variables and checks their completeness
	 */
	OBSERVATION_CHECKING_RESOLVER(ObservationBehaviorCheckingResolver.class.getName(), 
			FlawType.INVALID_BEHAVIOR),
	
	/**
	 * This resolver checks if the timeline of a state variable is actually consistent.
	 * 
	 *  Namely it checks the actual behavior of a state variable by checking the validity
	 *  w.r.t. state variable specification (specifically the value transition function)
	 */
	BEHAVIOR_CHECKING_RESOLVER(BehaviorCheckingResolver.class.getName(),
			FlawType.INVALID_BEHAVIOR);
	
	// resolver class name
	private String cname;
	// represents the type of flaw the resolver can handle
	private FlawType flawType;
	
	/**
	 * 
	 * @param cname
	 * @param ftype
	 */
	private ResolverType(String cname, FlawType ftype) {
		this.cname = cname;
		this.flawType = ftype;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClassName() {
		return this.cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawType getFlawType() {
		return flawType;
	}
}
