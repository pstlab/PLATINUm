package it.uniroma3.epsl2.framework.time.tn.uncertainty.ex;

import it.uniroma3.epsl2.framework.time.tn.ex.InconsistentDistanceConstraintException;

/**
 * 
 * @author anacleto
 *
 */
public class UnableToHandleContingentConstraintsException extends InconsistentDistanceConstraintException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg
	 */
	public UnableToHandleContingentConstraintsException(String msg) {
		super(msg);
	}
}
