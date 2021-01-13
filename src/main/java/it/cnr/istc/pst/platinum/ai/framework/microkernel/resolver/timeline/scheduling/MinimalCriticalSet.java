package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class MinimalCriticalSet implements Comparable<MinimalCriticalSet>
{
	protected OverlappingSet set;								// the set of overlapping activities
	private Set<Decision> decisions;							// activities composing the MCS
	private List<PrecedenceConstraint> solutions;				// a MCS can be solved by posting a simple precedence constraint
	private double schedulingCost;
	
	/**
	 * 
	 * @param set
	 */
	protected MinimalCriticalSet(OverlappingSet set, double cost) {
		this.set = set;
		this.decisions = new HashSet<>();
		this.solutions = new ArrayList<>();
		this.schedulingCost = cost;
	}
	
	/**
	 * 
	 * @param mcs
	 */
	protected MinimalCriticalSet(MinimalCriticalSet mcs) {
		this.set = mcs.set;
		this.decisions = new HashSet<>(mcs.decisions);
		this.solutions = new ArrayList<>(mcs.solutions);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTotalAmount() {
		// the amount is represented by the number of decisions composing the MCS
		return this.decisions.size();
	}
	
	/**
	 * 
	 * @param decision
	 * @return
	 */
	public boolean contains(Decision decision) {
		// check whether the MCS already contains the sample 
		return this.decisions.contains(decision);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getDecisions() {
		return new ArrayList<>(this.decisions);
	}
	
	/**
	 * 
	 * @param decision
	 */
	public void addDecision(Decision decision) {
		this.decisions.add(decision);
	}
	
	/**
	 * 
	 * @return
	 */
	public OverlappingSet getOverlappingSet() {
		return this.set;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<PrecedenceConstraint> getSolutions() {
		return new ArrayList<>(this.solutions);
	}
	
	/**
	 * The preserved value of a MCS is given by the average preserved values of its solutions
	 * 
	 * See [Laborie 2005] for further details about the preserved value heuristics 
	 * 
	 * @return
	 */
	public double getPreservedValue() 
	{
		// initialize preserved value
		double preserved = 0;
		// take into account the preserved values of solutions
		for (PrecedenceConstraint pc : this.solutions) {
			preserved += pc.getPreservedValue();
		}
		
		// get the average value
		preserved = preserved / this.solutions.size();
		// get computed value
		return preserved;
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param preserved
	 * @return
	 */
	public PrecedenceConstraint addSolution(Decision reference, Decision target, double preserved) //, double makespan) 
	{
		// create a precedence constraint
		PrecedenceConstraint pc = new PrecedenceConstraint(this.set, reference, target, this.schedulingCost);
		// set the value of resulting preserved space
		pc.setPreservedSpace(preserved);
		// add solution to the original flaw
		this.solutions.add(pc);
		// get constraint
		return pc;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(MinimalCriticalSet o) {
		// compare two MCS according to their preserved value
		double p1 = this.getPreservedValue();
		double p2 = o.getPreservedValue();
		// take into account solutions with a lower level of preserved value
		return p1 < p2 ? -1 : p1 > p2 ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[MCS samples= " + this.decisions + "]";
	}
}


