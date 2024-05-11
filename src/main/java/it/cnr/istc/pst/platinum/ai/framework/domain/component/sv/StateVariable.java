package it.cnr.istc.pst.platinum.ai.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.TransitionNotFoundException;

/**
 * 
 * @author alessandro
 *
 */
public abstract class StateVariable extends DomainComponent 
{
	protected List<StateVariableValue> values;							
	// SV's transition function
	protected Map<StateVariableValue, Map<StateVariableValue, Transition>> transitions;
	
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
		// add transition
		this.transitions.put(value, new HashMap<StateVariableValue, Transition>());
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
		// add transition
		this.transitions.get(reference).put(target, t);
		// get transition
		return t;
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 */
	public Transition getTransition(ComponentValue reference, ComponentValue target) 
			throws TransitionNotFoundException 
	{
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
	 * Analyze the state machine of the SV to extract all possible "acyclic" 
	 * paths between two values.
	 * 
	 * Please note that "acyclic" paths do not consider the case in which the 
	 * source is the same value as the target of the path. Indeed, it could be 
	 * necessary to find acyclic paths that starts and ends with the same state
	 * variable value
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public List<ValuePath> getPaths(ComponentValue source, ComponentValue target) {
		
		// list of all acyclic paths that start from the source value and end to the target value
		List<ValuePath> paths = new ArrayList<>();	
		// initialize value path
		ValuePath path = new ValuePath();
		path.addLastStep(source);
		// call recursive method to unfold state variable description and build possible paths
		this.computePaths(source, target, path, paths);
		// get the result list
		return paths;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	private void computePaths(ComponentValue source, ComponentValue target, ValuePath path, List<ValuePath> paths) { //, List<ComponentValue> visited) {
		
		// compare source value and target value
		if (source.equals(target)) {
			// add path to the paths
			paths.add(path);
			
		} else {
			
			// add source to the path
//			path.addLastStep(source);
			// check successors of the (current) source node
			for (ComponentValue successor : this.getDirectSuccessors(source)) {
				
				// check if the successor is contained in the current value path to avoid cycles
				if (!path.contains(successor)) {
					// create an alternative path for each successor
					ValuePath otherPath = new ValuePath(path.getSteps());
					
					// add source
					path.addLastStep(source);
					// add successor
					otherPath.addLastStep(successor);
					// recursive call
					this.computePaths(successor, target, otherPath, paths);
					
				}
//				else {
//					
//					// log something
//					System.out.println("\n>>>>> Source [" + source.getLabel() + "] Successor [" + successor.getLabel()+ "], Target [" + target.getLabel() + "]\n--> skip path: " + path + "\n");
//				}
			}
		}
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
