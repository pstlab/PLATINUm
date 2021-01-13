package it.cnr.istc.pst.platinum.ai.framework.protocol.lang.relation;

import it.cnr.istc.pst.platinum.ai.framework.protocol.lang.TokenProtocolDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class EndsDuringRelationProtocolDescriptor extends RelationProtocolDescriptor 
{
	private long horizon;
	
	/**
	 * 
	 * @param from
	 * @param to
	 * @param horizon
	 */
	public EndsDuringRelationProtocolDescriptor(TokenProtocolDescriptor from, TokenProtocolDescriptor to, long horizon) {
		super("ends_during", from, to);
		this.horizon = horizon;
	}
	
	/**
	 * 
	 */
	@Override
	public String export() {
		return this.to.getTimeline().getName() + " " + this.to.getId() + " "
				+ "start_before_end" + " [" + (this.first[0] > this.horizon ? "infty" : this.first[0]) + "," + (this.first[1] > this.horizon ? "infty" : this.first[1]) + "] "
				+ this.from.getTimeline().getName() + " " + this.from.getId() + "\n"
				+ "\t\t"
				+ this.from.getTimeline().getName() + " " + this.from.getId() + " "
				+ "end_before_end" + " [" + (this.second[0] > this.horizon ? "infty" : this.second[0]) + "," + (this.second[1] > this.horizon ? "infty" : this.second[1]) + "] "
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
