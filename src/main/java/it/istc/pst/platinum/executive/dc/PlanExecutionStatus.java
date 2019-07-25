package it.istc.pst.platinum.executive.dc;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author anacleto
 *
 */
public class PlanExecutionStatus 
{
	public long time;
	public Map<String, String> status;

	/**
	 * 
	 * @param time
	 */
	public PlanExecutionStatus(long time) {
		this.time = time;
		this.status = new HashMap<>();
	}

	
	public void addTimelineStatus(String tl, String token) {
		// add entry
		this.status.put(tl, token);
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
	 * @return
	 */
	public Map<String, String> getStatus() {
		return new HashMap<>(this.status);
	}
}
