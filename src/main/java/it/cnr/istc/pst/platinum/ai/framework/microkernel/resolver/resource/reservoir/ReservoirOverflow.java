package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.resource.reservoir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.ResourceEvent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.resource.reservoir.ReservoirResource;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author alessandro
 *
 */
public class ReservoirOverflow extends Flaw implements Comparable<ReservoirOverflow> 
{
	private List<ResourceEvent<?>> criticalSet;		// the consumption events generating the peak
	private List<ResourceEvent<?>> consumptions;	// subset of consumption events
	private List<ResourceEvent<?>> productions;		// subset of production events
	
	private double delta;							// amount exceeding the lower (negative) or upper bound (positive) of the resource
	private double initialLevel;					// the level of the resource at the beginning of the critical set
	
	/**
	 * 
	 * @param id
	 * @param resource
	 * @param set
	 * @param initialLevel
	 * @param delta
	 */
	protected ReservoirOverflow(int id, ReservoirResource resource, List<ResourceEvent<?>> set, double initialLevel, double delta) {
		super(id, resource, FlawType.RESERVOIR_OVERFLOW);
		
		// set fields
		this.initialLevel = initialLevel;
		this.delta = delta;
		
		// set the critical set
		this.criticalSet = new ArrayList<>(set);
		this.consumptions = new ArrayList<>();
		this.productions = new ArrayList<>();
		
		// set consumptions and production events
		for (ResourceEvent<?> event : this.criticalSet) {
			if (event.getAmount() < 0) {
				// add event to consumptions
				this.consumptions.add(event);
			}
			
			if (event.getAmount() > 0) {
				// add event to productions
				this.productions.add(event);
			}
		}
		
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
	public double getDelta() {
		return delta;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isOverProduction() {
		// a positive delta entails an over-production of the resource
		return this.delta > 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isOverConsumption() {
		// a negative delta entails an over-consumption of the resource
		return this.delta < 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ResourceEvent<?>> getCriticalSet() {
		List<ResourceEvent<?>> list = new ArrayList<>(this.criticalSet);
		Collections.sort(list);
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ResourceEvent<?>> getConsumptions() {
		// subset of consumptions
		List<ResourceEvent<?>> list = new ArrayList<>(this.consumptions);
		Collections.sort(list);
		// get the list
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ResourceEvent<?>> getProductions() {
		// subset of productions
		List<ResourceEvent<?>> list = new ArrayList<>(this.productions);
		Collections.sort(list);
		// get the list
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ReservoirOverflow o) {
		// compare overflows according to the amount of exceeding resource
		return Math.abs(this.delta) > Math.abs(o.delta) ? -1 : 
			Math.abs(this.delta) < Math.abs(o.delta) ? 1 : 
				// compare the size of the overflow
				this.criticalSet.size() > o.criticalSet.size() ? -1 : 
					this.criticalSet.size() < o.criticalSet.size() ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"initial-resource-level\": " + this.initialLevel + ", "
				+ "\"critical-set\": " + this.criticalSet + ", "
				+ "\"delta\": " + this.delta + " "
				+ "}";
	}
}
