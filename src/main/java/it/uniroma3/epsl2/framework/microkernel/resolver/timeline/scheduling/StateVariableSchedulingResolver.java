package it.uniroma3.epsl2.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.domain.component.sv.StateVariable;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.lang.plan.relations.temporal.BeforeRelation;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentReference;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.time.ex.TemporalConstraintPropagationException;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;
import it.uniroma3.epsl2.framework.time.lang.allen.BeforeIntervalConstraint;
import it.uniroma3.epsl2.framework.time.lang.query.ComputeMakespanQuery;
import it.uniroma3.epsl2.framework.time.lang.query.IntervalOverlapQuery;

/**
 * 
 * @author anacleto
 *
 */
public final class StateVariableSchedulingResolver <T extends StateVariable> extends Resolver
{
	@ComponentReference
	private T component;
	
	/**
	 * 
	 */
	protected StateVariableSchedulingResolver() {
		super(ResolverType.SV_SCHEDULING_RESOLVER);
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
				// set the resulting makespan
				solution.setMakespan(makespan);
				// add feasible solution to the peak
				peak.addSolution(solution);
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
	 * @param schedule
	 * @return
	 * @throws TemporalConstraintPropagationException
	 */
	private double checkScheduleFeasibility(List<Decision>schedule) 
			throws TemporalConstraintPropagationException
	{
		// computed makespan 
		double makespan = this.tdb.getOrigin();
		// list of propagate precedence constraints
		List<TemporalConstraint> committed = new ArrayList<>();
		try
		{
			for (int index = 0; index < schedule.size() - 1; index++) 
			{
				// get decisions
				Decision a = schedule.get(index);
				Decision b = schedule.get(index + 1);
				
				// create temporal constraint
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
				before.setReference(a.getToken().getInterval());
				before.setTarget(b.getToken().getInterval());
				// propagate temporal constraint
				this.tdb.propagate(before);
				// add committed constraint
				committed.add(before);
			}
			
			// check feasibility 
			this.tdb.checkConsistency();
			
			// feasible solution - compute the resulting makespan
			ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
			this.tdb.process(query);
			// get computed makespan
			makespan = query.getMakespan();
		}
		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// not feasible schedule
			this.logger.debug("Not feasible schedule constraint found\n- " + ex.getMessage() + "\n");
			// forward exception
			throw new TemporalConstraintPropagationException(ex.getMessage());
		}
		finally {
			// restore initial state
			for (TemporalConstraint cons : committed) {
				// remove constraint from network
				this.tdb.retract(cons);
			}
		}
		
		// get resulting makespan
		return makespan;
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
	
	/**
	 * Given a set of decisions to be scheduled, the method returns all the 
	 * possible sequences of decisions i.e., all the possible ways to schedule
	 * decisions
	 * 
	 * @param decisions
	 * @return
	 */
	private List<List<Decision>> schedule(Set<Decision> decisions) {
		// initialize permutations
		List<List<Decision>> result = new ArrayList<>();
		// compute permutations
		this.computePermutations(decisions, new ArrayList<Decision>(), result);
		// get permutations
		return result;
	}
	
	/**
	 * 
	 * @param decisions
	 * @param current
	 * @param result
	 */
	private void computePermutations(Set<Decision> decisions, List<Decision> current, List<List<Decision>> result) {
		// base step
		if (current.size() == decisions.size()) {
			result.add(current);
		}
		else {
			// recursive step
			for (Decision dec : decisions) {
				if (!current.contains(dec)) {
					// create a new current list
					List<Decision> list = new ArrayList<>(current);
					list.add(dec);
					// recursive call
					this.computePermutations(decisions, list, result);
				}
			}
		}
	}
}

