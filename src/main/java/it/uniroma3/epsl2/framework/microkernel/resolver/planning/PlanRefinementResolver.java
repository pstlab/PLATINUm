package it.uniroma3.epsl2.framework.microkernel.resolver.planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.domain.component.pdb.ParameterSynchronizationConstraint;
import it.uniroma3.epsl2.framework.domain.component.pdb.PlanDataBaseComponent;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationConstraint;
import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationRule;
import it.uniroma3.epsl2.framework.domain.component.pdb.TemporalSynchronizationConstraint;
import it.uniroma3.epsl2.framework.domain.component.pdb.TokenVariable;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.BindParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.EqualParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.NotEqualParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.ParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.temporal.TemporalRelation;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentReference;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
public class PlanRefinementResolver <T extends PlanDataBaseComponent> extends Resolver 
{
	@ComponentReference
	protected T component;
	
	/**
	 * 
	 */
	protected PlanRefinementResolver() {
		super(ResolverType.PLAN_REFINEMENT);
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
		for (Decision decision : this.component.getPendingDecisions()) {
			
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
			throws UnsolvableFlawFoundException {

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
			throw new UnsolvableFlawFoundException("Unsolvable flaw found on component " + this.component.getName() + ":\n" + flaw);
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
	 * @throws Exception
	 */
	private void doRestoreUnification(GoalUnification solution) 
			throws Exception
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
			// restore goal: SILENT -> PENDING
			this.component.restore(goal);
			// translated back relations
			for (Relation rel : toTranslate) {
				this.translateRelationFromUnificationToOriginalGoal(unif, goal, rel);
			}
			
			// not feasible solution
			throw new Exception(ex.getMessage());
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
				
				throw new Exception("Error while resetting flaw solution:\n- " + solution + "\n");
			}
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
		DomainComponent component = goal.getComponent();
		// get decision
		Decision goalDecision = goal.getDecision();

		// get (all) relations related to the goal
		List<Relation> toTranslate = this.component.getRelations(goalDecision);
		// search active decisions compatible for unification
		for (Decision unif : component.getActiveDecisions()) 
		{
			// check decisions' values
			if (unif.getValue().equals(goalDecision.getValue())) 
			{
				// prepare constraints to propagate
				for (Relation rel : toTranslate) {
					// translate relation from goal to unification
					this.translateRelationFromGoalToUnification(unif, goalDecision, rel);
				}
				
				// try to propagate constraints
				List<Relation> activated = new ArrayList<>();
				try 
				{
					// propagate to activate relations
					for (Relation rel : this.component.getToActivateRelations(unif)) {
						// activate relation
						this.component.add(rel);
						activated.add(rel);
					}
					
					// check temporal consistency
					this.tdb.checkConsistency();
					// check parameter consistency
					this.pdb.checkConsistency();
					
					// if everything goes right we've found a possible unification
					GoalUnification unification = new GoalUnification(goal, unif);
					// add solved goal
					unification.addSolvedGoal(goal.getDecision().getValue());
					goal.addSolution(unification);
					this.logger.debug("It is possible to unify goal= " + goalDecision + " with decision= " + unif + "\n");
				} 
				catch (RelationPropagationException | ConsistencyCheckException ex) {
					// not feasible unification
					this.logger.debug("Cannot unify/merge goal= " + goalDecision + " with decision= " + unif + "\n");
				}
				finally 
				{
					// deactivate activated relations
					for (Relation rel : activated) {
						// delete relation
						this.component.delete(rel);
					}
					
					// translate back all translated relations
					for (Relation rel : toTranslate) {
						// translate back relation
						this.translateRelationFromUnificationToOriginalGoal(unif, goalDecision, rel);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param goal
	 */
	private void doComputeExpansionSolutions(Goal goal) 
	{
		// check synchronization rules
		List<SynchronizationRule> rules = this.component.
				getSynchronizationRules(goal.getDecision().getValue());
		// check synchronizations
		if (rules.isEmpty()) 
		{
			// the goal can be justified without applying synchronization rules
			GoalExpansion expansion = new GoalExpansion(goal);
			// add solved goal
			expansion.addSolvedGoal(goal.getDecision().getValue());
			// add solution
			goal.addSolution(expansion);
			this.logger.debug("Found simple pending goal decision\n- goal= " + goal.getDecision() + "\n");
		}
		else 
		{
			// can do expansion
			for (SynchronizationRule rule : rules) 
			{
				// expansion solution
				GoalExpansion expansion = new GoalExpansion(goal, rule);
				// add solved goal
				expansion.addSolvedGoal(goal.getDecision().getValue());
				// add subgoals
				for (TokenVariable var : rule.getTokenVariables()) {
					expansion.addCreatedSubGoal(var.getValue());
				}
				// add solution
				goal.addSolution(expansion);
				this.logger.debug("Found goal decision\n- goal= " + goal.getDecision() + "\n-rule = " + rule + "\n");
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
		
		// get relations to translate
		List<Relation> toTranslate = this.component.getRelations(goal);
		// translate goal relations
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
		Set<Decision> dCreated = new HashSet<>();
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
			expansion.addCreatedRelations(rCreated);
		}
		catch (DecisionPropagationException ex) {
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
		// deactivate relations
		for (Relation rel : expansion.getActivatedRelations()) {
			this.component.delete(rel);
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
		// merged decision 
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
