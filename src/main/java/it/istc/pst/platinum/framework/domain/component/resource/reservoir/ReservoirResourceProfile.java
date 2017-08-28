package it.istc.pst.platinum.framework.domain.component.resource.reservoir;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.resource.ResourceProfile;

/**
 * 
 * @author anacleto
 *
 */
public class ReservoirResourceProfile extends ResourceProfile<ResourceUsageProfileSample> 
{
	private Set<ProductionCheckpoint> checkpoints;		// set of production checkpoints
	
	/**
	 * 
	 */
	protected ReservoirResourceProfile() {
		super();
		this.checkpoints = new HashSet<>();
	}
	
	/**
	 * 
	 * @param production
	 * @param potential
	 * @param lastConsumption
	 */
	public void addProductionCheckpoint(ProductionResourceEvent production, double potential, ConsumptionResourceEvent lastConsumption) {
		// create checkpoint
		ProductionCheckpoint ck = new ProductionCheckpoint(production, potential, lastConsumption);
		// add checkpoint to the list
		this.checkpoints.add(ck);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ProductionCheckpoint> getProductionCheckPoints() {
		return new ArrayList<>(this.checkpoints);
	}
}
