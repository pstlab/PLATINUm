package it.uniroma3.epsl2.framework.domain.component.sv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.ex.TransitionNotFoundException;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.time.lang.query.IntervalPseudoControllabilityQuery;
import it.uniroma3.epsl2.framework.time.tn.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class StateVariable extends DomainComponent 
{
	// list of values
	protected List<StateVariableValue> values;
	// SV's transition function
	protected Map<ComponentValue, Map<ComponentValue, Transition>> transitions;
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	protected StateVariable(String name, DomainComponentType type) {
		super(name, type);
		// initialize the list of values
		this.values = new ArrayList<>();
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
	 * @param value
	 * @param controllable
	 * @return
	 */
	public ComponentValue addValue(String value, boolean controllable) {
		// create value
		ComponentValue v = this.doCreateValue(value, new long[] {1, this.tdb.getHorizon()}, controllable);
		// add transition
		this.transitions.put(v, new HashMap<ComponentValue, Transition>());
		// get value
		return v;
	}
	
	/**
	 * 
	 * @param value
	 * @param controllable
	 * @param dmin
	 * @param dmax
	 * @return
	 */
	public ComponentValue addValue(String value, long[] duration, boolean controllable) {
		// create value
		ComponentValue v = this.doCreateValue(value, duration, controllable);
		// add transition
		this.transitions.put(v, new HashMap<ComponentValue, Transition>());
		// get value
		return v;
	}

	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 */
	public Transition addValueTransition(ComponentValue reference, ComponentValue target) {
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
	public List<ComponentValue> getDirectSuccessors(ComponentValue value) {
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
	public List<List<ComponentValue>> getPaths(ComponentValue source, ComponentValue target) {
		// list of available paths
		List<List<ComponentValue>> result = new ArrayList<>();
		// search for paths
		this.getPaths(new ArrayList<ComponentValue>(), source, target, result);
		// get resulting paths
		return result;
	}
	
	/**
	 * 
	 */
	@Override
	public List<ComponentValue> getValues() {
		return new ArrayList<ComponentValue>(this.values);
	}
	
	/**
	 * 
	 */
	@Override
	public ComponentValue getValueByName(String name) {
		ComponentValue value = null;
		for (ComponentValue v : this.values) {
			if (v.getLabel().equals(name)) {
				value = v;
				break;
			}
		}
		
		// check if value has been found
		if (value == null) {
			throw new RuntimeException("Value " + name + " not found");
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
	 * @param value
	 * @param duration
	 * @param controllable
	 * @return
	 */
	protected StateVariableValue doCreateValue(String value, long[] duration, boolean controllable) {
		// create and add value
		StateVariableValue v = new StateVariableValue(value, duration, controllable, this);
		this.values.add(v);
		return v;
	}
	
	/**
	 * 
	 * @param path
	 * @param current
	 * @param target
	 * @param result
	 */
	private void getPaths(List<ComponentValue> path, ComponentValue current, ComponentValue target, List<List<ComponentValue>> result) {
		// check current value w.r.t. the target
		if (current.equals(target) && !path.isEmpty()) {
			// add target
			path.add(current);
			// add path to the result
			result.add(path);
		}
		else if (!path.contains(current)) {
			// intermediate step
			path.add(current);
			for (ComponentValue successor : this.getDirectSuccessors(current)) {
				// recursive call
				this.getPaths(new ArrayList<>(path), successor, target, result);
			}
		}
	}
	
}
