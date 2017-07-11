package it.istc.pst.platinum.framework.domain.component.resource;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;

/**
 * 
 * @author anacleto
 *
 */
public class UnaryResource extends DiscreteResource 
{
	/**
	 * 
	 * @param name
	 */
	@DomainComponentConfiguration(resolvers = {
			// discrete resource scheduler
			ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER
	})
	protected UnaryResource(String name) {
		super(name);
		this.min = 0;
		this.max = 1;
		this.initial = this.max;
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
					1);
			
			// add event
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
					1);
			
			// add event
			list.add(prod);
		}
		
		// get list
		return list;
	}

}
