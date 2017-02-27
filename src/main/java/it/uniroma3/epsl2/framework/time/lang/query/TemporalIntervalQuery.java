package it.uniroma3.epsl2.framework.time.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQuery;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;

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