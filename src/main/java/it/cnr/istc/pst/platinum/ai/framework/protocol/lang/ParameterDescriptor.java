package it.cnr.istc.pst.platinum.ai.framework.protocol.lang;

/**
 * 
 * @author alessandro
 *
 */
public class ParameterDescriptor 
{
	private int id;
	private String name;						// parameter name
	private ParameterTypeDescriptor type;			// parameter type
	private long[] bounds;						// lower and upper bounds in case of Numeric Parameter
	private String[] values;					// allowed values in case of Enumeration Parameter
	
	/**
	 * Protected constructor method
	 * 
	 * @param id
	 * @param type
	 */
	public ParameterDescriptor(int id, ParameterTypeDescriptor type) {
		this.id = id;
		this.type = type;
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
	public long[] getBounds() {
		return bounds;
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
	public ParameterTypeDescriptor getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getValues() {
		return values;
	}
	
	/**
	 * 
	 * @param bounds
	 */
	public void setBounds(long[] bounds) {
		this.bounds = bounds;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @param values
	 */
	public void setValues(String[] values) {
		this.values = values;
	}
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ParameterDescriptor other = (ParameterDescriptor) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{<param" + this.id + "> " + this.name + " - type= " + this.type + "}";
	}
}
