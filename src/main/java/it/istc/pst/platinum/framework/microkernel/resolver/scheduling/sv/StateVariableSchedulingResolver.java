package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.sv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.resolver.scheduling.SchedulingResolver;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.lang.query.IntervalOverlapQuery;

/**
 * 
 * @author anacleto
 *
 */
public final class StateVariableSchedulingResolver <T extends StateVariable> extends SchedulingResolver
{
	@ComponentPlaceholder
	private T component;
	
	/**
	 * 
	 */
	protected StateVariableSchedulingResolver() {
		super(ResolverType.SV_SCHEDULING_RESOLVER.getLabel(), 
				ResolverType.SV_SCHEDULING_RESOLVER.getFlawType());
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get the flaw solution to consider
		DecisionSchedule ordering = (DecisionSchedule) solution;
		// prepare relations
		Set<Relation> relations = new HashSet<>();
		// setup relations
		for (int index = 0; index <= ordering.getSchedule().size() - 2; index++) 
		{
			// get adjacent decisions
			Decision reference = ordering.getSchedule().get(index);
			Decision target = ordering.getSchedule().get(index + 1);
			
			// create relation
			BeforeRelation rel = this.component.create(RelationType.BEFORE, reference, target);
			// set bounds
			rel.setBound(new long[] {0, this.component.getHorizon()});
			// add reference, target and constraint
			relations.add(rel);
			this.logger.debug("Applying flaw solution:\n"
					+ "- solution: " + solution + "\n"
					+ "- created temporal constratin: " + rel + "\n");
		}
		
		try 
		{
			// propagate relations
			this.component.add(relations);
			// set relation as activated
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
	protected List<Flaw> doFindFlaws() 
	{
		// list of peaks
		Set<Peak> peaks = new HashSet<>();
		// list of active decisions
		List<Decision> decisions = this.component.getActiveDecisions();
		// look for peaks
		for (int index = 0; index < decisions.size() - 1; index++)
		{
			// get active decision A 
			Decision a = decisions.get(index);
			for (int jndex = index + 1; jndex < decisions.size(); jndex++)
			{
				// get active decision B
				Decision b = decisions.get(jndex);
				// check overlapping intervals
				IntervalOverlapQuery query = this.tdb.createTemporalQuery(TemporalQueryType.INTERVAL_OVERLAP);
				// set intervals
				query.setReference(a.getToken().getInterval());
				query.setTarget(b.getToken().getInterval());
				// process query
				this.tdb.process(query);
				
				// check overlapping decisions on both bounds
				if (query.isOverlapping()) 
				{
					// we've got a peak
					Peak peak = new Peak(this.component);
					// set related decisions
					peak.add(a);
					peak.add(b);
					// add peak
					peaks.add(peak);
					
					// overlapping decisions
					this.logger.debug("Overlapping decisions found on component \"" + this.component.getName() + "\":\n"
							+ "- A= " + a + " -> " + a.getToken() +  "\n"
							+ "- B= " + b + " -> " + b.getToken() + "\n");
				}
			}
		}
		
		// get peaks
		return new ArrayList<Flaw>(peaks);
	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException 
	{
		// cast flaw
		Peak peak = (Peak) flaw;
		// compute all possible orderings of the tokens
		List<List<Decision>> orderings = this.schedule(new HashSet<>(peak.getDecisions()));
		// compute solutions
		for (List<Decision> ordering : orderings) 
		{
			// check possible schedule 
			DecisionSchedule solution = new DecisionSchedule(peak, ordering);
			try
			{
				// propagate solution and compute the resulting makespan
				double makespan = this.checkScheduleFeasibility(solution.getSchedule());
				// add feasible solution to the peak
				peak.addSolution(solution);
				// set the resulting makespan
				solution.setMakespan(makespan);
				this.logger.debug("Feasible solution of the peak:\n"
						+ "- peak: " + peak.getDecisions() + "\n"
						+ "- feasible solution: " + solution + "\n"
						+ "- resulting makespan: " + makespan + "\n");
			}
			catch (TemporalConstraintPropagationException ex) {
				// not feasible schedule, discard the related schedule
				this.logger.debug("Not feasible solution of the peak:\n- solution= " + solution + "\n... discarding solution\n");
			}
		}
		
		// check available solutions
		if (!peak.isSolvable()) {
			// throw exception
			throw new UnsolvableFlawFoundException("Unsolvable Peak found on state variable "
					+ "" + this.component.getName() + ":\n" + flaw);
		}
	}
	
	/**
	 * The StateVariableSchedulingResolver affects only temporal relations between activated decisions of the 
	 *  related components. Thus, it is not necessary to check activated/deactivated decisions.
	 *  
	 *  This method simply completely remove added relations from the plan in order to cancel the 
	 *  effects of a solution
	 *  
	 * @param solution
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// get the list of activated (and also created) relations
		List<Relation> relations = solution.getActivatedRelations();
		// manage activated relations
		for (Relation relation : relations) {
			// free created and activated relations
			this.component.free(relation);
		}
	}
	
	/**
	 * This method restores a previously retracted scheduling solution. 
	 * 
	 * The restore procedure consists in reactivating the previously computed and activated temporal relations
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
			for (Relation relation : list) 
			{
				// restore relation
				this.component.restore(relation);
				// activate relation
				this.component.add(relation);
				committed.add(relation);
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
}

