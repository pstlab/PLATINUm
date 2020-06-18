package it.istc.pst.platinum.framework.time.ex;

import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalConsistencyException extends ConsistencyCheckException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public TemporalConsistencyException(String msg) {
		super(msg);
	}
}
