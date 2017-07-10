package it.uniroma3.epsl2.framework.microkernel.resolver.planning;

import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;

/**
 * 
 * @author anacleto
 *
 */
public abstract class GoalJustification extends FlawSolution 
{
	protected Decision decision;
	protected JustificationType type; 
	
	/**
	 * 
	 * @param goal
	 * @param type
	 */
	protected GoalJustification(Goal goal, JustificationType type) {
		super(goal);
		this.type = type;
		this.decision = goal.getDecision();
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getGoalDecision() {
		return decision;
	}
	
	/*
	 * 
	 */
	public JustificationType getJustificationType() {
		return type;
	}
	
	/**
	 * 
	 * @author anacleto
	 *
	 */
	enum JustificationType {
		
		/**
		 * 
		 */
		EXPANSION,
		
		/**
		 * 
		 */
		UNIFICATION
	}
}
