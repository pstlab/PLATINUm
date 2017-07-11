package it.istc.pst.platinum.framework.time.tn.lang.query;

import it.istc.pst.platinum.framework.microkernel.query.TemporalQuery;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;

/**
 * 
 * @author anacleto
 *
 */
public abstract class TimePointQuery extends TemporalQuery {

	/**
	 * 
	 * @param type
	 */
	protected TimePointQuery(TemporalQueryType type) {
		super(type);
	}
}
