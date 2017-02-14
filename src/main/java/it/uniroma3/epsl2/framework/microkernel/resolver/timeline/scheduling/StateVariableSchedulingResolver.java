package it.uniroma3.epsl2.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.component.Token;
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
import it.uniroma3.epsl2.framework.time.lang.query.CheckIntervalDistanceQuery;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public final class StateVariableSchedulingResolver <T extends StateVariable> extends Resolver implements Comparator<Decision>
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
	 * Compare two decisions according to their tokens
	 */
	@Override
	public int compare(Decision d1, Decision d2) 
	{
		// get start times
		TimePoint s1 = d1.getToken().getInterval().getStartTime();
		TimePoint s2 = d2.getToken().getInterval().getStartTime();
		// compare start times w.r.t lower and upper bounds
		return s1.getLowerBound() < s2.getLowerBound() ? -1 : 
			s1.getLowerBound() == s2.getLowerBound() && s1.getUpperBound() <= s2.getUpperBound() ? -1 : 1;
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
		List<Relation> relations = new ArrayList<>();
		// setup relations
		for (int index = 0; index <= ordering.getSchedule().size() - 2; index++) 
		{
			// get adjacent decisions
			Decision reference = ordering.getSchedule().get(index);
			Decision target = ordering.getSchedule().get(index + 1);
			
			// create relation
			BeforeRelation rel = this.component.createRelation(RelationType.BEFORE, reference, target);
			// set bounds
			rel.setBound(new long[] {0, this.component.getHorizon()});
			// add reference, target and constraint
			relations.add(rel);
			
			this.logger.debug("Applying flaw solution\n- " + solution + "\nthrough before constraint " + rel);
		}
		
		try {
			// propagate relations
			this.component.addRelations(relations);
			// set added relations
			solution.addAddedRelations(relations);
		}
		catch (RelationPropagationException ex) {
			// free all relations
			for (Relation rel : relations) {
				this.component.free(rel);
			}
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
		// set of scheduled decisions
		Set<Set<Decision>> scheduled = new HashSet<>();
		
		// list of active decisions
		List<Decision> decisions = this.component.getActiveDecisions();
		// sort decisions
		Collections.sort(decisions, this);

		// look for peaks
		for (Decision source : decisions) 
		{
			for (Decision target : decisions) 
			{
				// check decisions and if a peak with them already exists
				if (!source.equals(target) && !this.checkInPeak(peaks, source, target) &&
						!this.checkInScheduled(scheduled, source, target)) 
				{
					// check flexibility
					CheckIntervalDistanceQuery query = this.tdb.
							createTemporalQuery(TemporalQueryType.CHECK_INTERVAL_DISTANCE);
					
					// set intervals
					query.setSource(source.getToken().getInterval());
					query.setTarget(target.getToken().getInterval());
					//process query
					this.tdb.process(query);

					// get bounds
					long dmin = query.getDistanceLowerBound();
					long dmax = query.getDistanceUpperBound();
					
					// check distance bounds
					if (dmin < 0 && dmax > 0) 
					{
						// overlapping decisions
						this.logger.debug("Overlapping decisions found [dmin= " + dmin +", dmax= " + dmax + "]\n" + source + "\n" + target);
						// we've got a peak
						Peak peak = new Peak(this.component);
						// set related decisions
						peak.add(source);
						peak.add(target);
						// add peak
						peaks.add(peak);
					}
					else 
					{
						// add decisions to scheduled set
						Set<Decision> sset = new HashSet<>();
						sset.add(source);
						sset.add(target);
						scheduled.add(sset);
						// already scheduled decision
						this.logger.debug("Already scheduled decisions [dmin= " + dmin +", dmax= " + dmax + "]\n- source: " + source + "\n- target: " + target);
					}
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
		List<List<Decision>> orderings = this.schedule(peak.getDecisions());
		// compute solutions
		for (List<Decision> ordering : orderings) 
		{
			// check consistency of possible schedule
			this.logger.debug("Check consistency of possible schedule\n" + ordering);
			// add a possible 
			DecisionSchedule solution = new DecisionSchedule(peak, ordering);
			// compute precedence constraints and check their temporal consistency
			if (this.checkScheduleConsistency(solution)) 
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
			throw new UnsolvableFlawFoundException("Unsolvable Peak found on state variable " + this.component.getName() + ":\n" + flaw);
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// manage added relations
		for (Relation rel : solution.getAddedRelations()) {
			// completely delete relation 
			this.component.free(rel);
		}
		
		// manage activated relations
		for (Relation rel : solution.getActivatedRelations()) {
			// deactivate relation
			this.component.delete(rel);
		}
		
		
		// manage created relations
		for (Relation rel : solution.getCreatedRelations()) {
			// delete pending relation
			this.component.delete(rel);
		}
		
		// delete activated decisions
		for (Decision dec : solution.getActivatedDecisisons()) {
			// deactivate decision
			this.component.delete(dec);
		}
		
		// delete pending decisions
		for (Decision dec : solution.getCreatedDecisions()) {
			// delete pending decisions
			this.component.delete(dec);
		}
		
	}
	
	/**
	 * 
	 */
	private List<List<Decision>> schedule(List<Decision> decisions) {
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
	private void computePermutations(List<Decision> decisions, List<Decision> current, List<List<Decision>> result) {
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
	private boolean checkScheduleConsistency(DecisionSchedule solution) 
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
			CheckIntervalDistanceQuery query = this.tdb.
					createTemporalQuery(TemporalQueryType.CHECK_INTERVAL_DISTANCE);
			
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
			
//			consistent = query.getDistanceLowerBound() >= 0 || query.getDistanceUpperBound() >= 0;
			consistent = !(dmin < 0 && dmax < 0);
			// next precedence constraint
			index++;
		}
		
		// get consistency result
		return consistent;
	}
	
	/**
	 * 
	 * @param peaks
	 * @param source
	 * @param target
	 * @return
	 */
	private boolean checkInPeak(Set<Peak> peaks, Decision source, Decision target) {
		boolean exist = false;
		for (Peak peak : peaks) {
			exist = peak.contains(source) && peak.contains(target);
			if (exist) {
				break;
			}
		}
		return exist;
	}
	
	/**
	 * 
	 * @param peaks
	 * @param source
	 * @param target
	 * @return
	 */
	private boolean checkInScheduled(Set<Set<Decision>> scheduled, Decision source, Decision target) {
		boolean exist = false;
		for (Set<Decision> set : scheduled) {
			exist = set.contains(source) && set.contains(target);
			if (exist) {
				break;
			}
		}
		return exist;
	}

	
}

