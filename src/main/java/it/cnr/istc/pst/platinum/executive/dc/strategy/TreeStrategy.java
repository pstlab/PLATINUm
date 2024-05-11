package it.cnr.istc.pst.platinum.executive.dc.strategy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Action;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Transition;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Wait;

public class TreeStrategy implements Strategy {
	private static int MAX_STRIKE = 999999;
	
	private long horizon;
	private long time;
	private int strikeTimer;
	private int strikeMaxReached;
	private TreeNodeState rootState;
	private Map<String,Long> timelineClocks;
	private Map<String,String> initialState; //substituted with root of tree, maintained for compatibility with older version
	private Set<String> uStates;
	private Map<Transition,Map<String,String>> uPostConditions;
	
	private Map<Long,Integer> out;

	private Set<StateSet> listStrategy;
	//private PrintWriter writer;

	// ------------------------------ CONSTRUCTORS ------------------

	public TreeStrategy(long horizon) {
		this.horizon = horizon;
		//this.rootState = new TreeNodeState();
		this.initialState = new HashMap<>();
		this.uPostConditions = new HashMap<>();
		this.listStrategy = new HashSet<>();
		this.time = 0;
		this.out = new HashMap<>();
		this.strikeTimer = 0;
		this.strikeMaxReached = 0 ;
	}

	//---------------------------------- METHODS --------------------
	
	// ! MUST BE CALLED AFTER ALL DATA IS IN
	@Override
	public void buildStrategyStructure() {
		for( StateSet set : this.listStrategy){
			if(set.isStateSetStatus(this.initialState)) {
				this.listStrategy.remove(set); //should not exist more
				this.rootState = new TreeNodeState(set,listStrategy,uStates);
				//this.listStrategy = null;
				return;
			}
		}
	}

	//returns next strategy step(s)
	@Override
	public List<Action> askAllStrategySteps(long plan_clock, Map<String,String> actualState, boolean isPlanClock) { //throws Exception {
		//System.out.println(actualState + "plan clock " + plan_clock + "\n" + "clock " + this.timelineClocks);
		long time  = System.currentTimeMillis();
		List<Action> actions = new ArrayList<>();
		this.updateExpectedState(actualState); 
		this.updateClocks(plan_clock-(this.timelineClocks.get("plan")));
		//System.out.println(initialState + "plan clock " + plan_clock + "clock " + this.timelineClocks);
		Action action = askSingleStrategyStep(initialState);
		//System.out.println(action + "\n");
		actions.add(action);
		while(action.getClass().equals(Transition.class)) {
			action = askSingleStrategyStep(this.initialState);
			actions.add(action);
		}
		time = System.currentTimeMillis() - time;
		this.time = this.time + time;
		if(this.out.containsKey(time)) this.out.put(time, this.out.get(time)+1);
		else this.out.put(time, 1);
		System.out.println(">>>>>>> Answer all strategy steps: " + time + "ms, " + "current time: " + this.time + "\n" + this.out + "\n");
		return actions;
	}

