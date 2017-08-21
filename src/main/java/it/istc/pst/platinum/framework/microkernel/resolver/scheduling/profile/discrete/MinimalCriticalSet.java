package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.profile.discrete;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceProfileSample;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class MinimalCriticalSet implements Comparable<MinimalCriticalSet>
{
	protected CriticalSet cs;								// the set of overlapping activities
	private Set<RequirementResourceProfileSample> samples;	// activities composing the MCS
	private List<PrecedenceConstraint> solutions;			// a MCS can be solved by posting a simple precedence constraint
	
	/**
	 * 
	 * @param cs
	 */
	protected MinimalCriticalSet(CriticalSet cs) {
		this.cs = cs;
		this.samples = new HashSet<>();
		this.solutions = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param mcs
	 */
	protected MinimalCriticalSet(MinimalCriticalSet mcs) {
		this.cs = mcs.cs;
		this.samples = new HashSet<>(mcs.samples);
		this.solutions = new ArrayList<>(mcs.solutions);
	}
	
	/**
	 * Get the total amount of resource required
	 * 
	 * @return
	 */
	public long getTotalAmount() {
		long amount = 0;
		for (RequirementResourceProfileSample sample : this.samples) {
			amount += sample.getAmount();
		}
		// get the computed total
		return amount;
	}
	
	/**
	 * 
	 * @param sample
	 * @return
	 */
	public boolean contains(RequirementResourceProfileSample sample) {
		// check whether the MCS already contains the sample 
		return this.samples.contains(sample);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<RequirementResourceProfileSample> getSamples() {
		return new ArrayList<>(this.samples);
	}
	
	/**
	 * 
	 * @param sample
	 */
	public void addSample(RequirementResourceProfileSample sample) {
		this.samples.add(sample);
	}
	
	/**
	 * 
	 * @return
	 */
	public CriticalSet getCriticalSet() {
		return cs;
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
	 * @param makespan
	 * @return
	 */
	protected PrecedenceConstraint addSolution(Decision reference, Decision target, double preserved, double makespan) 
	{
		// create a precedence constraint
		PrecedenceConstraint pc = new PrecedenceConstraint(this.cs, reference, target);
		// set the value of resulting preserved space
		pc.setPreservedSpace(preserved);
		// set the value of the resulting makespan
		pc.setMakespan(makespan);
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
		return p1 <= p2 ? -1 : 1;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[MCS samples= " + this.samples + "]";
	}
}
