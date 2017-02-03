package it.uniroma3.epsl2.framework.parameter.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.ParameterQuery;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQueryType;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public class CheckValuesParameterQuery extends ParameterQuery 
{
	private Parameter<?> param;
	
	/**
	 * 
	 */
	protected CheckValuesParameterQuery() {
		super(ParameterQueryType.CHECK_PARAMETER_VALUES);
	}
	
	/**
	 * 
	 * @param param
	 */
	public void setParameter(Parameter<?> param) {
		this.param = param;
	}
	
	/**
	 * 
	 * @return
	 */
	public Parameter<?> getParameter() {
		return param;
	}
}