	//returns next strategy step (repeat until wait!!) using tic
	@Override
	public List<Action> askAllStrategySteps(long tic, Map<String,String> actualState) { // throws Exception {
		System.out.println(initialState + "tic " + tic + "\n\n");
		long time  = System.currentTimeMillis();
		List<Action> actions = new ArrayList<>();
		this.updateExpectedState(actualState);
		this.updateClocks(tic);
		Action action = askSingleStrategyStep(this.initialState);
		actions.add(action);
		while(action.getClass().equals(Transition.class)) {
			action = askSingleStrategyStep(this.initialState);
			actions.add(action);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("\n"+ "Answer all strategy steps: " + time + "ms, " + "\n");
		return actions;
	}

	//returns one action predicted for next step *****
	private Action askSingleStrategyStep(Map<String, String> actualState) { // throws Exception {
		//System.out.println("ASK SINGLE STRATEGY STEP: "+ actualState + "\n\n");
		long time  = System.currentTimeMillis();
		try
		{

			if(this.rootState.isStateSetStatus(actualState)) {
				System.out.println("Searching for next step of " + this.rootState);
				StateStrategy win = this.rootState.searchNextStepStrategy(timelineClocks);
				this.timelineClocks = win.applyPostConditions(this.timelineClocks, this.horizon);
				updateExpectedState(win.getAction());
				time = System.currentTimeMillis() - time;
				//System.out.println("\n"+ "Answer single strategy steps: " + time + "ms\n");
				return win.getAction();
			}
		}
		catch (Exception ex) {
			System.out.println("Warning: no state or clock found -> return default action WAIT\n");
			time = System.currentTimeMillis() - time;
			//System.out.println("\n"+ "Answer single strategy steps: " + time + "ms\n");
		}

		// default action
		return new Wait();
	}

	//update list of expected states with list of actual states given, resets local clocks if state changed
	private void updateExpectedState(Map<String,String> actualStates) {
		for(String timeline : actualStates.keySet()) {			
			if(!this.initialState.get(timeline).equals(actualStates.get(timeline))) {
				resetClocks(timeline,this.initialState.get(timeline),actualStates.get(timeline));
				this.initialState.put(timeline, actualStates.get(timeline));
			}
		}
	}

	// Resets the local clocks of uncontrollable events that took place in the tic
	private void resetClocks(String timeline,String from, String to) {
		//this.timelineClocks.put(timeline+"."+timeline+"_clock",0);
		//System.out.println(" RESET CLOCK : " + timeline + " " + token + "<<<<<<<<<<<<<<<<<<<\n");
		Map<String,String> condToken = this.uPostConditions.get(new Transition(timeline,from,to));
		//System.out.println("CONDTOKEN : " + condToken + "\n");
		if(condToken!=null) {
			for(String cond : condToken.keySet()) {
				if(condToken.get(cond).contains("0")) {
					this.timelineClocks.put(cond, 0l);
				}
				if(condToken.get(cond).contains("H")) {
					this.timelineClocks.put(cond, this.horizon*2);
				}
			}
			this.timelineClocks.put(timeline, 0l);
		}}

	//updates, after a winning transition, the next expected state
	private void updateExpectedState(Action action) {
		if(action.getClass().equals(Transition.class)) {
			Transition t = (Transition) action;
			Map<String, String> rootStates = this.rootState.getParentState().getStateSet();
			rootStates.put(t.getTimeline(),t.getTransitionTo().getToken());
			this.rootState = this.rootState.getChild(rootStates);
		}
	}

	//updates clocks through plan clock
	private void updateClocks(long n) {
		for(String c : this.timelineClocks.keySet()) {
			//System.out.println(">>> INCREMENT CLOCK BY " + n + ":::: CLOCK " + c + "\n");
			this.timelineClocks.put(c, this.timelineClocks.get(c)+n);
		}

	}
	
	@Override
	public void updateUncontrollable(Map<String, String> updatedState) {
		// TODO Auto-generated method stub
		System.out.println("Unimplemented method 'updateUncontrollable'\n");
		
	}
	
	@Override
	public String toString() {
		return "Strategy [STRATEGY = " + this.rootState + "]";
	}

	//---------------------- GETTERS&SETTERS ------------------------

	public Map<String,Long> getTimelineClocks() {
		return this.timelineClocks;
	}

	@Override
	public void setTimelineClocks(Map<String,Long> tc) {
		this.timelineClocks = tc;
	}

	public Map<String, String> getExpectedState() {
		return initialState;
	}

	@Override
	public void setInitialState(Map<String, String> expectedState) {
		this.initialState = expectedState;
	}

	public Map<Transition, Map<String, String>> getuPostConditions() {
		return uPostConditions;
	}

	@Override
	public void setuPostConditions(Map<Transition, Map<String, String>> uPostConditions) {
		this.uPostConditions = uPostConditions;
	}

//	@Override //may be obsolete
//	public void getStrategyAsList(Set<StateSet> accumulatedStates) {
//		this.listStrategy = accumulatedStates;
//	}
	
	@Override
	public boolean isOutOfBounds() {
		return (this.strikeTimer > TreeStrategy.MAX_STRIKE);
	}
	
	public Set<String> getuStates() {
		return uStates;
	}

	@Override
	public void setuStates(Set<String> uStates) {
		this.uStates = uStates;
	}

	@Override
	public int getStrikeMaxReached() {
		return this.strikeMaxReached;
	}
	

	@Override
	public void getStrategyAsStrings(String ss, List<String> statesLine) {
		StateSet stateSet = new StateSet(ss,statesLine);
		this.listStrategy.add(stateSet);
	}

	/*public void setWriter(PrintWriter writer) {
		this.writer = writer;
		
	}*/
}
