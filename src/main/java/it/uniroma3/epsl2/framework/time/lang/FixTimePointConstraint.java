package it.uniroma3.epsl2.framework.time.lang;

import it.uniroma3.epsl2.framework.time.tn.TimePoint;

/**
 * 
 * @author anacleto
 *
 */
public class FixTimePointConstraint extends UnaryTemporalConstraint<TimePoint> 
{
	private long time;
	
	/**
	 * 
	 */
	protected FixTimePointConstraint() {
		super(TemporalConstraintType.FIX_TIME_POINT);
	}
	
	/**
	 * 
	 */
	public void setTime(long time) {
		this.time = time;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}
}
