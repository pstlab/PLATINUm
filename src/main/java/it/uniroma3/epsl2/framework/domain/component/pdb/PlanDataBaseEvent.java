package it.uniroma3.epsl2.framework.domain.component.pdb;

import java.util.concurrent.atomic.AtomicLong;

import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class PlanDataBaseEvent 
{
	private static final AtomicLong ID_COUNTER = new AtomicLong(0);
	private long id;
	private PlanDataBaseEventType type;
	private FlawSolution solution;
	
	/**
	 * 
	 * @param type
	 * @param solution
	 */
	protected PlanDataBaseEvent(PlanDataBaseEventType type, FlawSolution solution) {
		this.id = ID_COUNTER.getAndIncrement();
		this.type = type;
		this.solution = solution;
	}
	
	public long getId() {
		return id;
	}
	
	public FlawSolution getSolution() {
		return solution;
	}
	
	public PlanDataBaseEventType getType() {
		return type;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlanDataBaseEvent other = (PlanDataBaseEvent) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
