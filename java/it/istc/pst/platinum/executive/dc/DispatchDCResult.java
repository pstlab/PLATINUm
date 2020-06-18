package it.istc.pst.platinum.executive.dc;

import java.util.Map;

/**
 * 
 * @author anacleto
 *
 */
public class DispatchDCResult extends DCResult 
{
	private Map<String, String> tokens;
	/**
	 * 
	 */
	public DispatchDCResult(Map<String, String> tokens) {
		super(DCResultType.DISPATCH);
		this.tokens = tokens; 
	}
	
	public Map<String, String> getTokens(){
		return this.tokens; 
	}
}
