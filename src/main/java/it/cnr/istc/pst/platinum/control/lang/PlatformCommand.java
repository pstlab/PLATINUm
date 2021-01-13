package it.cnr.istc.pst.platinum.control.lang;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;
import it.cnr.istc.pst.platinum.control.platform.PlatformProxy;

/**
 * 
 * @author anacleto
 *
 */
public class PlatformCommand implements Comparable<PlatformCommand> 
{
	private ExecutionNode node;							// associated node
	private long id;									// platform command ID
	private long time;									// command issue time
	private PlatformCommandDescription description;		// command description
	private String name;								// command name 
	private String[] paramValues;						// command parameter values
	private Object data;								// additional general data
	private int commandType;							// set command type {1 - start, 0 - stop}
	
	/**
	 * 
	 * @param id
	 * @param node
	 * @param commandType
	 */
	public PlatformCommand(long id, ExecutionNode node, int commandType) {
		this.id = id; 
		this.node = node;
		// get command name from node
		this.name = PlatformProxy.extractCommandName(this.node);
		// get command parameters
		this.paramValues = PlatformProxy.extractCommandParameters(node);
		this.time = System.currentTimeMillis();
		// set command type
		this.commandType = commandType;
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param params
	 */
	public PlatformCommand(long id, String name, String[] params, int commandType) {
		this.node = null;
		this.id = id;
		this.name = name;
		this.paramValues = params;
		this.commandType = commandType;
	}
	
	/**
	 * 
	 * @param id
	 * @param desc
	 * @param params
	 * @param commandType
	 */
	public PlatformCommand(long id, PlatformCommandDescription desc, String[] params, int commandType) {
		this(id, desc.getName(), params, commandType);
	}
	
	/**
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public ExecutionNode getNode() {
		return node;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getParamValues() {
		return this.paramValues;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getExecutionTime() {
		return this.description.getExecutionTime();	
	}

	/**
	 * 
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCommandType() {
		return commandType;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		PlatformCommand other = (PlatformCommand) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public int compareTo(PlatformCommand o) {
		return this.time < o.time ? -1 : this.time > o.time ? 1 : 0;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style descrtiption
		return "{\"id\": " + this.id + ", \"name\": \"" + this.name + "\"}";
	}
}
