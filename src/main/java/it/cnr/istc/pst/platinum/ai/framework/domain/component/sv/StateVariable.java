package it.cnr.istc.pst.platinum.ai.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.TransitionNotFoundException;

/**
 * 
 * @author alessandro
 *
 */
public abstract class StateVariable extends DomainComponent {
	
	protected List<StateVariableValue> values;							
	// SV's transition function
	protected Map<StateVariableValue, Map<StateVariableValue, Transition>> transitions;
	
	protected Map<StateVariableValue, Map<StateVariableValue, Transition>> inverseTransitions;
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	protected StateVariable(String name, DomainComponentType type) {
		super(name, type);
		// initialize the list of values
		this.values = new ArrayList<StateVariableValue>();
		// initialize transition function
		this.transitions = new HashMap<>();
		// initialize transition function
		this.inverseTransitions = new HashMap<>();
	}
	
	/**
	 * 
	 * @param label
	 * @param duration
	 * @param controllable
	 * @return
	 */
	public StateVariableValue addStateVariableValue(String label, long[] duration, boolean controllable) {
		// create and add value
		StateVariableValue value = new StateVariableValue(label, duration, controllable, this);
		// add to available values
		this.values.add(value);
		// initialize transition data structure
		this.transitions.put(value, new HashMap<>());
		// initialize inverse transition data structure
		this.inverseTransitions.put(value, new HashMap<>());
		// get value
		return value;
	}
	
	/**
	 * State variable values without duration bounds are implicitly treated as controllable
	 * 
	 * @param label
	 * @return
	 */
	public StateVariableValue addStateVariableValue(String label) {
		return this.addStateVariableValue(label, new long [] {1l, this.tdb.getHorizon()}, true);
	}
	
	

	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 */
	public Transition addValueTransition(StateVariableValue reference, StateVariableValue target) {
		// check values
		if (!this.values.contains(reference) || !this.values.contains(target)) {
			throw new RuntimeException("One or both StateVariable values not found [reference= " + reference + " target= " + target + "]");
		}
		
		// create transition
		Transition t = new Transition(reference, target);
		
		// check transition
		if (!this.transitions.containsKey(reference)) {
			this.transitions.put(reference, new HashMap<>());
		}
		
		// add transition
		this.transitions.get(reference).put(target, t);
		
		// check inverse transition
		if (!this.inverseTransitions.containsKey(target) ) {
			this.inverseTransitions.put(target, new HashMap<>());
		}
		// add inverse transition
		this.inverseTransitions.get(target).put(reference, t);
		
		// get transition
		return t;
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 */
	public Transition getTransition(ComponentValue reference, ComponentValue target)  throws TransitionNotFoundException {
		
		// check if transition exists
		if (!this.transitions.containsKey(reference) || !this.transitions.get(reference).containsKey(target)) {
			// transition not found
			throw new TransitionNotFoundException("No transition found between values [reference= " + reference + " target= " + target + "]");
		}
		
		// check transitions
		return this.transitions.get(reference).get(target);
	}
	
	/**
	 * Returns the direct successors of the value according to
	 * the transition function of the State Variable
	 * 
	 * @param value
	 * @return
	 */
	public List<ComponentValue> getDirectSuccessors(ComponentValue value) {
		// get successors as a list
		return new ArrayList<>(this.transitions.get(value).keySet());
	}
	
	/**
	 * Returns the direct predecessors of the value according to 
	 * the transition function of the State Variable
	 * 
	 * @param value
	 * @return
	 */
	public List<ComponentValue> getDirectPredecessors(ComponentValue value) {
		// get predecessors as a list
		return new ArrayList<>(this.inverseTransitions.get(value).keySet());
	}
	
	/**
	 * Analyze the state machine of the SV to extract the shortest acyclic path between two values.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public ValuePath getShortestPath(ComponentValue source, ComponentValue target) {
		
		// call recursive method to unfold state variable description and build possible paths
		ValuePath path = this.bfs(source, target);
		// get the shortest path
		return path;
	}
	
	/**
	 * Compute the shortest path from a source to the target through Breadth-First search
	 * 
	 * @param source
	 * @param target
	 * @param path
	 * @param fringe
	 * @return
	 */
	private ValuePath bfs(ComponentValue source, ComponentValue target) {
		
		// initialize fringe
		List<ComponentValue> fringe = new ArrayList<>();
		// visited values
		Set<ComponentValue> visited = new HashSet<>();
		// initialize the associative map to keep track of parents
		Map<ComponentValue, ComponentValue> parents = new HashMap<>();
		// set the fringe
		for (ComponentValue succ : this.getDirectSuccessors(source) ) {
			// add to fringe
			fringe.add(succ);
			// update parent index
			parents.put(succ, source);
			
		}
		
		// add the first step
		visited.add(source);
		// initialize parent map
		parents.put(source, null);
		// set last value
		ComponentValue last = source;
		ComponentValue lastParent = source;
		
		// explore the fringe
		while (!fringe.isEmpty()) {
			
			// get the first value from the fringe
			ComponentValue value = fringe.remove(0);
			// check value
			if (value.equals(target)) {
				
				// update last value and stop the search
				last = value;
				// stop the search
				break;
				
			} else {
				
				// check if already visited
				if (!visited.contains(value) ) {
					
					// add the value to the set of visited ones
					visited.add(value);
					lastParent = value;
					for (ComponentValue succ : this.getDirectSuccessors(value)) {
						
						// add to fringe
						fringe.add(succ);
						// set visited parent
						parents.put(succ, lastParent);
					}
					
					// update last value
					last = value;
				}
			}
		}
		
		
		// prepare the path to reconstruct
		ValuePath path = new ValuePath();
		
		do {
			
			// add to the head to reconstruct the path from the back
			path.addFirstStep(last);
			// update to the next step by retrieving the parent
			last = parents.get(last);
			
		} while (last != null);
		
		// get computed (shortest) path
		return path;
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StateVariableValue> getValues() {
		return new ArrayList<StateVariableValue>(this.values);
	}
	
	/**
	 * 
	 */
	@Override
	public StateVariableValue getValueByName(String name) {
		StateVariableValue value = null;
		for (StateVariableValue v : this.values) {
			if (v.getLabel().equals(name)) {
				value = v;
				break;
			}
		}
		
		// check if value has been found
		if (value == null) {
			throw new RuntimeException("Value \"" + name + "\" not found on state variable \"" + this.name + "\"");
		}
		
		// get value
		return value;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"name\": \"" + this.name + "\", \"label\": \"" + this.type.getLabel()+ "\" }";
	}
	
}
