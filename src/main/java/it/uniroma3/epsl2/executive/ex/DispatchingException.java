package it.uniroma3.epsl2.executive.ex;

import it.uniroma3.epsl2.executive.lang.ExecutionFailureCause;

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
