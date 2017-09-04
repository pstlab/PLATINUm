package it.istc.pst.platinum.framework.microkernel.resolver;

import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.resolver.plan.PlanRefinementResolver;
import it.istc.pst.platinum.framework.microkernel.resolver.resource.discrete.DiscreteResourceSchedulingResolver;
import it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir.ReservoirResourceSchedulingResolver;
import it.istc.pst.platinum.framework.microkernel.resolver.timeline.checking.ObservationBehaviorCheckingResolver;
import it.istc.pst.platinum.framework.microkernel.resolver.timeline.checking.TimelineBehaviorCheckingResolver;
import it.istc.pst.platinum.framework.microkernel.resolver.timeline.planning.TimelineBehaviorPlanningResolver;
import it.istc.pst.platinum.framework.microkernel.resolver.timeline.scheduling.TImelineSchedulingResolver;

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
			FlawType.RESOURCE_OVERFLOW),
	
	/**
	 * 
	 */
	RESERVOIR_RESOURCE_SCHEDULING_RESOLVER(ReservoirResourceSchedulingResolver.class.getName(),
			"Reservoir Resource Scheduler",
			FlawType.RESOURCE_PLANNING),

	/**
	 * This resolver complies with the semantics of timelines as a 
	 * continuous sequence of not overlapping tokens. Specifically, 
	 * the resolver is responsible for managing token overlaps.
	 */
	TIMELINE_SCHEDULING_RESOLVER(TImelineSchedulingResolver.class.getName(),
			"State Variable Scheduler",
			FlawType.TIMELINE_OVERFLOW),
	
	/**
	 * This resolver complies with the semantics of timelines as a
	 * continuous sequence of not overlapping tokens. Specifically, this 
	 * resolver is responsible for managing gaps on a timeline and 
	 * complete its (temporal) behavior.
	 */
	TIMELINE_BEHAVIOR_PLANNING_RESOLVER(TimelineBehaviorPlanningResolver.class.getName(),
			"Timeline Gap Manager",
			FlawType.TIMELINE_BEHAVIOR_PLANNING),
	
	/**
	 * This resolver is responsible for verifying the temporal behavior of a state
	 * variable. Namely, the resolver verifies if the obtained behavior is consistent 
	 * with the state variable specification.
	 */
	TIMELINE_BEHAVIOR_CHECKING_RESOLVER(TimelineBehaviorCheckingResolver.class.getName(),
			"State Variable Behavior Checker",
			FlawType.TIMELINE_BEHAVIOR_CHECKING),
	
	/**
	 * This resolver is responsible for verifying the observed behavior of external 
	 * state variable. Namely, the resolver verifies if the behavior of the external 
	 * variables of the domain (given as input of the problem specification) is complete 
	 * and valid with respect to the domain specification.
	 */
	OBSERVATION_CHECKING_RESOLVER(ObservationBehaviorCheckingResolver.class.getName(),
			"Observation Checker",
			FlawType.TIMELINE_BEHAVIOR_CHECKING);
	
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
