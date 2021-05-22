package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.timeline.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.RelationPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.sv.StateVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.Resolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ResolverType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalConstraintPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraintType;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.allen.BeforeIntervalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalOverlapQuery;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author alessandro
 *
 */
public final class TimelineSchedulingResolver extends Resolver<StateVariable>
{
	private boolean load;
	private double schedulingCost;
	
	/**
	 * 
	 */
	protected TimelineSchedulingResolver() {
		super(ResolverType.TIMELINE_SCHEDULING_RESOLVER.getLabel(), 
				ResolverType.TIMELINE_SCHEDULING_RESOLVER.getFlawTypes());
		// set load flag
		this.load = false;
	}
	
	/**
	 * 
	 */
	private void load() {
		// get deliberative property file
		FilePropertyReader properties = new FilePropertyReader(
				FRAMEWORK_HOME + FilePropertyReader.DEFAULT_DELIBERATIVE_PROPERTY);
		// get weight
		this.schedulingCost = Double.parseDouble(properties.getProperty("scheduling-cost"));
		// set flag
		this.load = true;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException {
		
		// get the flaw solution to consider
		OverlappingSetSchedule schedule = (OverlappingSetSchedule) solution;
		// list of committed relations
		List<BeforeRelation> committed = new ArrayList<>();
		
		try {
			
			// apply all precedence constraints
			for (PrecedenceConstraint pc : schedule.getConstraints()) {
			
				// get reference and target decisions
				Decision reference = pc.getReference();
				Decision target = pc.getTarget();
				
				// create relation
				BeforeRelation before = this.component.create(
						RelationType.BEFORE, 
						reference, 
						target);
				
				// set bounds
				before.setBound(new long[] {
						0, 
						this.component.getHorizon()});
				// add created relation to solution
				solution.addCreatedRelation(before);
			
				// add relation to committed
				committed.add(before);
				// propagate relations
				this.component.activate(before);
				// add activated relations to solution
				solution.addActivatedRelation(before);
				// check feasibility
				this.tdb.verify();
			}
			
		} catch (RelationPropagationException | ConsistencyCheckException ex) {
			
			// write error message
			error("Error while applying flaw solution:\n"
					+ "- solution: " + solution + "\n");
			
			// clear data structure by remove all committed relations
			for (BeforeRelation before : committed) {
				
				// deactivate relation
				this.component.deactivate(before);
				// delete relation
				this.component.delete(before);
			}
			
			// not feasible solution
			throw new FlawSolutionApplicationException(ex.getMessage());
		}
	}
	
	
//	protected void doApply(FlawSolution solution) 
//			throws FlawSolutionApplicationException 
//	{
//		// get the flaw solution to consider
//		DecisionPrecedenceConstraint pc = (DecisionPrecedenceConstraint) solution;
//		// get reference and target decisions
//		Decision reference = pc.getReference();
//		Decision target = pc.getTarget();
//		
//		// create relation
//		BeforeRelation before = this.component.create(RelationType.BEFORE, reference, target);
//		// set bounds
//		before.setBound(new long[] {
//				0, 
//				this.component.getHorizon()});
//		// add created relation to solution
//		solution.addCreatedRelation(before);
//		
//		try
//		{
//			// propagate relations
//			this.component.activate(before);
//			// add activated relations to solution
//			solution.addActivatedRelation(before);
//			debug("Precedence constraint successfully created and activated:\n"
//					+ "- temporal constraint: " + before + "\n");
//			
//			// check feasibility
//			this.tdb.verify();
//		}
//		catch (RelationPropagationException | ConsistencyCheckException ex) 
//		{
//			// write error message
//			error("Error while applying flaw solution:\n"
//					+ "- solution: " + solution + "\n"
//					+ "- unfeasible precedence constraint: " + before + "\n");
//
//			// deactivate relation
//			this.component.deactivate(before);
//			// delete relation
//			this.component.delete(before);
//			// not feasible solution
//			throw new FlawSolutionApplicationException(ex.getMessage());
//		}
//	}
	
	/**
	 * 
	 */
	@Override
	protected List<Flaw> doFindFlaws() {
		
		// check flag
		if (!this.load) {
			this.load();
		}
		
		// list of critical sets
		List<Flaw> CSs = new ArrayList<>();
		// list of active decisions
		List<Decision> decisions = this.component.getActiveDecisions();
		// sort decisions
		Collections.sort(decisions);
		
		// look for critical sets
		for (int index = 0; index < decisions.size() - 1; index++) {
			
			// get active decision  
			Decision activity = decisions.get(index);
			// prepare a critical set
			OverlappingSet cs = new OverlappingSet(
					FLAW_COUNTER.getAndIncrement(), 
					this.component);
			// add current activity
			cs.add(activity);
			
			// find possibly overlapping decisions
			for (int jndex = index + 1; jndex < decisions.size(); jndex++) {
				
				// get another activity of the component
				Decision other = decisions.get(jndex);
				
				// check overlapping condition with the critical set
				if (this.conflict(cs, other)) {
					
					// add the activity to the critical set
					cs.add(other);
				}
				
			}
			
			// check the size of the critical set
			if (cs.size() > 1) {
				
				// the critical set actually represents a flaw of the component
				CSs.add(cs);
			}
		}
		
		// get the list of critical sets found
		return CSs;
	}
	
	/**
	 * Check if an activity overlaps ALL the activities composing a Critical Set
	 * 
	 * @param cs
	 * @param activity
	 * @return
	 */
	private boolean conflict(OverlappingSet cs, Decision activity) {
		
		// conflicting flag
		boolean conflict = true;
		// get events of the critical set
		List<Decision> activities = cs.getDecisions();
		// check set of events
		for (int index = 0; index < activities.size() && conflict; index++) {
			
			// get an activity of the critical set
			Decision csActivity = activities.get(index);
			// check overlapping condition
			IntervalOverlapQuery query = this.tdb.createTemporalQuery(TemporalQueryType.INTERVAL_OVERLAP);
			// set intervals
			query.setReference(csActivity.getToken().getInterval());
			query.setTarget(activity.getToken().getInterval());
			// process query
			this.tdb.process(query);
			
			// check whether the (flexible) temporal interval can overlap or not
			conflict = query.canOverlap();
		}
		
		// get result
		return conflict;
	}
	
	
//	@Override
//	protected List<Flaw> doFindFlaws() {
//		
//		// check flag
//		if (!this.load) {
//			this.load();
//		}
//		
//		// list of critical sets
//		List<Flaw> flaws = new ArrayList<>();
//		// list of active decisions
//		List<Decision> decisions = this.component.getActiveDecisions();
//		// sort decisions
//		Collections.sort(decisions);
//		
//		
//		// look for peaks
//		for (int index = 0; index < decisions.size() - 1; index++) {
//			// get active decision  
//			Decision reference = decisions.get(index);
//			// find possibly overlapping decisions
//			for (int jndex = index + 1; jndex < decisions.size(); jndex++)
//			{
//				// get another active decision
//				Decision target = decisions.get(jndex);
//				// check if intervals can overlap
//				IntervalOverlapQuery query = this.tdb.createTemporalQuery(
//						TemporalQueryType.INTERVAL_OVERLAP);
//				
//				// set time points
//				query.setReference(reference.getToken().getInterval());
//				query.setTarget(target.getToken().getInterval());
//				// process query
//				this.tdb.process(query);
//				// check overlapping 
//				if (query.canOverlap())
//				{
//					// conflict found
//					BinaryDecisionConflict c = new BinaryDecisionConflict(
//							FLAW_COUNTER.getAndIncrement(), 
//							this.component);
//
//					// set overlapping decisions
//					c.setDecisions(new Decision[] {
//							reference,
//							target
//					});
//					
//					// check if decisions overlaps
//					debug("Overlapping tokens:\n"
//							+ "- component: " + this.component + "\n"
//							+ "- reference token: " + reference + "\n"
//							+ "- target token: " + target + "\n");
//					
//					// add conflict
//					flaws.add(c);
//					
//					
//				} else {
//					
//					// check if decisions overlaps
//					debug("NOT overlapping tokens:\n"
//							+ "- component: " + this.component + "\n"
//							+ "- reference token: " + reference + "\n"
//							+ "- target token: " + target + "\n");
//				}
//			}
//		}
//		
//		// get the list
//		return flaws;
//	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawException {
		
		// get the critical set
		OverlappingSet cs = (OverlappingSet) flaw;
		
		/*
		 * A state variable does not allow any pair of overlapping of its activities. 
		 * 
		 * Each total ordering of the activities that compose a critical set may represent a solution
		 * 
		 */
		
		// compute all possible solutions of the critical set
		this.computeCriticalSetSolutions(cs);
	}
	
