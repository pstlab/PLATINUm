package it.cnr.istc.pst.platinum.ai.framework.domain.component.sv;

import java.util.HashMap;
import java.util.Map;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ParameterPlaceHolder;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public final class Transition 
{
	private StateVariableValue reference;
	private StateVariableValue target;
	private Map<Integer, Map<Integer, ParameterConstraintType>> constraints;
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	protected Transition(StateVariableValue reference, StateVariableValue target) {
		this.reference = reference;
		this.target = target;
		this.constraints = new HashMap<>();
		// initialize constraints
		int index = 0;
		while (index < this.reference.getNumberOfParameterPlaceHolders()) {
			this.constraints.put(index, new HashMap<Integer, ParameterConstraintType>());
			index++;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public StateVariableValue getReference() {
		return reference;
	}
	
	/**
	 * 
	 * @return
	 */
	public StateVariableValue getTarget() {
		return target;
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param type
	 */
	public void addParameterConstraint(ParameterPlaceHolder reference, ParameterPlaceHolder target, ParameterConstraintType type) {
		// get reference index
		int refIndex = this.reference.getParameterIndexByPlaceHolder(reference);
		int targetIndex = this.target.getParameterIndexByPlaceHolder(target);
		// add parameter
		this.constraints.get(refIndex).put(targetIndex, type);
	}
	
	/**
	 * 
	 * @param referenceParameterIndex
	 * @param targetParameterIndex
	 * @param type
	 */
	public void addParameterConstraint(int referenceParameterIndex, int targetParameterIndex, ParameterConstraintType type) {
		// add parameter constraint
		this.constraints.get(referenceParameterIndex).put(targetParameterIndex, type);
	}
	
	/**
	 * 
	 * @param referenceParameterIndex
	 * @param targetParameterIndex
	 * @return
	 */
	public boolean existsParameterConstraint(int referenceParameterIndex, int targetParameterIndex) {
		return this.constraints.containsKey(referenceParameterIndex) && this.constraints.get(referenceParameterIndex).containsKey(targetParameterIndex);
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 */
	public ParameterConstraintType getParameterConstraintType(int reference, int target) {
		return this.constraints.get(reference).get(target);
	}
}
