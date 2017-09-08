package it.istc.pst.platinum.protocol.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class TokenProtocolDescriptor implements Comparable<TokenProtocolDescriptor>
{
	private long id;								// token's id
	private TimelineProtocolDescriptor timeline;	// timeline
	private String predicate;					// token predicate
	// predicate's parameter information
	private List<EPSLParameterDescriptor> params;
	// token's temporal information
	private long[] startTimeBounds;				// token's start time interval
	private long[] endTimeBounds;				// token's end time interval
	private long[] durationBounds;				// token's duration interval
	
	/**
	 * 
	 * @param id
	 * @param timeline
	 * @param predicate
	 */
	public TokenProtocolDescriptor(long id, TimelineProtocolDescriptor timeline, String predicate) {
		this.id = id;
		this.timeline = timeline;
		this.predicate = predicate;
		this.params = new ArrayList<EPSLParameterDescriptor>();
		
		// add token to timeline
		timeline.addToken(this);
	}

	/**
	 * 
	 * @param startTimeBounds
	 * @param endTimeBounds
	 * @param durationBounds
	 */
	public void setTemporalInformation(long[] startTimeBounds, long[] endTimeBounds, long[] durationBounds) {
		this.startTimeBounds = startTimeBounds;
		this.endTimeBounds = endTimeBounds;
		this.durationBounds = durationBounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isControllable() {
		// check predicate
		return !(this.timeline.isExternal() || this.predicate.startsWith("_"));
	}
	
	/**
	 * 
	 * @param param
	 */
	public void addParameter(EPSLParameterDescriptor param) {
		this.params.add(param);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @return
	 */
	public TimelineProtocolDescriptor getTimeline() {
		return timeline;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPredicate() {
		return this.predicate;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean hasParameters() {
		return !this.params.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<EPSLParameterDescriptor> getParameters() {
		return new ArrayList<EPSLParameterDescriptor>(this.params);
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getParameterNames() {
		String[] pnames = new String[this.params.size()];
		for (int index = 0; index < pnames.length; index++) {
			pnames[index] = this.params.get(index).getName();
		}
		return pnames;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public EPSLParameterDescriptor getParameter(int index) {
		return this.params.get(index);
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getStartTimeBounds() {
		return startTimeBounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getEndTimeBounds() {
		return endTimeBounds;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getDurationBounds() {
		return durationBounds;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		// get instance
		return (obj instanceof TokenProtocolDescriptor) &&
				this.timeline.equals(((TokenProtocolDescriptor) obj).timeline) &&
				this.predicate.equals(((TokenProtocolDescriptor) obj).predicate) &&
				this.startTimeBounds[0] == ((TokenProtocolDescriptor) obj).startTimeBounds[0] &&
				this.startTimeBounds[1] == ((TokenProtocolDescriptor) obj).startTimeBounds[1] &&
				this.endTimeBounds[0] == ((TokenProtocolDescriptor) obj).endTimeBounds[0] &&
				this.endTimeBounds[1] == ((TokenProtocolDescriptor) obj).endTimeBounds[1] &&
				this.durationBounds[0] == ((TokenProtocolDescriptor) obj).durationBounds[0] &&
				this.durationBounds[1] == ((TokenProtocolDescriptor) obj).durationBounds[1];
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(TokenProtocolDescriptor o) {
		return this.startTimeBounds[0] <= o.getStartTimeBounds()[0] ? -1 : 1;
	}
	
	/**
	 * 
	 * @return
	 */
	public String export() {
		String str = "token " + this.id + " " + (!this.timeline.isExternal() && !this.isControllable() ? "uncontrollable" : "") 
				+  " { " + this.predicate + "";

		// set parameters
		for (EPSLParameterDescriptor param : this.params) {
			// check type
			if (param.getType().equals(ParameterTypeDescriptor.NUMERIC)) {
				str += "-" +  param.getBounds()[0];
			}
			else {
				str += "-" + param.getValues()[0];
			}
		}
		
		str += " ";
		
		str += "[" + this.endTimeBounds[0] + "," + this.endTimeBounds[1] + "] "
				+ "[" + this.durationBounds[0] + ", " + this.durationBounds[1] + "] }";
		return str;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String str = "<token" + this.id + "> AT [" + this.startTimeBounds[0] + ", " + this.startTimeBounds[1] + "] "
			+ "[" + this.endTimeBounds[0] + ", " + this.endTimeBounds[1] + "] "
			+ "[" + this.durationBounds[0] +", " + this.durationBounds[1] +"] "
			+ "" + (!this.timeline.isExternal() && !this.isControllable() ? "uncontrollable" : " ");
		str += " " + this.predicate + "( ";
		// set parameters
		for (EPSLParameterDescriptor param : this.params) {
			str += param.getName() + " = ";
			
			if (param.getType().equals(ParameterTypeDescriptor.NUMERIC)) {
				str += param.getBounds()[0]+ " ";
			}
			else {
				str += param.getValues()[0] + " ";
			}
		}
		str += ") ";
		return str;
	}
	
}
