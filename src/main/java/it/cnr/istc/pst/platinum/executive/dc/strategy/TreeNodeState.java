package it.cnr.istc.pst.platinum.executive.dc.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Transition;

public class TreeNodeState {
	private StateSet parentState;
	private Set<TreeNodeState> childStates;
	private Map<String,Map<String,String>> uChildStates;
	
	public TreeNodeState(StateSet sourceState, Set<StateSet> remainingListStates, Set<String> uStates) {
		this.parentState = sourceState;
		this.childStates = new HashSet<>();
		this.findChildStates(remainingListStates, uStates);
		this.uChildStates = new HashMap<>();
	}
	
	private void findChildStates(Set<StateSet> stateList, Set<String> uStates) {
		if(this.parentState.getStateSet().toString().contains("rsa=rsa14")) System.out.println("RSA14 REST: " + this.parentState +stateList);
		//action transitions in parent node
		Set<Transition> transitions = this.parentState.getTransitionStates();
		//set to keep found next states
		Set<StateSet> nextFound = new HashSet<>();
		//generate list of next states compatible with action of the parent node
		Set< Map<String,String> > nextStates = new HashSet<>(); 
		
		for (Transition t : transitions) {
			Map<String,String> stat = new HashMap<>();
			stat.put(t.getTransitionTo().getTimeline(),t.getTransitionTo().getToken());
			nextStates.add(stat);
		}
		//search for the next states in list in the remaining list of states to insert in the tree
		Set<StateSet> output = new HashSet<>();
		for( StateSet set : stateList ){
			for ( Map<String,String> next : nextStates ) {
				if(set.isStateSetStatusControllable(next,uStates)) { //equals with only controllable
					nextFound.add(set);
				}
				else {
					output.add(set);
				}
			}
		}
		this.setChildStates(nextFound,output, uStates);
	}
	
	private void setChildStates(Set<StateSet> childStates, Set<StateSet> rest, Set<String> uStates) {
		for( StateSet set : childStates) {
			this.childStates.add(new TreeNodeState(set,rest, uStates));
		}
		if (childStates.isEmpty() && !rest.isEmpty()) {
			//can happen, it's not an error
			//System.out.println("Some states are not reachable in this branch: " + rest );
		}
	}
	
	public StateSet getParentState() {
		return parentState;
	}

	public Set<TreeNodeState> getChildStates() {
		return childStates;
	}
	
	public TreeNodeState getChild(Map<String,String> s) {
		for(TreeNodeState child : this.childStates) {
			if(child.parentState.isStateSetStatus(s)){
				return child;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "TreeNodeState [parentState=" + parentState + ", childStates=" + childStates + ", uChildStates="
				+ uChildStates + "]";
	}

	public boolean isStateSetStatus(Map<String, String> actualState) {
		return this.parentState.isStateSetStatus(actualState);
	}
	
	public boolean isStateSetStatusControllable(Map<String, String> actualState, Set<String> uStates) {
		return this.parentState.isStateSetStatusControllable(actualState, uStates);
	}

	public StateStrategy searchNextStepStrategy(Map<String, Long> timelineClocks) throws Exception {
		try {
			return this.parentState.searchNextStepStrategy(timelineClocks);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new Exception();
	}

}
