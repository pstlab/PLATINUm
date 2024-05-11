package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author alessandro
 *
 */
public class OverlappingSetSchedule extends FlawSolution {
	
	private List<PrecedenceConstraint> constraints;				// precedence constraints
	
	/**
	 * 
	 * @param cs
	 * @param cost
	 */
	protected OverlappingSetSchedule(OverlappingSet cs, double cost) {
		super(cs, cost);
		this.constraints = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	public void addConstraint(Decision reference, Decision target) {
		
		// create constraint data
		PrecedenceConstraint pc = new PrecedenceConstraint(reference, target);
		// add constraint
		this.constraints.add(pc);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PrecedenceConstraint> getConstraints() {
		return new ArrayList<>(this.constraints);
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(FlawSolution o) {
		OverlappingSetSchedule schedule = (OverlappingSetSchedule) o;
		return this.constraints.size() > schedule.constraints.size() ? -1 : 
			this.constraints.size() < schedule.constraints.size() ? 1 : 0;
	}
}
