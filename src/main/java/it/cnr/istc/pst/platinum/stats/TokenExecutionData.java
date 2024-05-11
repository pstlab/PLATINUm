package it.cnr.istc.pst.platinum.stats;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class TokenExecutionData 
{
	private long timestamp;						// data record timestamp
	private String name;						// token name
	private long start;							// execution start time
	private long duration;						// observed duration
	private String component;					// component name
	
	/**
	 * 
	 * @param timestamp
	 * @param name
	 * @param start
	 * @param duration
	 * @param comp
	 */
	public TokenExecutionData(long timestamp, String name, long start, long duration, String comp) {
		this.timestamp = timestamp;
		this.name = name;
		this.start = start;
		this.duration = duration;
		this.component = comp;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComponent() {
		return component;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDuration() {
		return duration;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getStart() {
		return start;
	}
	
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
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
		TokenExecutionData other = (TokenExecutionData) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[TokenExecutionData name= " + this.name + ", start=" + this.start + ", duration= " + this.duration + "]";
	}
}
