package it.istc.pst.platinum.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.BeforeIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.ComputeMakespanQuery;
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
		try 
		{
			// get reference and target decisions
			Decision reference = pc.getReference();
			Decision target = pc.getTarget();
				
			// create relation
			BeforeRelation before = this.component.create(RelationType.BEFORE, reference, target);
			// set bounds
			before.setBound(new long[] {1, this.component.getHorizon()});
			// add created relation to solution
			solution.addCreatedRelation(before);
			this.logger.debug("Applying flaw solution:\n"
					+ "- solution: " + solution + "\n"
					+ "- created temporal constraint: " + before + "\n");
			
			// propagate relations
			this.component.activate(before);
			// add activated relations to solution
			solution.addActivatedRelation(before);
		}
		catch (RelationPropagationException ex) 
		{
			// deactivate activated relations
			for (Relation rel : solution.getActivatedRelations()) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				refComp.deactivate(rel);
			}
			
			// delete created relations
			for (Relation rel : solution.getCreatedRelations()) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				refComp.delete(rel);
			}

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
		// list of flaws
		List<OverlappingSet> overlapping = new ArrayList<>();
		// list of active decisions
		List<Decision> decisions = this.component.getActiveDecisions();
		// look for peaks
		for (int index = 0; index < decisions.size() - 1 && overlapping.isEmpty(); index++)
		{
			// get active decision A 
			Decision reference = decisions.get(index);
			// initialize the overlapping set
			OverlappingSet set = new OverlappingSet(this.component);
			set.add(reference);
			// find overlapping decisions
			for (int jndex = index + 1; jndex < decisions.size() && overlapping.isEmpty(); jndex++)
			{
				// get active decision B
				Decision target = decisions.get(jndex);
				// check if overlaps all decisions of the current set
				if (this.overlaps(set, target)) {
					// add decision to the set
					set.add(target);
					// directly add to overlapping set
					overlapping.add(set);
				}
			}
		}
		
		// list of flaws
		List<Flaw> flaws = new ArrayList<>();
		// check overlapping sets found
		if (!overlapping.isEmpty()) {
			// consider only the maximum overlapping set
			Collections.sort(overlapping);
			flaws.add(overlapping.get(0));
		}
		// get peaks
		return flaws;
	}
	
	/**
	 * 
	 * @param set
	 * @param target
	 * @return
	 */
	private boolean overlaps(OverlappingSet set, Decision target) 
	{
		// overlapping flag
		boolean overlaps = false;
		// check set decisions
		for (int index = 0; index < set.size() && !overlaps; index++)
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
			// check overlapping condition
			overlaps = query.isOverlapping();
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
		// cast flaw
		OverlappingSet set = (OverlappingSet) flaw;
		// sample the overlapping set by identifying the MCSs
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
				// prepare a precedence constraint to solve the MCS
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);

				try
				{
					// check the feasibility of the solution "reference < target"
					before.setReference(reference.getToken().getInterval());
					before.setTarget(target.getToken().getInterval());
					// set bounds
					before.setLowerBound(0);
					before.setUpperBound(this.tdb.getHorizon());
					
					// verify constraint feasibility
					this.tdb.propagate(before);
					this.tdb.checkConsistency();
					
					// compute the resulting preserved space
					double preserved = this.computePreservedSpaceHeuristicValue(
							reference.getToken().getInterval().getEndTime(), 
							target.getToken().getInterval().getStartTime());
					
					// compute the resulting makespan
					ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
					// process query
					this.tdb.process(query);
					// get computed makespan
					double makespan = query.getMakespan();
					
					// add solution to MCS
					PrecedenceConstraint pc = mcs.addSolution(reference, target, preserved, makespan);
					// print some debugging information
					this.logger.debug("Feasible solution of MCS found:\n"
							+ "- mcs: " + mcs + "\n"
							+ "- precedence constraint: " + pc + "\n");
				}
				catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
					// warning message
					this.logger.debug("Unfeasible solution found for MCS:\n- mcs: " + mcs + "\n- unfeasible precedence constraint: " + reference + " < " + target + "\n");
				}
				finally {
					// retract propagated constraint
					this.tdb.retract(before);
					// clear temporal relation
					before.clear();
				}
				
				try
				{
					// check the feasibility of the solution "target < reference"
					before.setReference(target.getToken().getInterval());
					before.setTarget(reference.getToken().getInterval());
					// set bounds
					before.setLowerBound(0);
					before.setUpperBound(this.tdb.getHorizon());
					
					// verify constraint feasibility
					this.tdb.propagate(before);
					this.tdb.checkConsistency();
					
					// compute the resulting preserved space
					double preserved = this.computePreservedSpaceHeuristicValue(
							target.getToken().getInterval().getEndTime(), 
							reference.getToken().getInterval().getStartTime());
					
					// compute the resulting makespan
					ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
					// process query
					this.tdb.process(query);
					// get the computed makespan
					double makespan = query.getMakespan();

					// add solution to MCS
					PrecedenceConstraint pc = mcs.addSolution(target, reference, preserved, makespan);
					// print some debugging information
					this.logger.debug("Feasible solution of MCS found:\n"
							+ "- mcs: " + mcs + "\n"
							+ "- precedence constraint: " + pc + "\n");
				}
				catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
					// warning message
					this.logger.debug("Unfeasible solution found for MCS:\n- mcs: " + mcs + "\n- unfeasible precedence constraint: " + target + " < " + reference + "\n");
				}
				finally {
					// retract propagated constraint
					this.tdb.retract(before);
					// clear temporal relation
					before.clear();
				}
				
				// check solutions
				if (mcs.getSolutions().isEmpty()) {
					// unsolvable MCS found
					throw new UnsolvableFlawException("Unsolvable MCS found on discrete resource " + this.component.getName() + "\n- mcs: " + mcs + "\n"); 
				}
				
				// add MCS to list
				MCSs.add(mcs);
			}
		}

		// Select the MCS with the "best" value of preserved space and set the related solutions as solutions of the flaw.
		Collections.sort(MCSs);
		// get the "best" MCS
		MinimalCriticalSet best = MCSs.get(0);
		
		// add computed solutions to the flaw
		for (PrecedenceConstraint pc : best.getSolutions()) {
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
	 * @param makespan
	 * @return
	 */
	public PrecedenceConstraint addSolution(Decision reference, Decision target, double preserved, double makespan) 
	{
		// create a precedence constraint
		PrecedenceConstraint pc = new PrecedenceConstraint(this.set, reference, target);
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
		return "[MCS samples= " + this.decisions + "]";
	}
}

