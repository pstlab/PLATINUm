package it.cnr.istc.pst.platinum.executive.dc;

/**
 * 
 * @author anacleto
 *
 */
public abstract class DCResult 
{	
	private DCResultType type;
	
	/**
	 * 
	 * @param type
	 */
	protected DCResult(DCResultType type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public DCResultType getType() {
		return type;
	}
}
