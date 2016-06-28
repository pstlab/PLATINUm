package it.uniroma3.epsl2.framework.time.tn.ex;

import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;

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
