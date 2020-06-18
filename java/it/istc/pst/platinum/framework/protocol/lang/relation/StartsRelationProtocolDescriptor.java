package it.istc.pst.platinum.framework.protocol.lang.relation;

import it.istc.pst.platinum.framework.protocol.lang.TokenProtocolDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class StartsRelationProtocolDescriptor extends RelationProtocolDescriptor 
{
	/**
	 * 
	 * @param from
	 * @param to
	 */
	protected StartsRelationProtocolDescriptor(TokenProtocolDescriptor from, TokenProtocolDescriptor to) {
		super("starts", from, to);
	}
	
	/**
	 * 
	 */
	@Override
	public String export() {
		return this.from.getTimeline().getName() + " " + this.from.getId() + " "
				+ this.type + "  " + this.to.getTimeline().getName() + " " 
				+ this.to.getId();
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.export();
	}
}