	/**
	 * 
	 * @param cs
	 * @throws UnsolvableFlawException
	 */
	private void computeCriticalSetSolutions(OverlappingSet cs) 
			throws UnsolvableFlawException {
		
		// get the list of overlapping activities
		List<Decision> activities = new ArrayList<>(cs.getDecisions());
		// compute feasible schedules
		this.doFindFeasibleSchedule(new ArrayList<>(), activities, cs);
		// check if no feasible solution exists
		if (cs.getSolutions().isEmpty()) {
			
			// unsolvable flaw
			throw new UnsolvableFlawException("Unsolvable flaw found on componnet \"" + this.component + "\"\n"
					+ "- flaw: " + cs + "\n\n");
		}
	}
	
	/**
	 * A solution to a critical set on a State Variable is a total ordering of the activities of the critical set.
	 * 
	 *  This method computes all possible orderings of the activities that are part of a critical set (i.e., the
	 *  permutations). The permutations that represent valid temporal constraints are considered as valid 
	 *  solutions to the critical set
	 * 
	 * @param schedule
	 * @param cs
	 * @param flaw
	 */
	private void doFindFeasibleSchedule(List<Decision> schedule, List<Decision> cs, OverlappingSet flaw) {
		
		// check if a schedule is ready for temporal checking
		if (cs.isEmpty()) {
			
			// check schedule resource feasibility first and then temporal feasibility
			if (this.checkTemporalFeasibility(schedule)) {
				
				// create flaw solution
				OverlappingSetSchedule solution = new OverlappingSetSchedule(flaw, this.schedulingCost);
				// set resulting constraints
				for (int i= 0; i < schedule.size() - 1; i++) {
					
					// add precedence constraint
					solution.addConstraint(schedule.get(i), schedule.get(i + 1));
				}
				
				// add solution to the flaw
				flaw.addSolution(solution);
			}
			
		} else {
			
			// check possible schedules until no solution is found 
			for (int index = 0; index < cs.size(); index++) {
				
				// get an activity from the critical set
				Decision activity = cs.remove(index);
				// add the activity to the possible schedule
				schedule.add(activity);
				
				// recursively build the permutation
				this.doFindFeasibleSchedule(schedule, cs, flaw);
				
				// remove event from the permutation
				schedule.remove(activity);
				// restore data of the critical set
				cs.add(index, activity);
			}
		}
	}
	
