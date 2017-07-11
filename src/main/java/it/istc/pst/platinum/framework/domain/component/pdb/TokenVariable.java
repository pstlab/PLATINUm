package it.istc.pst.platinum.framework.domain.component.pdb;

import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.ParameterPlaceHolder;

/**
 * 
 * @author anacleto
 *
 */
public class TokenVariable 
{
	private int id;
	private ComponentValue value;
	private String[] labels;
	
	// solving knowledge
	private boolean mandatoryExpansion;
	private boolean mandatoryUnificaiton;
	
	/**
	 * 
	 * @param id
	 * @param value
	 * @param labels
	 */
	protected TokenVariable(int id, ComponentValue value, String[] labels) {
		this.id = id;
		this.value = value;
		this.labels = labels;
		
		this.mandatoryExpansion = false;
		this.mandatoryUnificaiton = false;
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
	 */
	public void setMandatoryExpansion() {
		this.mandatoryExpansion = true;
		this.mandatoryUnificaiton = false;
	}
	
	/**
	 * 
	 */
	public void setMandatoryUnification() {
		this.mandatoryUnificaiton = true;
		this.mandatoryExpansion = false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMandatoryExpansion() {
		return mandatoryExpansion;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMandatoryUnificaiton() {
		return mandatoryUnificaiton;
	}
	
	/**
	 * 
	 * @return
	 */
	public ComponentValue getValue() {
		return value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getParameterLabels() {
		return this.labels;
	}
	
	/**
	 * 
	 * @param label
	 * @return
	 */
	public ParameterPlaceHolder getParameterPlaceHolderByLabel(String label) {
		// get placeholder
		return this.value.getParameterPlaceHolderByIndex(this.getParameterIndexByLabel(label));
	}
	
	/**
	 * 
	 * @param label
	 * @return
	 */
	public int getParameterIndexByLabel(String label) {
		int index = 0;
		boolean found = false;
		while (index < this.labels.length && !found) {
			if (this.labels[index].equals(label)) {
				found = true;
			}
			else {
				index++;
			}
		}
		
		// check 
		if (!found) {
			throw new RuntimeException("Parameter label not found label= " + label);
		}
		
		return index;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getParameterLabelByIndex(int index) {
		return this.labels[index];
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumberOfParameters() {
		return this.labels.length;
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
		TokenVariable other = (TokenVariable) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[TokenVariable " + this.id +  ":" + this.value + "]";
	}
}
