package it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb;

import java.util.concurrent.atomic.AtomicInteger;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ParameterPlaceHolder;

/**
 * 
 * @author anacleto
 *
 */
public class TokenVariable 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	private int id;
	private ComponentValue value;
	private String[] labels;
	
	// solving knowledge
	private boolean mandatoryExpansion;
	private boolean mandatoryUnificaiton;
	
	/**
	 * 
	 * @param value
	 * @param labels
	 */
	protected TokenVariable(ComponentValue value, String[] labels) {
		this.id = ID_COUNTER.getAndIncrement();
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
		// JSON style object description 
		return "{ id: " + this.id + ", value: \"" + this.value + "\" }";
	}
}
