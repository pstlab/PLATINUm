package it.istc.pst.platinum.framework.microkernel.resolver.resource.discrete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.ResourceProfileComputationException;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.DiscreteResource;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.DiscreteResourceProfile;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceProfileSample;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
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
import it.istc.pst.platinum.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResourceSchedulingResolver extends Resolver implements Comparator<RequirementResourceProfileSample> 
{ 
	@ComponentPlaceholder
	protected DiscreteResource component;

	/**
	 * 
	 */
	protected DiscreteResourceSchedulingResolver() {
		super(ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER.getLabel(), 
				ResolverType.DISCRETE_RESOURCE_SCHEDULING_RESOLVER.getFlawType());
	}

	/**
	 * 
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of flaws
		List<Flaw> flaws = new ArrayList<>();
		try
		{
			// check pessimistic resource profile
			DiscreteResourceProfile prp = this.component.computePessimisticResourceProfile();
			// compute flaws on profile
			List<CriticalSet> CSs = this.computeCriticalSets(prp);
			
			// check if any flaw has been found
			if (CSs.isEmpty()) {
				// check optimistic resource profile
				DiscreteResourceProfile orp = this.component.computeOptimisticResourceProfile();
				// compute flaws on profile
				CSs = this.computeCriticalSets(orp);
			}
			
			// check if empty
			if (!CSs.isEmpty()) {
				// sort CSs according to the total amount of resource requirement and get the the one with the maximum 
				Collections.sort(CSs);
				// consider only the CS with the maximum requirement of resource (i.e., the most critical one) 
				flaws.add(CSs.get(0));
			}
		}
		catch (ResourceProfileComputationException ex) {
			// profile computation error
			throw new RuntimeException("Resource profile computation error:\n- " + ex.getMessage() + "\n");
		}
		
		// get computed flaws
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(RequirementResourceProfileSample o1, RequirementResourceProfileSample o2) {
		// compare the amount of resource required
		return o1.getAmount() >= o2.getAmount() ? -1 : 1;
	}
	
	/**
	 * Å critical set (CS) is not necessary minimal. 
	 * 
	 * This method samples a critical set in order to find all the minimal critical sets (MCSs) available.
	 * 
	 * A minimal critical set is a "sub-peak" which can be solved by posting a precedence constraint between 
	 * any pair of activities.
	 * 
	 * @param cs
	 * @return
	 */
	private List<MinimalCriticalSet> sampleMCSs(CriticalSet cs)
	{
		// list of MCSs that can be extracted from the critical set
		List<MinimalCriticalSet> mcss = new ArrayList<>();
		// get the samples composing the critical set 
		List<RequirementResourceProfileSample> list = cs.getSamples();
		// sort samples in decreasing order of resource requirement
		Collections.sort(list, this);
		
		// sample MCSs from the CS
		for (int index = 0; index < list.size() -1; index++)  
		{
			// get current sample
			RequirementResourceProfileSample reference = list.get(index);
			// initialize an MCS
			MinimalCriticalSet mcs = new MinimalCriticalSet(cs);
			// add the current sample
			mcs.addSample(reference);
			
			// check other samples
			for (int jndex = index + 1; jndex < list.size(); jndex++) 
			{
				// get other sample
				RequirementResourceProfileSample other = list.get(jndex);
				// check the resulting amount 
				double amount = mcs.getTotalAmount() + other.getAmount();
				// an MCS is minimal so check the amount of resource required  (minimal condition)
				if (amount > this.component.getMaxCapacity()) 
				{
					// copy current MCS
					MinimalCriticalSet copy = new MinimalCriticalSet(mcs);
					// add sample to the MCS
					mcs.addSample(other);
					// add to the list of MCSs
					mcss.add(mcs);
					// go on with the search by using the copy 
					mcs = copy;
					
				}
				else {
					// simply add the sample and go on
					mcs.addSample(other);
				}
			}
		}
		
		// get sampled MCSs
		return mcss;
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

	
	/**
	 * 
	 * @param mcs
	 * @throws Exception - contains information concerning the unsolvable MCS
	 */
	private void computeMinimalCriticalSetSolutions(MinimalCriticalSet mcs) 
			throws UnsolvableFlawException
	{
		// list of samples
		List<RequirementResourceProfileSample> list = mcs.getSamples();
		// for each pair of decisions check the feasibility of a precedence constraint and compute the resulting preserved heuristic value
		for (int index = 0; index < list.size() - 1; index++)
		{
			// get reference decision
			Decision reference = list.get(index).getDecision();
			for (int jndex = index + 1; jndex < list.size(); jndex++)
			{
				// get target decision
				Decision target = list.get(jndex).getDecision();
				// prepare precedence constraint
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);

				try
				{
					/*
					 *  check feasibility of precedence constraint "reference < target" and compute the resulting preserved heuristic value
					 */
					
					// set reference interval
					before.setReference(reference.getToken().getInterval());
					// set target interval
					before.setTarget(target.getToken().getInterval());
					// set bounds
					before.setLowerBound(0);
					before.setUpperBound(this.tdb.getHorizon());
					
					// verify constraint feasibility through constraint propagation
					this.tdb.propagate(before);
					// check temporal consistency
					this.tdb.checkConsistency();
					

					// compute the preserved space heuristic value resulting after constraint propagation
					double preserved = this.computePreservedSpaceHeuristicValue(
							reference.getToken().getInterval().getEndTime(), 
							target.getToken().getInterval().getStartTime());
					
					// compute the resulting makespan of the temporal plan after constraint propagation
					ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
					// process query
					this.tdb.process(query);
					// get computed makespan
					double makespan = query.getMakespan();
					
					
					// create and add solution to the MCS
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
					/*
					 *  check feasibility of precedence constraint "target < reference" and compute the resulting preserved heuristic value
					 */
					
					// set reference interval
					before.setReference(target.getToken().getInterval());
					// set target interval
					before.setTarget(reference.getToken().getInterval());
					// set bounds
					before.setLowerBound(0);
					before.setUpperBound(this.tdb.getHorizon());
					
					// verify constraint feasibility through constraint propagation
					this.tdb.propagate(before);
					// check temporal consistency
					this.tdb.checkConsistency();
					
					
					// compute the preserved space heuristic value resulting after constraint propagation
					double preserved = this.computePreservedSpaceHeuristicValue(
							target.getToken().getInterval().getEndTime(), 
							reference.getToken().getInterval().getStartTime());
					
					// compute the resulting makespan of the temporal plan after constraint propagation
					ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
					// process query
					this.tdb.process(query);
					// get computed makespan
					double makespan = query.getMakespan();
					
					// create and add solution to the MCS
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
					// retract (inverted) precedence constraint
					this.tdb.retract(before);
					// clear temporal relation
					before.clear();
				}
			}
		}
		
		// check MCS solutions
		if (mcs.getSolutions().isEmpty()) {
			// unsolvable MCS found
			throw new UnsolvableFlawException("Unsolvable MCS found on discrete resource " + this.component.getName() + "\n- mcs: " + mcs + "\n");
		}
	}
	
	/**
	 * 	Given a set of overlapping activities that exceed the resource capacity (i.e. a peak) this method computes sets of 
	 * precedence constraints among activities composing the critical set (CS) in order to solve the peak 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawException 
	{
		// cast flaw
		CriticalSet cs = (CriticalSet) flaw;
		
		/*
		 * A critical set (CS) is not necessary minimal, so sample MCSs from the CS
		 */
		
		// sample the critical set in order to find minimal critical sets
		List<MinimalCriticalSet> mcss = this.sampleMCSs(cs);
		
		/*
		 * An MCS can be solved by posting a precedence constraint between any pair of activities. 
		 * 
		 * Thus, each MCS can have several solutions depending on the number of activities involved.
		 * 
		 * For each MCS compute the set of feasible solutions and the related preserved heuristic value 
		 */
		
		try
		{
			// compute feasible solutions of the sampled MCSs
			for (MinimalCriticalSet mcs : mcss) {
				// compute solutions and the related preserved values
				this.computeMinimalCriticalSetSolutions(mcs);
			}
			
			/*
			 * Rate MCSs according to the computed preserved heuristic value and select the best one for expansion
			 */
			
			// sort MCSs according to the computed preserved heuristic value
			Collections.sort(mcss);	
			// get the "best" MCS 
			MinimalCriticalSet best = mcss.get(0);
			
			// add computed solutions to the flow
			for (PrecedenceConstraint pc : best.getSolutions()) {
				// add this precedence constraint as a possible solution of the peak
				flaw.addSolution(pc);
			}
		}
		catch (Exception ex) {
			// unsolvable MCS found
			throw new UnsolvableFlawException("Unsolvable MCS found on discrete resourc e" + this.component.getName() + ":\n" + flaw);
		}
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
		// prepare relations
		Set<Relation> relations = new HashSet<>();
		// get reference and target decisions
		Decision reference = pc.getReference();
		Decision target = pc.getTarget();
			
		// create relation
		BeforeRelation rel = this.component.create(RelationType.BEFORE, reference, target);
		// set bounds
		rel.setBound(new long[] {1, this.component.getHorizon()});
		// add reference, target and constraint
		relations.add(rel);
		this.logger.debug("Applying flaw solution:\n"
				+ "- solution: " + solution + "\n"
				+ "- created temporal constraint: " + rel + "\n");
		
		try 
		{
			// propagate relations
			this.component.add(relations);
			// add activated relations to solution
			solution.addActivatedRelations(relations);
		}
		catch (RelationPropagationException ex) 
		{
			// clear memory from relation
			this.component.free(rel);
			// not feasible solution
			throw new FlawSolutionApplicationException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution solution) 
			throws RelationPropagationException 
	{
		// list of activated relations
		List<Relation> list = solution.getActivatedRelations();
		// list of committed relations
		List<Relation> committed = new ArrayList<>();
		try
		{
			// activate relations
			for (Relation rel : list) 
			{
				// restore relation
				this.component.restore(rel);
				// activate relation
				this.component.add(rel);
				committed.add(rel);
			}
		}
		catch (RelationPropagationException ex) 
		{
			// deactivate committed relations
			for (Relation relation : committed) {
				// free relation
				this.component.free(relation);
			}

			// error while restoring flaw solution
			throw new RelationPropagationException("Error while resetting flaw solution:\n- " + solution + "\n");
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// get the list of activated (and also created) relations
		List<Relation> relations = solution.getActivatedRelations();
		// manage activated relations
		for (Relation rel : relations) {
			// free created and activated relations
			this.component.free(rel);
		}
	}
	
	/**
	 * Compute the list of critical sets (CSs) on a (pessimistic or optimistic) resource profile 
	 * 
	 * @param profile
	 * @return
	 */
	private List<CriticalSet> computeCriticalSets(DiscreteResourceProfile profile)
	{
		// initialize the list of flaws
		List<CriticalSet> CSs = new ArrayList<>();
		// get profile samples
		List<RequirementResourceProfileSample> samples = profile.getSamples();
		
		// data structure to maintain data "learned" during flaw detection
		for (int index = 0; index < samples.size() - 1; index++)
		{
			// get current sample
			RequirementResourceProfileSample i = samples.get(index);
			// initialize the critical set
			CriticalSet cs = new CriticalSet((DiscreteResource) this.component);
			// add i to the current critical set
			cs.addSample(i);
			
			// check possible critical sets
			for (int jndex = index + 1; jndex < samples.size(); jndex++)
			{
				// get sample
				RequirementResourceProfileSample j = samples.get(jndex);
				// verify whether the current sample overlaps the considered critical set
				if (cs.isOverlapping(j)) {
					// add the sample to the critical set
					cs.addSample(j);
				}
			}
			
			// check critical set condition
			if (cs.getTotalRequirement() > this.component.getMaxCapacity()) {
				// a critical set has been found
				CSs.add(cs);
			}
		}
		
		// get the list of discovered critical sets
		return CSs;
	}
}

/**
 * 
 * @author anacleto
 *
 */
class MinimalCriticalSet implements Comparable<MinimalCriticalSet>
{
	protected CriticalSet cs;									// the set of overlapping activities
	private Set<RequirementResourceProfileSample> samples;		// activities composing the MCS
	private List<PrecedenceConstraint> solutions;				// a MCS can be solved by posting a simple precedence constraint
	
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
