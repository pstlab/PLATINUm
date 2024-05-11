package it.cnr.istc.pst.platinum.ai.executive.lang.ex;

import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;

/**
 * 
 * @author anacleto
 *
 */
public class NodeDispatchingException extends ExecutionException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public NodeDispatchingException(String msg, ExecutionFailureCause cause) {
		super(msg, cause);
	}
}
