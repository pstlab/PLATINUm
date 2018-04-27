package it.istc.pst.platinum.executive.dc.tga.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.executive.dc.tga.conditions.TGAGuards;


public class TGATransition {

	/* Action is a pair of maps. 
	 * The first, initialStates, contains the initial states of the transitions, the second the final states.
	 * Both maps has the same set of keys, that are the state variables of the system. */
	private Map<String, String> initialStates;
	private Map<String, String> finalStates; 
	private TGAGuards guards;
	private String newValues;

	public TGATransition(Set<String> variables){
		this.initialStates = new HashMap<String, String>();
		this.finalStates = new HashMap<String, String>(); 
		this.guards = new TGAGuards();

		for (String v : variables){
			this.initialStates.put(v, ""); 
			this.finalStates.put(v, ""); 
		}

	}

	public void setInitialAndFinalStates(String actionFromLine){ 
		String[] split = actionFromLine.split(" ");
		String[] split2;

		for (int i = 0; i < split.length; i++){
			split[i] = split[i].trim();
			split2 = split[i].split("->");
			
			this.initialStates.put(split2[0].substring(0, split2[0].indexOf(".")), split2[0].substring(split2[0].indexOf(".")));
			this.finalStates.put(split2[1].substring(0, split2[1].indexOf(".")), split2[1].substring(split2[1].indexOf(".")));
		}

	}

	/*This constructor takes a string, taken from the terminal output, and create the object Action.*/	
	public void setGuardsAndNewValues(String conditions) {
		this.guards = new TGAGuards();
		this.guards.setGuard(conditions.substring(0, conditions.indexOf("tau"))); 
		this.newValues = conditions.substring(conditions.indexOf("tau,")+3);
	}

	public Map<String, String> getInitialStates(){
		return this.initialStates; 
	}

	public Map<String, String> getFinalStates(){
		return this.finalStates; 
	}

	public TGAGuards getGuards(){
		return this.guards; 
	}

	public String getNewValues(){
		return this.newValues; 
	}
}
