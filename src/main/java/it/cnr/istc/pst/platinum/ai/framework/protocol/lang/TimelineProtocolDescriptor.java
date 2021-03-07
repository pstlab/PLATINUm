package it.cnr.istc.pst.platinum.ai.framework.protocol.lang;

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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimelineProtocolDescriptor other = (TimelineProtocolDescriptor) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "<timeline" + this.id + "> " + this.name + " " + (this.external ? "<external> " : " ");
	}
}
