package it.istc.pst.platinum.framework.domain.component.resource.reservoir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.ex.ResourceProfileComputationException;
import it.istc.pst.platinum.framework.domain.component.resource.Resource;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.parameter.lang.NumericParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.lang.FixTimePointConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.tn.TimePoint;
import it.istc.pst.platinum.framework.time.tn.lang.query.TimePointScheduleQuery;

/**
 * 
 * @author anacleto
 *
 */
public class ReservoirResource extends Resource<ResourceUsageValue> implements ReservoirResourceProfileManager
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
			// reservoir resource scheduler
			ResolverType.RESERVOIR_RESOURCE_SCHEDULING_RESOLVER
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
	 * @return
	 */
	public ResourceUsageValue addConsumptionValue() {
		// add the value
		return this.addValue(CONSUMPTION_LABEL, new long[] {1, this.tdb.getHorizon()}, true);
	}
	
	/**
	 * 
	 * @return
	 */
	public ResourceUsageValue addProductionValue() {
		// add the value
		return this.addValue(PRODUCTION_LABEL, new long[] {1, this.tdb.getHorizon()}, true);
	}
	
	/**
	 * 
	 */
	@Override
	public ResourceUsageValue addValue(String label, long[] duration, boolean controllable) 
	{
		// resource usage
		ResourceUsageValue res = null;
		// check consumption label and related value
		if (label.equals(CONSUMPTION_LABEL) && this.consumption == null) 
		{
			// create resource consumption value
			this.consumption = new ResourceConsumptionValue(CONSUMPTION_LABEL, duration, controllable, this);
			// set also the domains of the "amount" parameter 
			NumericParameterDomain dom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", 
					ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
			// set domain bounds
			dom.setLowerBound(0);
			dom.setUpperBound(this.max);
			// set parameter domain
			this.consumption.addParameterPlaceHolder(dom);
			// add to values
			this.values.add(this.consumption);
			// set value
			res = this.consumption;
		}
		
		// check production label and related value
		if (label.equals(PRODUCTION_LABEL) && this.production == null) 
		{
			// create resource production value
			this.production = new ResourceProductionValue(PRODUCTION_LABEL, duration, controllable, this);
			// set also the domains of the "amount" parameter 
			NumericParameterDomain dom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", 
					ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
			// set domain bounds
			dom.setLowerBound(0);
			dom.setUpperBound(this.max);
			// set parameter domain
			this.production.addParameterPlaceHolder(dom);
			// add to values
			this.values.add(this.production);
			// set value
			res = this.production;
		}
		
		// get resource usage
		return res;
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
	@Override
	public List<ResourceUsageValue> getValues() {
		// list of values
		return new ArrayList<ResourceUsageValue>(this.values);
	}

	/**
	 * 
	 */
	@Override
	public ResourceUsageValue getValueByName(String name) {
		// requirement value
		ResourceUsageValue value = null;
		for (ResourceUsageValue v : this.values) {
			if (v.getLabel().equals(name)) {
				value = v;
				break;
			}
		}
		
		// check if value has been found
		if (value == null) {
			throw new RuntimeException("Value \"" + name + "\" not found on reservoir resource \"" + this.name + "\"");
		}
		
		// get the requirement value
		return value;
	}

	/**
	 * 
	 */
	@Override
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
	@Override
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
				long amount = -consumption.getAmount();
				
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
				this.tdb.checkConsistency();
				
				// create profile sample
				UsageResourceProfileSample sample = new UsageResourceProfileSample(time, amount);
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
				long amount = production.getAmount();
				
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
				this.tdb.checkConsistency();
				
				
				// create profile sample
				UsageResourceProfileSample sample = new UsageResourceProfileSample(time, amount);
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
	@Override
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
				long amount = -consumption.getAmount();
				
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
				this.tdb.checkConsistency();
				
				// create profile sample
				UsageResourceProfileSample sample = new UsageResourceProfileSample(time, amount);
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
				long amount = production.getAmount();
				
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
				this.tdb.checkConsistency();
				
				
				// create profile sample
				UsageResourceProfileSample sample = new UsageResourceProfileSample(time, amount);
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
