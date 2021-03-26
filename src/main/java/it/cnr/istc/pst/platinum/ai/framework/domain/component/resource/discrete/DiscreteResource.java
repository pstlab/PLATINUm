package it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.discrete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.PlanElementStatus;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.ResourceProfileComputationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.Resource;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEvent;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ResolverType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.NumericParameterDomain;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomainType;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConstraintPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.FixTimePointConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TimePoint;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query.TimePointScheduleQuery;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResource extends Resource
{
	public static final String REQUIREMENT_LABEL = "REQUIREMENT";		// language constant
	private RequirementResourceValue requirement;						// requirement value
	
	
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// planning resolver
			ResolverType.PLAN_REFINEMENT,
			// discrete resource scheduler
			ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER
	})
	protected DiscreteResource(String name) {
		super(name, DomainComponentType.RESOURCE_DISCRETE);
		this.min = Integer.MIN_VALUE + 1;
		this.max = Integer.MAX_VALUE - 1;
		this.initial = this.max;
		// create the requirement value
		this.requirement = null;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		super.init();
		// setup the only value available for a discrete resource component
		this.requirement = new RequirementResourceValue(
				REQUIREMENT_LABEL, 
				new long[] {
						1, this.tdb.getHorizon()
				}, 
				this);
		
		// set also the domains of the "amount" parameter 
		NumericParameterDomain dom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", 
				ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
		
		// set domain bounds
		dom.setLowerBound(0);
		dom.setUpperBound(this.max);
		// set parameter domain
		this.requirement.addParameterPlaceHolder(dom);
	}

	/**
	 * Get the list of (temporally flexible) requirements of a discrete resource
	 * 
	 * @return
	 */
	public List<RequirementResourceEvent> getRequirements() 
	{
		// list of values
		List<RequirementResourceEvent> list = new ArrayList<>();
		// check active decisions
		for (Decision activity : this.decisions.get(PlanElementStatus.ACTIVE)) 
		{
			// check the amount of resource required
			int amount = this.getRequiredAmountOfResource(activity);
			// create resource requirement event
			RequirementResourceEvent event = new RequirementResourceEvent(activity, amount);
			// add event to the list
			list.add(event);
		}
		
		// sort the list of events
		Collections.sort(list);
		// get list
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<ResourceEvent<?>> getEvents() {
		// get requirements
		return new ArrayList<>(this.getRequirements());
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RequirementResourceValue> getValues() {
		// get values - only one element expected
		List<RequirementResourceValue> list = new ArrayList<>();
		list.add(this.requirement);
		// get list of available values
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public RequirementResourceValue getRequirementValue() {
		// get requirement values - the first element of the list
		return this.requirement;
	}

	/**
	 * 
	 */
	@Override
	public RequirementResourceValue getValueByName(String name) 
	{	
		// get the requirement value
		return this.requirement;
	}
	
	/**
	 * Compute the pessimistic profile (PRP) of a discrete resource
	 * 
	 * @return
	 * @throws ResourceProfileComputationException
	 */
	public DiscreteResourceProfile computePessimisticResourceProfile() 
			throws ResourceProfileComputationException
	{
		// initialize the profile 
		DiscreteResourceProfile prp = new DiscreteResourceProfile();
		// prepare a list of committed constraints
		List<TemporalConstraint> committed = new ArrayList<>();
		try
		{
			// get requirements
			List<RequirementResourceEvent> events = this.getRequirements();
			for (RequirementResourceEvent event : events) 
			{
				// schedule the start time to the lower bound (consumptions occur as soon as possible)
				TimePoint consumption = event.getStart();
				// schedule the end time to the upper bound (productions occur as late as possible)
				TimePoint production = event.getEnd();
				
				// check the updated schedule of the start time point
				TimePointScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.TP_SCHEDULE);
				// set the time point
				query.setTimePoint(consumption);
				// process query
				this.tdb.process(query);
				
				// get start time of the sample
				long start = consumption.getLowerBound();
				
				// check feasibility of the start time
				FixTimePointConstraint fixStart = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
				// set time point
				fixStart.setReference(consumption);
				// set the lower bound
				fixStart.setTime(start);
				// propagate constraint
				this.tdb.propagate(fixStart);
				committed.add(fixStart);
				// check consistency
				this.tdb.verify();
				
				// check updated scheduled of the end time point
				query.setTimePoint(production);
				// process query
				this.tdb.process(query);
				
				// get end time of the sample 
				long end = production.getUpperBound();
				
				// check feasibility of the end time
				FixTimePointConstraint fixEnd = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
				fixEnd.setReference(production);
				fixEnd.setTime(end);
				// propagate constraint
				this.tdb.propagate(fixEnd);
				committed.add(fixEnd);
				// check consistency
				this.tdb.verify();
				
				// create sample and add sample to the profile
				RequirementResourceProfileSample sample = new RequirementResourceProfileSample(event, start, end);
				prp.addSampel(sample);
			}
		}
		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// impossible to compute a profile for the resource
			throw new ResourceProfileComputationException("Impossible to compute the pessimistic profile on discrete resource \"" + this.name + "\"\n- message: " + ex.getMessage() + "\n");
		}
		finally 
		{
			// retract all propagated temporal constraints
			for (TemporalConstraint constraint : committed) {
				// retract constraint
				this.tdb.retract(constraint);
			}
		}

		// get computed profile
		return prp;
	}
	
	/**
	 * Compute the optimistic profile (ORP) of a discrete resource
	 * 
	 * @return
	 * @throws ResourceProfileComputationException
	 */
	public DiscreteResourceProfile computeOptimisticResourceProfile() 
			throws ResourceProfileComputationException
	{
		// initialize the profile
		DiscreteResourceProfile orp = new DiscreteResourceProfile();
		
		// prepare a list of committed constraints
		List<TemporalConstraint> committed = new ArrayList<>();
		try
		{
			// get requirements
			List<RequirementResourceEvent> events = this.getRequirements();
			for (RequirementResourceEvent event : events) 
			{
				// schedule the start time to the lower bound (consumptions occur as soon as possible)
				TimePoint consumption = event.getStart();
				// schedule the end time to the upper bound (productions occur as late as possible)
				TimePoint production = event.getEnd();
				
				// check the updated schedule of the start time point
				TimePointScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.TP_SCHEDULE);
				// set the time point
				query.setTimePoint(consumption);
				// process query
				this.tdb.process(query);
				
				// get start time of the sample
				long start = consumption.getUpperBound();
				
				// check feasibility of the start time
				FixTimePointConstraint fixStart = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
				// set time point
				fixStart.setReference(consumption);
				// set the lower bound
				fixStart.setTime(start);
				// propagate constraint
				this.tdb.propagate(fixStart);
				committed.add(fixStart);
				// check consistency
				this.tdb.verify();
				
				// check updated scheduled of the end time point
				query.setTimePoint(production);
				// process query
				this.tdb.process(query);
				
				// get end time of the sample 
				long end = production.getLowerBound();
				
				// check feasibility of the end time
				FixTimePointConstraint fixEnd = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
				fixEnd.setReference(production);
				fixEnd.setTime(end);
				// propagate constraint
				this.tdb.propagate(fixEnd);
				committed.add(fixEnd);
				// check consistency
				this.tdb.verify();
				
				// create sample and add sample to the profile
				RequirementResourceProfileSample sample = new RequirementResourceProfileSample(event, start, end);
				orp.addSampel(sample);
			}
		}
		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// impossible to compute a profile for the resource
			throw new ResourceProfileComputationException("Impossible to compute the optimistic profile on discrete resource \"" + this.name + "\"\n- message: " + ex.getMessage() + "\n");
		}
		finally 
		{
			// retract all propagated temporal constraints
			for (TemporalConstraint constraint : committed) {
				// retract constraint
				this.tdb.retract(constraint);
			}
		}
		
		// get computed profile
		return orp;
	}
}
