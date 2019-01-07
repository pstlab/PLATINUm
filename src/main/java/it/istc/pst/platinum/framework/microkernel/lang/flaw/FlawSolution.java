package it.istc.pst.platinum.framework.microkernel.lang.flaw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Agenda;
import it.istc.pst.platinum.framework.microkernel.lang.plan.PartialPlan;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FlawSolution implements Comparable<FlawSolution>
{
	private static final AtomicInteger ID_COUNTER = new AtomicInteger(0);
	private int id;
	protected Flaw flaw;
	
	// decisions managed during the application of the solution
	protected Set<Decision> dCreated;					// decisions added to plan as pending
	protected Set<Decision> dActivated;					// pending decisions added to plan
	
	// relations managed during the application of the solution
	protected Set<Relation> rCreated;					// relation created 
	protected Set<Relation> rActivated;					// relation activated
	
	// representation of the associated partial plan and agenda
	protected PartialPlan partialPlan;					// take track of the partial plan associated to the solution
	protected Agenda agenda;							// take track of the agenda associated to the solution
	
	/**
	 * 
	 * @param flaw
	 */
	protected FlawSolution(Flaw flaw) 
	{
		// set flaw solution ID
		this.id = ID_COUNTER.getAndIncrement();
		// set flaw
		this.flaw = flaw;
		// initialize data structures
		this.dCreated = new HashSet<>();
		this.dActivated = new HashSet<>();
		this.rCreated = new HashSet<>();
		this.rActivated = new HashSet<>();
		// initialize the partial plan 
		this.partialPlan = new PartialPlan();
		// initialize the agenda
		this.agenda = new Agenda();
	}
	
	/**
	 * 
	 * @return
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public Flaw getFlaw() {
		return flaw;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract double getCost();
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getCreatedDecisions() {
		return new ArrayList<>(this.dCreated);
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void addCreatedDecision(Decision dec) {
		this.dCreated.add(dec);
	}
	
	/**
	 * 
	 * @param decs
	 */
	public void addCreatedDecisions(Collection<Decision> decs) {
		this.dCreated.addAll(decs);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getActivatedDecisisons() {
		return new ArrayList<>(this.dActivated);
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void addActivatedDecision(Decision dec) {
		this.dActivated.add(dec);
	}
	
	/**
	 * 
	 * @param decs
	 */
	public void addActivatedDecisions(Collection<Decision> decs) {
		this.dActivated.addAll(decs);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Relation> getCreatedRelations() {
		return new ArrayList<>(this.rCreated);
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void addCreatedRelation(Relation rel) {
		this.rCreated.add(rel);
	}
	
	/**
	 * 
	 * @param rels
	 */
	public void addCreatedRelations(Collection<Relation> rels) {
		this.rCreated.addAll(rels);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Relation> getActivatedRelations() {
		return new ArrayList<>(this.rActivated);
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void addActivatedRelation(Relation rel) {
		this.rActivated.add(rel);
	}
	
	/**
	 * 
	 * @param rels
	 */
	public void addActivatedRelations(Collection<Relation> rels) {
		this.rActivated.addAll(rels);
	}
	
	/**
	 * 
	 * @return
	 */
	public PartialPlan getPartialPlan() {
		return partialPlan;
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void addDecisionToPartialPlan(Decision dec) {
		this.partialPlan.addBehavior(dec);
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void addRelationToPartialPlan(Relation rel) {
		this.partialPlan.addBehaviorConstraint(rel);
	}
	
	/**
	 * 
	 * @param type
	 * @param reference
	 * @param target
	 */
	public void addRelationToPartialPlan(RelationType type, Decision reference, Decision target) {
		this.partialPlan.addBehaviorConstraint(type, reference, target);
	}
	
	/**
	 * 
	 * @return
	 */
	public Agenda getAgenda() {
		return agenda;
	}
	
	/**
	 * 
	 * @param goal
	 */
	public void addGoalToAgenda(ComponentValue value) {
		this.agenda.addGoalComponentBehavior(value);
	}
	
	
	
	/**
	 * 
	 */
	@Override
	public int compareTo(FlawSolution o) {
		// default comparison according to the ID
		return this.id < o.id ? -1 : this.id > o.id ? 1 : 0;
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlawSolution other = (FlawSolution) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
