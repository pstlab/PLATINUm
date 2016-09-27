package it.uniroma3.epsl2.framework.domain.component;

import it.uniroma3.epsl2.framework.parameter.lang.EnumerationConstantParameter;
import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericConstantParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameter;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public class Predicate {
	
	private int id;
	private ComponentValue value;
	private String[] labels;
	private Parameter[] parameters;
	
	/**
	 * 
	 * @param id
	 * @param value
	 */
	protected Predicate(int id, ComponentValue value) {
		this.id = id;
		this.value = value;
		this.labels = new String[value.getNumberOfParameterPlaceHolders()];
		this.parameters = new Parameter[value.getNumberOfParameterPlaceHolders()];
	}
	
	/**
	 * 
	 * @return
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public final ComponentValue getValue() {
		return this.value;
	}
	
	/**
	 * 
	 * @param index
	 * @param label
	 * @param param
	 */
	public void setParameter(int index, String label, Parameter param) {
		this.labels[index] = label;
		this.parameters[index] = param;
	}
	
//	/**
//	 * 
//	 * @param label
//	 * @param param
//	 * @return
//	 */
//	public int addParameter(String label, Parameter param) {
//		int index = 0;
//		boolean added = false;
//		while (index < this.parameters.length && !added) {
//			// check position
//			if (this.parameters[index] == null) {
//				// position found
//				this.labels[index] = label;
//				this.parameters[index] = param;
//				added = true;
//			}
//			index++;
//		}
//		// get index
//		return index;
//	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Parameter getParameterByIndex(int index) {
		return this.parameters[index];
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
			// check label
			if (this.labels[index] == label) {
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
	 * @param label
	 * @return
	 */
	public Parameter getParameter(String label) {
		// get label's index
		int index = this.getParameterIndexByLabel(label);
		return this.getParameterByIndex(index);
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
		Predicate other = (Predicate) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		String description = "[Predicate id= " + this.id +", value= " + this.value.getLabel() + "(";
		// add parameters
		for (Parameter param : this.parameters) {
			// check type
			switch (param.getType()) 
			{
				// numeric parameter
				case NUMERIC_PARAMETER_TYPE : {
					NumericParameter p = (NumericParameter) param;
					description += p.getLabel() + "= [" + p.getLowerBound() + "," + p.getUpperBound() +"] ";
				}
				break;
				
				// constant numeric parameter
				case CONSTANT_NUMERIC_PARAMETER_TYPE : {
					NumericConstantParameter p = (NumericConstantParameter) param;
					description += p.getLabel() + "= " + p.getValue() +" ";
				}
				break;
				
				// constant enumeration parameter
				case CONSTANT_ENUMERATION_PARAMETER_TYPE : {
					EnumerationConstantParameter p = (EnumerationConstantParameter) param;
					description += p.getLabel() + "= " + p.getValue() + " ";
				}
				break;
				
				// enumeration parameter
				case ENUMERATION_PARAMETER_TYPE : {
					EnumerationParameter p = (EnumerationParameter) param;
					description += p.getLabel() + "= " + p.getValue() + " ";
				}
				break;
			}
		}
		// close parameter description
		description += ")]";
		return description;
	}
}
