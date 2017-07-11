package it.istc.pst.platinum.framework.time.tn.ex;

import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalConsistencyCheckException extends ConsistencyCheckException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public TemporalConsistencyCheckException(String msg) {
		super(msg);
	}
}
