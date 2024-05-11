package it.cnr.istc.pst.platinum.ai.framework.domain.component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomain;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ComponentValue 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	protected int id;
	protected String label;
	protected ComponentValueType type;
	private long[] duration;
	private boolean controllable;
	protected DomainComponent component;
	protected List<ParameterPlaceHolder> placeholders;
	protected boolean complex;								// false if the value has implications on other values through synchronization rules
	
	/**
	 * 
	 * @param value
	 * @param type
	 * @param duration
	 * @param controllable
	 * @param component
	 */
	protected ComponentValue(String value, ComponentValueType type, long[] duration, boolean controllable, DomainComponent component) {
		this.id = ID_COUNTER.getAndIncrement();
		this.type = type;
		this.duration = duration;
		this.controllable = controllable;
		this.label = value;
		this.component = component;
		this.placeholders = new ArrayList<>();
		this.complex = false;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public ComponentValueType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public DomainComponent getComponent() {
		return component;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isControllable() {
		return controllable;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getDurationBounds() {
		return this.duration;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDurationLowerBound() {
		return this.duration[0];
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDurationUpperBound() {
		return this.duration[1];
	}
	
	/**
	 * 
	 * @param domain
	 */
	public void addParameterPlaceHolder(ParameterDomain domain) {
		this.placeholders.add(new ParameterPlaceHolder(this, domain));
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ParameterPlaceHolder> getParameterPlaceHolders() {
		return new ArrayList<>(this.placeholders);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public ParameterPlaceHolder getParameterPlaceHolderByIndex(int index) {
		return this.placeholders.get(index);
	}
	
	/**
	 * 
	 */
	public void setComplex() {
		this.complex = true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isComplex() {
		return this.complex;
	}
	
	/**
	 * 
	 * @param ph
	 * @return
	 */
	public int getParameterIndexByPlaceHolder(ParameterPlaceHolder ph) {
		int index = 0;
		boolean found = true;
		while (index < this.placeholders.size() && !found) {
			if (this.placeholders.get(index).equals(ph)) {
				found = true;
			}
			else {
				index++;
			}
		}
		
		// check if found
		if (!found) {
			throw new RuntimeException("Parameter not on value " + this + " by place-holder " + ph);
		}
		
		return index;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberOfParameterPlaceHolders() {
		return this.placeholders.size();
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentValue other = (ComponentValue) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style object description
		return "{\"label\": \"" + this.label + "\"}";
	}
}
