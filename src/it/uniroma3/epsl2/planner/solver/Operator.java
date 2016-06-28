package it.uniroma3.epsl2.planner.solver;

import java.util.concurrent.atomic.AtomicInteger;

import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class Operator implements Comparable<Operator> {

	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	private int id;
	private FlawSolution solution;
	
	/**
	 * 
	 * @param solution
	 */
	protected Operator(FlawSolution solution) {
		this.id = ID_COUNTER.getAndIncrement();
		this.solution = solution;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public Flaw getFlaw() {
		return this.solution.getFlaw();
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawSolution getFlawSolution() {
		return solution;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Operator o) {
		return this.id <= o.id ? -1 : 1;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operator other = (Operator) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Operator id= " + this.id + " solution= " + this.solution + "]";
	}
}
