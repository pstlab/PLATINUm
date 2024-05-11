package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;

/**
 * 
 * @author anacleto
 *
 */
public class ProblemFact extends ProblemFluent
{
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param duration
	 */
	protected ProblemFact(ComponentValue value, String[] labels, 
			long[] start, long[] end, long[] duration) {
		super(ProblemFluentType.FACT, value, labels, start, end, duration);
	}
}
