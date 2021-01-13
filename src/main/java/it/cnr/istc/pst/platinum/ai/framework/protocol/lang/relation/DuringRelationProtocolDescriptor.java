package it.cnr.istc.pst.platinum.ai.framework.protocol.lang.relation;

import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.TokenProtocolDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class DuringRelationProtocolDescriptor extends RelationProtocolDescriptor 
{
	private long horizon;
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param horizon
	 */
	public DuringRelationProtocolDescriptor(TokenProtocolDescriptor from, TokenProtocolDescriptor to, long horizon) {
		super("during", from, to);
		this.horizon = horizon;
	}
	
	/**
	 * 
	 */
	@Override
	public String export() {
		return this.from.getTimeline().getName() + " " + this.from.getId() + " "
				+ this.type + " [" + (this.first[0] > this.horizon ? "infty" : this.first[0]) + "," + (this.first[1] > this.horizon ? "infty" : this.first[1]) + "] "
				+ " [" + (this.second[0] > this.horizon ? "infty" : this.second[0]) + "," + (this.second[1] > this.horizon ? "infty" : this.second[1]) + "] " 
				+ this.to.getTimeline().getName() + " " + this.to.getId();
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.export();
	}
}
