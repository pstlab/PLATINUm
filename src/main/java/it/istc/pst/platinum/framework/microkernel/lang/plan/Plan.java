package it.istc.pst.platinum.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author anacleto
 *
 */
public class Plan 
{
	private List<Decision> decisions;
	private List<Relation> relations;
	
	/**
	 * 
	 * @param name
	 * @param horizon
	 */
	public Plan() {
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
