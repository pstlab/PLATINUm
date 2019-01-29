package it.istc.pst.platinum.control.lang;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author anacleto
 *
 */
public class Goal implements Comparable<Goal> 
{
	private static AtomicLong GoalIdCounter = new AtomicLong(0);
	private long id;
	private long timestamp;
	private GoalStatus status;
	
	/**
	 * 
	 */
	public Goal() 
	{ 
		this.id = GoalIdCounter.getAndIncrement();
		this.timestamp = System.currentTimeMillis();
		this.status = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public GoalStatus getStatus() {
		return status;
	}
	
	/**
	 * 
	 * @param status
	 */
	public void setStatus(GoalStatus status) {
		this.status = status;
	}
	

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goal other = (Goal) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Goal o) {
		// chronological order
		return this.timestamp < o.timestamp ? -1 : this.timestamp > o.timestamp ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[Goal id: " + this.id +", status: " + this.status + "]";
	}
}
