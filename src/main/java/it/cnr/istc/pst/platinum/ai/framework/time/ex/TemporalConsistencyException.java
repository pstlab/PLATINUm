package it.cnr.istc.pst.platinum.ai.framework.time.ex;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;

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
