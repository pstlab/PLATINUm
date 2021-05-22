package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;

/**
 * 
 * @author alessandro
 *
 */
public class OverlappingSet extends Flaw implements Comparable<OverlappingSet> 
{
	private Set<Decision> decisions;
	
	/**
	 * 
	 * @param id
	 * @param sv
	 */
	protected OverlappingSet(int id, StateVariable sv) {
		super(id, sv, FlawType.TIMELINE_OVERFLOW);
		this.decisions = new HashSet<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		return this.decisions.size();
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void add(Decision dec) {
		this.decisions.add(dec);
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void remove(Decision dec) {
		this.decisions.remove(dec);
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public boolean contains(Decision dec) {
		return this.decisions.contains(dec);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getDecisions() {
		// get sorted list
		return new ArrayList<>(this.decisions);
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(OverlappingSet o) {
		// check sizes of overlapping sets
		return this.size() > o.size() ? -1 : this.size() < o.size() ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[OverlappingSet requirement= " + this.size() + "]";
	}
}
