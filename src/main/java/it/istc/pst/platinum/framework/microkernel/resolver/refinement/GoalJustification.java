package it.istc.pst.platinum.framework.microkernel.resolver.refinement;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;

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
