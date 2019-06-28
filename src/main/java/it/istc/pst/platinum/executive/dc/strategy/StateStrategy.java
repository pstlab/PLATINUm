package it.istc.pst.platinum.executive.dc.strategy;

import java.util.HashMap;
import java.util.Map;

import it.istc.pst.platinum.executive.dc.strategy.clock.ClockSet;
import it.istc.pst.platinum.executive.dc.strategy.result.Action;
import it.istc.pst.platinum.executive.dc.strategy.result.Transition;

public class StateStrategy {
	ClockSet clockRelations;
	Action action;
	Map<String,String> postConditions;
	
//-------------------CONSTRUCTORS ----------------------
	
	//Action without postcondition (wait)
	public StateStrategy(ClockSet clocks, Action act) {
		this.clockRelations = clocks;
		this.action = act;
		this.postConditions = new HashMap<>();
	}
	
	//Transition to read
	public StateStrategy(ClockSet clocks, String line) {
		this.clockRelations = clocks;
		this.action = extractAction(line);
		this.postConditions = extractPostConditions(line);
	}
	
	
//-------------------- METHODS --------------------------
	
	//extracts clocks of postcondition from a transition
	private Map<String,String> extractPostConditions(String line) {
		Map<String,String> temp = new HashMap<>();
		String[] firstSplit = line.split("tau,");
		String[] splitPostConditions = firstSplit[1].split(",");
		for(String post : splitPostConditions) {
			if(post.contains(":=")) {
				String[] split = post.split(":=");
				String c = split[0].trim();
				if(c.contains("_")) c = c.substring(0,c.indexOf("_"));
				if(c.contains(".")) c = c.substring(0,c.indexOf("."));
				temp.put(c, split[1].trim());
			}	
		}
		return temp;
	}

	//extracts the transition from the line
	private Action extractAction(String line) {
		String[] split = line.split("\\s");
		for(String s:split) {
			if (s.contains("->")) return new Transition(s);
		}
		return null;
	}
	
	//applies post conditions after transition
	public Map<String,Long> applyPostConditions(Map<String,Long> status, long horizon){  
		for(String cond : this.postConditions.keySet()) {
			if(this.postConditions.keySet().equals("0")) {
				status.put(cond, 0l);
			}
			if(this.postConditions.keySet().equals("H")) {
				status.put(cond, horizon);
			}
		}
		return status;
	}

//-------------------------- GETTERS/SETTERS ---------------
	
	public ClockSet getClockRelations() {
		return clockRelations;
	}

	public Action getAction() {
		return action;
	}

	public Map<String,String> getPostConditions() {
		return postConditions;
	}
	@Override
	public String toString() {
		return "StateStrategy [clockRelations=" + clockRelations + ", action=" + action + ", postConditions="
				+ postConditions + "]";
	}
}
