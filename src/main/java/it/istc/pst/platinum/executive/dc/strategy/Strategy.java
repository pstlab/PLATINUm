package it.istc.pst.platinum.executive.dc.strategy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.executive.dc.strategy.result.Action;
import it.istc.pst.platinum.executive.dc.strategy.result.Transition;
import it.istc.pst.platinum.executive.dc.strategy.result.Wait;

public class Strategy {
	long horizon;
	private Set<StateSet> states;
	private Map<String,Long> timelineClocks;
	private Map<String,String> expectedState;

	// ------------------------------ CONSTRUCTORS ------------------
	
	public Strategy(long horizon) {
		this.horizon = horizon;
		this.states = new HashSet<>();
		this.expectedState = new HashMap<>();
	}
	
	//---------------------------------- METHODS --------------------

	//returns next strategy step (repeat until wait!!) using plan clock
	public List<Action> askAllStrategySteps(long plan_clock, Map<String,String> actualState, boolean isPlanClock) { //throws Exception {
		System.out.println(expectedState + "plan clock " + plan_clock + "\n\n" + "clock " + this.timelineClocks);
		List<Action> actions = new ArrayList<>();
		this.updateExpectedState(actualState);
		this.updateClocks(plan_clock-(this.timelineClocks.get("plan")));
		Action action = askSingleStrategyStep(expectedState);
		System.out.println(action + "\n");
		actions.add(action);
		while(action.getClass().equals(Transition.class)) {
			action = askSingleStrategyStep(this.expectedState);
			actions.add(action);
		}
		return actions;
	}
	
	//returns next strategy step (repeat until wait!!) using tic
	public List<Action> askAllStrategySteps(long tic, Map<String,String> actualState) { // throws Exception {
		System.out.println(expectedState + "tic " + tic + "\n\n");
		List<Action> actions = new ArrayList<>();
		this.updateExpectedState(actualState);
		this.updateClocks(tic);
		Action action = askSingleStrategyStep(this.expectedState);
		actions.add(action);
		while(action.getClass().equals(Transition.class)) {
			action = askSingleStrategyStep(this.expectedState);
			actions.add(action);
		}
		return actions;
	}

	//returns one action predicted for next step
	private Action askSingleStrategyStep(Map<String, String> actualState) { // throws Exception {
		System.out.println(actualState + "\n\n");
		try
		{
			for(StateSet s : this.states) {
				if(s.isStateSetStatus(actualState)) { //change in map
					StateStrategy win = s.searchNextStepStrategy(timelineClocks);
					this.timelineClocks = win.applyPostConditions(this.timelineClocks, this.horizon);
					updateExpectedState(win.getAction());
					return win.getAction();
				}
			}
		}
		catch (Exception ex) {
			System.out.println("Warning: no state or clock found -> return default action WAIT\n");
		}
		
		// default action
		return new Wait();
	}

	//update list of expected states with list of actual states given, resets local clocks if state changed
	void updateExpectedState(Map<String,String> actualStates) {
		for(String timeline : actualStates.keySet()) {			
			if(!this.expectedState.get(timeline).equals(actualStates.get(timeline))) {
				this.expectedState.put(timeline, actualStates.get(timeline));
				resetClock(timeline);
			}
		}
	}

	// Resets the local clocks of uncontrollable events that took place in the tic
	void resetClock(String timeline) {
		//this.timelineClocks.put(timeline+"."+timeline+"_clock",0);
		this.timelineClocks.put(timeline, 0l);
	}

	//updates, after a winning transition, the next expected state
	void updateExpectedState(Action action) {
		if(action.getClass().equals(Transition.class)) {
			Transition t = (Transition) action;
			this.expectedState.put(t.getTransitionTo().getTimeline(),t.getTransitionTo().getToken());
		}
	}

	//updates clocks through plan clock
	void updateClocks(long n) {
		for(String c : this.timelineClocks.keySet()) {
			this.timelineClocks.put(c, this.timelineClocks.get(c)+n);
		}
		
	}
	
	@Override
	public String toString() {
		return "Strategy [STRATEGY = " + this.states + "]";
	}
	
	//---------------------- GETTERS&SETTERS ------------------------
	
	public Set<StateSet> getStates() {
		return this.states;
	}

	public void addState(StateSet states) {
		this.states.add(states);
	}
	
	public Map<String,Long> getTimelineClocks() {
		return this.timelineClocks;
	}
	
	public void setTimelineClocks(Map<String,Long> tc) {
		this.timelineClocks = tc;
	}
	
	public Map<String, String> getExpectedState() {
		return expectedState;
	}

	public void setExpectedState(Map<String, String> expectedState) {
		this.expectedState = expectedState;
	}
	
}