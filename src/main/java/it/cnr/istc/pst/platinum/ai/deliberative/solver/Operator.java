package it.cnr.istc.pst.platinum.ai.deliberative.solver;

import java.util.concurrent.atomic.AtomicInteger;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;

/**
 * 
 * @author anacleto
 *
 */
public class Operator implements Comparable<Operator> 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	
	private int id;									// operator's ID
	private FlawSolution solution;					// applied flaw solution 
	private boolean applied;						// application flag used to keep track of the first time the operator is applied
	
	/**
	 * 
	 * @param solution
	 */
	protected Operator(FlawSolution solution) {
		this.id = ID_COUNTER.getAndIncrement();
		this.solution = solution;
		this.applied = false;
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
	 */
	public void setApplied() {
		this.applied = true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isApplied() {
		return this.applied;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getCost() {
		return this.solution.getCost();
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
		return this.id < o.id ? -1 : this.id > o.id ? 1 : 0;
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
		return "{ id: " + this.id + ", cost: " + this.getCost() + ", solution: " + this.solution + " }";
	}
}
