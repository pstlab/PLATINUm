package it.istc.pst.platinum.executive.ex;

import it.istc.pst.platinum.executive.lang.ExecutionFailureCause;

/**
 * 
 * @author anacleto
 *
 */
public abstract class ExecutionException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private ExecutionFailureCause failureCause;

	/**
	 * 
	 * @param cause
	 */
	public ExecutionException(ExecutionFailureCause cause) {
		super();
		this.failureCause = cause;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionFailureCause getFailureCause() {
		return failureCause;
	}
}
