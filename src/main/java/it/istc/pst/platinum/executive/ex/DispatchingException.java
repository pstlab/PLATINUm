package it.istc.pst.platinum.executive.ex;

import it.istc.pst.platinum.executive.lang.ExecutionFailureCause;

/**
 * 
 * @author anacleto
 *
 */
public class DispatchingException extends ExecutionException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param cause
	 */
	public DispatchingException(ExecutionFailureCause cause) {
		super(cause);
	}
}
