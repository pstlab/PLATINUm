package it.istc.pst.platinum.framework.domain.component.resource.reservoir;

import java.util.List;

import it.istc.pst.platinum.framework.domain.component.ex.ResourceProfileComputationException;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceProfileManager;

/**
 * 
 * @author anacleto
 *
 */
public interface ReservoirResourceProfileManager extends ResourceProfileManager 
{
	/**
	 * 
	 * @return
	 */
	public List<ProductionResourceEvent> getProductions();
	
	/**
	 * 
	 * @return
	 */
	public List<ConsumptionResourceEvent> getConsumptions();
	
	/**
	 * Compute the pessimistic profile (PRP) of a reservoir resource
	 * 
	 * @return
	 * @throws ResourceProfileComputationException
	 */
	public ReservoirResourceProfile computePessimisticResourceProfile() 
			throws ResourceProfileComputationException;
	
	/**
	 * Compute the optimistic profile (ORP) of a reservoir resource
	 * 
	 * @return
	 * @throws ResourceProfileComputationException
	 */
	public ReservoirResourceProfile computeOptimisticResourceProfile() 
			throws ResourceProfileComputationException;
}
