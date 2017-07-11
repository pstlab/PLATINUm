package it.istc.pst.platinum.framework.microkernel.query;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalQuery extends Query 
{
	private TemporalQueryType type;
	
	/**
	 * 
	 */
	protected TemporalQuery(TemporalQueryType type) {
		super();
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public TemporalQueryType getType() {
		return type;
	}
	
}
