package it.istc.pst.platinum.framework.time.lang.query;

import it.istc.pst.platinum.framework.microkernel.query.TemporalQuery;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TemporalIntervalQuery extends TemporalQuery {

	/**
	 * 
	 * @param type
	 */
	protected TemporalIntervalQuery(TemporalQueryType type) {
		super(type);
	}
}
