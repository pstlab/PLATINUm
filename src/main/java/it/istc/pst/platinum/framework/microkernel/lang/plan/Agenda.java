package it.istc.pst.platinum.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class Agenda 
{
	private Map<DomainComponent, Set<ComponentBehavior>> goals;
	
	/**
	 * 
	 */
	public Agenda() {
		// initialize agenda
		this.goals = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ComponentBehavior> getGoals() {
		// list of pending decisions
		List<ComponentBehavior> list = new ArrayList<>();
		// get components' pending decisions of the partial plan
		for (Set<ComponentBehavior> pending : this.goals.values()) {
			// add all pending decisions
			list.addAll(pending);
		}
		
		// get the list of pending decisions of the partial plan
		return list;
	}
	
	/**
	 * 
	 * @param comp
	 * @return
	 */
	public List<ComponentBehavior> getGoalsByComponent(DomainComponent comp) {
		// list of behaviors
		List<ComponentBehavior> list = new ArrayList<>();
		if (this.goals.containsKey(comp)) {
			list.addAll(this.goals.get(comp));
		}
		// get list of goal behaviors
		return list;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void addGoalComponentBehavior(ComponentValue value) {
		// add the pending decision to the partial plan
		if (!this.goals.containsKey(value.getComponent())) {
			// initialize the entry
			this.goals.put(value.getComponent(), new HashSet<>());
		}
		
		// create behavior
		ComponentBehavior b = new ComponentBehavior(value, null, value.getDurationBounds());
		// add the goal to the agenda of the partial plan
		this.goals.get(value.getComponent()).add(b);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Agenda goals: " + this.getGoals() + "]";
	}
}
