package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.profile.discrete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.ResourceProfileComputationException;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.DiscreteResourceProfile;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.DiscreteResourceProfileManager;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceProfileSample;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.resolver.scheduling.SchedulingResolver;
import it.istc.pst.platinum.framework.microkernel.resolver.scheduling.profile.reservoir.PrecedenceConstraintPosting;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResourceSchedulingResolver <T extends DomainComponent<?> & DiscreteResourceProfileManager> extends SchedulingResolver implements Comparator<RequirementResourceProfileSample> 
{ 
	@ComponentPlaceholder
	protected T component;

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
			if (flaws.isEmpty()) {
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
				long amount = mcs.getTotalAmount() + other.getAmount();
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
	 * 
	 * @param mcs
	 * @throws Exception - contains information concerning the unsolvable MCS
	 */
	private void computeMinimalCriticalSetSolutions(MinimalCriticalSet mcs) 
			throws Exception
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
				Decision target  =list.get(jndex).getDecision();
				
				try
				{
					/*
					 * TODO : check feasibility of precedence constraint "reference < target" and compute the resulting preserved heuristic value
					 */
					
					double preserved = 0;
					
					// create and add solution to the MCS
					mcs.createSolution(reference, target, preserved);
				}
				catch (Exception ex) {
					// warning message
					this.logger.debug("Unfeasible solution found for MCS:\n- mcs: " + mcs + "\n- unfeasible precedence constraint: " + reference + " < " + target + "\n");
				}
				
				
				try
				{
					/*
					 * TODO : check the feasibility of precedence constraint "target < reference" and compute the resulting preserved heuristic value
					 */
					
					double preserved = 0;
					
					// create and add solution to the MCS
					mcs.createSolution(target, reference, preserved);
				}
				catch (Exception ex) {
					// warning message
					this.logger.debug("Unfeasible solution found for MCS:\n- mcs: " + mcs + "\n- unfeasible precedence constraint: " + target + " < " + reference + "\n");
				}
			}
		}
		
		// check MCS solutions
		if (mcs.getSolutions().isEmpty()) {
			// unsolvable MCS found
			throw new Exception("Unsolvable MCS found on discrete resource " + this.component.getName() + "\n- mcs: " + mcs + "\n");
		}
	}
	
	/**
	 * 	Given a set of overlapping activities that exceed the resource capacity (i.e. a peak) this method computes sets of 
	 * precedence constraints among activities composing the critical set (CS) in order to solve the peak 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException 
	{
		// cast flaw
		CriticalSet cs = (CriticalSet) flaw;
		
		/*
		 * TODO : A critical set (CS) is not necessary minimal, so sample MCSs from the CS
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
			 * TODO : rate MCSs according to the computed preserved heuristic value and select the best one for expansion
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
			throw new UnsolvableFlawFoundException("Unsolvable MCS found on discrete resourc e" + this.component.getName() + ":\n" + flaw);
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
		PrecedenceConstraintPosting pcp = (PrecedenceConstraintPosting) solution;
		// prepare relations
		Set<Relation> relations = new HashSet<>();
		// setup relations
		for (int index = 0; index <= pcp.getPrecedences().size() - 2; index++) 
		{
			// get adjacent decisions
			Decision reference = pcp.getPrecedences().get(index);
			Decision target = pcp.getPrecedences().get(index + 1);
			
			// create relation
			BeforeRelation rel = this.component.create(RelationType.BEFORE, reference, target);
			// set bounds
			rel.setBound(new long[] {1, this.component.getHorizon()});
			// add reference, target and constraint
			relations.add(rel);
			this.logger.debug("Applying flaw solution:\n"
					+ "- solution: " + solution + "\n"
					+ "- created temporal constraint: " + rel + "\n");
		}
		
		try 
		{
			// propagate relations
			this.component.add(relations);
			// add activated relations to solution
			solution.addActivatedRelations(relations);
		}
		catch (RelationPropagationException ex) 
		{
			// free all relations
			for (Relation rel : relations) {
				// clear memory from relation
				this.component.free(rel);
			}
			
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
		Map<RequirementResourceProfileSample, Set<RequirementResourceProfileSample>> skip = new HashMap<>();
		for (RequirementResourceProfileSample i : samples) 
		{
			// initialize the critical set
			CriticalSet cs = new CriticalSet(this.component);
			// add i to the current critical set
			cs.addSample(i);
			// check possible critical sets
			for (RequirementResourceProfileSample j : samples)
			{
				// take into account distinct samples
				if (!i.equals(j) && !skip.get(i).contains(j)) 
				{
					// verify whether the current sample overlaps the considered critical set
					if (cs.isOverlapping(j)) 
					{
						// add the sample to the critical set
						cs.addSample(j);
						// add skip information
						if (skip.containsKey(i)) {
							skip.put(i, new HashSet<>());
						}
						if (skip.containsKey(j)) {
							skip.put(j, new HashSet<>());
						}
						
						// add skip information
						skip.get(i).add(j);
						skip.get(j).add(i);
					}
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
