package it.uniroma3.epsl2.framework.domain.component.resource;

import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
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
		
		// resource scheduling resolver
		ResolverType.RESOURCE_SCHEDULING_RESOLVER
	},
	
	/*
	 * TODO - set different component view for resources
	 */
	
	view = ComponentViewType.GANTT
)
public class DiscreteResource extends Resource 
{

	/**
	 * 
	 * @param name
	 */
	protected DiscreteResource(String name) {
		super(name, DomainComponentType.RESOURCE_DISCRETE);
	}

	/**
	 * 
	 */
	@Override
	public void checkPseudoControllability() 
			throws PseudoControllabilityCheckException 
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	@Override
	public List<ResourceEvent> getProductions() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 */
	@Override
	public List<ResourceEvent> getConsumptions() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
