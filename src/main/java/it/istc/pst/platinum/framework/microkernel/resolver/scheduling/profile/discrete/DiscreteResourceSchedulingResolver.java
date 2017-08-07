package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.profile.discrete;

import java.util.ArrayList;
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
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;

/**
 * 
 * @author anacleto
 *
 */
public class DiscreteResourceSchedulingResolver <T extends DomainComponent<?> & DiscreteResourceProfileManager> extends SchedulingResolver 
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
			flaws.addAll(this.computeCriticalSets(prp));
			
			// check if any flaw has been found
			if (flaws.isEmpty()) {
				// check optimistic resource profile
				DiscreteResourceProfile orp = this.component.computeOptimisticResourceProfile();
				// compute flaws on profile
				flaws.addAll(this.computeCriticalSets(orp));
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
	 * 	FIXME -> Leverage MCS to compute peak solutions 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException 
	{
		// cast flaw
		CriticalSet profileFlaw = (CriticalSet) flaw;
		// compute all possible orderings of the tokens
		List<List<Decision>> orderings = this.schedule(profileFlaw.getPeak());
		// compute solutions
		for (List<Decision> ordering : orderings) 
		{
			// add a possible 
			FlawSolution solution = new PrecedenceConstraintPosting(profileFlaw, ordering);
			try
			{
				// propagate solution and compute the resulting makespan
				double makespan = this.checkScheduleFeasibility(ordering);
				// set resulting makespan
				solution.setMakespan(makespan);
				profileFlaw.addSolution(solution);
				this.logger.debug("Feasible solution of the peak:\n"
						+ "- peak: " + profileFlaw.getPeak() + "\n"
						+ "- feasible solution: " + solution + "\n"
						+ "- resulting makespan: " + makespan + "\n");
			}
			catch (TemporalConstraintPropagationException ex) {
				// not feasible schedule, discard the related schedule
				this.logger.debug("Not feasible solution of the peak:\n- solution= " + solution + "\n... discarding solution\n");
			}
		}
		
		// check available solutions
		if (!profileFlaw.isSolvable()) {
			// throw exception
			throw new UnsolvableFlawFoundException("Unsolvable Peak found on state variable " + this.component.getName() + ":\n" + flaw);
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
	private List<Flaw> computeCriticalSets(DiscreteResourceProfile profile)
	{
		// initialize the list of flaws
		List<Flaw> flaws = new ArrayList<>();
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
				flaws.add(cs);
			}
		}
		
		// get the list of discovered critical sets
		return flaws;
	}
}
