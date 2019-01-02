package it.istc.pst.platinum.framework.microkernel.resolver.timeline.behavior.planning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.TransitionNotFoundException;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.domain.component.sv.Transition;
import it.istc.pst.platinum.framework.domain.component.sv.ValuePath;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.EqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.NotEqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.MeetsRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.NotFeasibleGapCompletionException;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintType;
import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.ex.TemporalIntervalCreationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.MeetsIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.IntervalDistanceQuery;
import it.istc.pst.platinum.framework.time.lang.query.IntervalOverlapQuery;

/**
 * 
 * @author anacleto
 *
 */
public final class TimelineBehaviorPlanningResolver extends Resolver<StateVariable> 
{
	/**
	 * 
	 */
	protected TimelineBehaviorPlanningResolver() {
		super(ResolverType.TIMELINE_BEHAVIOR_PLANNING_RESOLVER.getLabel(), 
				ResolverType.TIMELINE_BEHAVIOR_PLANNING_RESOLVER.getFlawTypes());
	}
	
	/**
	 * 
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get the flaw solution to consider
		GapCompletion completion = (GapCompletion) solution;
		// check solution path
		if (completion.getPath().isEmpty()) 
		{
			try 
			{
				// direct token transition between active decisions
				Decision reference = completion.getLeftDecision();
				Decision target = completion.getRightDecision();
				
				// create constraint
				MeetsRelation meets = this.component.create(RelationType.MEETS, reference, target);
				// add created relation
				solution.addCreatedRelation(meets);
				// propagate relation
				this.component.activate(meets);
				// add activated relation to solution
				solution.addActivatedRelation(meets);
				
				// create parameter relations
				Set<Relation> pRels = this.createParameterRelations(reference, target);
				// add created relation
				solution.addCreatedRelations(pRels);
				// propagate relation
				this.component.activate(pRels);
				// add activated relation to solution
				solution.addActivatedRelations(pRels);
			}
			catch (TransitionNotFoundException | RelationPropagationException ex) 
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
					// delete relation
					refComp.delete(rel);
				}
				
				// not feasible solution
				throw new FlawSolutionApplicationException(ex.getMessage());
			}
		}
		else 
		{
			// create the list of the decisions
			List<Decision> transition = new ArrayList<>();
			// add the gap-left decision
			transition.add(completion.getLeftDecision());
			try
			{
				// create intermediate decisions
				for (ComponentValue value : completion.getPath()) 
				{
					// create parameters' labels
					String[] labels = new String[value.getNumberOfParameterPlaceHolders()];
					for (int index = 0; index < labels.length; index++) {
						labels[index] = "label-" + index;
					}
					
					// create pending decision
					Decision dec = this.component.create(value, labels);
					// these decisions can be set as mandatory expansion
					dec.setMandatoryExpansion();
					// add created decision to transition
					transition.add(dec);
					// add pending decision
					solution.addCreatedDecision(dec);
					
					// check if "simple" value
					if (!value.isComplex()) {
						// directly activate decision
						Set<Relation> rels = this.component.activate(dec);
						// add to activated decisions and relations
						solution.addActivatedDecision(dec);
						solution.addActivatedRelations(rels);
					}
				}
				// add the gap-right decision
				transition.add(completion.getRightDecision());
				
				// prepare relations
				for (int index = 0; index < transition.size() - 1; index++) 
				{
					// get adjacent decisions
					Decision reference = transition.get(index);
					Decision target = transition.get(index + 1);
					
					// create pending relation
					MeetsRelation meets = this.component.create(RelationType.MEETS, reference, target);
					// add created relation
					solution.addCreatedRelation(meets);
					// check if the relation can be activated
					if (meets.canBeActivated()) {
						// activate temporal relation
						this.component.activate(meets);
						// add activated relation
						solution.addActivatedRelation(meets);
					}
					
					// create parameter relations
					Set<Relation> pRels = this.createParameterRelations(reference, target);
					for (Relation prel : pRels) {
						// add relation to solution
						solution.addCreatedRelation(prel);
						// check if relation can be activated
						if (prel.canBeActivated()) {
							// activate parameter relation
							this.component.activate(prel);
							// add activated relation
							solution.addActivatedRelation(prel);
						}
					}
				}
			}
			catch (DecisionPropagationException | RelationPropagationException | TransitionNotFoundException ex) 
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
				
				// deactivate activated decisions
				for (Decision dec : solution.getActivatedDecisisons()) {
					// get component
					DomainComponent dComp = dec.getComponent();
					dComp.deactivate(dec);
				}
				
				// delete created decisions 
				for (Decision dec : solution.getCreatedDecisions()) {
					// get component
					DomainComponent dComp = dec.getComponent();
					dComp.free(dec);
				} 
				
				// throw exception
				throw new FlawSolutionApplicationException(ex.getMessage());
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of gaps
		List<Flaw> flaws = new ArrayList<>();
		// get the "ordered" list of tokens on the component
		List<Decision> list = this.component.getActiveDecisions();
		// check gaps between adjacent decisions
		for (int index = 0; index < list.size() - 1; index++) 
		{
			// get two adjacent decisions
			Decision left = list.get(index);
			Decision right = list.get(index + 1);
			// check if scheduled
			IntervalOverlapQuery query = this.tdb.createTemporalQuery(TemporalQueryType.INTERVAL_OVERLAP);
			query.setReference(left.getToken().getInterval());
			query.setTarget(right.getToken().getInterval());
			// process query
			this.tdb.process(query);
			// check if resolver precondition is violated 
			if (query.canOverlap()) 
			{
				// precondition not satisfied
				logger.warning("[Warning] Timeline behavior planning failure:\n"
						+ "- component: " + this.component + "\n"
						+ "- [reason] Behaviors cannot be plant because potentially overlapping tokens have been found:\n"
						+ "\t- reference: " + left + "\n"
						+ "\t- target: " + right + "\n");
				// clear data structures and stop finding flaws
				flaws = new ArrayList<>();
				break;
			}
			
			
			// compute distance between tokens 
			IntervalDistanceQuery distance = this.tdb.
					createTemporalQuery(TemporalQueryType.INTERVAL_DISTANCE);
			// set intervals
			query.setReference(left.getToken().getInterval());
			query.setTarget(right.getToken().getInterval());
			// process query
			this.tdb.process(query);
			
			// check if the tokens are directly connected 
			if (distance.getDistanceLowerBound() == 0 && distance.getDistanceUpperBound() == 0) 
			{
				// there is not actually a gap between the two tokens but semantics must be verified for plan execution 
				boolean connected = false;
				Iterator<Relation> it = this.component.getActiveRelations(left, right).iterator();
				while (it.hasNext() && !connected) {
					// next relation
					Relation rel = it.next();
					// check type
					connected = rel.getType().equals(RelationType.MEETS);
				}
				
				// check if decisions are linked
				if (!connected) {
					// force transition by adding a MEETS constraint between the two tokens
					Gap gap = new Gap(FLAW_COUNTER.getAndIncrement(), this.component, left, right);
					// add "simple gap"
					flaws.add(gap);
					logger.debug("Not a gap has been actually found but transition semantics must be satisfied for execution through a MEETS constraint:\n"
							+ "- componetn: " + this.component + "\n"
							+ "- reference: " + left + "\n"
							+ "- target: " + right + "\n");
					
				}
			}
			else //if(dmin >= 0 && dmax > 0) 
			{
				// we've got a gap between the two tokens
				Gap gap = new Gap(FLAW_COUNTER.getAndIncrement(), this.component, left, right, new long[] {
						distance.getDistanceLowerBound(), 
						distance.getDistanceUpperBound()}
				);
				// add gap
				flaws.add(gap);
				logger.debug("Gap found on component: "
						+ "- component: " + this.component + "\n"
						+ "- reference: " + left + "\n"
						+ "- target: " + right + "\n"
						+ "- distance: [" + distance.getDistanceLowerBound() + ", " + distance.getDistanceUpperBound() + "]\n");
			}
//			else {
//				// not scheduled decisions
//				isScheduled = false;
//			}
		}
		
		// get detected gaps
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawException 
	{
		// get the gap
		Gap gap = (Gap) flaw;
		// check gap type
		switch (gap.getGapType()) 
		{
			// incomplete time-line
			case INCOMPLETE_TIMELINE : 
			{
				// check all (acyclic) paths among tokens
				List<ValuePath> paths = this.component.getPaths(
						gap.getLeftDecision().getValue(), 
						gap.getRightDecision().getValue());
				
				// check found solutions
				if (paths.isEmpty()) {
					// not gap completion found
					logger.debug("Not gap completion found:\n"
							+ "- gap: " + gap + "\n");
				}
				
				// create a solution for each possible path
				for (ValuePath path : paths)
				{
					// get steps
					List<ComponentValue> steps = path.getSteps();
					// remove the source and destination values from the path
					steps.remove(0);
					steps.remove(steps.size() - 1);
					
					/*
					 * TODO: VERIFICARE COMPATIBILITA DURATA PERCORSO TEORICO CON DISTANCE TEMPORALE TRA I TOKEN DEL FLAW
					 */
					
					
					// gap solution
					GapCompletion solution = new GapCompletion(gap, steps);
					// add solution to the flaw
					gap.addSolution(solution);
					// print gap information
					logger.debug("Gap found on component " + this.component.getName() + ":\n"
							+ "- distance: [dmin= " + gap.getDmin() + ", dmax= " +  gap.getDmax() + "] \n"
							+ "- left-side decision: " + gap.getLeftDecision() + "\n"
							+ "- right-side decision: " + gap.getRightDecision() + "\n"
							+ "- solution path: " + path + "\n");
				}
				
