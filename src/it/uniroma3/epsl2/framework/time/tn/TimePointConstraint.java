package it.uniroma3.epsl2.framework.time.tn;

import it.uniroma3.epsl2.framework.domain.component.Constraint;
import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;

/**
 * 
 * @author anacleto
 *
 */
public class TimePointConstraint extends Constraint 
{
	private TimePoint reference;
	private TimePoint target;
	private long[] distance;
	private boolean controllable;
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param distance
	 * @param controllable
	 */
	protected TimePointConstraint(TimePoint from, TimePoint to, long[] distance, boolean controllable) {
		super("TP_DISTANCE", ConstraintCategory.TEMPORAL_CONSTRAINT);
		this.reference = from;
		this.target = to;
		this.distance = distance;
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
	 * @return
	 */
	public TimePoint getReference() {
		return reference;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getTarget() {
		return target;
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
		return "[DistanceConstraint id= (" + this.id + ":" + this.reference.getId() + "-" + this.target.getId() + ") "
				+ "<" + (this.controllable ? "c" : "u") + "> "
				+ "duration= [" + this.distance[0] + ", " + this.distance[1] + "] "
				+ "reference= " + this.reference + " "
				+ "target= " + this.target + "]";
	}
}
