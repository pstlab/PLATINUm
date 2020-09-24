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
	private List<ConsumptionResourceEvent> criticalSet;		// the consumption events generating the peak
	private Set<ProductionCheckpoint> checkpoints;			// set of production checkpoints of the profile 
	private double delta;									// amount of resource exceeding the capacity
	private double initialLevel;							// the level of the resource at the beginning of the critical set
	
	/**
	 * 
	 * @param id
	 * @param resource
	 * @param set
	 * @param delta
	 * @param initialLevel
	 * @param points
	 */
	protected Peak(int id, ReservoirResource resource, List<ConsumptionResourceEvent> set, double delta, double initialLevel, Collection<ProductionCheckpoint> points) {
		super(id, resource, FlawType.RESOURCE_PRODUCTION_PLANNING);
		this.criticalSet = new ArrayList<>(set);
		this.checkpoints = new HashSet<>(points);
		this.delta = delta;
		this.initialLevel = initialLevel;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getInitialLevel() {
		return initialLevel;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ConsumptionResourceEvent> getCriticalSet() {
		List<ConsumptionResourceEvent> list = new ArrayList<>(this.criticalSet);
		Collections.sort(list);
		return list;
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
		return Math.abs(this.delta) >= Math.abs(o.delta) ? -1 : 1;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"delta\": " + delta + ", "
				+ "\"critical-set\": " + this.criticalSet + " }";
	}
}