//				// check if empty
//				if (!paths.isEmpty())
//				{
//					// consider the subset of paths with the shortest number of steps
//					Collections.sort(paths);
//					// list of "optimal" paths
//					List<ValuePath> optimal = new ArrayList<>();
//					// get the first
//					ValuePath oPath = paths.remove(0);
//					
//					
//					// consider the "shortest paths" only for solving the current flaw
//					optimal.add(oPath);
//					// list of "non-optimal paths"
//					List<ValuePath> nonOptimal = new ArrayList<>();
//					for (ValuePath path : paths) {
//						// check if optimal path
//						if (path.size() == oPath.size()) {
//							optimal.add(path);
//						}
//						else {	// non optimal
//							nonOptimal.add(path);
//						}
//					}
//					
//					// compute solution for "optimal" paths
//					for (ValuePath path : optimal)
//					{
//						// get steps
//						List<ComponentValue> steps = path.getSteps();
//						// remove the source and destination values from the path
//						steps.remove(0);
//						steps.remove(steps.size() - 1);
//						try
//						{
//							// check feasibility of gap solution
//							GapCompletion solution = new GapCompletion(gap, steps);
//							// check temporal feasibility of solution
//							double makespan = this.checkBehaviorTemporalFeasibility(solution);
//							// set the resulting makespan
//							solution.setMakespan(makespan);
//							// add solution to the flaw
//							gap.addSolution(solution);
//							// print gap information
//							logger.debug("Gap found on component " + this.component.getName() + ":\n"
//									+ "- distance: [dmin= " + gap.getDmin() + ", dmax= " +  gap.getDmax() + "] \n"
//									+ "- left-side decision: " + gap.getLeftDecision() + "\n"
//									+ "- right-side decision: " + gap.getRightDecision() + "\n"
//									+ "- solution path: " + path + "\n");
//							
//						}
//						catch (NotFeasibleGapCompletionException ex) {
//							// not feasible gap completion found
//							logger.debug("Not feasible gap completion found:\n"
//									+ "- gap: " + gap + "\n"
//									+ "- solution: " + path + "\n"
//									+ "- message: \"" + ex.getMessage() + "\"\n");
//						}
//					}
					
