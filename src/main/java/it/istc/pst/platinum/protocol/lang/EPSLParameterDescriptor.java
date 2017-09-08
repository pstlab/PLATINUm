package it.istc.pst.platinum.protocol.lang;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class EPSLParameterDescriptor 
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
	public EPSLParameterDescriptor(int id, ParameterTypeDescriptor type) {
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
		return new Integer(this.id).hashCode();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof EPSLParameterDescriptor) && (((EPSLParameterDescriptor) obj).id == this.id);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{<param" + this.id + "> " + this.name + " - type= " + this.type + "}";
	}
}
