package it.istc.pst.platinum.executive.lang.ex;

import it.istc.pst.platinum.executive.lang.failure.ExecutionFailureCause;

/**
 * 
 * @author anacleto
 *
 */
public class TokenDispatchingException extends ExecutionException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public TokenDispatchingException(String msg, ExecutionFailureCause cause) {
		super(msg, cause);
	}
}
