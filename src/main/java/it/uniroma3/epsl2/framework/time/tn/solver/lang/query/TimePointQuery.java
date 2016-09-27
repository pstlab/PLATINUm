package it.uniroma3.epsl2.framework.time.tn.solver.lang.query;

import it.uniroma3.epsl2.framework.microkernel.query.TemporalQuery;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;

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
