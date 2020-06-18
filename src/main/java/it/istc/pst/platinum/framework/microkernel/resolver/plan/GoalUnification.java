package it.istc.pst.platinum.framework.microkernel.resolver.plan;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.utils.properties.FilePropertyReader;

/**
 * 
 * @author anacleto
 *
 */
public class GoalUnification extends GoalJustification 
{
	private Decision unification;		// merging decision
	private Set<Relation> toTranslate;	// list of translated relations
	
	/**
	 * 
	 * @param goal
	 * @param dec
	 */
	protected GoalUnification(Goal goal, Decision dec) {
		super(goal, JustificationType.UNIFICATION);
		this.unification = dec;
		this.toTranslate = new HashSet<>();
	}
	
	/**
	 * 
	 */
	@Override
	public double getCost() {
		// get property file 
		FilePropertyReader property = FilePropertyReader.getDeliberativePropertyFile();
		// read property
		String cost = property.getProperty("unification-cost");
		// parse and get double value
		return Double.parseDouble(cost);
	}
	
	/**
	 * 
	 * @param translated
	 */
	public void setTranslatedRelations(Collection<Relation> translated) {
		this.toTranslate = new HashSet<>(translated);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Relation> getToTranslate() {
		return toTranslate;
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
		return "[GoalUnification decision= " + this.decision + " unificaiton= " + this.unification + "]";
	}
}
