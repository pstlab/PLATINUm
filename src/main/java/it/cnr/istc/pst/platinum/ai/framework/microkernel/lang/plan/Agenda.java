package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;

/**
 * 
 * @author anacleto
 *
 */
public class Agenda 
{
	private List<Decision> decisions;
	private List<Relation> relations;
	
	/**
	 * 
	 */
	public Agenda() {
		this.decisions = new ArrayList<>();
		this.relations = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getDecisions() {
		return new ArrayList<>(this.decisions);
	}
	
	/**
	 * 
	 * @param goal
	 */
	public void add(Decision goal) {
		this.decisions.add(goal);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Relation> getRelations() {
		return relations;
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void add(Relation rel) {
		this.relations.add(rel);
	}
	
}
