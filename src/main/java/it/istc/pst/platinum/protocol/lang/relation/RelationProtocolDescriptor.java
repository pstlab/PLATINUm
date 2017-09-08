package it.istc.pst.platinum.protocol.lang.relation;

import it.istc.pst.platinum.protocol.lang.TokenProtocolDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public abstract class RelationProtocolDescriptor 
{
	protected String type;
	protected TokenProtocolDescriptor from;
	protected TokenProtocolDescriptor to;
	protected long[] first;
	protected long[] second;
	
	/**
	 * 
	 * @param type
	 * @param from
	 * @param to
	 */
	protected RelationProtocolDescriptor(String type, TokenProtocolDescriptor from, TokenProtocolDescriptor to) {
		this.type = type;
		this.from = from;
		this.to = to;
		this.first = null;
		this.second = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public TokenProtocolDescriptor getFrom() {
		return from;
	}
	
	/**
	 * 
	 * @return
	 */
	public TokenProtocolDescriptor getTo() {
		return to;
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public void setBounds(long [][] bounds) {
		if (bounds != null && bounds.length == 1) {
			this.first = bounds[0];
			this.second = null;
		}
		else if (bounds != null && bounds.length == 2) {
			this.first = bounds[0];
			this.second = bounds[1];
		}
		else {
			this.first = null;
			this.second = null;
		}
	}
	
//	/**
//	 * 
//	 * @param first
//	 */
//	public void setFirstBound(long[] first) {
//		this.first = first;
//	}
//	
//	/**
//	 * 
//	 * @param second
//	 */
//	public void setSecondBound(long[] second) {
//		this.second = second;
//	}
	
	/**
	 * 
	 * @return
	 */
	public String getFromTimelineName() {
		return this.from.getTimeline().getName();
	}
	

	/**
	 * 
	 * @return
	 */
	public String getFromTokenId() {
		return ""+this.from.getId();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getToTimelineName() {
		return this.to.getTimeline().getName();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getToTokenId() {
		return ""+this.to.getId();
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getFirstBound() {
		return this.first;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getSecondBound() {
		return this.second;
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract String export();
	
	/**
	 * 
	 */
	public abstract String toString();
}