//					// check if a solution has been found among optimal paths
//					if (!gap.isSolvable()) 
//					{
//						// compute solution for "non optimal" paths
//						for (ValuePath path : nonOptimal)
//						{
//							System.out.println("\n- gap: " + gap + "\n- path: " + path.getSteps() + "\n");
//							
//							// get steps
//							List<ComponentValue> steps = path.getSteps();
//							// remove the source and destination values from the path
//							steps.remove(0);
//							steps.remove(steps.size() - 1);
//							try
//							{
//								// check feasibility of gap solution
//								GapCompletion solution = new GapCompletion(gap, steps);
//								// check temporal feasibility of solution
//								double makespan = this.checkBehaviorTemporalFeasibility(solution);
//								// set the resulting makespan
//								solution.setMakespan(makespan);
//								// add solution to the flaw
//								gap.addSolution(solution);
//								// print gap information
//								logger.debug("Gap found on component " + this.component.getName() + ":\n"
//										+ "- distance: [dmin= " + gap.getDmin() + ", dmax= " +  gap.getDmax() + "] \n"
//										+ "- left-side decision: " + gap.getLeftDecision() + "\n"
//										+ "- right-side decision: " + gap.getRightDecision() + "\n"
//										+ "- solution path: " + path + "\n"
//										+ "- resulting makespan: " + makespan + "\n");
//								
//							}
//							catch (NotFeasibleGapCompletionException ex) {
//								// not feasible gap completion found
//								logger.debug("Not feasible gap completion found:\n"
//										+ "- gap: " + gap + "\n"
//										+ "- solution: " + path + "\n"
//										+ "- message: \"" + ex.getMessage() + "\"\n");
//							}
//						}
//					}
//				}
			}
			break;
		
			// semantic connection missing
			case SEMANTIC_CONNECTION : 
			{
				// direct connection between decisions
				GapCompletion solution = new GapCompletion(gap, new ArrayList<ComponentValue>());
				
				
//				try
//				{
					// check solution feasibility
//					double makespan = this.checkBehaviorTemporalFeasibility(solution);
					// set the makespan
//					solution.setMakespan(makespan);
				
				
				// add gap solution
				gap.addSolution(solution);
				
				
//				}
//				catch (NotFeasibleGapCompletionException ex) {
//					logger.debug("Not feasible gap completion found:\n"
//							+ "- gap: " + gap + "\n"
//							+ "- solution: " + solution + "\n"
//							+ "- message: \""+ ex.getMessage() + "\"\n");
//				}
			}
			break;
		}
		
		// check if solvable
		if (!flaw.isSolvable()) {
			throw new UnsolvableFlawException("Unsolvable gap found on component " + this.component.getName() + ":\n" + flaw);
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 * @throws TransitionNotFoundException
	 */
	private Set<Relation> createParameterRelations(Decision reference, Decision target) 
			throws TransitionNotFoundException 
	{
		// relations
		Set<Relation> rels = new HashSet<>();
		// get transition between values
		Transition t = this.component.getTransition(reference.getValue(), target.getValue());
		
		// reference parameter position index
		int referenceParameterIndex = 0;
		while (referenceParameterIndex < reference.getParameterLabels().length) {
			
			// target parameter position index
			int targetParameterIndex = 0;
			while (targetParameterIndex < target.getParameterLabels().length) {
				
				// check if a parameter constraint exists
				if (t.existsParameterConstraint(referenceParameterIndex, targetParameterIndex)) {
					// get parameter constraint type
					ParameterConstraintType pConsType = t.getParameterConstraintType(referenceParameterIndex, targetParameterIndex);
					
					// add (local) pending parameter constraint
					switch (pConsType) {
					
						// equal parameter constraint
						case EQUAL : {
							
							// create (pending) local relation
							EqualParameterRelation equal = (EqualParameterRelation) this.component.create(RelationType.EQUAL_PARAMETER, reference, target);
							// set reference parameter label
							equal.setReferenceParameterLabel(reference.getParameterLabelByIndex(referenceParameterIndex));
							// set target parameter label
							equal.setTargetParameterLabel(target.getParameterLabelByIndex(targetParameterIndex));
							// add create relation to solution
							rels.add(equal);
						}
						break;
						
						// not equal parameter
						case NOT_EQUAL : {
							
							// create (pending) local relation
							NotEqualParameterRelation notEqual = (NotEqualParameterRelation) this.component.create(RelationType.NOT_EQUAL_PARAMETER, reference, target);
							// set reference parameter label
							notEqual.setReferenceParameterLabel(reference.getParameterLabelByIndex(referenceParameterIndex));
							notEqual.setTargetParameterLabel(target.getParameterLabelByIndex(targetParameterIndex));
							// add create relation to solution
							rels.add(notEqual);
						}
						break;
						
						default : {
							
							/*
							 * TODO: <<<<----- VERIFICARE SE VANNO GESTITI ALTRI TIPI DI VINCOLI
							 */
							
							throw new RuntimeException("Unknown parameter constraint type in state variable transition " + pConsType);
						}
					}
				}
				
				// next target parameter index
				targetParameterIndex++;
			}
			
			// next index
			referenceParameterIndex++;
		}
		
		// get created relations
		return rels;
	}
	
//	/**
//	 * 
//	 * @param completion
//	 * @return
//	 * @throws NotFeasibleGapCompletionException
//	 */
//	private double checkBehaviorTemporalFeasibility(GapCompletion completion) 
//			throws NotFeasibleGapCompletionException
//	{
//		// initialize makespan
//		double makespan = Double.MIN_VALUE + 1;
//		// list of committed relations
//		List<TemporalConstraint> committedConstraints = new ArrayList<>();
//		List<TemporalInterval> committedIntervals = new ArrayList<>();
//		try
//		{
//			// check solution path
//			if (completion.getPath().isEmpty()) 
//			{
//				// direct token transition between active decisions
//				Decision reference = completion.getLeftDecision();
//				Decision target = completion.getRightDecision();
//				
//				// create temporal constraint
//				MeetsIntervalConstraint meets = this.tdb.createTemporalConstraint(TemporalConstraintType.MEETS);
//				// set reference 
//				meets.setReference(reference.getToken().getInterval());
//				meets.setTarget(target.getToken().getInterval());
//				
//				// propagate constraint and check consistency
//				this.tdb.propagate(meets);
//				// add to committed constraint
//				committedConstraints.add(meets);
//				// check temporal feasibility
//				this.tdb.verify();
//				
////				// feasible solution, compute the resulting makespan
////				ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
////				this.tdb.process(query);
//				// set the resulting makespan
////				makespan = query.getMakespan();
//			}
//			else 
//			{
//				// prepare the sequence of temporal intervals
//				List<TemporalInterval> intervals = new ArrayList<>();
//				// add left interval
//				intervals.add(completion.getLeftDecision().getToken().getInterval());
//				
//				// create intermediate intervals
//				for (ComponentValue value : completion.getPath()) 
//				{
//					// create temporal interval
//					TemporalInterval i = this.tdb.createTemporalInterval(value.getDurationBounds(), value.isControllable());
//					// add intermediate interval
//					intervals.add(i);
//					// add committed interval
//					committedIntervals.add(i);
//				}
//				// add last interval
//				intervals.add(completion.getRightDecision().getToken().getInterval());
//							
//				// try to propagate temporal relations between adjacent intervals
//				for (int index = 0; index < intervals.size() - 1; index++) 
//				{
//					// get adjacent decisions
//					TemporalInterval i = intervals.get(index);
//					TemporalInterval j = intervals.get(index + 1);
//					// create and activate temporal constraint
//					MeetsIntervalConstraint meets = this.tdb.createTemporalConstraint(TemporalConstraintType.MEETS);
//					// set reference
//					meets.setReference(i);
//					meets.setTarget(j);
//					
//					// propagate temporal constraint
//					this.tdb.propagate(meets);
//					// add to committed constraints
//					committedConstraints.add(meets);
//				}
//				
//				// check temporal feasibility
//				this.tdb.verify();
////				// feasible solution, compute the resulting makespan
////				ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
////				this.tdb.process(query);
////				// set the resulting makespan
////				makespan = query.getMakespan();
//			}
//		}
//		catch (TemporalIntervalCreationException | TemporalConstraintPropagationException | ConsistencyCheckException ex) {
//			// throw exception
//			throw new NotFeasibleGapCompletionException(ex.getMessage());
//		}
//		finally 
//		{
//			// clear data structure if necessary
//			for (TemporalConstraint constraint : committedConstraints) {
//				// retract temporal constraint
//				this.tdb.retract(constraint);
//			}
//			
//			// clear data structure if necessary
//			for (TemporalInterval interval : committedIntervals) {
//				// delete temporal intervals and related time points
//				this.tdb.deleteTemporalInterval(interval);
//			}
//		}
//		
//		// get computed makespan
//		return makespan;
//	}
}
