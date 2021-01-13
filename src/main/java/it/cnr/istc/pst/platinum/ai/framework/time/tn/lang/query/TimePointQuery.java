package it.cnr.istc.pst.platinum.ai.framework.time.tn.lang.query;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQuery;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;

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
