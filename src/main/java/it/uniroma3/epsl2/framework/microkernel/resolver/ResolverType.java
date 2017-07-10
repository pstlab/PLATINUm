package it.uniroma3.epsl2.framework.microkernel.resolver;

import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.microkernel.resolver.planning.PlanRefinementResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.DiscreteResourceSchedulingResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.ReservoirResourceSchedulingResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.timeline.behavior.ObservationBehaviorCheckingResolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.timeline.behavior.StateVariableBehaviorCheckingResolver;
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
			"Plan Refinement", 
			FlawType.PLAN_REFINEMENT),
	
	/**
	 * This resolver is responsible for managing discrete resources. The 
	 * resolver analyzes optimistic and pessimistic profiles of a resource
	 * and posts precedence constraints to solve peaks. 
	 */
	DISCRETE_RESOURCE_SCHEDULING_RESOLVER(DiscreteResourceSchedulingResolver.class.getName(),
			"Discrete Resource Scheduler",
			FlawType.RESOURCE_PEAK),
	
	/**
	 * 
	 */
	RESERVOIR_RESOURCE_SCHEDULING_RESOLVER(ReservoirResourceSchedulingResolver.class.getName(),
			"Reservoir Resource Scheduler",
			FlawType.RESOURCE_PEAK),

	/**
	 * This resolver complies with the semantics of timelines as a 
	 * continuous sequence of not overlapping tokens. Specifically, 
	 * the resolver is responsible for managing token overlaps.
	 */
	SV_SCHEDULING_RESOLVER(StateVariableSchedulingResolver.class.getName(),
			"State Variable Scheduler",
			FlawType.SV_SCHEDULING),
	
	/**
	 * This resolver complies with the semantics of timelines as a
	 * continuous sequence of not overlapping tokens. Specifically, this 
	 * resolver is responsible for managing gaps on a timeline and 
	 * complete its (temporal) behavior.
	 */
	SV_GAP_RESOLVER(StateVariableGapResolver.class.getName(),
			"Timeline Gap Manager",
			FlawType.SV_GAP),
	
	/**
	 * This resolver is responsible for verifying the temporal behavior of a state
	 * variable. Namely, the resolver verifies if the obtained behavior is consistent 
	 * with the state variable specification.
	 */
	SV_BEHAVIOR_CHECKING_RESOLVER(StateVariableBehaviorCheckingResolver.class.getName(),
			"State Variable Behavior Checker",
			FlawType.INVALID_BEHAVIOR),
	
	/**
	 * This resolver is responsible for verifying the observed behavior of external 
	 * state variable. Namely, the resolver verifies if the behavior of the external 
	 * variables of the domain (given as input of the problem specification) is complete 
	 * and valid with respect to the domain specification.
	 */
	OBSERVATION_CHECKING_RESOLVER(ObservationBehaviorCheckingResolver.class.getName(),
			"Observation Checker",
			FlawType.INVALID_BEHAVIOR);
	
	// resolver class name
	private String cname;
	private String label;
	// represents the type of flaw the resolver can handle
	private FlawType flawType;
	
	/**
	 * 
	 * @param cname
	 * @param label
	 * @param ftype
	 */
	private ResolverType(String cname, String label, FlawType ftype) {
		this.cname = cname;
		this.label = label;
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
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawType getFlawType() {
		return flawType;
	}
}
