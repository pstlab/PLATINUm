package it.istc.pst.platinum.framework.microkernel.resolver.refinement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.pdb.ParameterSynchronizationConstraint;
import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBaseComponent;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationConstraint;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationRule;
import it.istc.pst.platinum.framework.domain.component.pdb.TemporalSynchronizationConstraint;
import it.istc.pst.platinum.framework.domain.component.pdb.TokenVariable;
import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.EqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.NotEqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.ParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.AfterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.BeforeRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.ContainsRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.DuringRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.EndsDuringRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.StartsDuringRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.NotFeasibleExpansionException;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.NotFeasibleUnificationException;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.ex.TemporalIntervalCreationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;
import it.istc.pst.platinum.framework.time.lang.allen.AfterIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.BeforeIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.ContainsIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.DuringIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.EndsDuringIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.EqualsIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.MeetsIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.MetByIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.allen.StartsDuringIntervalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.ComputeMakespanQuery;

/**
 * 
 * @author anacleto
 *
 */
public class PlanRefinementResolver <T extends PlanDataBaseComponent> extends Resolver 
{
	@ComponentPlaceholder
	protected T component;
	
	/**
	 * 
	 */
	protected PlanRefinementResolver() {
		super(ResolverType.PLAN_REFINEMENT.getLabel(), ResolverType.PLAN_REFINEMENT.getFlawType());
	}
	
