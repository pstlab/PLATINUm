package it.cnr.istc.pst.platinum.ai.executive.lang.ex;

import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutionException extends Exception {
	private static final long serialVersionUID = 1L;
	
	protected ExecutionFailureCause cause; 
	
	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public ExecutionException(String msg, ExecutionFailureCause cause) {
		super(msg);
		this.cause = cause;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionFailureCause getFailureCause() {
		return this.cause;
	}
}
