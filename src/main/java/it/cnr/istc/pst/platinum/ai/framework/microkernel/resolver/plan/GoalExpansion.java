package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.plan;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.SynchronizationRule;

/**
 * 
 * @author anacleto
 *
 */
public class GoalExpansion extends GoalJustification 
{
	private SynchronizationRule rule;
	
	/**
	 * 
	 * @param goal
	 * @param rule
	 * @param cost
	 */
	protected GoalExpansion(Goal goal, SynchronizationRule rule, double cost) {
		super(goal, JustificationType.EXPANSION, cost);
		this.rule = rule;
	}
	
	/**
	 * 
	 * @param goal
	 * @param cost
	 */
	protected GoalExpansion(Goal goal, double cost) {
		super(goal, JustificationType.EXPANSION, cost);
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
		// JSON style object description 
		return "{ "
				+ "\"type\": \"EXPANSION\", "
				+ "\"goal\": " +  this.decision + ", "
				+ "\"rule\": " + this.rule +" }";
	}
}