	/**
	 * 
	 * @param schedule
	 * @return
	 */
	private boolean checkTemporalFeasibility(List<Decision> schedule) {
		
		// feasibility flag
		boolean feasible = true;
		// list of propagated constraints
		List<BeforeIntervalConstraint> committed = new ArrayList<>();
		
		// check pairs of events 
		for (int index = 0; index < schedule.size() - 1 && feasible; index++) {
			
			try {
				
				// get activities
				Decision a1 = schedule.get(index);
				Decision a2 = schedule.get(index + 1);
				
				// get associated tokens and temporal intervals to check schedule feasibility
				TemporalInterval i1 = a1.getToken().getInterval();
				TemporalInterval i2 = a2.getToken().getInterval();
				
				// create precedence constraint "i1 < i2"
				BeforeIntervalConstraint before = this.tdb.createTemporalConstraint(
						TemporalConstraintType.BEFORE);
				
				// set constraint data
				before.setReference(i1);
				before.setTarget(i2);
				before.setLowerBound(0);
				before.setUpperBound(this.tdb.getHorizon());
				
				// add constraints to committed
				committed.add(before);
				// propagate constraint
				this.tdb.propagate(before);
				// check temporal feasibility
				this.tdb.verify();
				
			} catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
				
				// not feasible schedule
				feasible = false;
				// log data
				debug("Component [" + this.label + "] temporally unfeasible schedule:\n"
						+ "- potential schedule critical set: " + schedule + "\n");
				
			} finally  {
				
				// retract all committed constraints
				for (BeforeIntervalConstraint before : committed) {
					// retract temporal constraint
					this.tdb.retract(before);
				}
			}
		}
		
