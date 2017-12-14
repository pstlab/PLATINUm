package it.istc.pst.platinum.framework.protocol.lang.relation;

import it.istc.pst.platinum.framework.protocol.lang.TokenProtocolDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class MetByRelationProtocolDescriptor extends RelationProtocolDescriptor
{
	/**
	 * 
	 * @param from
	 * @param to
	 */
	public MetByRelationProtocolDescriptor(TokenProtocolDescriptor from, TokenProtocolDescriptor to) {
		super("meets", from, to);
	}
	
	/**
	 * 
	 */
	@Override
	public String export() {
		return this.from.getTimeline().getName() + " " + this.from.getId() + " "
				+ this.type + " " + this.to.getTimeline().getName() + " " + this.to.getId();
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return this.export();
	}
}
