package it.uniroma3.epsl2.framework.domain.component.resource;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;

/**
 * 
 * @author anacleto
 *
 */
public class ReservoirResource extends Resource
{
	private ResourceConsumptionValue consumption;		// production value
	private ResourceProductionValue production;			// consumption value
	
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
	 * @param value
	 */
	public void addConsumptionValue(String value) 
	{
		// setup domain
		NumericParameterDomain dom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
		dom.setLowerBound(0);
		dom.setUpperBound(this.max);
		// create value
		this.consumption = new ResourceConsumptionValue(value, new long[] {1, this.tdb.getHorizon()}, this);
		// set domain
		this.consumption.addParameterPlaceHolder(dom);
		// add value
		this.values.add(this.consumption);
	}
	
	/**
	 * 
	 * @param value
	 */
	public void addProductionValue(String value)
	{
		// setup domain
		NumericParameterDomain dom  = this.pdb.createParameterDomain("res-" + this.name + "-amount", ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
		dom.setLowerBound(0);
		dom.setUpperBound(this.max);
		// create value
		this.production = new ResourceProductionValue(value, new long[] {1, this.tdb.getHorizon()}, this);
		// set domain
		this.production.addParameterPlaceHolder(dom);
		// add value
		this.values.add(this.production);
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
	public ResourceConsumptionValue getConsumptionValue() {
		return this.consumption;
	}

	/**
	 * 
	 */
	@Override
	public List<ResourceEvent> getProductions() 
	{
		// list of production events
		List<ResourceEvent> list = new ArrayList<>();
		// check active decisions
		for (Decision dec : this.getActiveDecisions())
		{
			// check value
			if (dec.getValue().equals(this.production)) {
				// check amount of production
				int amount = this.checkAmount(dec);
				// create production event
				ResourceEvent prod = new ProductionResourceEvent(
						dec, 
						dec.getToken().getInterval().getEndTime(), 
						amount);
				// add event
				list.add(prod);
			}
		}
		
		// get production events
		return list;
	}

	/**
	 * 
	 */
	@Override
	public List<ResourceEvent> getConsumptions() 
	{
		// list of consumption events
		List<ResourceEvent> list = new ArrayList<>();
		// check active decisions
		for (Decision dec : this.getActiveDecisions())
		{
			// check value
			if (dec.getValue().equals(this.consumption)) {
				// check amount of consumption
				int amount = this.checkAmount(dec);
				// create consumption event
				ResourceEvent cons = new ConsumptionResourceEvent(dec, 
						dec.getToken().getInterval().getStartTime(), 
						amount);
				// add event
				list.add(cons);
			}
		}
		
		// get consumption events
		return list;
	}
}
