package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.sv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class Peak extends Flaw implements Comparator<Decision> 
{
	private Set<Decision> decisions;
	
	/**
	 * 
	 * @param c
	 */
	protected Peak(DomainComponent c) {
		super(c, FlawType.SV_SCHEDULING);
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
	 * @param t
	 */
	public void remove(Decision dec) {
		this.decisions.remove(dec);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getMaxStartTime() {
		long max = Long.MIN_VALUE + 1;
		for (Decision dec : this.decisions) {
			// get start point
			TimePoint start = dec.getToken().
					getInterval().getStartTime();
			// check bound
			if (start.getLowerBound() > max) {
				max = start.getLowerBound();
			}
		}
		// get maximum start time
		return max;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getMinEndTime() {
		long min = Long.MAX_VALUE - 1;
		for (Decision dec : this.decisions) {
			// get end point
			TimePoint end = dec.getToken().
					getInterval().getEndTime();
			// check bound
			if (end.getLowerBound() < min) {
				min = end.getLowerBound();
			}
		}
		// get minimum end time
		return min;
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
		List<Decision> list = new ArrayList<>(this.decisions);
		// order list
		Collections.sort(list, this);
		// get sorted list
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(Decision o1, Decision o2) {
		// compare start times
		return (o1.getStart()[0] < o2.getStart()[0]) ? -1 : 
			(o1.getStart()[0] == o2.getStart()[0] && o1.getStart()[1] <= o2.getStart()[1]) ? -1 : 1;
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		// set's hash code
		return this.decisions.hashCode();
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Peak) && (this.decisions.equals(((Peak) obj).decisions));
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		String str = "[Peak size=" + this.size() + " decisions= " + this.decisions + " solutions=\n";
		for (FlawSolution sol : this.getSolutions()) {
			str += "\t- schedule= " + sol + "\n";
		}
				
		str += "]";
		return str;
	}
}
