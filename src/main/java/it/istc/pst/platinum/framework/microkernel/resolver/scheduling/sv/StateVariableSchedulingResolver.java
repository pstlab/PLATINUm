package it.istc.pst.platinum.framework.microkernel.resolver.scheduling.sv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
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
			this.logger.debug("Applying flaw solution\n- " + solution + "\nthrough before constraint " + rel);
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
		this.logger.debug("Check overlapping decisions on component= " + this.component.getName() + "\n"
				+ "- " + decisions + "\n");
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
					this.logger.debug("Overlapping decisions found:\n"
							+ "- A= " + a + " -> " + a.getToken() +  "\n"
							+ "- B= " + b + " -> " + b.getToken() + "\n");
				}
			}
		}
		
		// check if peaks have been found
		if (!peaks.isEmpty()) {
			// print peaks found
			this.logger.debug("Peaks found on " + this.component +":\n" + peaks);
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
				this.logger.debug("Feasible solution of the peak:\n- solution= " + solution + "\n");
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
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// manage activated relations
		for (Relation rel : solution.getActivatedRelations()) {
			// deactivate relation
			this.component.delete(rel);
		}
		
		// delete activated decisions: ACTIVE -> PENDING
		for (Decision dec : solution.getActivatedDecisisons()) {
			// deactivate decision
			this.component.delete(dec);
		}
		
		// delete pending decisions: PENDING -> SILENT
		for (Decision dec : solution.getCreatedDecisions()) {
			// delete pending decisions
			this.component.delete(dec);
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution solution) 
			throws Exception 
	{
		// get created decisions 
		List<Decision> dCreated = solution.getCreatedDecisions();
		// restore created decisions
		for (Decision dec : dCreated) {
			// restore decision
			this.component.restore(dec);
		}
		
		// get activated decisions
		List<Decision> dActivated = solution.getActivatedDecisisons();
		List<Decision> commitDecs = new ArrayList<>();
		// activate decisions
		for (Decision dec : dActivated) 
		{
			try
			{
				// activate decision
				this.component.add(dec);
				commitDecs.add(dec);
			}
			catch (DecisionPropagationException ex) 
			{
				// deactivate committed decisions
				for (Decision d : commitDecs) {
					// deactivate decision
					this.component.delete(d);
				}

				// error while resetting flaw solution
				throw new Exception("Error while resetting flaw solution:\n- " + solution + "\n");
			}
		}
		
		// get activated relations
		List<Relation> rActivated = solution.getActivatedRelations();
		// list of committed relations
		List<Relation> commitRels = new ArrayList<>();
		// activate relations
		for (Relation rel : rActivated) 
		{
			try
			{
				// activate relation
				this.component.add(rel);
				commitRels.add(rel);
			}
			catch (RelationPropagationException ex) {
				// deactivate committed relations
				for (Relation r : commitRels) {
					// deactivate relation
					this.component.delete(r);
				}
				
				// deactivate committed decisions
				for (Decision d : commitDecs) {
					// deactivate 
					this.component.delete(d);
				}
				
				// error while restoring flaw solution
				throw new Exception("Error while resetting flaw solution:\n- " + solution + "\n");
			}
		}
	}
}

