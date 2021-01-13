package it.cnr.istc.pst.platinum.control.lang;

/**
 * 
 * @author anacleto
 *
 */
public class TokenDescription 
{
	private String component;				// name of the component the goal refers to 
	private String value;					// temporal predicate of the component the goal refers to
	private String[] labels;				// labels of the parameters of the predicate
	private long[] start;					// start time flexible interval
	private long[] end;						// end time flexible interval
	private long[] duration;				// duration flexible interval
	
	
	
	/**
	 * 
	 * @param component
	 * @param value
	 */
	public TokenDescription(String component, String value) {
		this.component = component;
		this.value = value;
		this.labels = null;
		this.start = null;
		this.end = null;
		this.duration = null;
	}
	
	/**
	 * 
	 * @param component
	 * @param value
	 * @param labels
	 */
	public TokenDescription(String component, String value, String[] labels) {
		this(component, value);
		this.labels = labels;
	}

	/**
	 * 
	 * @param component
	 * @param value
	 * @param labels
	 * @param start
	 */
	public TokenDescription(String component, String  value, String[] labels, long[] start) {
		this(component, value, labels);
		this.start = start;
	}
	
	/**
	 * 
	 * @param component
	 * @param value
	 * @param labels
	 * @param start
	 * @param duration
	 */
	public TokenDescription(String component, String  value, String[] labels, long[] start, long[] duration) {
		this(component, value, labels, start);
		this.duration = duration;
	}
	
	/**
	 * 
	 * @param component
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param duration
	 */
	public TokenDescription(String component, String  value, String[] labels, long[] start, long[] end, long[] duration) {
		this(component, value, labels, start, duration);
		this.end = end;
	}

	public String getComponent() {
		return component;
	}

	public String getValue() {
		return value;
	}

	public String[] getLabels() {
		return labels;
	}

	public long[] getStart() {
		return start;
	}

	public long[] getEnd() {
		return end;
	}

	public long[] getDuration() {
		return duration;
	}
	
	
}