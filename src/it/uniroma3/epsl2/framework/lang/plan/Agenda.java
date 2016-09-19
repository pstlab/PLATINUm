package it.uniroma3.epsl2.framework.lang.plan;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author anacleto
 *
 */
public class Agenda 
{
	private List<Decision> goals;
	private List<Relation> relations;
	
	/**
	 * 
	 * @param name
	 * @param horizon
	 */
	public Agenda() {
		this.goals = new ArrayList<>();
		this.relations = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getGoals() {
		return new ArrayList<>(this.goals);
	}
	
	/**
	 * 
	 * @param goal
	 */
	public void add(Decision goal) {
		this.goals.add(goal);
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