	/**
	 * 
	 */
	@Override
	protected List<Flaw> doFindFlaws() 
	{
		// list of goals
		List<Flaw> flaws = new ArrayList<>();
		// check pending decisions
		for (Decision decision : this.component.getPendingDecisions()) 
		{
			// add sub-goal
			Goal goal = new Goal(this.component, decision);
			// check if external component
			if (decision.getComponent().isExternal()) {
				// set mandatory unification
				goal.setMandatoryUnification();
			}
			
			// add goal to flaws
			flaws.add(goal);
		}
		// get detected flaws
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	protected void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException 
	{
		// get goal
		Goal goal = (Goal) flaw;
		// check solving information
		if (!goal.isMandatoryUnification()) {
			// compute expansion solutions
			this.doComputeExpansionSolutions(goal);
		}
		
		// check solving information
		if (!goal.isMandatoryExpansion()) {
			// compute unification solutions
			this.doComputeUnificationSolutions(goal);
		}
		
		// check if solvable
		if (!flaw.isSolvable()) {
			// simply throw exception
			throw new UnsolvableFlawFoundException("Unsolvable flaw found on component " + this.component.getName() + ":"
					+ "\n" + flaw + "\n");
		}
	}
	
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	@Override
	protected void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get goal justification
		GoalJustification just = (GoalJustification) solution;
		// check type 
		switch (just.getJustificationType()) 
		{
			// expansion step
			case EXPANSION : {
				// apply solution
				this.doApplyExpansion((GoalExpansion) just);
			}
			break;
			
			// unification step
			case UNIFICATION : {
				// apply solution
				this.doApplyUnification((GoalUnification) just);
			}
			break;
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution solution) 
			throws Exception 
	{
		// get goal justification
		GoalJustification just = (GoalJustification) solution;
		// check type 
		switch (just.getJustificationType())
		{
			// restore expansion
			case EXPANSION : {
				// restore solution
				this.doRestoreExpansion((GoalExpansion) just);
			}
			break;
			
			// restore unification
			case UNIFICATION : {
				// restore unification
				this.doRestoreUnification((GoalUnification) just);
			}
			break;
		}
	}
	
	/**
	 * 
	 * @param solution
	 * @throws RelationPropagationException
	 */
	private void doRestoreUnification(GoalUnification solution) 
			throws RelationPropagationException
	{
		// get original goal
		Decision goal = solution.getGoalDecision();
		// get unification decision
		Decision unif = solution.getUnificationDecision();
		
		// get relations to translate
		Set<Relation> toTranslate = solution.getToTranslate();
		// translate goal relations
		for (Relation rel : toTranslate) {
			// translate relation
			this.translateRelationFromGoalToUnification(unif, goal, rel);
		}
		
		// get to activate relations
		Set<Relation> toActivate = new HashSet<>(solution.getActivatedRelations());
		try	
		{
			// remove original goal: PENDING -> SILENT
			this.component.delete(goal);
			// activate relations
			this.component.add(toActivate);
		}
		catch (RelationPropagationException ex) 
		{
			// translated back relations
			for (Relation rel : toTranslate) {
				this.translateRelationFromUnificationToOriginalGoal(unif, goal, rel);
			}
			
			// restore goal: SILENT -> PENDING
			this.component.restore(goal);
			// not feasible solution
			throw new RelationPropagationException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	private void doRestoreExpansion(GoalExpansion solution) 
			throws Exception 
	{
		// restore created decisions
		for (Decision dec : solution.getCreatedDecisions()) {
			// restore decision
			this.component.restore(dec);
		}
		
		// restore created relations
		for (Relation rel : solution.getCreatedRelations()) {
			// restore relation
			this.component.restore(rel);
		}
		
		// committed decisions and relations
		List<Decision> commitDecs = new ArrayList<>();
		List<Relation> commitRels = new ArrayList<>();
		
		try
		{
			// get activated decisions
			List<Decision> dActivated = solution.getActivatedDecisisons();
			// get activated relations
			List<Relation> rActivated = solution.getActivatedRelations();
			
			// activate decisions
			for (Decision dec : dActivated) 
			{
				// activate decision
				List<Relation> list = new ArrayList<>(this.component.add(dec));
				// add committed relations and decisions
				commitRels.addAll(list);
				commitDecs.add(dec);
				// remove from activated relations if needed
				rActivated.removeAll(list);
			}
			
			// activate missing relations
			for (Relation rel : rActivated) {
				// activate relation
				this.component.add(rel);
				// add to committed
				commitRels.add(rel);
			}
		}
		catch (DecisionPropagationException | RelationPropagationException ex) 
		{
			// deactivate committed relations
			for (Relation relation : commitRels) {
				// deactivate relation
				this.component.delete(relation);
			}
			
			// deactivate committed decisions
			for (Decision decision : commitDecs) {
				// deactivate decision
				this.component.delete(decision);
			}
			
			// remove created relations
			for (Relation relation : solution.getCreatedRelations()) {
				// remove relation
				this.component.free(relation);
			}
			
			// remove created decisions
			for (Decision decision : solution.getCreatedDecisions()) {
				// move decision to SILENT
				this.component.delete(decision);
			}

			// error while resetting flaw solution
			throw new Exception("Error while resetting goal expansion solution:\n"
					+ "- goal expansion: " + solution + "\n");
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void doRetract(FlawSolution solution) 
	{
		// check solution type
		GoalJustification justif = (GoalJustification) solution;
		switch (justif.type) 
		{
			// retract expansion
			case EXPANSION : {
				// do retract expansion
				this.doRetractExpansion((GoalExpansion) justif);
			}
			break;
			
			// retract unification
			case UNIFICATION : {
				// do retract unification
				this.doRetractUnification((GoalUnification) justif);
			}
			break;
		}
	}
	
	/**
	 * 
	 * @param goal
	 */
	private void doComputeUnificationSolutions(Goal goal) 
	{ 
		// get goal-related component
		DomainComponent<?> component = goal.getComponent();
		// search active decisions compatible for unification
		for (Decision unif : component.getActiveDecisions()) 
		{
			// check decisions' values
			if (unif.getValue().equals(goal.getDecision().getValue())) 
			{
				try
				{
					// check unification feasibility
					double makespan = this.checkUnificationTemporalFeasibility(goal.getDecision(), unif);
					// if everything goes right we've found a possible unification
					GoalUnification unification = new GoalUnification(goal, unif);
					// set the resulting makespan
					unification.setMakespan(makespan);
					// add solution
					goal.addSolution(unification);
					this.logger.debug("Feasible unification found:\n"
							+ "- planning goal: " + goal + "\n"
							+ "- unification decision: " + unification + "\n"
							+ "- resulting makespan: " + makespan + "\n");
				}
				catch (NotFeasibleUnificationException ex) {
					this.logger.debug("Not feasible goal unification found:\n"
							+ "- planning goal: " + goal + "\n"
							+ "- message: \"" + ex.getMessage() + "\"\n");
				}
			}
		}
	}
	
	/**
	 * 
	 * @param goal
	 * @param decision
	 * @return
	 * @throws NotFeasibleUnificationException
	 */
	private double checkUnificationTemporalFeasibility(Decision goal,  Decision decision) 
			throws NotFeasibleUnificationException
	{
		// initialize the makespan
		double makespan = Double.MIN_VALUE + 1;
		// list of propagated temporal constraints
		List<TemporalConstraint> committed = new ArrayList<>();
		try 
		{
			// get all (pending) relations concerning the goal
			List<Relation> pending = this.component.getRelations(goal);
			// get unification decision temporal interval
			TemporalInterval i = decision.getToken().getInterval();
			
			// propagate "translated" constraints
			for (Relation relation : pending)
			{
				// check temporal relations concerning the goal
				if (relation.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT) && 
						(this.component.isActive(relation.getReference()) || 
								this.component.isActive(relation.getTarget()))) 
				{
					// check relation type
					switch (relation.getType()) 
					{
						case AFTER: 
						{
							AfterRelation after = (AfterRelation) relation;
							// create temporal constraint
							AfterIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.AFTER);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setLowerBound(after.getLowerBound());
							constraint.setUpperBound(after.getUpperBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						case BEFORE: 
						{
							BeforeRelation before = (BeforeRelation) relation;
							// create temporal constraint
							BeforeIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setLowerBound(before.getLowerBound());
							constraint.setUpperBound(before.getUpperBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						case CONTAINS: 
						{
							ContainsRelation contains = (ContainsRelation) relation;
							// create temporal constraint
							ContainsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.CONTAINS	);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setStartTimeBound(contains.getStartTimeBound());
							constraint.setEndTimeBound(contains.getEndTimeBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						case DURING: 
						{
							DuringRelation during = (DuringRelation) relation;
							// create temporal constraint
							DuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.DURING);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setStartTimeBound(during.getStartTimeBound());
							constraint.setEndTimeBound(during.getEndTimeBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						case ENDS_DURING: 
						{
							EndsDuringRelation endsduring = (EndsDuringRelation) relation;
							// create temporal constraint
							EndsDuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.ENDS_DURING);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setFirstBound(endsduring.getFirstBound());
							constraint.setSecondBound(endsduring.getSecondBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						case EQUALS: 
						{
							// create temporal constraint
							EqualsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.EQUALS);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						case MEETS: 
						{
							// create temporal constraint
							MeetsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.MEETS);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						case MET_BY: 
						{
							// create temporal constraint
							MetByIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.MET_BY);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						case STARTS_DURING: 
						{
							StartsDuringRelation startsduring = (StartsDuringRelation) relation;
							// create temporal constraint
							StartsDuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.STARTS_DURING);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setFirstBound(startsduring.getFirstBound());
							constraint.setSecondBound(startsduring.getSecondBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committed.add(constraint);
							break;
						}
						default: {
							throw new RuntimeException("Unknownw relation type: " + relation.getType() + "\n");
						}
					}
				}
			}
			
			// check temporal consistency
			this.tdb.checkConsistency();
			// feasible solution, compute the resulting makespan
			ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
			this.tdb.process(query);
			// set the resulting makespan
			makespan = query.getMakespan();
		} 
		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// not feasible unification
			throw new NotFeasibleUnificationException("Not feasible unification found:\n"
					+ "- planning goal: " + goal + "\n"
					+ "- unification decision: " + decision + "\n");
		}
		finally 
		{
			// delete all committed temporal constraints
			for (TemporalConstraint constraint : committed) {
				// delete constraint
				this.tdb.retract(constraint);
			}
		}
		
		// get computed makespan
		return makespan;
	}
	
	/**
	 * 
	 * @param goal
	 * @return
	 * @throws NotFeasibleExpansionException
	 */
	private double checkExpansionTemporalFeasibility(Decision goal) 
			throws NotFeasibleExpansionException
	{
		// initialize the makespan
		double makespan = Double.MIN_VALUE + 1;
		// list of committed intervals
		List<TemporalInterval> committedIntervals = new ArrayList<>();
		List<TemporalConstraint> committedConstraints = new ArrayList<>();
		try
		{
			// create a temporal interval
			TemporalInterval i = this.tdb.createTemporalInterval(goal.getDuration(), goal.isControllable());
			// propagate temporal interval
			committedIntervals.add(i);
			
			// check pending decision of the plan
			List<Relation> pending = this.component.getPendingRelations(goal);
			// try to propagate related temporal constraints
			for (Relation relation : pending) 
			{
				// check temporal relations concerning the goal
				if (relation.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT) && 
						(this.component.isActive(relation.getReference()) || 
								this.component.isActive(relation.getTarget()))) 
				{
					// check relation type
					switch (relation.getType()) 
					{
						case AFTER: 
						{
							AfterRelation after = (AfterRelation) relation;
							// create temporal constraint
							AfterIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.AFTER);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setLowerBound(after.getLowerBound());
							constraint.setUpperBound(after.getUpperBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						case BEFORE: 
						{
							BeforeRelation before = (BeforeRelation) relation;
							// create temporal constraint
							BeforeIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setLowerBound(before.getLowerBound());
							constraint.setUpperBound(before.getUpperBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						case CONTAINS: 
						{
							ContainsRelation contains = (ContainsRelation) relation;
							// create temporal constraint
							ContainsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.CONTAINS	);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setStartTimeBound(contains.getStartTimeBound());
							constraint.setEndTimeBound(contains.getEndTimeBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						case DURING: 
						{
							DuringRelation during = (DuringRelation) relation;
							// create temporal constraint
							DuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.DURING);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setStartTimeBound(during.getStartTimeBound());
							constraint.setEndTimeBound(during.getEndTimeBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						case ENDS_DURING: 
						{
							EndsDuringRelation endsduring = (EndsDuringRelation) relation;
							// create temporal constraint
							EndsDuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.ENDS_DURING);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setFirstBound(endsduring.getFirstBound());
							constraint.setSecondBound(endsduring.getSecondBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						case EQUALS: 
						{
							// create temporal constraint
							EqualsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.EQUALS);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						case MEETS: 
						{
							// create temporal constraint
							MeetsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.MEETS);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						case MET_BY: 
						{
							// create temporal constraint
							MetByIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.MET_BY);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						case STARTS_DURING: 
						{
							StartsDuringRelation startsduring = (StartsDuringRelation) relation;
							// create temporal constraint
							StartsDuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.STARTS_DURING);
							// check reference
							if (relation.getReference().equals(goal)) {
								constraint.setReference(i);
								constraint.setTarget(relation.getTarget().getToken().getInterval());
							}
							
							// check target
							if (relation.getTarget().equals(goal)) {
								constraint.setReference(relation.getReference().getToken().getInterval());
								constraint.setTarget(i);
							}
							
							// set bounds
							constraint.setFirstBound(startsduring.getFirstBound());
							constraint.setSecondBound(startsduring.getSecondBound());
							// propagate temporal constraint
							this.tdb.propagate(constraint);
							// add to committed 
							committedConstraints.add(constraint);
							break;
						}
						default: {
							throw new RuntimeException("Unknownw relation type: " + relation.getType() + "\n");
						}
					}
				}
			}
			
		
			// check consistency
			this.tdb.checkConsistency();
			// feasible solution, compute the resulting makespan
			ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
			this.tdb.process(query);
			// set the resulting makespan
			makespan = query.getMakespan();
		}
		catch (TemporalIntervalCreationException | TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			throw new NotFeasibleExpansionException(ex.getMessage());
		}
		finally 
		{
			// remove propagated constraints
			for (TemporalConstraint constraint : committedConstraints) {
				this.tdb.retract(constraint);
			}
			// remove propagated intervals
			for (TemporalInterval interval : committedIntervals) {
				this.tdb.deleteTemporalInterval(interval);
			}
		}
		
		// get computed makespan
		return makespan;
	}
	
	/**
	 * 
	 * @param goal
	 */
	private void doComputeExpansionSolutions(Goal goal) 
	{
		try 
		{
			// check feasibility of goal expansion
			double makespan = this.checkExpansionTemporalFeasibility(goal.getDecision());
			// check synchronization rules
			List<SynchronizationRule> rules = this.component.getSynchronizationRules(goal.getDecision().getValue());
			// check synchronizations
			if (rules.isEmpty()) 
			{
				// the goal can be justified without applying synchronization rules
				GoalExpansion expansion = new GoalExpansion(goal);
				// set the resulting makespan
				expansion.setMakespan(makespan);
				// add solution
				goal.addSolution(expansion);
				this.logger.debug("Simple goal found:\n"
						+ "- planning goal: " + goal.getDecision() + "\n"
						+ "- resulting makespan: " + makespan + "\n");
			}
			else 
			{
				// can do expansion
				for (SynchronizationRule rule : rules) 
				{
					// expansion solution
					GoalExpansion expansion = new GoalExpansion(goal, rule);
					// set the resulting makespan
					expansion.setMakespan(makespan);
					// add solution
					goal.addSolution(expansion);
					this.logger.debug("Complex goal found:\n"
							+ "- planning goal: " + goal.getDecision() + "\n"
							+ "- synchronization rule: " + rule + "\n"
							+ "- resulting makespan: " + makespan + "\n");
				}
			}
		}
		catch (NotFeasibleExpansionException ex) {
			this.logger.debug("Not feasible goal expansion found:\n"
					+ "- planning goal: " + goal + "\n"
					+ "- message: \"" + ex.getMessage() + "\"\n");
		}
	}
	
	/**
	 * 
	 * @param unification
	 * @throws Exception
	 */
	private void doApplyUnification(GoalUnification unification) 
			throws FlawSolutionApplicationException 
	{
		// get original goal
		Decision goal = unification.getGoalDecision();
		// get unifying decision
		Decision unif = unification.getUnificationDecision();
		
		// get all (pending) relations concerning the planning goal
		List<Relation> toTranslate = this.component.getRelations(goal);
		// translate pending relations by replacing goal's information with unification decision's information
		for (Relation rel : toTranslate) {
			// translate relation
			this.translateRelationFromGoalToUnification(unif, goal, rel);
		}
		
		// get to activate relations after translation
		Set<Relation> toActivate = new HashSet<>(this.component.getToActivateRelations(unif));
		try	
		{
			// remove original goal: PENDING -> SILENT
			this.component.delete(goal);
			// activate relations
			this.component.add(toActivate);
			// add activated relations
			unification.addActivatedRelations(toActivate);
			// add translated pending relations as created
			unification.setTranslatedRelations(toTranslate);
		}
		catch (RelationPropagationException ex) 
		{
			// restore goal: SILENT -> PENDING
			this.component.restore(goal);
			// translated back relations
			for (Relation rel : toTranslate) {
				this.translateRelationFromUnificationToOriginalGoal(unif, goal, rel);
			}
			
			// not feasible solution
			throw new FlawSolutionApplicationException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param expansion
	 * @throws Exception
	 */
	private void doApplyExpansion(GoalExpansion expansion) 
			throws FlawSolutionApplicationException 
	{
		// get goal
		Decision goal = expansion.getGoalDecision();
		// list of created decisions (i.e. introduced subgoals)
		Set<Decision> dCreated = new HashSet<>();
		// list of created relations (i.e. pending relations)
		Set<Relation> rCreated = new HashSet<>();
		
		// check rule
		if (expansion.hasSubGoals()) 
		{
			// get synchronization rule to apply
			SynchronizationRule rule = expansion.getSynchronizationRule();
			// create an index of token variable
			Map<TokenVariable, Decision> var2dec = new HashMap<>();
			var2dec.put(rule.getTriggerer(), goal);
			// add pending decisions
			for (TokenVariable var : rule.getTokenVariables()) 
			{
				// create a pending decision
				Decision pending = this.component.create(
						var.getValue(),
						var.getParameterLabels());
				
				// set causal link
				pending.setCausalLink(goal);
				// check solving knowledge
				if (var.isMandatoryExpansion()) {
					pending.setMandatoryExpansion();
				}

				// check solving knowledge
				if (var.isMandatoryUnificaiton()) {
					pending.setMandatoryUnification();
				}
				
				// add entry to cache
				var2dec.put(var, pending);
				// add created decision
				dCreated.add(pending);
			}
			
			// add pending relations
			for (SynchronizationConstraint c : rule.getConstraints()) 
			{
				// check category
				switch (c.getCategory()) 
				{
					// temporal category
					case TEMPORAL_CONSTRAINT : 
					{ 
						// temporal constraint
						TemporalSynchronizationConstraint tc = (TemporalSynchronizationConstraint) c;
						// get decisions
						Decision reference = var2dec.get(tc.getSource());
						Decision target = var2dec.get(tc.getTarget());
						// create pending relation
						TemporalRelation rel = (TemporalRelation) this.component.create(tc.getType(), reference, target);
						rel.setBounds(tc.getBounds());
						// add created relation
						rCreated.add(rel);
					}
					break;
					
					// parameter category
					case PARAMETER_CONSTRAINT: 
					{
						// parameter constraint
						ParameterSynchronizationConstraint pc = (ParameterSynchronizationConstraint) c;
						// get decisions
						Decision reference = var2dec.get(pc.getSource());
						Decision target = var2dec.get(pc.getTarget());
						
						// create pending relation
						ParameterRelation rel = (ParameterRelation) this.component.create(pc.getType(), reference, target);
						
						// check parameter relation type
						switch (rel.getType())
						{
							// bind parameter relation
							case BIND_PARAMETER : 
							{
								// bind constraint
								BindParameterRelation bind = (BindParameterRelation) rel;
								// set binding value
								bind.setValue(pc.getTargetLabel());
								// set reference label
								if (pc.getSource().equals(rule.getTriggerer())) 
								{
									// get trigger label index
									int index = rule.getTriggerer().getParameterIndexByLabel(pc.getReferenceLabel());
									// set decision's label
									String label = goal.getParameterLabelByIndex(index);
									// set label
									bind.setReferenceParameterLabel(label);
								}
								else {
									bind.setReferenceParameterLabel(pc.getReferenceLabel());
								}
							}
							break;
							
							// equal parameter relation
							case EQUAL_PARAMETER : 
							{
								// get relation
								EqualParameterRelation eq = (EqualParameterRelation) rel;
								
								// check if source is the trigger
								if (pc.getSource().equals(rule.getTriggerer())) 
								{
									// get trigger label index
									int index = rule.getTriggerer().getParameterIndexByLabel(pc.getReferenceLabel());
									// get decions's label
									String label = goal.getParameterLabelByIndex(index);
									// set label
									eq.setReferenceParameterLabel(label);
								}
								else {
									// directly set the label
									eq.setReferenceParameterLabel(pc.getReferenceLabel());
								}
								
								// check if target is the trigger
								if (pc.getTarget().equals(rule.getTriggerer())) 
								{
									// get trigger label index
									int index = rule.getTriggerer().getParameterIndexByLabel(pc.getTargetLabel());
									// get decision's label
									String label = goal.getParameterLabelByIndex(index);
									// set label
									eq.setTargetParameterLabel(label);
								}
								else {
									// directly set the label
									eq.setTargetParameterLabel(pc.getTargetLabel());
								}
							}
							break;
							
							// not-equal parameter relation
							case NOT_EQUAL_PARAMETER : 
							{
								// get relation
								NotEqualParameterRelation neq = (NotEqualParameterRelation) rel;
								
								// check if source is the trigger
								if (pc.getSource().equals(rule.getTriggerer())) 
								{
									// get trigger label index
									int index = rule.getTriggerer().getParameterIndexByLabel(pc.getReferenceLabel());
									// get decions's label
									String label = goal.getParameterLabelByIndex(index);
									// set label
									neq.setReferenceParameterLabel(label);
								}
								else {
									// directly set the label
									neq.setReferenceParameterLabel(pc.getReferenceLabel());
								}
								
								// check if target is the trigger
								if (pc.getTarget().equals(rule.getTriggerer())) 
								{
									// get trigger label index
									int index = rule.getTriggerer().getParameterIndexByLabel(pc.getTargetLabel());
									// get decision's label
									String label = goal.getParameterLabelByIndex(index);
									// set label
									neq.setTargetParameterLabel(label);
								}
								else {
									// directly set the label
									neq.setTargetParameterLabel(pc.getTargetLabel());
								}
							}
							break;
							
							default : {
								throw new RuntimeException("Unknown parameter constraint type - " + rel.getType());
							}
						}
						
						// add created relation
						rCreated.add(rel);
					}
				}
			}
		}
		
		try 
		{
			// activate decision
			Set<Relation> rActivated = this.component.add(goal);
			// set information to flaw solution
			expansion.addActivatedDecision(goal);
			expansion.addCreatedDecisions(dCreated);
			expansion.addActivatedRelations(rActivated);
			// remove activated relations if any
			rCreated.removeAll(rActivated);
			// add resulting created relations (pending relations)
			expansion.addCreatedRelations(rCreated);
		}
		catch (DecisionPropagationException ex) 
		{
			// delete created decisions
			for (Decision pending : dCreated) {
				// delete 
				this.component.delete(pending);
			}
			
			// delete created relations
			for (Relation relation : rCreated) {
				// free relation
				this.component.free(relation);
			}
			
			// throw exception
			throw new FlawSolutionApplicationException(ex.getMessage());
		}
	}


	/**
	 * 
	 * @param expansion
	 */
	private void doRetractExpansion(GoalExpansion expansion) 
	{
		// deactivate activated relations
		for (Relation relation : expansion.getActivatedRelations()) {
			// deactivate relation
			this.component.delete(relation);
		}
		
		// remove created relations
		for (Relation relation : expansion.getCreatedRelations()) {
			// completely remove relation
			this.component.free(relation);
		}
		
		// delete activated decisions: ACTIVE -> PENDING
		for (Decision dec : expansion.getActivatedDecisisons()) {
			// deactivate decision
			this.component.delete(dec);
		}
		
		// delete pending decisions: PENDING -> SILENT
		for (Decision dec : expansion.getCreatedDecisions()) {
			// delete pending decisions
			this.component.delete(dec);
		}
	}
	
	/**
	 * 
	 * @param unification
	 */
	private void doRetractUnification(GoalUnification unification) 
	{
		// original goal 
		Decision goal = unification.getGoalDecision();
		// unification decision 
		Decision unif = unification.getUnificationDecision();
		
		// deactivate activated relations
		for (Relation rel : unification.getActivatedRelations()) {
			this.component.delete(rel);
		}
		
		// translate back relations
		for (Relation rel : unification.getToTranslate()) {
			// translate relation
			this.translateRelationFromUnificationToOriginalGoal(unif, goal, rel);
		}
		
		// restore original planning goal
		this.component.restore(goal);
	}
	
	/**
	 * 
	 * @param unification
	 * @param goal
	 * @param rel
	 */
	private void translateRelationFromGoalToUnification(Decision unification, Decision goal, Relation rel) 
	{
		// check relation category
		switch (rel.getCategory()) 
		{
			// manage temporal constraint translation
			case TEMPORAL_CONSTRAINT : 
			{
				// check and set reference
				if (rel.getReference().equals(goal)) {
					// update reference
					rel.setReference(unification);
				}
				
				// check and set target
				if (rel.getTarget().equals(goal)) {
					// update target
					rel.setTarget(unification);
				}
			}
			break;
			
			// manage parameter constraint translation
			case PARAMETER_CONSTRAINT : 
			{
				// get relation
				ParameterRelation pRel = (ParameterRelation) rel;
				
				// translate reference if needed
				if (pRel.getReference().equals(goal)) {
					// update reference
					pRel.setReference(unification);
					// update reference label
					int index = goal.getParameterIndexByLabel(pRel.getReferenceParameterLabel());
					// set reference parameter label
					pRel.setReferenceParameterLabel(unification.getParameterLabelByIndex(index));
				}
				
				
				// check relation type
				switch (pRel.getType())
				{
					// bind parameter relation
					case BIND_PARAMETER : {
						// get relation
						BindParameterRelation bind = (BindParameterRelation) pRel;
						bind.setTarget(bind.getReference());
					}
					break;
					
					// equal parameter relation
					case EQUAL_PARAMETER : 
					{
						// get relation
						EqualParameterRelation eq = (EqualParameterRelation) pRel;
						// binary relation - translate target if needed 
						if (pRel.getTarget().equals(goal)) 
						{
							// update target
							eq.setTarget(unification);
							// update target label
							int index = goal.getParameterIndexByLabel(eq.getTargetParameterLabel());
							// set target label
							eq.setTargetParameterLabel(unification.getParameterLabelByIndex(index));
						}
					}
					break;
					
					// not equal parameter relation
					case NOT_EQUAL_PARAMETER : 
					{
						// get relation
						NotEqualParameterRelation neq = (NotEqualParameterRelation) pRel;
						// binary relation - translate target if needed 
						if (neq.getTarget().equals(goal)) 
						{
							// update target
							neq.setTarget(unification);
							// update target label
							int index = goal.getParameterIndexByLabel(neq.getTargetParameterLabel());
							// set target label
							neq.setTargetParameterLabel(unification.getParameterLabelByIndex(index));
						}
					}
					break;
					
					default : {
						throw new RuntimeException("Unknown parameter relation type " + pRel.getType());
					}
				}
			}
			break;
		}
	}
	
	/**
	 * 
	 * @param unification
	 * @param goal
	 * @param rel
	 */
	private void translateRelationFromUnificationToOriginalGoal(Decision unification, Decision goal, Relation rel) 
	{
		// check relation type
		switch (rel.getCategory()) 
		{
			// translating temporal relation
			case TEMPORAL_CONSTRAINT : 
			{
				// update references
				if (rel.getReference().equals(unification)) {
					// set reference decision
					rel.setReference(goal);
				}
	
				// update target
				if (rel.getTarget().equals(unification)) {
					rel.setTarget(goal);
				}
			}
			break;
			
			// translate parameter relation
			case PARAMETER_CONSTRAINT : 
			{
				// get relation
				ParameterRelation pRel = (ParameterRelation) rel;
				// update reference if needed
				if (pRel.getReference().equals(unification)) 
				{
					// set reference
					pRel.setReference(goal);
					// get unification reference parameter label index
					int index = unification.getParameterIndexByLabel(pRel.getReferenceParameterLabel());
					// set goal reference parameter label
					pRel.setReferenceParameterLabel(goal.getParameterLabelByIndex(index));
				}
				
				// check parameter relation type
				switch (pRel.getType())
				{
					// bind parameter relation
					case BIND_PARAMETER : 
					{
						// bind constraint
						BindParameterRelation bind = (BindParameterRelation) pRel;
						// set the target
						bind.setTarget(bind.getReference());
					}
					break;
					
					// equal parameter relation
					case EQUAL_PARAMETER : 
					{
						// get relation
						EqualParameterRelation eq = (EqualParameterRelation) pRel;
						// binary parameter relation - update target reference if needed
						if (eq.getTarget().equals(unification)) 
						{
							// set target
							eq.setTarget(goal);
							// get unification target parameter label index
							int index = unification.getParameterIndexByLabel(eq.getTargetParameterLabel());
							// set goal target parameter label
							eq.setTargetParameterLabel(goal.getParameterLabelByIndex(index));
						}
					}
					break;
					
					// not-equal parameter relation
					case NOT_EQUAL_PARAMETER : 
					{
						// get relation
						NotEqualParameterRelation neq = (NotEqualParameterRelation) pRel;
						// binary parameter relation - update target reference if needed
						if (neq.getTarget().equals(unification)) 
						{
							// set target
							neq.setTarget(goal);
							// get unification target parameter label index
							int index = unification.getParameterIndexByLabel(neq.getTargetParameterLabel());
							// set goal target parameter label
							neq.setTargetParameterLabel(goal.getParameterLabelByIndex(index));
						}
					}
					break;
					
					default : {
						throw new RuntimeException("Unknown parameter relation type - " + rel.getType());
					}
				}
			}
			break;
		}
	}
}