		// get feasibility flag
		return feasible;
		
	}
	
//	@Override
//	protected void doComputeFlawSolutions(Flaw flaw) 
//		throws UnsolvableFlawException 
//	{
//		// get detected conflict
//		BinaryDecisionConflict conflict = (BinaryDecisionConflict) flaw;
//
//		// check possible precedence constraints
//		Decision reference = conflict.getDecisions()[0];
//		Decision target = conflict.getDecisions()[1];
//		// create possible solutions
//		DecisionPrecedenceConstraint pc1 = new DecisionPrecedenceConstraint(conflict, reference, target, this.schedulingCost);
//		DecisionPrecedenceConstraint pc2 = new DecisionPrecedenceConstraint(conflict, target, reference, this.schedulingCost);
//		
//		// temporal constraints
//		BeforeIntervalConstraint before1 = null;
//		BeforeIntervalConstraint before2 = null;
//		
//		try {
//
//			// create temporal constraint
//			before1 = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
//			before1.setLowerBound(0);
//			before1.setUpperBound(this.component.getHorizon());
//			before1.setReference(reference.getToken().getInterval());
//			before1.setTarget(target.getToken().getInterval());
//			
//			// propagate interval constraint
//			this.tdb.propagate(before1);
//			// check consistency
//			this.tdb.verify();
//			
//			// add solution and deactivate relation
//			conflict.addSolution(pc1);
//		}
//		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
//			// discard relation
//			debug("Unfeasible precedence constraint:\n"
//					+ "\t- reference: " + reference + "\n"
//					+ "\t- target: " + target + "\n");
//		}
//		finally {
//			
//			// remove constraint
//			if (before1 != null) {
//				// retract constraint
//				this.tdb.retract(before1);
//				// clear constraint
//				before1.clear();
//			}
//		}
//		
//		
//		try {
//			
//			// create temporal constraint
//			before2 = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
//			before2.setLowerBound(0);
//			before2.setUpperBound(this.component.getHorizon());
//			before2.setReference(target.getToken().getInterval());
//			before2.setTarget(reference.getToken().getInterval());
//			
//			// propagate interval constraint
//			this.tdb.propagate(before2);
//			// check consistency
//			this.tdb.verify();
//			
//			// add solution and deactivate relation
//			conflict.addSolution(pc2);
//		}
//		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
//			// discard relation
//			debug("Unfeasible precedence constraint:\n"
//					+ "\t- reference: " + target + "\n"
//					+ "\t- target: " + reference + "\n");
//		}
//		finally {
//			
//			// remove constraint
//			if (before2 != null) {
//				// retract constraint
//				this.tdb.retract(before2);
//				// clear constraint
//				before2.clear();
//			}
//		}
//		
//		
//		// check if any solution has been found
//		if (conflict.getSolutions().isEmpty()) {
//			throw new UnsolvableFlawException("Unsolvable decision conflict on timeline:\n"
//					+ "\t- component: " + this.component.getName() + "\n"
//					+ "\t- decisions: " + conflict.getDecisions()[0] + ", " + conflict.getDecisions()[1] + "\n");
//		}
//	}
}

