package it.istc.pst.platinum.executive.pdb;

import it.istc.pst.platinum.framework.parameter.lang.ParameterType;

/**
 * 
 * @author anacleto
 *
 */
public class NodePredicate 
{
	private String component;
	private String timeline;
	private String signature;
	private ParameterType[] types;
	private String[] parameters;
	
	/**
	 * 
	 * @param component
	 * @param timeline
	 * @param signature
	 * @param types
	 * @param parameters
	 */
	protected NodePredicate(String component, String timeline, String signature, ParameterType[] types, String[] parameters) {
		this.component = component;
		this.timeline = timeline;
		this.signature = signature;
		this.types = types;
		this.parameters = parameters;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComponent() {
		return component;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTimeline() {
		return timeline;
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
