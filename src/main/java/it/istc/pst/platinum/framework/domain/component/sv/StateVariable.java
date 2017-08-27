package it.istc.pst.platinum.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.ex.TransitionNotFoundException;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.time.lang.query.IntervalPseudoControllabilityQuery;
import it.istc.pst.platinum.framework.time.tn.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class StateVariable extends DomainComponent<StateVariableValue> 
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
	 */
	@Override
	public void checkPseudoControllability() 
			throws PseudoControllabilityCheckException 
	{
		// issues
		List<Decision> issues = new ArrayList<>();
		for (Decision dec : this.getActiveDecisions()) 
		{
			// check if token is controllable
			if (!dec.isControllable()) 
			{
				// check actual duration
				IntervalPseudoControllabilityQuery query = this.tdb.
						createTemporalQuery(TemporalQueryType.INTERVAL_PSEUDO_CONTROLLABILITY);
				
				// set related temporal interval
				query.setInterval(dec.getToken().getInterval());
				// process
				this.tdb.process(query);
				// check
				if (!query.isPseudoControllable()) {
					// add issue
					issues.add(dec);
				}
			}
		}
		
		// check issues
		if (!issues.isEmpty()) {
			// prepare exception
			PseudoControllabilityCheckException ex = new PseudoControllabilityCheckException("Controllability issues found on component " + this.name);
			for (Decision issue : issues) {
				ex.addIssue(issue);
			}
			// throw exception
			throw ex;
		}
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
	public Transition getTransition(StateVariableValue reference, StateVariableValue target) 
			throws TransitionNotFoundException {
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
	public List<StateVariableValue> getDirectSuccessors(StateVariableValue value) {
		// get successors as a list
		return new ArrayList<>(this.transitions.get(value).keySet());
	}
	
	/**
	 * Analyze the state machine of the SV to extract all possible "acyclic" 
	 * paths between two values.
	 * 
	 * Please note that "acyclic" paths do not consider the case in which the 
	 * source is the same value as the target of the path.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public List<List<StateVariableValue>> getPaths(StateVariableValue source, StateVariableValue target) 
	{
		// list of available paths
		List<List<StateVariableValue>> result = new ArrayList<>();
		// check source and target
		if (source.equals(target)) 
		{
			// initialize the path
			List<StateVariableValue> path = new ArrayList<>();
			path.add(source);
			// get successors
			for (StateVariableValue value : this.getDirectSuccessors(source)) {
				// directly calls
				this.computePaths(path, value, target, result);
			}
		}
		else {
			// search for paths
			this.computePaths(new ArrayList<StateVariableValue>(), source, target, result);
		}
		// get resulting paths
		return result;
	}
	
	/**
	 * 
	 */
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
		return "[StateVariable name= " + this.name + " label= " + this.type.getLabel()+ "]";
	}
	
	/**
	 * 
	 * @param path
	 * @param current
	 * @param target
	 * @param result
	 */
	private void computePaths(List<StateVariableValue> path, StateVariableValue current, StateVariableValue target, List<List<StateVariableValue>> result) 
	{
		// base step
		if (current.equals(target)) {
			// add target
			path.add(current);
			// add path to the result
			result.add(new ArrayList<>(path));
			// remove last added element
			path.remove(path.size() -1);
		}
		else	// recursive step
		{
			// check cycle
			if (path.contains(current)) {
				// skip path
				this.logger.debug("Avoid cycles on SV paths\n- (partial) path: " + path + "\n- current value: " + current + "\n- target: " + target + "\n");
			}
			else	// no cycle found 
			{
				//add current value to the path
				path.add(current);
				// recursive calls
				for (StateVariableValue successor : this.getDirectSuccessors(current)) {
					// recursive call
					this.computePaths(path, successor, target, result);
				}
				// remove last added element
				path.remove(path.size() - 1);
			}
		}
	}
	
}
