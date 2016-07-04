package it.uniroma3.epsl2.executive.pdb;

import it.uniroma3.epsl2.framework.parameter.lang.ParameterType;

/**
 * 
 * @author anacleto
 *
 */
public class NodePredicate {

	private String signature;
	private ParameterType[] types;
	private String[] parameters;
	
	/**
	 * 
	 * @param signature
	 * @param types
	 * @param parameters
	 */
	protected NodePredicate(String signature, ParameterType[] types, String[] parameters) {
		this.signature = signature;
		this.types = types;
		this.parameters = parameters;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSignature() {
		return signature;
	}
	
	/**
	 * 
	 * @return
	 */
	public ParameterType[] getParameterTypes() {
		return types;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getParameters() {
		return parameters;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getValueOfParameter(int index) {
		return this.parameters[index];
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public ParameterType getTypeOfParameter(int index) {
		return this.types[index];
	}

	/**
	 * 
	 * @return
	 */
	public String getGroundSignature() {
		String str = this.signature;
		for (String param : this.parameters) {
			str += "-" + param;
		}
		return str;
	}
}
