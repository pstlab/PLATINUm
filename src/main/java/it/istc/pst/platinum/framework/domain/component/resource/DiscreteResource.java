package it.istc.pst.platinum.framework.domain.component.resource;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.parameter.lang.NumericParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResource extends Resource 
{
	// the only value allowed
	private ResourceRequirementValue requirement;
	
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// discrete resource scheduler
			ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER
	})
	protected DiscreteResource(String name) {
		super(name, DomainComponentType.RESOURCE_DISCRETE);
		this.min = Integer.MIN_VALUE + 1;
		this.max = Integer.MAX_VALUE - 1;
		this.initial = this.max;
		this.requirement = null;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void addRequirementValue(String value)
	{
		// setup domain
		NumericParameterDomain dom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
		dom.setLowerBound(0);
		dom.setUpperBound(this.max);
		// create value
		this.requirement = new ResourceRequirementValue(value, new long[] {1, this.tdb.getHorizon()}, this);
		// set domain
		this.requirement.addParameterPlaceHolder(dom);
		// add value
		this.values.add(this.requirement);
	}
	
	/**
	 * 
	 * @return
	 */
	public ResourceRequirementValue getRequirementValue() {
		return this.requirement;
	}

	/**
	 * 
	 */
	@Override
	public List<ResourceEvent> getProductions() 
	{
		// list of events
		List<ResourceEvent> list = new ArrayList<>();
		// check resource requirements
		for (Decision dec : this.getActiveDecisions())
		{
			// check amount
			int amount = this.checkAmount(dec);
			// create production event
			ResourceEvent prod = new ProductionResourceEvent(
					dec, 
					dec.getToken().getInterval().getEndTime(), 
					amount);
			// add event
			list.add(prod);
		}

		// get productions
		return list;
	}

	/**
	 * 
	 */
	@Override
	public List<ResourceEvent> getConsumptions() 
	{
		// list of consumptions
		List<ResourceEvent> list = new ArrayList<>();
		for (Decision dec : this.getActiveDecisions()) 
		{
			// check amount 
			int amount = this.checkAmount(dec);
			// create consumption event
			ResourceEvent cons = new ConsumptionResourceEvent(
					dec, 
					dec.getToken().getInterval().getStartTime(), 
					amount);
			// add event
			list.add(cons);
		}
		
		// get consumptions
		return list;
	}
}
