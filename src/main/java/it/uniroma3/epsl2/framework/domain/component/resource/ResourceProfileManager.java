package it.uniroma3.epsl2.framework.domain.component.resource;

import java.util.List;

import it.uniroma3.epsl2.framework.microkernel.lang.plan.resource.ResourceEvent;

/**
 * 
 * @author anacleto
 *
 */
public interface ResourceProfileManager 
{
	/**
	 * 
	 * @return
	 */
	public long getMinCapacity();
	
	/**
	 * 
	 * @return
	 */
	public long getMaxCapacity();
	
	/**
	 * 
	 * @return
	 */
	public long getInitialCapacity();
	
	/**
	 * 
	 * @return
	 */
	public List<ResourceEvent> getProductions();
	
	/**
	 * 
	 * @return
	 */
	public List<ResourceEvent> getConsumptions();
}
