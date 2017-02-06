package it.uniroma3.epsl2.framework.domain.component;

import it.uniroma3.epsl2.framework.parameter.lang.EnumerationParameter;
import it.uniroma3.epsl2.framework.parameter.lang.NumericParameter;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public class Predicate 
{
	private int id;
	private ComponentValue value;
	private String[] labels;
	private Parameter<?>[] parameters;
	
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
	 * @return
	 */
	public Parameter<?>[] getParameters()
	{
		return this.parameters;
	}
	
	public String getGroundSignature()
	{
		// set signature
		String sign = this.value.getLabel();
		// check parameter values
		for(Parameter<?> param : this.parameters) 
		{
			sign += "-";
			// check parameter type
			switch (param.getType())
			{
				case ENUMERATION_PARAMETER_TYPE : {
					// get parameter 
					EnumerationParameter ep = (EnumerationParameter) param;
					// get allowed values
					for (int index = 0; index < ep.getValues().length; index++) {
						// get value 
						String val = ep.getValues()[index];
						sign += val;
						if (index < ep.getValues().length - 1) {
							sign += "_";
						}
					}
				}
				break;
			
				case NUMERIC_PARAMETER_TYPE : {
					// get parameter
					NumericParameter np = (NumericParameter) param;
					sign += np.getLowerBound() + "_" + np.getUpperBound();
				}
				break;
			}
 		}
		
		// get signature
		return sign;
	}
	
	/**
	 * 
	 * @param index
	 * @param label
	 * @param param
	 */
	public void setParameter(int index, String label, Parameter<?> param) {
		this.labels[index] = label;
		this.parameters[index] = param;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Parameter<?> getParameterByIndex(int index) {
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
	public Parameter<?> getParameter(String label) {
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
	public String toString() 
	{
		String description = "[Predicate id= " + this.id +", value= " + this.value.getLabel() + "(";
		// add parameters
		for (int index = 0; index < this.parameters.length; index++) 
		{
			// get parameter
			Parameter<?> param = this.parameters[index];
			// check type
			switch (param.getType())
			{
				case ENUMERATION_PARAMETER_TYPE : {
					// get parameter
					EnumerationParameter enu = (EnumerationParameter) param;
					description += param.getLabel() + "= ";
					for (String val : enu.getValues()) {
						description += val;
					}
				}
				break;
				
				case NUMERIC_PARAMETER_TYPE : {
					// get parameter
					NumericParameter num = (NumericParameter) param;
					description += param.getLabel() + "= ";
					if (num.getLowerBound() == num.getUpperBound()) {
						description += num.getLowerBound();
					}
					else {
						description += "[" + num.getLowerBound() + " , " + num.getUpperBound() + "] ";
					}
				}
				break;
			}
			
			if (index < this.parameters.length -1) {
				description += ", ";
			}
		}
		// close parameter description
		description += ")]";
		return description;
	}
}
