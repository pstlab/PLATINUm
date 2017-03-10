package it.uniroma3.epsl2.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.component.Token;
import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.domain.component.sv.StateVariable;
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
import it.uniroma3.epsl2.framework.time.lang.query.IntervalDistanceQuery;

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
				// check overlapping decisions on both bounds
				if (this.overlaps(a, b)) 
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
							+ "- A= " + a + "\n"
							+ "- B= " + b + "\n"
							+ "-peak= " + peak);
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
			// check consistency of possible schedule
			this.logger.debug("Check consistency of possible schedule\n" + ordering);
			// add a possible 
			DecisionSchedule solution = new DecisionSchedule(peak, ordering);
			// compute precedence constraints and check their temporal consistency
			if (this.isConsistentSchedule(solution)) 
			{
				// feasible solution of the peak
				this.logger.debug("Feasible solution of the peak\n" + solution);
				// add solution
				peak.addSolution(solution);
			}
			else 
			{
				// not feasible solution 
				this.logger.debug("Discarding not feasible solution of the peak:\n" + solution);
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
	
	/**
	 * 
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
	
	/**
	 * Given a possible schedule of tokens to solve a peak, the method checks
	 * if the schedule is consistent 
	 * 
	 * @param solution
	 */
	private boolean isConsistentSchedule(DecisionSchedule solution) 
	{
		// consistency flag of the schedule
		boolean consistent = true;
		int index = 0;
		int size = solution.getSchedule().size();
		while (index <= size - 2 && consistent) 
		{
			// get related tokens
			Token i = solution.getSchedule().
					get(index).getToken();
			Token j = solution.getSchedule().
					get(index + 1).getToken();
			
			// check interval distance
			IntervalDistanceQuery query = this.tdb.
					createTemporalQuery(TemporalQueryType.INTERVAL_DISTANCE);
			
			// set parameters
			query.setSource(i.getInterval());
			query.setTarget(j.getInterval());
			// process query
			this.tdb.process(query);
			
			/* 
			 * Check the consistency of the precedence constraint, pc(i,j), that 
			 * we are going to add in the network.
			 * 
			 * - If distance(i,j) = [-d, +d] then the precedence constraint will actually
			 * schedule the two intervals
			 * 
			 * - If distance(i,j) = [d, D] then the precedence constraint is redundant 
			 * because tokens are already scheduled 
			 * 
			 * - If distance(i,j) = [-D, -d] then the precedence constraint will cause a 
			 * temporal inconsistency because the interval j precedes interval i,
			 * i.e. j is before i actually
			 */
			
			long dmin = query.getDistanceLowerBound();
			long dmax = query.getDistanceUpperBound();
			
			consistent = !(dmin < 0 && dmax < 0);
			// next precedence constraint
			index++;
		}
		
		// get consistency result
		return consistent;
	}
}

