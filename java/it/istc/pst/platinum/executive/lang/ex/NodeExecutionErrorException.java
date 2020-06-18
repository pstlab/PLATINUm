package it.istc.pst.platinum.executive.lang.ex;

import it.istc.pst.platinum.executive.lang.failure.ExecutionFailureCause;

/**
 * 
 * @author anacleto
 *
 */
public class NodeExecutionErrorException extends ExecutionException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param cause
	 */
	public NodeExecutionErrorException(String msg, ExecutionFailureCause cause) {
		super(msg, cause);
	}
}
