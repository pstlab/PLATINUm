package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;

/**
 * 
 * @author anacleto
 *
 */
public class ProblemGoal extends ProblemFluent
{
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param duration
	 */
	protected ProblemGoal(ComponentValue value, String[] labels, 
			long[] start, long[] end, long[] duration) {
		super(ProblemFluentType.GOAL, value, labels, start, end, duration);
	}
}
