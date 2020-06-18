package it.istc.pst.platinum.executive.dc.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateSet {
	
	Map<String,String> stateSet;
	List<StateStrategy> stateStrategies;

	public StateSet() {
		this.stateSet = new HashMap<>();
		this.stateStrategies = new ArrayList<>();
	}
	public Map<String, String> getStateSet() {
		return stateSet;
	}

	public void addStateSet(State state) {
		this.stateSet.put(state.getTimeline(),state.getToken());
	}

	public List<StateStrategy> getStateStrategies() {
		return stateStrategies;
	}
	
	public void addStateStrategy(StateStrategy stateStrategy) {
		this.stateStrategies.add(stateStrategy);
	}
	
	public boolean isStateSetStatus(Map<String,String> stateStatus) {
		return this.stateSet.equals(stateStatus);
	}
	
	public StateStrategy searchNextStepStrategy(Map<String,Long> status) throws Exception {
		for( StateStrategy ss : stateStrategies) {
			if(ss.getClockRelations().evaluate(status)) return ss;
		}
		throw new Exception();
	}
	
	
	@Override
	public String toString() {
		return "\n" + "StateSet [ " + "\n\t" + "stateSet=" + stateSet + "," + "\n\t" + "stateStrategies=" + stateStrategies + "]";
	}
	
	
}
