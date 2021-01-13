package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;

/**
 * 
 * @author anacleto
 *
 */
public class Plan 
{
	private Map<DomainComponent, Set<Decision>> decisions;
	private List<Relation> relations;
	private Map<DomainComponent, Double[]> makespan;
	
	/**
	 * 
	 */
	public Plan() {
		this.decisions = new HashMap<>();
		this.relations = new ArrayList<>();
		this.makespan = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<DomainComponent, Double[]> getMakespan() {
		return new HashMap<>(this.makespan);
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
 		
 		
 		// update makespan 
 		if (!this.makespan.containsKey(goal.getComponent())) {
 			// set min and max durations as makespan 
 			this.makespan.put(goal.getComponent(), new Double[] {
 				(double) goal.getDuration()[0],
 				(double) goal.getDuration()[1]
 			});
 		}
 		else {
 			// update makespan
 			this.makespan.put(goal.getComponent(), new Double[] {
 					this.makespan.get(goal.getComponent())[0] + ((double) goal.getDuration()[0]),
 					this.makespan.get(goal.getComponent())[1] + ((double) goal.getDuration()[1]),
 			});
 		}
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
		String json = "{\n";
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
