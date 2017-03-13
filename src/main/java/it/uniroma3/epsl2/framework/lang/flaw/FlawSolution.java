package it.uniroma3.epsl2.framework.lang.flaw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.framework.lang.plan.Agenda;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FlawSolution 
{
	private static int ID_COUNTER = 0;
	private int id;
	protected Flaw flaw;
	
	// decisions managed during the application of the solution
	protected Set<Decision> dCreated;					// decisions added to plan as pending
	protected Set<Decision> dActivated;					// pending decisions added to plan
	
	// relations managed during the application of the solution
	protected Set<Relation> rCreated;					// relation created 
	protected Set<Relation> rActivated;					// relation activated
	
	private Agenda agenda;							// agenda resulting from the application of the solution
	private double makespan;						// makespan resulting from the application of the solution
	
	/**
	 * 
	 * @param flaw
	 */
	protected FlawSolution(Flaw flaw) 
	{
		this.id = getNextId();
		this.flaw = flaw;
		// initialize data structures
		this.dCreated = new HashSet<>();
		this.dActivated = new HashSet<>();
		this.rCreated = new HashSet<>();
		this.rActivated = new HashSet<>();
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
	public Agenda getAgenda() {
		return agenda;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getMakespan() {
		return makespan;
	}
	
	/**
	 * 
	 * @param agenda
	 */
	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}
	
	/**
	 * 
	 * @param makespan
	 */
	public void setMakespan(double makespan) {
		this.makespan = makespan;
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

	/**
	 * 
	 * @return
	 */
	private synchronized static int getNextId() {
		return ID_COUNTER++;
	}
}
