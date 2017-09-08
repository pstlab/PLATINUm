package it.istc.pst.platinum.protocol.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class TimelineProtocolDescriptor 
{
	private int id; 
	private String component;
	private String name;
	private boolean external;
	private List<TokenProtocolDescriptor> tokens;
	
	/**
	 * 
	 * @param id
	 * @param component
	 * @param name
	 * @param external
	 */
	public TimelineProtocolDescriptor(int id, String component, String name, boolean external) {
		this.id = id;
		this.component = component;
		this.name = name;
		this.external = external;
		this.tokens = new ArrayList<TokenProtocolDescriptor>();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getComponent() {
		return component;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isExternal() {
		return this.external;
	}
	
	/**
	 * 
	 * @param token
	 */
	public void addToken(TokenProtocolDescriptor token) {
		this.tokens.add(token);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<TokenProtocolDescriptor> getTokens() {
		List<TokenProtocolDescriptor> list = new ArrayList<TokenProtocolDescriptor>(this.tokens);
		Collections.sort(list);
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public String export() {
		String desc = this.name + " {\n";
		for (TokenProtocolDescriptor token : this.tokens) {
			desc += "\t" + token.export() + "\n";
		}
		desc += "}";
		return desc;
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		return new Integer(this.id).hashCode();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof TimelineProtocolDescriptor) && (((TimelineProtocolDescriptor) obj).id == this.id);
	}
	
	@Override
	public String toString() {
		return "<timeline" + this.id + "> " + this.name + " " + (this.external ? "<external> " : " ");
	}
}
