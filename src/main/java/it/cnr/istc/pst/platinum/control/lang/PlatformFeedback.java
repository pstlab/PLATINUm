package it.cnr.istc.pst.platinum.control.lang;

/**
 * 
 * @author alessandro
 *
 */
public class PlatformFeedback extends PlatformMessage implements Comparable<PlatformFeedback> 
{
	private PlatformCommand cmd;						// the command the feedback refers to
	private PlatformFeedbackType type;					// feedback type
	private long time;									// feedback issue time
	
	/**
	 * 
	 * @param id
	 * @param cmd
	 * @param type
	 */
	public PlatformFeedback(long id, PlatformCommand cmd, PlatformFeedbackType type) {
		super(id);
		this.type = type;
		this.cmd = cmd;
		this.time = System.currentTimeMillis();
	}
	
	/**
	 * 
	 * @return
	 */
	public PlatformCommand getCmd() {
		return cmd;
	}
	
	/**
	 * 
	 * @return
	 */
	public PlatformFeedbackType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(PlatformFeedback o) {
		return this.time < o.time ? -1 : this.time > o.time ? 1 : 0;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style description
		return "{\"id\": " + this.id + ", \"cmd\": " + this.cmd + ", \"type\": \"" + this.type +"\"}";
	}
}
