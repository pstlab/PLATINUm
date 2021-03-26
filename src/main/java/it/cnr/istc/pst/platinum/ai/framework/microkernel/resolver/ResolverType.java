package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.plan.PlanRefinementResolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.resource.discrete.DiscreteResourceSchedulingResolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.resource.reservoir.ReservoirResourceSchedulingResolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.checking.ObservationBehaviorCheckingResolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.checking.TimelineBehaviorCheckingResolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.behavior.planning.TimelineBehaviorPlanningResolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling.TimelineSchedulingResolver;

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
	PLAN_REFINEMENT(
			PlanRefinementResolver.class.getName(), 
			"Plan Refinement", 
			new FlawType[] {
					FlawType.PLAN_REFINEMENT
			}),
	
	/**
	 * This resolver is responsible for managing discrete resources. The 
	 * resolver analyzes optimistic and pessimistic profiles of a resource
	 * and posts precedence constraints to solve peaks. 
	 */
	DISCRETE_RESOURCE_SCHEDULING_RESOLVER(
			DiscreteResourceSchedulingResolver.class.getName(),
			"Discrete Resource Scheduler",
			new FlawType[] {
				FlawType.DISCRETE_OVERFLOW
			}),
	
	/**
	 * 
	 */
	RESERVOIR_RESOURCE_SCHEDULING_RESOLVER(
			ReservoirResourceSchedulingResolver.class.getName(),
			"Reservoir Resource Scheduler",
			new FlawType[] {
					FlawType.RESERVOIR_OVERFLOW
			}),
	
	/**
	 * TODO - Not implemented yet
	 */
	RESERVOIR_RESOURCE_PLANNING_RESOLVER(
			null,
			"Reservoir Resource Planner",
			new FlawType[] {
					FlawType.RESERVOIR_PLANNING,
					FlawType.RESERVOIR_PROFILE_UPDATE
			}),
	
	/**
	 * This resolver complies with the semantics of timelines as a 
	 * continuous sequence of not overlapping tokens. Specifically, 
	 * the resolver is responsible for managing token overlaps.
	 */
	TIMELINE_SCHEDULING_RESOLVER(
			TimelineSchedulingResolver.class.getName(),
			"State Variable Scheduler",
			new FlawType[] {
				FlawType.TIMELINE_OVERFLOW
			}),
	
	/**
	 * This resolver complies with the semantics of timelines as a
	 * continuous sequence of not overlapping tokens. Specifically, this 
	 * resolver is responsible for managing gaps on a timeline and 
	 * complete its (temporal) behavior.
	 */
	TIMELINE_BEHAVIOR_PLANNING_RESOLVER(
			TimelineBehaviorPlanningResolver.class.getName(),
			"Timeline Gap Manager",
			new FlawType[] {
					FlawType.TIMELINE_BEHAVIOR_PLANNING
	}),
	
	/**
	 * This resolver is responsible for verifying the temporal behavior of a state
	 * variable. Namely, the resolver verifies if the obtained behavior is consistent 
	 * with the state variable specification.
	 */
	TIMELINE_BEHAVIOR_CHECKING_RESOLVER(
			TimelineBehaviorCheckingResolver.class.getName(),
			"State Variable Behavior Checker",
			new FlawType[] {
				FlawType.TIMELINE_BEHAVIOR_CHECKING
			}),
	
	/**
	 * This resolver is responsible for verifying the observed behavior of external 
	 * state variable. Namely, the resolver verifies if the behavior of the external 
	 * variables of the domain (given as input of the problem specification) is complete 
	 * and valid with respect to the domain specification.
	 */
	OBSERVATION_CHECKING_RESOLVER(
			ObservationBehaviorCheckingResolver.class.getName(),
			"Observation Checker",
			new FlawType[] {
				FlawType.TIMELINE_BEHAVIOR_CHECKING
			});
	
	// resolver class name
	private String cname;
	private String label;
	// represents the type of flaw the resolver can handle
	private FlawType[] flawTypes;
	
	/**
	 * 
	 * @param cname
	 * @param label
	 * @param ftype
	 */
	private ResolverType(String cname, String label, FlawType[] ftypes) {
		this.cname = cname;
		this.label = label;
		this.flawTypes = ftypes;
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
	public FlawType[] getFlawTypes() {
		return flawTypes;
	}
}
