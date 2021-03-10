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
 * @author anacleto
 *
 */
public class ReservoirOverflow extends Flaw implements Comparable<ReservoirOverflow> 
{
	private List<ResourceEvent<?>> criticalSet;		// the consumption events generating the peak
	private double initialLevel;							// the level of the resource at the beginning of the critical set
	
	/**
	 * 
	 * @param id
	 * @param resource
	 * @param set
	 * @param initialLevel
	 */
	protected ReservoirOverflow(int id, ReservoirResource resource, List<ResourceEvent<?>> set, double initialLevel) {
		super(id, resource, FlawType.RESERVOIR_OVERFLOW);
		this.criticalSet = new ArrayList<>(set);
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
	public List<ResourceEvent<?>> getCriticalSet() {
		List<ResourceEvent<?>> list = new ArrayList<>(this.criticalSet);
		Collections.sort(list);
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(ReservoirOverflow o) {
		// compare peaks according to "delta" values
		return this.criticalSet.size() < o.criticalSet.size() ? -1 : 
			this.criticalSet.size() > o.criticalSet.size() ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"initial-resource-level\": " + this.initialLevel + ", "
				+ "\"critical-set\": " + this.criticalSet + " }";
	}
}
