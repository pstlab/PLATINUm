package it.istc.pst.platinum.framework.microkernel.resolver.plan;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;

/**
 * 
 * @author anacleto
 *
 */
public class GoalUnification extends GoalJustification 
{
	private Decision unification;								// merging decision
	private Set<Relation> translatedReferenceGoalRelations;		
	private Set<Relation> translatedTargetGoalRelations;
	
//	private Set<Relation> toTranslate;		// list of translated relations
	
	/**
	 * 
	 * @param goal
	 * @param dec
	 */
	protected GoalUnification(Goal goal, Decision dec, double cost) {
		super(goal, JustificationType.UNIFICATION, cost);
		this.unification = dec;
//		this.toTranslate = new HashSet<>();
		this.translatedReferenceGoalRelations = new HashSet<>();
		this.translatedTargetGoalRelations = new HashSet<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Relation> getTranslatedReferenceGoalRelations() {
		return new HashSet<>(translatedReferenceGoalRelations);
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void setTranslatedReferenceGoalRelation(Collection<Relation> rels) {
		this.translatedReferenceGoalRelations = new HashSet<>(rels);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Relation> getTranslatedTargetGoalRelations() {
		return new HashSet<>(translatedTargetGoalRelations);
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void setTranslatedTargetGoalRealtion(Collection<Relation> rels) {
		this.translatedTargetGoalRelations = new HashSet<>(rels);
	}
	
	/**
	 * 
	 * @param goal
	 */
	protected void setGoal(Decision goal) {
		this.decision = goal;
	}
	
	/**
	 * Get the (active) decision the goal can merge with
	 * 
	 * @return
	 */
	public Decision getUnificationDecision() {
		return this.unification;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style object description
		return "{ "
				+ "\"type\": \"UNIFICATION\", "
				+ "\"goal\": " + this.decision + ", "
				+ "\"decision\": " + this.unification + " }";
	}
}
