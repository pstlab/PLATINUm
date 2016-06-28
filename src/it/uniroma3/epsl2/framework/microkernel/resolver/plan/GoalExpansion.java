package it.uniroma3.epsl2.framework.microkernel.resolver.plan;

import it.uniroma3.epsl2.framework.domain.component.pdb.SynchronizationRule;
import it.uniroma3.epsl2.framework.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public class GoalExpansion extends GoalJustification {

	private SynchronizationRule rule;
	
	/**
	 * 
	 * @param decision
	 * @param rule
	 */
	protected GoalExpansion(Goal goal, SynchronizationRule rule) {
		super(goal, JustificationType.EXPANSION);
		this.rule = rule;
	}
	
	/**
	 * 
	 * @param decision
	 */
	protected GoalExpansion(Goal goal) {
		super(goal, JustificationType.EXPANSION);
		this.rule = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasSubGoals() {
		return this.rule != null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getGoalDecision() {
		return decision;
	}
	
	/**
	 * 
	 * @return
	 */
	public SynchronizationRule getSynchronizationRule() {
		return rule;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[GoalExpansion decision= " + this.decision + " synchronization-rule= " + this.rule + "]";
	}
}
