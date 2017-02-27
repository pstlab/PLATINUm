package it.uniroma3.epsl2.framework.domain.component;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomain;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ComponentValue 
{
	private static int ID_COUNTER = 0;
	protected int id;
	protected String label;
	protected ComponentValueType type;
	private long[] duration;
	private boolean controllable;
	protected DomainComponent component;
	protected List<ParameterPlaceHolder> placeholders;
	
	/**
	 * 
	 * @param value
	 * @param type
	 * @param duration
	 * @param controllable
	 * @param component
	 */
	protected ComponentValue(String value, ComponentValueType type, long[] duration, boolean controllable, DomainComponent component) {
		this.id = getNextId();
		this.type = type;
		this.duration = duration;
		this.controllable = controllable;
		this.label = value;
		this.component = component;
		this.placeholders = new ArrayList<>();
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
		return "[ComponentValue <" + (this.controllable ? "c" : "u") + "> id=" + this.id + " value= " + this.label + "]";
	}
	
	/**
	 * 
	 * @return
	 */
	private synchronized static int getNextId() {
		return ID_COUNTER++;
	}
}
