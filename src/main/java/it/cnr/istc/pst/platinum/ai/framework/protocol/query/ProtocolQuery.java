package it.cnr.istc.pst.platinum.ai.framework.protocol.query;

/**
 * 
 * @author alessandroumbrico
 *
 */
public abstract class ProtocolQuery 
{
	protected ProtocolQueryType type;
	
	/**
	 * 
	 * @param type
	 */
	protected ProtocolQuery(ProtocolQueryType type) {
		this.type = type;
	}
	
	public ProtocolQueryType getType() {
		return type;
	}
}
