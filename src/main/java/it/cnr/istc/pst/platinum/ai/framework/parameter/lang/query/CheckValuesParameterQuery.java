package it.cnr.istc.pst.platinum.ai.framework.parameter.lang.query;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.ParameterQuery;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.ParameterQueryType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.Parameter;

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
