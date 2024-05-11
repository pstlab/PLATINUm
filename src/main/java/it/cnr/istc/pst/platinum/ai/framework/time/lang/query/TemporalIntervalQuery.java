package it.cnr.istc.pst.platinum.ai.framework.time.lang.query;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQuery;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;

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
