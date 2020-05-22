package it.istc.pst.platinum.framework.microkernel.resolver.plan;

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
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationConstraint;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationRule;
import it.istc.pst.platinum.framework.domain.component.pdb.TemporalSynchronizationConstraint;
import it.istc.pst.platinum.framework.domain.component.pdb.TokenVariable;
import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.EqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.NotEqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.ParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.microkernel.resolver.plan.GoalJustification.JustificationType;
import it.istc.pst.platinum.framework.parameter.ex.ParameterConstraintPropagationException;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.time.ex.TemporalConstraintPropagationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class PlanRefinementResolver extends Resolver<DomainComponent>
{	
	/**
	 * 
	 */
	protected PlanRefinementResolver() {
		super(ResolverType.PLAN_REFINEMENT.getLabel(), 
				ResolverType.PLAN_REFINEMENT.getFlawTypes());
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
			Goal goal = new Goal(FLAW_COUNTER.getAndIncrement(), this.component, decision);
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
			throws UnsolvableFlawException 
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
			throw new UnsolvableFlawException("Unsolvable flaw found on component " + this.component.getName() + ":"
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
		// check if unification 
		if (just.getJustificationType().equals(JustificationType.UNIFICATION)) {
			// restore unification
			this.doRestoreUnification((GoalUnification) just);
		}
		else {
			// "standard" way of restoring a flaw solution
			super.doRestore(solution);
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
		
		// list of committed relations
		Set<Relation> rCommitted = new HashSet<>();
		try	
		{
			// get component decision 
			DomainComponent goalComp = goal.getComponent();
			// remove original goal: PENDING -> SILENT
			goalComp.free(goal);
			// activate-back relations
			for (Relation rel : solution.getActivatedRelations()) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				// activate relations
				refComp.activate(rel);
				// add to committed
				rCommitted.add(rel);
			}
		}
		catch (RelationPropagationException ex) 
		{
			// deactivate committed relations
			for (Relation rel : rCommitted) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				refComp.deactivate(rel);
			}
			
			// translated back relations
			for (Relation rel : toTranslate) {
				this.translateRelationFromUnificationToOriginalGoal(unif, goal, rel);
			}
			
			// restore goal: SILENT -> PENDING
			DomainComponent goalComp = goal.getComponent();
			goalComp.restore(goal);
			// not feasible solution
			throw new RelationPropagationException(ex.getMessage());
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
		// check if unification solution
		if (justif.getJustificationType().equals(JustificationType.UNIFICATION)) {
			// special management of unification
			this.doRetractUnification((GoalUnification) solution);
		}
		else {
			// "standard" management of flaw solution
			super.doRetract(solution);
		}
	}
	
	/**
	 * 
	 * @param goal
	 */
	private void doComputeUnificationSolutions(Goal goal) 
	{ 
		// search active decisions compatible for unification
		for (Decision unif : this.component.getActiveDecisions()) 
		{
			// check predicate and temporal unification
			if (this.isPredicateUnificationFeasible(goal.getDecision(), unif) && 
					this.isTemporalUnificationFeasible(goal.getDecision(), unif))
			{
				// possible unification found
				GoalUnification unification = new GoalUnification(goal, unif);
				// add unification solution
				goal.addSolution(unification);
				info("Feasible unification found:\n"
						+ "- planning goal: " + goal + "\n"
						+ "- unification decision: " + unification + "\n");
			}
			else {
				// unification not feasible
				info("No feasible unification:\n"
						+ "- planning goal: " + goal + "\n"
						+ "- decision : \"" + unif + "\"\n");
			}
		}
	}
	
	/**
	 * 
	 * @param goal
	 * @param decision
	 * @return
	 */
	private boolean isPredicateUnificationFeasible(Decision goal, Decision decision) 
	{
		// feasibility flag
		boolean feasible = true;
		// first check if the decisions refer to the same values
		if (!decision.getValue().equals(goal.getValue())) {
			// not feasible unification
			feasible = false;
			debug("Not feasible predicate unification:\n"
					+ "- planning goal: " + goal + "\n"
					+ "- unification decision: " + decision + "\n"); 
		}
		else 
		{
			// list of committed parameter constraints
			List<ParameterConstraint> committed = new ArrayList<>();
			// list of translated parameter relations - reference
			List<ParameterRelation> translatedReferenceGoalRelations = new ArrayList<>();
			// list of translated parameter relations - target
			List<ParameterRelation> translatedTargetGoalRelations = new ArrayList<>();
			try
			{
				// get goal component
				DomainComponent goalComp = goal.getComponent();
				// get all (pending) relation concerning the goal decision
				Set<Relation> pending = goalComp.getRelations(goal);
				// check relations
				for (Relation rel : pending)
				{
					// check parameter constraint type
					if (rel.getCategory().equals(ConstraintCategory.PARAMETER_CONSTRAINT))
					{
						// check relation type
						switch (rel.getType())
						{
							// bind parameter
							case BIND_PARAMETER: 
							{
								// the goal can be only the reference of the relation
								ParameterRelation pRel = (ParameterRelation) rel;
								// get relation reference parameter label
								String refParamLabel = pRel.getReferenceParameterLabel();
								// get label index
								int refParameterIndex = pRel.getReference().getParameterIndexByLabel(refParamLabel);
								// get unification decision parameter label
								String label = decision.getParameterLabelByIndex(refParameterIndex);

								// update reference decision 
								pRel.setReference(decision);
								// update reference label of the relation 
								pRel.setReferenceParameterLabel(label);
								// add relation to the list of translated ones
								translatedReferenceGoalRelations.add(pRel);

								// create parameter constraint
								ParameterConstraint cons = pRel.create();
								// propagate constraint
								this.pdb.propagate(cons);
								// add constraint to committed list
								committed.add(cons);
							}
							break;
							
							case EQUAL_PARAMETER : 
							{
								// get parameter relation
								EqualParameterRelation eqRel = (EqualParameterRelation) rel;
								// check if the goal is the reference or the parameter constraint 
								if (eqRel.getReference().equals(goal))
								{
									// get relation reference parameter label
									String refParamLabel = eqRel.getReferenceParameterLabel();
									// get label index
									int refParameterIndex = eqRel.getReference().getParameterIndexByLabel(refParamLabel);
									// get unification decision parameter label
									String label = decision.getParameterLabelByIndex(refParameterIndex);

									// update reference decision 
									eqRel.setReference(decision);
									// update reference label of the relation 
									eqRel.setReferenceParameterLabel(label);
									// add relation to the list of translated ones
									translatedReferenceGoalRelations.add(eqRel);

									
									// check if both decisions are active after translation in order to check constraint feasibility
									if (eqRel.getReference().getComponent().isActive(eqRel.getReference()) && 
											eqRel.getTarget().getComponent().isActive(eqRel.getTarget()))
									{
										// create parameter constraint
										ParameterConstraint cons = eqRel.create();
										// propagate constraint
										this.pdb.propagate(cons);
										// add constraint to committed list
										committed.add(cons);
									}
								}
								else // the goal is the target of the relation 
								{
									// get relation reference parameter label
									String refParamLabel = eqRel.getTargetParameterLabel();
									// get label index
									int refParameterIndex = eqRel.getTarget().getParameterIndexByLabel(refParamLabel);
									// get unification decision parameter label
									String label = decision.getParameterLabelByIndex(refParameterIndex);

									// update reference decision 
									eqRel.setTarget(decision);
									// update reference label of the relation 
									eqRel.setTargetParameterLabel(label);
									// add relation to the list of translated ones
									translatedTargetGoalRelations.add(eqRel);

									if (eqRel.getReference().getComponent().isActive(eqRel.getReference()) && 
											eqRel.getTarget().getComponent().isActive(eqRel.getTarget()))
									{
										// create parameter constraint
										ParameterConstraint cons = eqRel.create();
										// propagate constraint
										this.pdb.propagate(cons);
										// add constraint to committed list
										committed.add(cons);
									}
								}
							}
							break;
							
							case NOT_EQUAL_PARAMETER : 
							{
								// get parameter relation
								NotEqualParameterRelation neqRel = (NotEqualParameterRelation) rel;
								// check if the goal is the reference or the parameter constraint 
								if (neqRel.getReference().equals(goal))
								{
									// get relation reference parameter label
									String refParamLabel = neqRel.getReferenceParameterLabel();
									// get label index
									int refParameterIndex = neqRel.getReference().getParameterIndexByLabel(refParamLabel);
									// get unification decision parameter label
									String label = decision.getParameterLabelByIndex(refParameterIndex);

									// update reference decision 
									neqRel.setReference(decision);
									// update reference label of the relation 
									neqRel.setReferenceParameterLabel(label);
									// add relation to the list of translated ones
									translatedReferenceGoalRelations.add(neqRel);

									if (neqRel.getReference().getComponent().isActive(neqRel.getReference()) && 
											neqRel.getTarget().getComponent().isActive(neqRel.getTarget()))
									{
										// create parameter constraint
										ParameterConstraint cons = neqRel.create();
										// propagate constraint
										this.pdb.propagate(cons);
										// add constraint to committed list
										committed.add(cons);
									}
								}
								else // the goal is the target of the relation 
								{
									// get relation reference parameter label
									String refParamLabel = neqRel.getTargetParameterLabel();
									// get label index
									int refParameterIndex = neqRel.getTarget().getParameterIndexByLabel(refParamLabel);
									// get unification decision parameter label
									String label = decision.getParameterLabelByIndex(refParameterIndex);

									// update reference decision 
									neqRel.setTarget(decision);
									// update reference label of the relation 
									neqRel.setTargetParameterLabel(label);
									// add relation to the list of translated ones
									translatedTargetGoalRelations.add(neqRel);

									if (neqRel.getReference().getComponent().isActive(neqRel.getReference()) && 
											neqRel.getTarget().getComponent().isActive(neqRel.getTarget()))
									{
										// create parameter constraint
										ParameterConstraint cons = neqRel.create();
										// propagate constraint
										this.pdb.propagate(cons);
										// add constraint to committed list
										committed.add(cons);
									}
								}
							}
							break;
							
							
							default:
								// unknown parameter relation
								throw new RuntimeException("Unknown Parameter relation type : " + rel.getType() + "\n");
						}
					}
				}
				
				// check consistency of the plan
				this.pdb.verify();
			}
			catch (ConsistencyCheckException | ParameterConstraintPropagationException ex) {
				// not feasible 
				feasible = false;
				// not feasible unification
				debug("Not feasible predicate unification:\n"
						+ "- planning goal: " + goal + "\n"
						+ "- unification decision: " + decision + "\n");
			}
			finally 
			{
				// retract all committed parameter constraints
				for (ParameterConstraint constraint : committed) {
					// delete parameter constraint
					this.pdb.retract(constraint);
				}
				
				// translated back parameter relations
				for (ParameterRelation rel : translatedReferenceGoalRelations)
				{
					// get relation reference parameter label
					String refParamLabel = rel.getReferenceParameterLabel();
					// get label index
					int pIndex = rel.getReference().getParameterIndexByLabel(refParamLabel);
					// get goal decision parameter label
					String label = goal.getParameterLabelByIndex(pIndex);
					
					// update relation
					rel.setReference(goal);
					rel.setReferenceParameterLabel(label);
					// clear relation constraint
					rel.clear();
				}
				
				
				// translated back parameter relations
				for (ParameterRelation rel : translatedTargetGoalRelations)
				{
					// check relation
					switch (rel.getType())
					{
						case EQUAL_PARAMETER : 
						{
							// get equal relation
							EqualParameterRelation eqRel = (EqualParameterRelation) rel;
							// get relation reference parameter label
							String tarParamLabel = eqRel.getTargetParameterLabel();
							// get label index
							int pIndex = eqRel.getTarget().getParameterIndexByLabel(tarParamLabel);
							// get goal decision parameter label
							String label = goal.getParameterLabelByIndex(pIndex);
							
							// update relation
							eqRel.setTarget(goal);
							eqRel.setTargetParameterLabel(label);
							// clear relation constraint
							eqRel.clear();
						}
						break;
							
						case NOT_EQUAL_PARAMETER : 
						{
							// get equal relation
							NotEqualParameterRelation neqRel = (NotEqualParameterRelation) rel;
							// get relation reference parameter label
							String tarParamLabel = neqRel.getTargetParameterLabel();
							// get label index
							int pIndex = neqRel.getTarget().getParameterIndexByLabel(tarParamLabel);
							// get goal decision parameter label
							String label = goal.getParameterLabelByIndex(pIndex);
							
							// update relation
							neqRel.setTarget(goal);
							neqRel.setTargetParameterLabel(label);
							// clear relation constraint
							neqRel.clear();
						}
						break;
							
						default:
							// unknown parameter relation
							throw new RuntimeException("Unknown Parameter relation type : " + rel.getType() + "\n"); 
							
					}
				}
			}
		}
		
		// get feasibility flag
		return feasible;
	}
	
	/**
	 * 
	 * @param goal
	 * @param decision
	 * @return
	 */
	private boolean isTemporalUnificationFeasible(Decision goal,  Decision decision) 
	{
		// feasibility flag
		boolean feasible = true;
		
		// list of propagated temporal constraints
		List<TemporalConstraint> committed = new ArrayList<>();
		// list of translated parameter relations - reference
		List<TemporalRelation> translatedReferenceGoalRelations = new ArrayList<>();
		// list of translated parameter relations - target
		List<TemporalRelation> translatedTargetGoalRelations = new ArrayList<>();
		try 
		{
			// get goal component
			DomainComponent goalComp = goal.getComponent();
			// get all (pending) relations concerning the goal
			Set<Relation> pending = goalComp.getRelations(goal);
			// translate goal-related relations and propagate them to check the temporal feasibility of the unification
			for (Relation relation : pending)
			{
				// check relation category
				if (relation.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT))
				{
					// get temporal relation
					TemporalRelation tRel = (TemporalRelation) relation;
					// check the reference of the relation
					if (tRel.getReference().equals(goal)) 
					{
						// update the reference with unification decision
						tRel.setReference(decision);
						// add relation to translated ones
						translatedReferenceGoalRelations.add(tRel);
					}
					
					// check target
					if (tRel.getTarget().equals(goal)) 
					{
						// update the relation target with unification decision
						tRel.setTarget(decision);
						// add relation to translated ones
						translatedTargetGoalRelations.add(tRel);
					}
					
					
					// check if relation decisions are both active
					if (tRel.getReference().getComponent().isActive(tRel.getReference()) &&
							tRel.getTarget().getComponent().isActive(tRel.getTarget()))
					{
						// create constraint
						TemporalConstraint constraint = tRel.create();
						// propagate temporal constraint
						this.tdb.propagate(constraint);
						// add to committed 
						committed.add(constraint);
					}
				}
			}
			
			// check temporal consistency of the plan
			this.tdb.verify();
		} 
		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
			// not feasible unification 
			feasible = false;
			// not feasible unification
			debug("Not feasible temporal unification:\n"
					+ "- planning goal: " + goal + "\n"
					+ "- unification decision: " + decision + "\n");
		}
		finally 
		{
			// retract all committed temporal constraints
			for (TemporalConstraint constraint : committed) {
				// delete temporal constraint
				this.tdb.retract(constraint);
			}
			
			// translated back temporal relations
			for (TemporalRelation rel : translatedReferenceGoalRelations)
			{
				// update relation
				rel.setReference(goal);
				// clear relation constraint
				rel.clear();
			}
			
			// translated back temporal relations
			for (TemporalRelation rel : translatedTargetGoalRelations)
			{
				// update relation
				rel.setTarget(goal);
				// clear relation constraint
				rel.clear();
			}
		}
		
		// get feasibility flag
		return feasible;
		
		
//		// list of propagated temporal constraints
//		List<TemporalConstraint> committed = new ArrayList<>();
//		try 
//		{
//			// get goal component
//			DomainComponent goalComp = goal.getComponent();
//			// get all (pending) relations concerning the goal
//			Set<Relation> pending = goalComp.getRelations(goal);
//			// get unification decision temporal interval
//			TemporalInterval i = decision.getToken().getInterval();
//			
//			// translate goal-related relations and propagate them to check the temporal feasibility of the unification
//			for (Relation relation : pending)
//			{
//				// get reference component
//				DomainComponent refComp = relation.getReference().getComponent();
//				// get target component
//				DomainComponent tarComp = relation.getTarget().getComponent();
//				// check temporal relations concerning the goal
//				if (relation.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT) && 
//						(refComp.isActive(relation.getReference()) || 
//								tarComp.isActive(relation.getTarget()))) 
//				{
//					// check relation type
//					switch (relation.getType()) 
//					{
//						case AFTER: 
//						{
//							AfterRelation after = (AfterRelation) relation;
//							// create temporal constraint
//							AfterIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.AFTER);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// set bounds
//							constraint.setLowerBound(after.getLowerBound());
//							constraint.setUpperBound(after.getUpperBound());
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case BEFORE: 
//						{
//							BeforeRelation before = (BeforeRelation) relation;
//							// create temporal constraint
//							BeforeIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.BEFORE);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// set bounds
//							constraint.setLowerBound(before.getLowerBound());
//							constraint.setUpperBound(before.getUpperBound());
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case CONTAINS: 
//						{
//							ContainsRelation contains = (ContainsRelation) relation;
//							// create temporal constraint
//							ContainsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.CONTAINS);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// set bounds
//							constraint.setStartTimeBound(contains.getStartTimeBound());
//							constraint.setEndTimeBound(contains.getEndTimeBound());
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case DURING: 
//						{
//							DuringRelation during = (DuringRelation) relation;
//							// create temporal constraint
//							DuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.DURING);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// set bounds
//							constraint.setStartTimeBound(during.getStartTimeBound());
//							constraint.setEndTimeBound(during.getEndTimeBound());
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case ENDS_DURING: 
//						{
//							EndsDuringRelation endsduring = (EndsDuringRelation) relation;
//							// create temporal constraint
//							EndsDuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.ENDS_DURING);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// set bounds
//							constraint.setFirstBound(endsduring.getFirstBound());
//							constraint.setSecondBound(endsduring.getSecondBound());
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case EQUALS: 
//						{
//							// create temporal constraint
//							EqualsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.EQUALS);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case MEETS: 
//						{
//							// create temporal constraint
//							MeetsIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.MEETS);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case MET_BY: 
//						{
//							// create temporal constraint
//							MetByIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.MET_BY);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case STARTS_DURING: 
//						{
//							StartsDuringRelation startsduring = (StartsDuringRelation) relation;
//							// create temporal constraint
//							StartsDuringIntervalConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.STARTS_DURING);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i);
//								constraint.setTarget(relation.getTarget().getToken().getInterval());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval());
//								constraint.setTarget(i);
//							}
//							
//							// set bounds
//							constraint.setFirstBound(startsduring.getFirstBound());
//							constraint.setSecondBound(startsduring.getSecondBound());
//							// propagate temporal constraint
//							this.tdb.propagate(constraint);
//							// add to committed 
//							committed.add(constraint);
//						}
//						break;
//						
//						case START_START : 
//						{
//							// get relation
//							StartStartRelation ss = (StartStartRelation) relation;
//							// create temporal constraint
//							TimePointDistanceConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.TIME_POINT_DISTANCE);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i.getStartTime());
//								constraint.setTarget(relation.getTarget().getToken().getInterval().getStartTime());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval().getStartTime());
//								constraint.setTarget(i.getStartTime());
//							}
//							
//							// set bounds
//							constraint.setDistanceLowerBound(ss.getLowerBound());
//							constraint.setDistanceUpperBound(ss.getUpperBound());
//							// propagate
//							this.tdb.propagate(constraint);
//							// add to committed
//							committed.add(constraint);
//						}
//						break;
//						
//						case END_END : 
//						{
//							// get relation
//							EndEndRelation ee = (EndEndRelation) relation;
//							// create temporal constraint
//							TimePointDistanceConstraint constraint = this.tdb.createTemporalConstraint(TemporalConstraintType.TIME_POINT_DISTANCE);
//							// check reference
//							if (relation.getReference().equals(goal)) {
//								constraint.setReference(i.getEndTime());
//								constraint.setTarget(relation.getTarget().getToken().getInterval().getEndTime());
//							}
//							
//							// check target
//							if (relation.getTarget().equals(goal)) {
//								constraint.setReference(relation.getReference().getToken().getInterval().getEndTime());
//								constraint.setTarget(i.getEndTime());
//							}
//							
//							// set bounds
//							constraint.setDistanceLowerBound(ee.getLowerBound());
//							constraint.setDistanceUpperBound(ee.getUpperBound());
//							// propagate
//							this.tdb.propagate(constraint);
//							// add to committed
//							committed.add(constraint);
//						}
//						break;
//						
//						default: {
//							throw new RuntimeException("Unknownw relation type: " + relation.getType() + "\n");
//						}
//					}
//				}
//			}
//			
//			// check temporal consistency of the plan
//			this.tdb.verify();
//		} 
//		catch (TemporalConstraintPropagationException | ConsistencyCheckException ex) {
//			// not feasible unification
//			throw new NotFeasibleUnificationException("Not feasible unification found:\n"
//					+ "- planning goal: " + goal + "\n"
//					+ "- unification decision: " + decision + "\n");
//		}
//		finally 
//		{
//			// retract all committed temporal constraints
//			for (TemporalConstraint constraint : committed) {
//				// delete temporal constraint
//				this.tdb.retract(constraint);
//			}
//		}
		
		
	}
	
	/**
	 * 
	 * @param goal
	 */
	private void doComputeExpansionSolutions(Goal goal) 
	{
		// check synchronization rules
		List<SynchronizationRule> rules = this.component.getSynchronizationRules(goal.getDecision().getValue());
		// check synchronizations
		if (rules.isEmpty()) 
		{
			// the goal can be justified without applying synchronization rules
			GoalExpansion expansion = new GoalExpansion(goal);
			// add decision being activated
			expansion.addDecisionToPartialPlan(goal.getDecision());
			
			// add solution
			goal.addSolution(expansion);
			// print debug message
			debug("Simple goal found no synchronization is triggered after expansion:\n"
					+ "- planning goal: " + goal.getDecision() + "\n");
		}
		else 
		{
			// can do expansion
			for (SynchronizationRule rule : rules) 
			{
				// expansion solution
				GoalExpansion expansion = new GoalExpansion(goal, rule);
				// add decision being activated
				expansion.addDecisionToPartialPlan(goal.getDecision());
				// check rule body and add next goals
				for (TokenVariable var : rule.getTokenVariables()) {
					// add goal to the agenda
					expansion.addGoalToAgenda(var.getValue());
				}
				
				// add solution
				goal.addSolution(expansion);
				// print debug message
				debug("Complex goal found:\n"
						+ "- planning goal: " + goal.getDecision() + "\n"
						+ "- synchronization rule: " + rule + "\n");
			}
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
		DomainComponent goalComp = goal.getComponent();
		// set translated pending relations as created
		unification.setTranslatedRelations(goalComp.getRelations(goal));
		// translate pending relations by replacing goal's information with unification decision's information
		for (Relation rel : unification.getToTranslate()) {
			// translate relation
			this.translateRelationFromGoalToUnification(unif, goal, rel);
		}
		
		try	
		{
			// remove original goal: PENDING -> SILENT
			goalComp.free(goal);
			// activate relations
			for (Relation rel : goalComp.getToActivateRelations(unif)) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				// activate relation
				refComp.activate(rel);
				// add activated relation
				unification.addActivatedRelation(rel);
			}
		}
		catch (RelationPropagationException ex) 
		{
			// deactivate activated relations
			for (Relation rel : unification.getActivatedRelations()) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				refComp.deactivate(rel);
			}
			
			// restore goal: SILENT -> PENDING
			goalComp.restore(goal);
			// translated back relations
			for (Relation rel : unification.getToTranslate()) {
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
				// get target component
				DomainComponent target = var.getValue().getComponent(); 
				// create a pending decision
				Decision pending = target.create(
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
				expansion.addCreatedDecision(pending);
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
						// get reference component
						DomainComponent refComp = reference.getComponent();
						// create pending relation
						TemporalRelation rel = refComp.create(tc.getType(), reference, target);
						// set bounds
						rel.setBounds(tc.getBounds());
						// add created relation
						expansion.addCreatedRelation(rel);
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
						// get reference component
						DomainComponent refComp = reference.getComponent();
						// create pending relation
						ParameterRelation rel = (ParameterRelation) refComp.create(pc.getType(), reference, target);
						
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
						expansion.addCreatedRelation(rel);
					}
				}
			}
		}
		
		try 
		{
			// get goal component
			DomainComponent goalComp = goal.getComponent();
			// activate decision
			Set<Relation> list = goalComp.activate(goal);
			// add goal to activated decisions
			expansion.addActivatedDecision(goal);
			// add to activated relations
			expansion.addActivatedRelations(list);
		}
		catch (DecisionPropagationException ex) 
		{
			// deactivate activated relations
			for (Relation rel : expansion.getActivatedRelations()) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				refComp.deactivate(rel);
			}
			
			// deactivate activated decisions
			for (Decision dec : expansion.getActivatedDecisisons()) {
				// get component
				DomainComponent refComp = dec.getComponent();
				refComp.deactivate(dec);
			}
			
			// delete created relations
			for (Relation rel : expansion.getCreatedRelations()) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				refComp.delete(rel);
			}
			
			// delete created decisions 
			for (Decision dec : expansion.getCreatedDecisions()) {
				// get component
				DomainComponent comp = dec.getComponent();
				comp.free(dec);
			}
			
			// throw exception
			throw new FlawSolutionApplicationException(ex.getMessage());
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
			// get reference component
			DomainComponent refComp = rel.getReference().getComponent();
			refComp.deactivate(rel);
		}
		
		// translate back relations
		for (Relation rel : unification.getToTranslate()) {
			// translate relation
			this.translateRelationFromUnificationToOriginalGoal(unif, goal, rel);
		}

		// get goal component
		DomainComponent goalComp = goal.getComponent();
		// restore original planning goal
		goalComp.restore(goal);
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
