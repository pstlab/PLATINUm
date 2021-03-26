package it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.reservoir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
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
public class ReservoirResource extends Resource
{
	public static final String CONSUMPTION_LABEL = "CONSUMPTION";
	public static final String PRODUCTION_LABEL = "PRODUCTION";
	private ResourceConsumptionValue consumption;							// production value
	private ResourceProductionValue production;								// consumption value
	
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// planning resolver
			ResolverType.PLAN_REFINEMENT,
			// reservoir resource scheduler
			ResolverType.RESERVOIR_RESOURCE_SCHEDULING_RESOLVER,
			// reservoir resource planning
//			ResolverType.RESERVOIR_RESOURCE_PLANNING_RESOLVER
	})
	protected ReservoirResource(String name) {
		super(name, DomainComponentType.RESOURCE_RESERVOIR);
		this.min = Integer.MIN_VALUE + 1;
		this.max = Integer.MAX_VALUE - 1;
		this.initial = this.max;
		this.consumption = null;
		this.production = null;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void init() 
	{
		super.init();
		// create resource consumption value
		this.consumption = new ResourceConsumptionValue(CONSUMPTION_LABEL, new long[] {1, this.tdb.getHorizon()}, true, this);
		// set also the domains of the "amount" parameter 
		NumericParameterDomain cDom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", 
				ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
		// set domain bounds
		cDom.setLowerBound(0);
		cDom.setUpperBound(this.max);
		// set parameter domain
		this.consumption.addParameterPlaceHolder(cDom);
		
		// create resource production value
		this.production = new ResourceProductionValue(PRODUCTION_LABEL, new long[] {1, this.tdb.getHorizon()}, true, this);
		// set also the domains of the "amount" parameter 
		NumericParameterDomain pDom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", 
				ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
		// set domain bounds
		pDom.setLowerBound(0);
		pDom.setUpperBound(this.max);
		// set parameter domain
		this.production.addParameterPlaceHolder(pDom);
	}
	
	/**
	 * 
	 * @return
	 */
	public ResourceProductionValue getProductionValue() {
		return this.production;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResourceUsageValue getConsumptionValue() {
		return this.consumption;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ResourceUsageValue> getValues() {
		// list of values
		List<ResourceUsageValue> values = new ArrayList<>();
		values.add(this.consumption);
		values.add(this.production);
		return values;
	}

	/**
	 * 
	 */
	@Override
	public ResourceUsageValue getValueByName(String name) 
	{
		// the value
		ResourceUsageValue value = null;
		// check consumption value
		if (name.equals(CONSUMPTION_LABEL)) {
			// set consumption value
			value = this.consumption;
		}
		
		// check production value
		if (name.equals(PRODUCTION_LABEL)) {
			// set production value
			value = this.production;
		}
		
		// unknown value
		if (value == null) {
			throw new RuntimeException("Unknown reservoir resource value \"" + name + "\"\n");
		}
		
		// get the requirement value
		return value;
	}

	/**
	 * 
	 */
	public List<ProductionResourceEvent> getProductions() 
	{
		// list of production events
		List<ProductionResourceEvent> list = new ArrayList<>();
		// check active decisions
		for (Decision activity : this.getActiveDecisions())
		{
			// check value
			if (activity.getValue().equals(this.production)) 
			{
				// check amount of production
				int amount = this.getRequiredAmountOfResource(activity);
				// create production event
				ProductionResourceEvent prod = new ProductionResourceEvent(activity, amount);
				// add event
				list.add(prod);
			}
		}
		
		// sort events
		Collections.sort(list);
		// get production events
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<ResourceEvent<?>> getEvents() {
		// list of events
		List<ResourceEvent<?>> list = new ArrayList<>();
		// add consumptions
		list.addAll(this.getConsumptions());
		// add productions
		list.addAll(this.getProductions());
		// sort events
		Collections.sort(list);
		// get the list
		return list;
	}

	/**
	 * 
	 */
	public List<ConsumptionResourceEvent> getConsumptions() 
	{
		// list of consumption events
		List<ConsumptionResourceEvent> list = new ArrayList<>();
		// check active decisions
		for (Decision activity : this.getActiveDecisions())
		{
			// check value
			if (activity.getValue().equals(this.consumption)) 
			{
				// check amount of consumption
				int amount = this.getRequiredAmountOfResource(activity);
				// create consumption event
				ConsumptionResourceEvent cons = new ConsumptionResourceEvent(activity, amount); 
				// add event
				list.add(cons);
			}
		}
		
		// sort consumptions
		Collections.sort(list);
		// get consumption events
		return list;
	}
	
	/**
	 * 
	 */
	public ReservoirResourceProfile computeOptimisticResourceProfile() 
			throws ResourceProfileComputationException
	{
		// initialize optimistic profile
		ReservoirResourceProfile orp = new ReservoirResourceProfile();
		// list of committed temporal constraints
		List<TemporalConstraint> committed = new ArrayList<>();
		try
		{
			// get consumptions
			List<ConsumptionResourceEvent> consumptions = this.getConsumptions();
			for (ConsumptionResourceEvent consumption : consumptions)
			{
				// get consumption temporal event
				TimePoint event = consumption.getEvent();
				// get amount of resource consumed (negative number)
				double amount = -consumption.getAmount();
				
				// check the current schedule of the time point
				TimePointScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.TP_SCHEDULE);
				query.setTimePoint(event);
				// process query
				this.tdb.process(query);
				
				// schedule consumption as late as possible
				long time = event.getUpperBound();
				
				// check schedule feasibility
				FixTimePointConstraint fix = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
				fix.setReference(event);
				fix.setTime(time);
				// propagate constraint
				this.tdb.propagate(fix);
				// add to committed
				committed.add(fix);
				// check consistency
				this.tdb.verify();
				
				// create profile sample
				ResourceUsageProfileSample sample = new ResourceUsageProfileSample(consumption, time, amount);
				// add sample
				orp.addSampel(sample);
			}
			
			// get productions
			List<ProductionResourceEvent> productions = this.getProductions();
			for (ProductionResourceEvent production : productions)
			{
				// get production temporal event
				TimePoint event = production.getEvent();
				// get amount of resource produced (positive number)
				double amount = production.getAmount();
				
				// check the current schedule of the time point
				TimePointScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.TP_SCHEDULE);
				query.setTimePoint(event);
				// process query
				this.tdb.process(query);
				
				// schedule production as soon as possible 
				long time = event.getLowerBound();
				
				// check schedule feasibility
				FixTimePointConstraint fix = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
				fix.setReference(event);
				fix.setTime(time);
				// propagate constraint
				this.tdb.propagate(fix);
				// add to committed
				committed.add(fix);
				// check consistency
				this.tdb.verify();
				
				
				// create profile sample
				ResourceUsageProfileSample sample = new ResourceUsageProfileSample(production, time, amount);
				// add sample
				orp.addSampel(sample);
			}
		}
		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// impossible to compute a profile for the resource
			throw new ResourceProfileComputationException("Impossible to compute the optimistic profile on reservoir resource \"" + this.name + "\"\n- message: " + ex.getMessage() + "\n");
		}
		finally 
		{
			// retract all propagated constraints
			for (TemporalConstraint constraint: committed) {
				// retract constraint
				this.tdb.retract(constraint);
			}
		}
		
		// get computed profile
		return orp;
	}
	
	/**
	 * 
	 */
	public ReservoirResourceProfile computePessimisticResourceProfile() 
			throws ResourceProfileComputationException
	{
		// initialize pessimistic profile
		ReservoirResourceProfile prp = new ReservoirResourceProfile();
		// list of committed temporal constraints
		List<TemporalConstraint> committed = new ArrayList<>();
		try
		{
			// get consumptions
			List<ConsumptionResourceEvent> consumptions = this.getConsumptions();
			for (ConsumptionResourceEvent consumption : consumptions)
			{
				// get consumption temporal event
				TimePoint event = consumption.getEvent();
				// get amount of resource consumed (negative number)
				double amount = -consumption.getAmount();
				
				// check the current schedule of the time point
				TimePointScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.TP_SCHEDULE);
				query.setTimePoint(event);
				// process query
				this.tdb.process(query);
				
				// schedule consumption as soon as possible
				long time = event.getLowerBound();
				
				// check schedule feasibility
				FixTimePointConstraint fix = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
				fix.setReference(event);
				fix.setTime(time);
				// propagate constraint
				this.tdb.propagate(fix);
				// add to committed
				committed.add(fix);
				// check consistency
				this.tdb.verify();
				
				// create profile sample
				ResourceUsageProfileSample sample = new ResourceUsageProfileSample(consumption, time, amount);
				// add sample
				prp.addSampel(sample);
			}
			
			// get productions
			List<ProductionResourceEvent> productions = this.getProductions();
			for (ProductionResourceEvent production : productions)
			{
				// get production temporal event
				TimePoint event = production.getEvent();
				// get amount of resource produced (positive number)
				double amount = production.getAmount();
				
				// check the current schedule of the time point
				TimePointScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.TP_SCHEDULE);
				query.setTimePoint(event);
				// process query
				this.tdb.process(query);
				
				// schedule production as late as possible 
				long time = event.getUpperBound();
				
				// check schedule feasibility
				FixTimePointConstraint fix = this.tdb.createTemporalConstraint(TemporalConstraintType.FIX_TIME_POINT);
				fix.setReference(event);
				fix.setTime(time);
				// propagate constraint
				this.tdb.propagate(fix);
				// add to committed
				committed.add(fix);
				// check consistency
				this.tdb.verify();
				
				
				// create profile sample
				ResourceUsageProfileSample sample = new ResourceUsageProfileSample(production, time, amount);
				// add sample 
				prp.addSampel(sample);
			}
		}
		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// impossible to compute a profile for the resource
			throw new ResourceProfileComputationException("Impossible to compute the pessimistic profile on reservoir resource \"" + this.name + "\"\n- message: " + ex.getMessage() + "\n");
		}
		finally 
		{
			// retract all propagated constraints
			for (TemporalConstraint constraint: committed) {
				// retract constraint
				this.tdb.retract(constraint);
			}
		}

		// get computed profile
		return prp;
	}
}
