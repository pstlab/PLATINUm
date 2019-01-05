package it.istc.pst.platinum.deliberative.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class Agenda 
{
//	private Map<DomainComponent, List<ComponentValue>> goals;
	private Map<DomainComponent, Set<Decision>> goals;
	
	/**
	 * 
	 * @param name
	 * @param horizon
	 */
	protected Agenda() {
		// initialize agenda
		this.goals = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getGoals() {
		// list of pending decisions
		List<Decision> list = new ArrayList<>();
		// get components' pending decisions of the partial plan
		for (Set<Decision> pending : this.goals.values()) {
			// add all pending decisions
			list.addAll(pending);
		}
		
		// get the list of pending decisions of the partial plan
		return list;
	}
	
	/**
	 * 
	 * @param goal
	 */
	public void add(Decision goal) {
		// add the pending decision to the partial plan
		if (!this.goals.containsKey(goal.getComponent())) {
			// initialize the entry
			this.goals.put(goal.getComponent(),	new HashSet<>());
		}
		
		// add the goal to the agenda of the partial plan
		this.goals.get(goal.getComponent()).add(goal);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Agenda #goals= " + this.goals.size() + "]";
	}
}
