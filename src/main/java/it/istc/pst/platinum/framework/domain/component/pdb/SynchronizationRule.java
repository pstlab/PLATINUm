package it.istc.pst.platinum.framework.domain.component.pdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;

/**
 * 
 * @author anacleto
 *
 */
public class SynchronizationRule 
{
	private int idCounter;
	private TokenVariable triggerer;
	private Map<Integer, TokenVariable> tokenVariables;
	private Map<TokenVariable, Map<TokenVariable, List<SynchronizationConstraint>>> constraints;
	
	/**
	 * 
	 * @param triggerer
	 */
	protected SynchronizationRule(ComponentValue triggerer, String[] labels) {
		this.idCounter = 0;
		this.tokenVariables = new HashMap<>();
		this.constraints = new HashMap<>();
		// create trigger token variable
		this.triggerer = this.addTokenVariable(triggerer, labels);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public TokenVariable addTokenVariable(ComponentValue value, String[] labels) {
		// create token variable
		TokenVariable v = new TokenVariable(this.getNextTokenVariableId(), value, labels);
		this.tokenVariables.put(v.getId(), v);
		return v;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param rel
	 */
	public TemporalSynchronizationConstraint addTemporalConstraint(TokenVariable source, TokenVariable target, RelationType type, long[][] bounds) {
		// create relation
		TemporalSynchronizationConstraint rel = new TemporalSynchronizationConstraint(type, source, target, bounds);
		// add constraint
		if (!this.constraints.containsKey(source)) {
			this.constraints.put(source, new HashMap<TokenVariable, List<SynchronizationConstraint>>());
		}
		if (!this.constraints.get(source).containsKey(target)) {
			this.constraints.get(source).put(target, new ArrayList<SynchronizationConstraint>());
		}
		// set relation
		this.constraints.get(source).get(target).add(rel);
		// get relation
		return rel;
	}
	
	/**
	 * 
	 * @param source
	 * @param target
	 * @param type
	 * @param referenceLabel
	 * @param targetLabel
	 * @return
	 */
	public ParameterSynchronizationConstraint addParameterConstraint(TokenVariable source, TokenVariable target, RelationType type, String referenceLabel, String targetLabel) {
		// create relation
		ParameterSynchronizationConstraint rel = new ParameterSynchronizationConstraint(type, source, referenceLabel, target, targetLabel);
		// add constraint
		if (!this.constraints.containsKey(source)) {
			this.constraints.put(source, new HashMap<TokenVariable, List<SynchronizationConstraint>>());
		}
		if (!this.constraints.get(source).containsKey(target)) {
			this.constraints.get(source).put(target, new ArrayList<SynchronizationConstraint>());
		}
		// set relation
		this.constraints.get(source).get(target).add(rel);
		// get relation
		return rel;
	}
	
	/**
	 * 
	 * @param source
	 * @param type
	 * @param referenceLabel
	 * @param value
	 * @return
	 */
	public ParameterSynchronizationConstraint addBindingConstraint(TokenVariable source, RelationType type, String referenceLabel, String value) { 
		// create constraint
		ParameterSynchronizationConstraint rel = new ParameterSynchronizationConstraint(type, source, referenceLabel, source, value);
		// add constraint
		if (!this.constraints.containsKey(source)) {
			this.constraints.put(source, new HashMap<TokenVariable, List<SynchronizationConstraint>>());
		}
		if (!this.constraints.get(source).containsKey(source)) {
			this.constraints.get(source).put(source, new ArrayList<SynchronizationConstraint>());
		}
		// set relation
		this.constraints.get(source).get(source).add(rel);
		return rel;
	}
	
	/**
	 * 
	 * @return
	 */
	public TokenVariable getTriggerer() {
		return triggerer;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<SynchronizationConstraint> getConstraints() {
		List<SynchronizationConstraint> constraints = new ArrayList<>();
		for (TokenVariable reference : this.constraints.keySet()) {
			for (TokenVariable target : this.constraints.get(reference).keySet()) {
				constraints.addAll(this.constraints.get(reference).get(target));
			}
		}
		return constraints;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TokenVariable> getTokenVariables() {
		List<TokenVariable> vars = new ArrayList<>();
		for (TokenVariable var : this.tokenVariables.values()) {
			if (!var.equals(this.triggerer)) {
				vars.add(var);
			}
		}
		// get token variables
		return vars;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[SynchronizationRule triggerer= " + this.triggerer + " constraints= " + this.constraints + "]";
	}
	
	/**
	 * 
	 * @return
	 */
	private synchronized int getNextTokenVariableId() {
		return idCounter++;
	}
}
