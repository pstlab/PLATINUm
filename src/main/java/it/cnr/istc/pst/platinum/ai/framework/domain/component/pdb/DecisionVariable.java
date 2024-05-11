package it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class DecisionVariable implements Comparable<DecisionVariable> 
{
	private int id;
	private String component;
	private String predicate;		// grounded predicate
	private long[] start;
	private long[] end;
	private long[] duration;
	
	/**
	 * 
	 * @param id
	 * @param component
	 * @param value
	 * @param start
	 * @param end
	 * @param duration
	 */
	protected DecisionVariable(
			int id, 
			String component, 
			String value, 
			long[] start, 
			long[] end, 
			long[] duration) {
		
		this.id = id;
		this.component = component;
		this.predicate = value;
		this.start = start;
		this.end = end;
		this.duration = duration;
	}

	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
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
	public String getValue() {
		return predicate;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getStart() {
		return start;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getEnd() {
		return end;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getDuration() {
		return duration;
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		DecisionVariable other = (DecisionVariable) obj;
		if (id != other.id)
			return false;
		return true;
	}


	/**
	 * 
	 */
	@Override
	public int compareTo(DecisionVariable o) {
		// compare start and end bounds
		return this.start[0] < o.start[0] ? -1 : 
				this.start[0] > o.start[0] ? 1 : 
					this.end[0] < o.end[0] ? -1 : 
						this.end[0] > o.end[0] ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style object description
		return "{ "
				+ "\"id\": " + this.id + ", "
				+ "\"component\": \"" + this.component + "\", "
				+ "\"predicate\": \"" + this.predicate + "\", "
				+ "\"start\": [" + this.start[0] + ", " + this.start[1] + "], "
				+ "\"end\": [" + this.end[0] + ", " + this.end[1] + "], "
				+ "\"duration\": [" + this.duration[0] + ", " + this.duration[1] + "]"
				+ " }";
	}
}
