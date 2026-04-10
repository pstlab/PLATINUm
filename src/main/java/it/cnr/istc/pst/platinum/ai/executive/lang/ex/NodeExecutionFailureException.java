package it.cnr.istc.pst.platinum.ai.executive.lang.ex;

import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;

/**
 * 
 * @author anacleto
 *
 */
public class NodeExecutionFailureException extends ExecutionException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param cause
	 */
	public NodeExecutionFailureException(String msg, ExecutionFailureCause cause) {
		super(msg, cause);
	}
}
