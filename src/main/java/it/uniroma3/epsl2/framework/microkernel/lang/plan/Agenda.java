package it.uniroma3.epsl2.framework.microkernel.lang.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class Agenda 
{
	private Map<DomainComponent, List<ComponentValue>> goals;
	
	/**
	 * 
	 * @param name
	 * @param horizon
	 */
	public Agenda() {
		this.goals = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ComponentValue> getGoals() {
		List<ComponentValue> list = new ArrayList<>();
		for (List<ComponentValue> goals : this.goals.values()) {
			list.addAll(goals);
		}
		return list;
	}
	
	/**
	 * 
	 * @param goal
	 */
	public void add(ComponentValue goal) {
		if (!this.goals.containsKey(goal.getComponent())) {
			this.goals.put(goal.getComponent(),	new ArrayList<>());
		}
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
