package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.DecisionVariable;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;

/**
 * 
 * @author anacleto
 *
 */
public class Plan 
{
	private Map<DomainComponent, List<DecisionVariable>> decisions;		// partial plan
 	private Map<DomainComponent, List<Flaw>> agenda; 					// flaws associated to the resulting partial plan
 	
 	// consolidated information about a partial plan 
 	private Map<DomainComponent, Double[]> makespan;					// consolidated makespan of SVs
	
	
//	private Map<DomainComponent, Set<Decision>> decisions;
	private List<Relation> relations;
//	private Map<DomainComponent, Double[]> makespan;
	
	/**
	 * 
	 * @param plan
	 */
	public Plan(Plan plan) 
	{
		this.decisions = new HashMap<>(plan.decisions);
		this.relations = new ArrayList<>(plan.relations);
		this.agenda = new HashMap<>(plan.agenda);
		this.makespan = new HashMap<>(plan.makespan);
	}
	
	/**
	 * 
	 */
	public Plan() 
	{
		// initialize data structures
		this.decisions = new HashMap<>();
		this.relations = new ArrayList<>();
		this.agenda = new HashMap<>();
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
	 * @param component
	 * @return
	 */
	public double[] getMakespan(DomainComponent component) {
		
		// set heuristic makespan
		double[] mk = new double[] {
				0,
				0
		};
		
		// check if component exists
		if (this.makespan.containsKey(component)) {
			// set consolidated lower bound
			mk[0] = this.makespan.get(component)[0];
			mk[1] = this.makespan.get(component)[1];
			
		}
		
		// get makespan
		return mk;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<DecisionVariable> getDecisions() {
		// list of all decision variables
		List<DecisionVariable> list = new ArrayList<>();
		for (DomainComponent comp : this.decisions.keySet()) {
			list.addAll(this.decisions.get(comp));
		}
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<DomainComponent, List<DecisionVariable>> getAllDecisions() {
		return new HashMap<>(this.decisions);
	}
	
	/**
	 * 
	 * @param comp
	 * @return
	 */
	public List<DecisionVariable> getDecisions(DomainComponent comp) {
		
		// list of component's decisions
		List<DecisionVariable> list = new ArrayList<>();
		// check if component exists
		if (this.decisions.containsKey(comp)) {
			// get list by component
			list.addAll(this.decisions.get(comp));
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param component
	 * @param flaw
	 */
	public void add(DomainComponent component, Flaw flaw) 
	{
		if (!this.agenda.containsKey(component)) {
			this.agenda.put(component, new ArrayList<>());
		}
		
		this.agenda.get(component).add(flaw);
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Flaw> getFlaws() {
		Set<Flaw> flaws = new HashSet<>();
		for (DomainComponent comp : this.agenda.keySet()) {
			flaws.addAll(this.agenda.get(comp));
		}
		return flaws;
	}
	
	/**
	 * 
	 * @param comp
	 * @return
	 */
	public Set<Flaw> getFlaws(DomainComponent comp) {
		return new HashSet<>(this.agenda.get(comp));
	}
	
	/**
	 * 
	 * @param component
	 * @param decision
	 */
	public void add(DomainComponent component, DecisionVariable decision) 
	{
		if (!this.decisions.containsKey(component)) {
			this.decisions.put(component, new ArrayList<>());
		}
		
 		this.decisions.get(component).add(decision);
 		
 		
 		// update makespan 
 		if (!this.makespan.containsKey(component)) {
 			// set min and max durations as makespan 
 			this.makespan.put(component, new Double[] {
 				(double) decision.getDuration()[0],
 				(double) decision.getDuration()[1]
 			});
 		}
 		else {
 			// update makespan
 			this.makespan.put(component, new Double[] {
 					this.makespan.get(component)[0] + ((double) decision.getDuration()[0]),
 					this.makespan.get(component)[1] + ((double) decision.getDuration()[1]),
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
