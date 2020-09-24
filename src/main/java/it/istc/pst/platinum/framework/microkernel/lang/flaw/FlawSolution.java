package it.istc.pst.platinum.framework.microkernel.lang.flaw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;

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
	protected double cost;						// solution cost
	
	// decisions managed during the application of the solution
	protected Set<Decision> dCreated;					// decisions added to plan as pending
	protected Set<Decision> dActivated;					// pending decisions added to plan
	
	// relations managed during the application of the solution
	protected Set<Relation> rCreated;					// relation created 
	protected Set<Relation> rActivated;					// relation activated
	
	/**
	 * 
	 * @param flaw
	 */
	protected FlawSolution(Flaw flaw, double cost) 
	{
		// set flaw solution ID
		this.id = ID_COUNTER.getAndIncrement();
		// set flaw
		this.flaw = flaw;
		// set cost
		this.cost = cost;
		// set data structures
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
	public double getCost() {
		return this.cost;
	}
	
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
	public List<Decision> getActivatedDecisions() {
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
