package it.uniroma3.epsl2.framework.microkernel.resolver.sv.scheduling;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public final class DecisionSchedule extends FlawSolution {
	
	private List<Decision> schedule;
	
	/**
	 * 
	 * @param peak
	 * @param schedule
	 */
	protected DecisionSchedule(Peak peak, List<Decision> schedule) {
		super(peak);
		this.schedule= new ArrayList<>(schedule);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getSchedule() {
		return new ArrayList<>(this.schedule);
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[DecisionSchedule schedule= " + this.schedule + "]";
	}
	
}
