package it.cnr.istc.pst.platinum.executive.dc;

/**
 * 
 * @author anacleto
 *
 */
public class WaitDCResult extends DCResult 
{
	private long until; 
	/**
	 * 
	 */
	public WaitDCResult(long until) {
		super(DCResultType.WAIT);
		this.until = until; 
	}
	
	public long getUntil(){
		return this.until; 
	}
}
