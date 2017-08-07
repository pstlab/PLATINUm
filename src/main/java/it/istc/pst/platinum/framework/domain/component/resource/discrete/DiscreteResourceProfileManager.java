package it.istc.pst.platinum.framework.domain.component.resource.discrete;

import java.util.List;

import it.istc.pst.platinum.framework.domain.component.ex.ResourceProfileComputationException;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceProfileManager;

/**
 * 
 * @author anacleto
 *
 */
public interface DiscreteResourceProfileManager extends ResourceProfileManager
{
	/**
	 * Get the list of (temporally flexible) requirements of a discrete resource
	 * 
	 * @return
	 */
	public List<RequirementResourceEvent> getRequirements();
	
	/**
	 * Compute the pessimistic profile (PRP) of a discrete resource
	 * 
	 * @return
	 * @throws ResourceProfileComputationException
	 */
	public DiscreteResourceProfile computePessimisticResourceProfile() 
			throws ResourceProfileComputationException;
	
	/**
	 * Compute the optimistic profile (ORP) of a discrete resource
	 * 
	 * @return
	 * @throws ResourceProfileComputationException
	 */
	public DiscreteResourceProfile computeOptimisticResourceProfile() 
			throws ResourceProfileComputationException;
}
