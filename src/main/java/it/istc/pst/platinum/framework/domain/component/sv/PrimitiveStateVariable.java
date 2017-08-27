package it.istc.pst.platinum.framework.domain.component.sv;

import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;

/**
 * 
 * @author anacleto
 *
 */
public final class PrimitiveStateVariable extends StateVariable //implements DiscreteResourceProfileManager
{
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// scheduling resolver
			ResolverType.TIMELINE_SCHEDULING_RESOLVER,
			// time-line gap resolver
			ResolverType.TIMELINE_BEHAVIOR_PLANNING_RESOLVER,
			// behavior checking resolver
			ResolverType.TIMELINE_BEHAVIOR_CHECKING_RESOLVER
	})
	protected PrimitiveStateVariable(String name) {
		super(name, DomainComponentType.SV_PRIMITIVE);
	}

//	/**
//	 * 
//	 */
//	@Override
//	public long getMinCapacity() {
//		return 0;
//	}
//
//	/**
//	 * 
//	 */
//	@Override
//	public long getMaxCapacity() {
//		return 1;
//	}
//
//	/**
//	 * 
//	 */
//	@Override
//	public long getInitialCapacity() {
//		return 1;
//	}
//
//	/**
//	 * 
//	 */
//	@Override
//	public List<RequirementResourceEvent> getRequirements() 
//	{
//		// initialize the list of resource requirements
//		List<RequirementResourceEvent> list = new ArrayList<>();
//		// check active decisions
//		for (Decision activity : this.decisions.get(PlanElementStatus.ACTIVE)) 
//		{
//			// create resource requirement event
//			RequirementStateVariableResourceEvent event = new RequirementStateVariableResourceEvent(activity);
//			// add event to the list
//			list.add(event);
//		}
//		
//		// sort the list of events
//		Collections.sort(list);
//		// get the list of requirements
//		return list;
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	@Override
//	public DiscreteResourceProfile computePessimisticResourceProfile() 
//	{
//		// initialize the profile 
//		StateVariableResourceProfile prp = new StateVariableResourceProfile();
//		// prepare a list of committed constraints
//		List<TemporalConstraint> committed = new ArrayList<>();
//		try
//		{
//			// get requirements
//			List<RequirementResourceEvent> events = this.getRequirements();
//			for (RequirementResourceEvent event : events) 
//			{
//				// schedule the start time to the lower bound (consumptions occur as soon as possible)
//				TimePoint consumption = event.getStart();
//				// schedule the end time to the upper bound (productions occur as late as possible)
//				TimePoint production = event.getEnd();
//				// get amount of resource requirement
//				long amount = event.getAmount();
//				
//				// check the updated schedule of the start time point
//				TimePointScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.TP_SCHEDULE);
//				// set the time point
//				query.setTimePoint(consumption);
//				// process query
//				this.tdb.process(query);
//				
//				// get start time of the sample
//				long start = consumption.getLowerBound();
//				
//				// check feasibility of the start time
//				FixTimePointConstraint fixStart = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
//				// set time point
//				fixStart.setReference(consumption);
//				// set the lower bound
//				fixStart.setTime(start);
//				// propagate constraint
//				this.tdb.propagate(fixStart);
//				committed.add(fixStart);
//				// check consistency
//				this.tdb.checkConsistency();
//				
//				// check updated scheduled of the end time point
//				query.setTimePoint(production);
//				// process query
//				this.tdb.process(query);
//				
//				// get end time of the sample 
//				long end = production.getUpperBound();
//				
//				// check feasibility of the end time
//				FixTimePointConstraint fixEnd = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
//				fixEnd.setReference(production);
//				fixEnd.setTime(end);
//				// propagate constraint
//				this.tdb.propagate(fixEnd);
//				committed.add(fixEnd);
//				// check consistency
//				this.tdb.checkConsistency();
//				
//				// create sample and add sample to the profile
//				RequirementResourceProfileSample sample = new RequirementResourceProfileSample(amount, start, end);
//				prp.addSampel(sample);
//			}
//		}
//		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
//			this.logger.debug("Impossible to compute the pessimistic profile on discrete resource \"" + this.name + "\"\n- message: " + ex.getMessage() + "\n");
//			// clear profile
//			prp = new DiscreteResourceProfile(this);
//		}
//		finally 
//		{
//			// retract all propagated temporal constraints
//			for (TemporalConstraint constraint : committed) {
//				// retract constraint
//				this.tdb.retract(constraint);
//			}
//		}
//
//		// get computed profile
//		return prp;
//	}
//
//	@Override
//	public DiscreteResourceProfile computeOptimisticResourceProfile() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
