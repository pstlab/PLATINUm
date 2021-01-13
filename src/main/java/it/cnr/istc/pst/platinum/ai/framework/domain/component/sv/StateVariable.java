package it.cnr.istc.pst.platinum.ai.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.TransitionNotFoundException;

/**
 * 
 * @author anacleto
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
	
//	/**
//	 * 
//	 */
//	@Override
//	public void checkPseudoControllability() 
//			throws PseudoControllabilityCheckException 
//	{
//		// issues
//		List<Decision> issues = new ArrayList<>();
//		for (Decision dec : this.getActiveDecisions()) 
//		{
//			// check if token is controllable
//			if (!dec.isControllable()) 
//			{
//				// check actual duration
//				IntervalPseudoControllabilityQuery query = this.tdb.
//						createTemporalQuery(TemporalQueryType.INTERVAL_PSEUDO_CONTROLLABILITY);
//				
//				// set related temporal interval
//				query.setInterval(dec.getToken().getInterval());
//				// process
//				this.tdb.process(query);
//				// check
//				if (!query.isPseudoControllable()) {
//					// add issue
//					issues.add(dec);
//				}
//			}
//		}
//		
//		// check issues
//		if (!issues.isEmpty()) {
//			// prepare exception
//			PseudoControllabilityCheckException ex = new PseudoControllabilityCheckException("Controllability issues found on component " + this.name);
//			for (Decision issue : issues) {
//				ex.addIssue(issue);
//			}
//			// throw exception
//			throw ex;
//		}
//	}
	
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
	 * 
	 * @param label
	 * @param controllable
	 * @return
	 */
	public StateVariableValue addStateVariableValue(String label, boolean controllable) {
		return this.addStateVariableValue(label, new long [] {1l, this.tdb.getHorizon()}, controllable);
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
	public List<ValuePath> getPaths(ComponentValue source, ComponentValue target) 
	{
		// list of all acyclic paths that start from the source value and end to the target value
		List<ValuePath> result = new ArrayList<>();
		// check the case in which the source value is equal to the target value
		if (source.equals(target))
		{
			// just start the search starting from the direct successors of the current value
			for (ComponentValue successor : this.getDirectSuccessors(source))
			{
				// just avoid reflexive transition (that should not be allowed in the domain specification)
				if (!source.equals(successor)) {
					// get list of paths
					for (ValuePath path : this.computePaths(successor, target, new ArrayList<>())) {
						// add the source value in front of the path
						path.addFirstStep(source);
						// add the path to the result list
						result.add(path);
					}
				}
			}
		}
		else {
			// call recursive function to compute all acyclic paths between state variable values
			result = this.computePaths(source, target, new ArrayList<>());
		}
		
		// sort paths according to their length
		Collections.sort(result);
		// get the result list
		return result;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param visited
	 * @return
	 */
	private List<ValuePath> computePaths(ComponentValue source, ComponentValue target, List<ComponentValue> visited)
	{
		// result list
		List<ValuePath> result = new ArrayList<>();
		
		// compare source value and target value
		if (source.equals(target)) {
			// create value path
			ValuePath path = new ValuePath();
			// add last step
			path.addLastStep(source);
			// add to result
			result.add(path);
		}
		else
		{
			// add current node to visited list
			visited.add(source);
			// navigate the state variable towards the target value
			for (ComponentValue value : this.getDirectSuccessors(source))
			{
				// check cycle
				if (!visited.contains(value))
				{
					// get partial paths found through recursive call
					List<ValuePath> paths = this.computePaths(value, target, visited);
					// aggregate and build the result
					for (ValuePath path : paths) 
					{
						// add current value the current (partial) path 
						path.addFirstStep(source);
						// add the current (partial) path to the result list
						result.add(path);
					}
				}
			}
			
			// remove visited value
			visited.remove(source);
		}
		
		// get list of paths
		return result;
	}
	
	
//	public List<ValuePath> getPaths(ComponentValue source, ComponentValue target) 
//	{
//		// list of available paths
//		List<ValuePath> result = new ArrayList<>();
//		// check source and target
//		if (source.equals(target)) 
//		{
//			// initialize the path
//			List<ComponentValue> steps = new ArrayList<>();
//			steps.add(source);
//			// get successors
//			for (ComponentValue value : this.getDirectSuccessors(source)) {
//				// directly calls
//				this.computePaths(steps, value, target, result);
//			}
//		}
//		else {
//			// search for paths
//			this.computePaths(new ArrayList<ComponentValue>(), source, target, result);
//		}
//		// get resulting paths
//		return result;
//	}
	
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
	
//	/**
//	 * 
//	 * @param steps
//	 * @param current
//	 * @param target
//	 * @param result
//	 */
//	private void computePaths(List<ComponentValue> steps, ComponentValue current, ComponentValue target, List<ValuePath> result) 
//	{
//		// base step
//		if (current.equals(target)) {
//			// add target
//			steps.add(current);
//			// add path to the result
//			result.add(new ValuePath(steps));
//			// remove last added element
//			steps.remove(steps.size() -1);
//		}
//		else	// recursive step
//		{
//			//add current value to the path
//			steps.add(current);
//			// recursive calls
//			for (ComponentValue successor : this.getDirectSuccessors(current)) {
//				// check cycle
//				if (!steps.contains(successor)) {
//					// recursive call
//					this.computePaths(steps, successor, target, result);
//				}
//			}
//			// remove last added element
//			steps.remove(steps.size() - 1);
//		}
//	}
	
}
