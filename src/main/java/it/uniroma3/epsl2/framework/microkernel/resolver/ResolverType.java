package it.uniroma3.epsl2.framework.microkernel.resolver;

import it.uniroma3.epsl2.framework.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.microkernel.resolver.planning.PlanRefinementResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.discrete.DiscreteResourceSchedulingResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.timeline.behavior.StateVariableBehaviorCheckingResolver;
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
	 * This resolver is responsible for managing discrete resources. The 
	 * resolver analyzes optimistic and pessimistic profiles of a resource
	 * and posts precedence constraints to solve peaks. 
	 */
	DISCRETE_RESOURCE_SCHEDULING_RESOLVER(DiscreteResourceSchedulingResolver.class.getName(), 
			FlawType.RESOURCE_PEAK),

	/**
	 * This resolver complies with the semantics of timelines as a 
	 * continuous sequence of not overlapping tokens. Specifically, 
	 * the resolver is responsible for managing token overlaps.
	 */
	SV_SCHEDULING_RESOLVER(StateVariableSchedulingResolver.class.getName(), 
			FlawType.SV_SCHEDULING),
	
	/**
	 * This resolver complies with the semantics of timelines as a
	 * continuous sequence of not overlapping tokens. Specifically, this 
	 * resolver is responsible for managing gaps on a timeline and 
	 * complete its (temporal) behavior.
	 */
	SV_GAP_RESOLVER(StateVariableGapResolver.class.getName(), 
			FlawType.SV_GAP),
	
	/**
	 * This resolver is responsible for verifying the temporal behavior of a state
	 * variable. Namely, the resolver verifies if the obtained behavior is consistent 
	 * with the state variable specification.
	 */
	SV_BEHAVIOR_CHECKING_RESOLVER(StateVariableBehaviorCheckingResolver.class.getName(),
			FlawType.INVALID_BEHAVIOR),
	
	/**
	 * This resolver is responsible for verifying the observed behavior of external 
	 * state variable. Namely, the resolver verifies if the behavior of the external 
	 * variables of the domain (given as input of the problem specification) is complete 
	 * and valid with respect to the domain specification.
	 */
	OBSERVATION_CHECKING_RESOLVER(ObservationBehaviorCheckingResolver.class.getName(), 
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
