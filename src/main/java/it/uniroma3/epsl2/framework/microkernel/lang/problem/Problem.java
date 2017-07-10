package it.uniroma3.epsl2.framework.microkernel.lang.problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public class Problem 
{
	private Map<ProblemFluentType, List<ProblemFluent>> fluents;
	private List<ProblemConstraint> constraints;
	private Map<ProblemFluent, List<ProblemConstraint>> fluent2constraints;
	
	/**
	 * 
	 */
	public Problem() {
		// setup data
		this.fluents = new HashMap<>();
		this.fluents.put(ProblemFluentType.FACT, new ArrayList<ProblemFluent>());
		this.fluents.put(ProblemFluentType.GOAL, new ArrayList<ProblemFluent>());
		this.constraints = new ArrayList<>();
		this.fluent2constraints = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ProblemFact> getFacts() {
		List<ProblemFact> facts = new ArrayList<>();
		for (ProblemFluent fluent : this.fluents.get(ProblemFluentType.FACT)) {
			facts.add((ProblemFact) fluent);
		}
		// get facts
		return facts;
	}
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @return
	 */
	public ProblemFact addFact(ComponentValue value, String[] labels, long[] start, long[] end) {
		ProblemFact f = new ProblemFact(value, labels, start, end, value.getDurationBounds());
		this.fluents.get(f.getType()).add(f);
		this.fluent2constraints.put(f, new ArrayList<ProblemConstraint>());
		return f;
	}
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param duration
	 * @return
	 */
	public ProblemFact addObservation(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) {
		ProblemFact f = new ProblemFact(value, labels, start, end, duration);
		this.fluents.get(f.getType()).add(f);
		this.fluent2constraints.put(f, new ArrayList<ProblemConstraint>());
		return f;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ProblemGoal> getGoals() {
		List<ProblemGoal> goals = new ArrayList<>();
		for (ProblemFluent goal : this.fluents.get(ProblemFluentType.GOAL)) {
			goals.add((ProblemGoal) goal);
		}
		// get goal
		return goals;
	}
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @return
	 */
	public ProblemGoal addGoal(ComponentValue value, String[] labels, long[] start, long[] end) {
		ProblemGoal g = new ProblemGoal(value, labels, start, end, value.getDurationBounds());
		this.fluents.get(ProblemFluentType.GOAL).add(g);
		this.fluent2constraints.put(g, new ArrayList<ProblemConstraint>());
		return g;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ProblemConstraint> getConstraints() {
		// get list
		return new ArrayList<>(this.constraints);
	}
	
	/**
	 * 
	 * @param fluent
	 * @return
	 */
	public List<ProblemConstraint> getConstraintWithReference(ProblemFluent fluent) {
		return new ArrayList<>(this.fluent2constraints.get(fluent));
	}
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 * @param bounds
	 */
	public void addTemporalConstraint(RelationType type, ProblemGoal reference, ProblemGoal target, long[][] bounds) {
		// create constraint
		ProblemConstraint constraint = new TemporalProblemConstraint(type, reference, target, bounds);
		// add constraint
		this.constraints.add(constraint);
		this.fluent2constraints.get(reference).add(constraint);
	}
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param referenceLabel
	 * @param target
	 * @param targetLabel
	 */
	public void addParameterConstraint(RelationType type, ProblemFluent reference, String referenceLabel, ProblemFluent target, String targetLabel) {
		// create parameter constraint
		ProblemConstraint constraint = new ParameterProblemConstraint(type, reference, target, referenceLabel, targetLabel);
		// add constraint
		this.constraints.add(constraint);
		this.fluent2constraints.get(reference).add(constraint);
	}
	
	/**
	 * 
	 * @param reference
	 * @param referenceLabel
	 * @param value
	 */
	public void addBindingParameterConstraint(ProblemFluent reference, String referenceLabel, String value) {
		// create parameter constraint
		ProblemConstraint constraint = new ParameterProblemConstraint(RelationType.BIND_PARAMETER, reference, reference, referenceLabel, value);
		// add constraint
		this.constraints.add(constraint);
		this.fluent2constraints.get(reference).add(constraint);
	}

}
