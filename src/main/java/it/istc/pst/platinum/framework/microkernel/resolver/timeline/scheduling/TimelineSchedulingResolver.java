package it.istc.pst.platinum.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.time.lang.query.IntervalOverlapQuery;
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public final class TimelineSchedulingResolver extends Resolver<StateVariable>
{
	/**
	 * 
	 */
	protected TimelineSchedulingResolver() {
		super(ResolverType.TIMELINE_SCHEDULING_RESOLVER.getLabel(), 
				ResolverType.TIMELINE_SCHEDULING_RESOLVER.getFlawTypes());
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get the flaw solution to consider
		PrecedenceConstraint pc = (PrecedenceConstraint) solution;
		// get reference and target decisions
		Decision reference = pc.getReference();
		Decision target = pc.getTarget();
			
		// create relation
		BeforeRelation before = this.component.create(RelationType.BEFORE, reference, target);
		// set bounds
		before.setBound(new long[] {0, this.component.getHorizon()});
		
		info("Applying flaw solution:\n"
				+ "- precedence constraint: " + solution + "\n");

		try
		{
			// propagate relations
			this.component.activate(before);
			// add created relation to solution
			solution.addCreatedRelation(before);
			// add activated relations to solution
			solution.addActivatedRelation(before);
			debug("Precedence constraint successfully created and activated:\n"
					+ "- temporal constraint: " + before + "\n");
		}
		catch (RelationPropagationException ex) 
		{
			// deactivate relation if necessary
			this.component.deactivate(before);
			// delete relation
			this.component.delete(before);
			// write error message
			error("Error while applying flaw solution:\n"
					+ "- solution: " + solution + "\n"
					+ "- unfeasible precedence constraint: " + before + "\n");
			
			// not feasible solution
			throw new FlawSolutionApplicationException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of critical sets
		List<OverlappingSet> CSs = new ArrayList<>();
		// list of active decisions
		List<Decision> decisions = this.component.getActiveDecisions();
		// look for peaks
		for (int index = 0; index < decisions.size() - 1; index++)
		{
			// get active decision A 
			Decision reference = decisions.get(index);
			// initialize a critical set
			OverlappingSet cs = new OverlappingSet(FLAW_COUNTER.getAndIncrement(), this.component);
			// add the reference token to the current critical set
			cs.add(reference);
			// find possibly overlapping decisions
			for (int jndex = index + 1; jndex < decisions.size(); jndex++)
			{
				// get another active decision
				Decision target = decisions.get(jndex);
				// check if decisions overlaps
				debug("Token overlapping check:\n"
						+ "- component: " + this.component + "\n"
						+ "- reference token: " + reference + "\n"
						+ "- target token: " + target + "\n");
				
				// check if target decision can temporally overlap all the decisions of the current set
				if (this.overlaps(cs, target)) 
				{
					// add decision to the set
					cs.add(target);
					// peak found
					debug("Overlapping token found:\n"
							+ "- component: " + this.component + "\n"
							+ "- reference token: " + reference + "\n"
							+ "- current overlapping sets: " + cs + "\n");
				}
			}
			
			// check if a critical set has been found
			if (cs.size() > 1) {
				// add the current set to the (possibly) overlapping sets
				CSs.add(cs);
			}
		}
		
		// list of flaws
		List<Flaw> flaws = new ArrayList<>();
		// check critical sets found
		if (!CSs.isEmpty()) 
		{
			// consider the maximum critical set only
			Collections.sort(CSs);
			flaws.add(CSs.get(0));
			// flaw generation
			debug("Critical sets found found:\n"
					+ "- number of cricial sets: " + CSs.size() + "\n"
					+ "- component: " + this.component + "\n"
					+ "- maximum overlapping set selected: " + CSs.get(0) + "\n");
		}
		
		// get peaks
		return flaws;
	}
	
	/**
	 * Compare a target decision with all the decisions composing an overlapping set and checks whether they overlaps or not. 
	 * 
	 * The method returns true if and only if the target decision overlaps all the decisions of the set. Namely, according to 
	 * the underlying temporal flexibility, if the target decisions can be sorted with respect to at least one of the decisions 
	 * composing the overlapping set then, the method returns false.
	 * 
	 * @param set
	 * @param target
	 * @return
	 */
	private boolean overlaps(OverlappingSet set, Decision target) 
	{
		// overlapping flag
		boolean overlaps = true;
		// check set decisions
		for (int index = 0; index < set.size() && overlaps; index++)
		{
			// get a decision from the set
			Decision reference = set.getDecisions().get(index);
			// check if current decision and target overlap
			IntervalOverlapQuery query = this.tdb.createTemporalQuery(TemporalQueryType.INTERVAL_OVERLAP);
			// set intervals
			query.setReference(reference.getToken().getInterval());
			query.setTarget(target.getToken().getInterval());
			// process query
			this.tdb.process(query);
			// check whether the (flexible) temporal interval can overlap or not
			overlaps = query.canOverlap();
		}
		
		// get result
		return overlaps;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawException 
	{
		// get the critical set to be solved
		OverlappingSet set = (OverlappingSet) flaw;
		// sample the critical set by identifying MCSs (binary MCSs in this case)
		List<MinimalCriticalSet> MCSs = new ArrayList<MinimalCriticalSet>();
		for (int index = 0; index < set.size() - 1; index++)
		{
			// get current decision
			Decision reference = set.getDecisions().get(index);
			for (int jndex = index + 1; jndex < set.size(); jndex++) 
			{
				// get target decision 
				Decision target = set.getDecisions().get(jndex);
				
				// create the MCS
				MinimalCriticalSet mcs = new MinimalCriticalSet(set);
				// add reference decision
				mcs.addDecision(reference);
				// add to current MCS
				mcs.addDecision(target);
				

				// compute the preserved heuristics
				double preserved = this.computePreservedSpaceHeuristicValue(
						reference.getToken().getInterval().getEndTime(), 
						target.getToken().getInterval().getStartTime());
				// prepare the precedence constraint: reference < target
				PrecedenceConstraint pc1 = mcs.addSolution(reference, target, preserved);
				
				// compute the preserved heuristics
				preserved = this.computePreservedSpaceHeuristicValue(
						target.getToken().getInterval().getEndTime(), 
						reference.getToken().getInterval().getStartTime());
				// prepare precedence constraint: target < reference
				PrecedenceConstraint pc2 = mcs.addSolution(target, reference, preserved);
				
				
				// add MCS to list of flaw solution
				MCSs.add(mcs);
				debug("Possible solution of the Critical Set found:\n"
					+ "- CS: " + set + "\n"
					+ "- MCS: " + mcs + "\n"
					+ "- Possible solutions:\n"
					+ "\t(a) precedence constraint: " + pc1 + "\n"
					+ "\t(b) precedence constraint: " + pc2 + "\n");						
			}
		}

		// Select the MCS with the "best" value of preserved space and set the related solutions as solutions of the flaw.
		Collections.sort(MCSs);
		// get the "best" MCS according to the computed values of the "preserved space heuristics"
		MinimalCriticalSet best = MCSs.get(0);
		
		// add computed solutions to the flaw
		for (PrecedenceConstraint pc : best.getSolutions()) {
			// add activated relation
			pc.addRelationToPartialPlan(RelationType.BEFORE, pc.getReference(), pc.getTarget());
			// add precedence constraint
			flaw.addSolution(pc);
		}
	}
	
	/**
	 * Estimate the preserved values of time point domains after propagation of a precedence constraint "tp1 < tp2".
	 * 
	 * The method assumes that the precedence constraint is feasible and that the bounds of the time points have been updated 
	 * according to precedence constraint (i.e. the underlying temporal must encapsulate additional information coming from 
	 * the temporal constraint) 
	 * 
	 * @param tp1
	 * @param tp2
	 * @return
	 */
	private double computePreservedSpaceHeuristicValue(TimePoint tp1, TimePoint tp2)
	{
		// initialize value
		double preserved = 0;
		// compute parameters
		double A = (tp2.getUpperBound() - tp2.getLowerBound() + 1) * (tp1.getUpperBound() - tp1.getLowerBound() + 1);
		double B = (tp2.getUpperBound() - tp1.getLowerBound() + 1) * (tp2.getUpperBound() - tp1.getLowerBound() + 2);
		double Cmin = Math.max(0, (tp2.getLowerBound() - tp1.getLowerBound()) * (tp2.getLowerBound() - tp1.getLowerBound() + 1));
		double Cmax = Math.max(0, (tp2.getUpperBound() - tp1.getUpperBound() * (tp2.getUpperBound() - tp1.getUpperBound() + 1)));

		// compute preserved space value
		preserved = (B - Cmin - Cmax) / (2 * A);
		
		// get computed heuristic value
		return preserved;
	}
}

/**
 * 
 * @author anacleto
 *
 */
class MinimalCriticalSet implements Comparable<MinimalCriticalSet>
{
	protected OverlappingSet set;								// the set of overlapping activities
	private Set<Decision> decisions;							// activities composing the MCS
	private List<PrecedenceConstraint> solutions;				// a MCS can be solved by posting a simple precedence constraint
	
	/**
	 * 
	 * @param set
	 */
	protected MinimalCriticalSet(OverlappingSet set) {
		this.set = set;
		this.decisions = new HashSet<>();
		this.solutions = new ArrayList<>();
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
		PrecedenceConstraint pc = new PrecedenceConstraint(this.set, reference, target);
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

