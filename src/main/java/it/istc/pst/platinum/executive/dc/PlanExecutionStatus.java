package it.istc.pst.platinum.executive.dc;

import java.util.HashMap;
import java.util.Map;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class PlanExecutionStatus 
{
	private long time;
	private Map<String, String> status;

	/**
	 * 
	 * @param time
	 */
	protected PlanExecutionStatus(long time) {
		this.time = time;
		this.status = new HashMap<>();
	}
	
	/**
	 * 
	 */
	public void addTimelineStatus(ExecutionNode node) {
		// get timeline description
		String tl = node.getComponent() + "." + node.getTimeline();
		// get token (ground) signature
		String signature = node.getGroundSignature();
		// add entry to the map
		this.status.put(tl, signature);
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
