package it.uniroma3.epsl2.framework.domain.component.resource;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.time.tn.uncertainty.ex.PseudoControllabilityCheckException;
import it.uniroma3.epsl2.framework.utils.view.component.ComponentViewType;

/**
 * 
 * @author anacleto
 *
 */
@DomainComponentConfiguration(
		
		resolvers = {
				
			ResolverType.RESOURCE_SCHEDULING_RESOLVER
		},
		
		/*
		 *  TODO - change visualization type for resources
		 */
		view = ComponentViewType.GANTT
	)
public class UnaryResource extends Resource 
{
	/**
	 * 
	 * @param name
	 */
	protected UnaryResource(String name) {
		super(name, DomainComponentType.RESOURCE_UNARY);
		this.min = 0;
		this.max = 1;
		this.initial = this.max;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// add value
		this.values.add(new ResourceRequirement(new long[] {1,this.tdb.getHorizon()}, this));
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public List<ResourceEvent> getConsumptions() 
	{
		// list of events
		List<ResourceEvent> list = new ArrayList<>();
		// check resource requirements
		for (Decision dec : this.getActiveDecisions()) 
		{
			// create consumption event
			ResourceEvent cons = new ConsumptionResourceEvent(
					dec, 
					dec.getToken().getInterval().getStartTime(), 
					1l);
			
			// add events
			list.add(cons);
		}
		
		// get list
		return list;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public List<ResourceEvent> getProductions() 
	{
		// list of events
		List<ResourceEvent> list = new ArrayList<>();
		// check resource requirements
		for (Decision dec : this.getActiveDecisions())
		{
			// create production event
			ResourceEvent prod = new ProductionResourceEvent(
					dec, 
					dec.getToken().getInterval().getEndTime(), 
					1l);
			
			// add events
			list.add(prod);
		}
		
		// get list
		return list;
	}

	/**
	 * 
	 * @throws PseudoControllabilityCheckException
	 */
	@Override
	public void checkPseudoControllability() 
			throws PseudoControllabilityCheckException 
	{
		// nothing to do
	}
	
}
