package it.uniroma3.epsl2.framework.domain.component.resource;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.lang.plan.resource.ResourceEvent;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Resource extends DomainComponent implements ResourceProfileManager
{	
	protected long min;						// minimum resource capacity
	protected long max;						// maximum resource capacity
	protected long initial;					// initial resource capacity
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
	public void setMinCapacity(long min) {
		this.min = min;
	}

	/**
	 * 
	 * @param max
	 */
	public void setMaxCapacity(long max) { 
		this.max = max;
	}
	
	/**
	 * 
	 * @param initial
	 */
	public void setInitialCapacity(long initial) {
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
}
