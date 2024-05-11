package it.cnr.istc.pst.platinum.executive.dc.strategy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Action;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Transition;

public interface Strategy {
	void buildStrategyStructure();
	List<Action> askAllStrategySteps(long plan_clock, Map<String,String> actualState, boolean isPlanClock);
	List<Action> askAllStrategySteps(long tic, Map<String,String> actualState);
	void updateUncontrollable(Map<String,String> updatedState);
	void setTimelineClocks(Map<String,Long> tc);
	void setInitialState(Map<String, String> expectedState);
	void setuPostConditions(Map<Transition, Map<String, String>> uPostConditions);
	//void getStrategyAsList(Set<StateSet> accumulatedStates); //could be removed
	void getStrategyAsStrings(String ss, List<String> statesLine); //new updated version
	void setuStates(Set<String> uStates);
	boolean isOutOfBounds();
	int getStrikeMaxReached();
}
