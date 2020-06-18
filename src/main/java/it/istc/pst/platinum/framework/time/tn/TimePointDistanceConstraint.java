package it.istc.pst.platinum.framework.time.tn;

import it.istc.pst.platinum.framework.time.lang.BinaryTemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public class TimePointDistanceConstraint extends BinaryTemporalConstraint<TimePoint, TimePoint> 
{
	private long[] distance;
	private boolean controllable;
	
	/**
	 * 
	 */
	protected TimePointDistanceConstraint() {
		super(TemporalConstraintType.TIME_POINT_DISTANCE);
		// default behavior
		this.controllable = true;
		this.distance = new long[2];
	}
	
	/**
	 * 
	 * @param controllable
	 */
	public void setControllable(boolean controllable) {
		this.controllable = controllable;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isControllable() {
		return controllable;
	}
	
	/**
	 * 
	 * @param distance
	 */
	public void setDistanceLowerBound(long distance) {
		this.distance[0] = distance;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDistanceLowerBound() {
		return this.distance[0];
	}
	
	/**
	 * 
	 * @param distance
	 */
	public void setDistanceUpperBound(long distance) {
		this.distance[1] = distance;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDistanceUpperBound() {
		return this.distance[1];
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[TimePointDistanceConstraint id= (" + this.id + ":" + this.reference.getId() + "-" + this.target.getId() + ") "
				+ "<" + (this.controllable ? "c" : "u") + "> "
				+ "duration= [" + this.distance[0] + ", " + this.distance[1] + "] "
				+ "reference= " + this.reference + " "
				+ "target= " + this.target + "]";
	}
}
