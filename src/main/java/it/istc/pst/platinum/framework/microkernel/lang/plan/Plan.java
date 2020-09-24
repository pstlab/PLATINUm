package it.istc.pst.platinum.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;

/**
 * 
 * @author anacleto
 *
 */
public class Plan 
{
	private Map<DomainComponent, Set<Decision>> decisions;
	private List<Relation> relations;
	private double[] makespan;
	private double[] duration;
	
	/**
	 * 
	 * @param name
	 * @param horizon
	 */
	public Plan() {
		this.decisions = new HashMap<>();
		this.relations = new ArrayList<>();
		this.makespan = new double[] {
				Double.MAX_VALUE - 1, 
				Double.MAX_VALUE - 1
		};
		
		this.duration = new double[] {
				Double.MAX_VALUE - 1, 
				Double.MAX_VALUE - 1
		};
	}
	
	/**
	 * 
	 * @param makespan
	 */
	public void setMakespan(double[] makespan) {
		this.makespan = makespan;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public double[] getMakespan() {
		return this.makespan;
	}
	
	/**
	 * 
	 * @param duration
	 */
	public void setBehaviorDuration(double[] duration) {
		this.duration = duration;
	}
	
	/**
	 * 
	 * @return
	 */
	public double[] getBehaviorDuration() {
		return this.duration;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getDecisions() {
		List<Decision> list = new ArrayList<>();
		for (DomainComponent comp : this.decisions.keySet()) {
			list.addAll(this.decisions.get(comp));
		}
		return list;
	}
	
	/**
	 * 
	 * @param comp
	 * @return
	 */
	public List<Decision> getDecisions(DomainComponent comp) {
		// check if component exists
		if (this.decisions.containsKey(comp)) {
			// get list by component
			return new ArrayList<>(this.decisions.get(comp)); 
		}
		else {
			// return an empty list
			return new ArrayList<>();
		}
	}
	
	/**
	 * 
	 * @param goal
	 */
	public void add(Decision goal) {
		if (!this.decisions.containsKey(goal.getComponent())) {
			this.decisions.put(goal.getComponent(), new HashSet<>());
		}
		
 		this.decisions.get(goal.getComponent()).add(goal);
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
	
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style description
		String json = "{\n"
				+ "\tmakespan: [" + this.makespan[0] + ", " + this.makespan[1] +"],\n";
		
		// add decisions by component
		for (DomainComponent comp : this.decisions.keySet()) {
			json += "\t" + comp.getName() + ": " + this.decisions.get(comp).toString() + ",\n";
		}
		
		// add relations
		json +=  "\trelations: " + this.relations.toString() + ",\n";
		
		// close json object
		json += "\n}";
		// get json description
		return json;
	}
	
	
}
