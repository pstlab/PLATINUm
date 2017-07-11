package it.istc.pst.platinum.framework.domain.component.resource;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.resource.ResourceEvent;
import it.istc.pst.platinum.framework.microkernel.query.ParameterQueryType;
import it.istc.pst.platinum.framework.parameter.lang.NumericParameter;
import it.istc.pst.platinum.framework.parameter.lang.query.CheckValuesParameterQuery;
import it.istc.pst.platinum.framework.time.tn.ex.PseudoControllabilityCheckException;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Resource extends DomainComponent implements ResourceProfileManager
{	
	protected int min;						// minimum resource capacity
	protected int max;						// maximum resource capacity
	protected int initial;					// initial resource capacity
	protected List<ComponentValue> values;	// list of values
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	protected Resource(String name, DomainComponentType type) {
		super(name, type);
		this.values = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public long getMinCapacity() {
		return this.min;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public long getMaxCapacity() {
		return this.max;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public long getInitialCapacity() {
		return this.initial;
	}
	

	/**
	 * 
	 * @param min
	 */
	public void setMinCapacity(int min) {
		this.min = min;
	}

	/**
	 * 
	 * @param max
	 */
	public void setMaxCapacity(int max) { 
		this.max = max;
	}
	
	/**
	 * 
	 * @param initial
	 */
	public void setInitialCapacity(int initial) {
		this.initial = initial;
	}
	
	/**
	 * 
	 */
	@Override
	public List<ComponentValue> getValues() {
		return new ArrayList<>(this.values);
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
	public void checkPseudoControllability() 
			throws PseudoControllabilityCheckException {
		// nothing to do
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public abstract List<ResourceEvent> getProductions();
	
	/**
	 * 
	 * @return
	 */
	@Override
	public abstract List<ResourceEvent> getConsumptions();
	
	/**
	 * 
	 * @return
	 */
	protected int checkAmount(Decision dec)
	{
		// get value of resource
		NumericParameter param = (NumericParameter) dec.getParameterByIndex(0);
		// prepare query
		CheckValuesParameterQuery query = this.pdb.createQuery(ParameterQueryType.CHECK_PARAMETER_VALUES);
		// set parameter
		query.setParameter(param);
		// process query
		this.pdb.process(query);
		// get computed parameter
		return param.getLowerBound();
	}
}
