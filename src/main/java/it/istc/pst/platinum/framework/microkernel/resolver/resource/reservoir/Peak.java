package it.istc.pst.platinum.framework.microkernel.resolver.resource.reservoir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ConsumptionResourceEvent;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResource;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author anacleto
 *
 */
public class Peak extends Flaw implements Comparable<Peak> 
{
	private List<ConsumptionResourceEvent> consumptions;		// the consumption events generating the peak
	private Set<ProductionCheckpoint> checkpoints;			// set of production checkpoints
	private double delta;
	
	/**
	 * 
	 * @param resource
	 * @param consumptions
	 * @param delta
	 * @param points
	 */
	protected Peak(ReservoirResource resource, List<ConsumptionResourceEvent> consumptions, double delta, Collection<ProductionCheckpoint> points) {
		super(resource, FlawType.RESOURCE_PLANNING);
		this.consumptions = new ArrayList<>(consumptions);
		this.checkpoints = new HashSet<>(points);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ConsumptionResourceEvent> getConsumption() {
		return new ArrayList<>(this.consumptions);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ProductionCheckpoint> getProductionCheckpoints() {
		// list of checkpoints
		List<ProductionCheckpoint> list = new ArrayList<>(this.checkpoints);
		// sort checkpoints
		Collections.sort(list);
		// get sorted list of checkpoints
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getDelta() {
		return delta;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Peak o) {
		// compare peaks according to "delta" values
		return this.delta >= o.delta ? -1 : 1;
	}
}
