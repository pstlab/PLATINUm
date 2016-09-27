package it.uniroma3.epsl2.framework.time.lang.allen;

import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class DuringIntervalConstraint extends TemporalConstraint 
{
	private long[] startTimeBounds;
	private long[] endTimeBounds;
	
	/**
	 * 
	 */
	protected DuringIntervalConstraint() {
		super(TemporalConstraintType.DURING);
		this.startTimeBounds = new long[2];
		this.endTimeBounds = new long[2];
	}
	
	/**
	 * 
	 */
	@Override
	public void setBounds(long[][] bounds) {
		this.startTimeBounds = bounds[0];
		this.endTimeBounds = bounds[1];
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public void setStartTimeBounds(long[] bounds) {
		this.startTimeBounds[0] = bounds[0];
		this.startTimeBounds[1] = bounds[1];
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public void setEndTimeBounds(long[] bounds) {
		this.endTimeBounds[0] = bounds[0];
		this.endTimeBounds[1] = bounds[1];
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getStartTimeBounds() {
		return startTimeBounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getEndTimeBounds() {
		return endTimeBounds;
	}
}
