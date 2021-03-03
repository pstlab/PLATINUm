package it.cnr.istc.pst.platinum.executive.dc.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.executive.dc.strategy.clock.ClockSet;
import it.cnr.istc.pst.platinum.executive.dc.strategy.loader.StrategyLoader;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Transition;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Wait;

public class StateSet {
	
	Map<String,String> stateSet;
	List<StateStrategy> stateStrategies;

	public StateSet() {
		this.stateSet = new HashMap<>();
		this.stateStrategies = new ArrayList<>();
	}
	
	public StateSet(String stateset, List<String> state) {
		this();
		readState(stateset);
		extractStateStrategyFromStringList(state);
	}
	
	//extract state string 
	private void readState(String line){
		String[] states = line.substring(line.indexOf('(')+1, line.indexOf(')')-1).trim().split("\\s");
		for(String state:states) {
			this.addStateSet(new State(state));
		}	
	}

	//extract strategies strings associated with the state
	public void extractStateStrategyFromStringList(List<String> ls) {
		for(String s : ls) {
			if(s.startsWith(StrategyLoader.MARKER_WAIT)) { //wait action
				//System.out.println(line + "\n\n");
				for (ClockSet clocks : readClockSets(s)) this.addStateStrategy(new StateStrategy(clocks,new Wait()));
			}
			else if(s.startsWith(StrategyLoader.MARKER_TRANSITION)) { //transition action
				//System.out.println(line + "\n\n");
				for(ClockSet clocks : readClockSets(s)) this.addStateStrategy(new StateStrategy(clocks,s));
			}
		}
	}
	
	private List<ClockSet> readClockSets(String line) {
		List<ClockSet> set = new ArrayList<>();
		String[] splitClockSets = line.split("[||]");
		for(String clockSet : splitClockSets) { 
			if (clockSet.length()>2) {
				String[] clocks = clockSet.substring(clockSet.indexOf('(')+1, clockSet.indexOf(')')).trim().split("\\s");
				ClockSet cSet = new ClockSet(clocks);
				set.add(cSet); 
			}
		}
		return set;	
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
	
	public Set<Transition> getTransitionStates(){
		Set<Transition> set = new HashSet<>();
		for ( StateStrategy ss : this.stateStrategies ){
			if( ss.getAction().getClass().equals(Transition.class)) {
				Transition act = (Transition) ss.getAction();
				set.add(act);
			}
		}
		return set;
	}
	
	public void addStateStrategy(StateStrategy stateStrategy) {
		this.stateStrategies.add(stateStrategy);
	}
	
	public boolean isStateSetStatus(Map<String,String> stateStatus) {
		return this.stateSet.equals(stateStatus);
	}
	
	public boolean isStateSetStatusControllable(Map<String,String> stateStatus, Set<String> uStates) {
		for (String tl : stateStatus.keySet()) {	
			if(!uStates.contains(tl) && !stateStatus.get(tl).equals(this.stateSet.get(tl))) { return false;}
		}
		return true;
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
