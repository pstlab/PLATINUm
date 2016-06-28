package it.uniroma3.epsl2.framework.time;

import java.util.concurrent.atomic.AtomicInteger;

import it.uniroma3.epsl2.framework.time.tn.TemporalData;
import it.uniroma3.epsl2.framework.time.tn.TimePoint;
import it.uniroma3.epsl2.framework.time.tn.TimePointConstraint;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalInterval extends TemporalData 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	
	private TimePoint start;					// time point's start time
	private TimePoint end;						// time point's end time
	private long nominalDurationLowerBound;		// interval's nominal duration lower bound
	private long nominalDurationUpperBound;		// interval's nominal duration upper bound
	private long durationLowerBound;			// interval's duration actual lower bound
	private long durationUpperBound;			// interval's duration actual upper bound
	private boolean controllable;				// interval's controllability information
	
	/**
	 * 
	 * @param duration
	 */
	protected TemporalInterval(TimePointConstraint duration) {
		super(ID_COUNTER.getAndIncrement());
		this.start = duration.getReference();
		this.end = duration.getTarget();
		this.nominalDurationLowerBound = duration.getDistanceLowerBound();
		this.nominalDurationUpperBound = duration.getDistanceUpperBound();
		this.controllable = duration.isControllable();
		
		// initialize actual lower and upper distances
		this.durationLowerBound = Long.MIN_VALUE + 1;
		this.durationUpperBound = Long.MAX_VALUE - 1;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getStartTime() {
		return start;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimePoint getEndTime() {
		return end;
	}
	
	/**
	 * Get interval's nominal duration lower bound
	 * 
	 * @return
	 */
	public long getNominalDurationLowerBound() {
		return nominalDurationLowerBound;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDurationLowerBound() {
		return this.durationLowerBound;
	}
	
	/**
	 * 
	 * @param lb
	 */
	public void setDurationLowerBound(long lb) {
		this.durationLowerBound = lb;
	}
	
	/**
	 * Get interval's nominal duration upper bound
	 * 
	 * @return
	 */
	public long getNominalDurationUpperBound() {
		return nominalDurationUpperBound;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDurationUpperBound() {
		return this.durationUpperBound;
	}
	
	/**
	 * 
	 * @param ub
	 */
	public void setDurationUpperBound(long ub) {
		this.durationUpperBound = ub;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isControllable() {
		return this.controllable;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[TemporalInterval id=" + this.id +" "
				+ "{" + (this.controllable ? "c" : "u") + "} "
				+ "duration= [" + this.durationLowerBound + ", " + this.durationUpperBound + "] "
				+ "nominal-duration= [" + this.nominalDurationLowerBound + ", " + this.nominalDurationUpperBound + "]"
				+ "start= " + this.start + " "
				+ "end= " + this.end + "]";
	}
}
