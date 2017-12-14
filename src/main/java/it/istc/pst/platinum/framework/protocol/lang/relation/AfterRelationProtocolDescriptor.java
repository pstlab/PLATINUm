package it.istc.pst.platinum.framework.protocol.lang.relation;

import it.istc.pst.platinum.framework.protocol.lang.TokenProtocolDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class AfterRelationProtocolDescriptor extends RelationProtocolDescriptor 
{
	private long horizon;
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param horizon
	 */
	public AfterRelationProtocolDescriptor(TokenProtocolDescriptor from, TokenProtocolDescriptor to, long horizon) {
		super("after", from, to);
		this.horizon = horizon;
	}
	
	/**
	 * 
	 */
	@Override
	public String export() {
		return this.from.getTimeline().getName() + " " + this.from.getId() + " "
				+ this.type + " [" + (this.first[0] > this.horizon ? "infty" : this.first[0]) + "," + (this.first[1] > this.horizon ? "infty" : this.first[1]) + "] "
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
