package it.uniroma3.epsl2.framework.domain.component.resource.costant;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;
import it.uniroma3.epsl2.framework.utils.view.component.ComponentViewType;

/**
 * 
 * @author anacleto
 *
 */
@DomainComponentConfiguration(
		
	resolvers = {
		
		// resource scheduler
		ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER
	},
	
	/*
	 * TODO - set different component view for resources
	 */
	view = ComponentViewType.GANTT
)
public class DiscreteResource extends Resource 
{
	// the only value allowed
	private ResourceRequirement requirement;
	
	/**
	 * 
	 * @param name
	 */
	protected DiscreteResource(String name) {
		super(name, DomainComponentType.RESOURCE_DISCRETE);
		this.min = Integer.MIN_VALUE + 1;
		this.max = Integer.MAX_VALUE - 1;
		this.initial = this.max;
		this.requirement = null;
	}
	
	/**
	 * 
	 */
	@Override
	public List<ComponentValue> getValues() {
		// check data
		if (this.requirement == null) 
		{
			// setup domain
			NumericParameterDomain dom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
			dom.setLowerBound(0);
			dom.setUpperBound(this.max);
			// create value
			this.requirement = new ResourceRequirement(new long[] {1, this.tdb.getHorizon()}, this);
			// set domain
			this.requirement.addParameterPlaceHolder(dom);
			// add value
			this.values.add(this.requirement);
		}
		
		// get all values
		return super.getValues();
	}
	
	/**
	 * 
	 */
	@Override
	public ComponentValue getValueByName(String name) {
		// check data
		if (this.requirement == null) {
			// setup domain
			NumericParameterDomain dom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
			dom.setLowerBound(0);
			dom.setUpperBound(this.max);
			// create value
			this.requirement = new ResourceRequirement(new long[] {1, this.tdb.getHorizon()}, this);
			// set domain
			this.requirement.addParameterPlaceHolder(dom);
			// add value
			this.values.add(this.requirement);
		}
		
		// get value
		return super.getValueByName(name);
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
					dec, dec.getToken().getInterval().getStartTime(), 
					amount);
			// add event
			list.add(cons);
		}
		
		// get consumptions
		return list;
	}
}
