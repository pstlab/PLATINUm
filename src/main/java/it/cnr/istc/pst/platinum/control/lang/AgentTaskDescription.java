package it.cnr.istc.pst.platinum.control.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author alessandro
 *
 */
public class AgentTaskDescription extends PlatformMessage {
	
	private List<TokenDescription> goals;
	private List<TokenDescription> facts;
	
	/**
	 * 
	 * @param id
	 */
	public AgentTaskDescription(long id) {
		super(id);
		this.goals = new ArrayList<>();
		this.facts = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param fact
	 */
	public void addFactDescription(TokenDescription fact) {
		this.facts.add(fact);
	}
	
	/**
	 * 
	 * @param goal
	 */
	public void addGoalDescription(TokenDescription goal) {
		this.goals.add(goal);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TokenDescription> getFacts() {
		return new ArrayList<>(this.facts);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TokenDescription> getGoals() {
		return new ArrayList<>(this.goals);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style description
		String json = "{\n\tgoals: [\n";
		for (TokenDescription goal : goals) {
			// add JSON object description
			json += "\t\t" + goal + ",\n";
		}
						
		json += "\t],\n\n\tfacts: [\n";
		for (TokenDescription fact : this.facts) {
			// add JSON object description
			json += "\t\t" + fact + ",\n";
		}
				
		json += "\t],\n\n}\n";
		// get JSON
		return json;
	}
}
