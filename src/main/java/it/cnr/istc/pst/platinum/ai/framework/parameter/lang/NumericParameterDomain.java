package it.cnr.istc.pst.platinum.ai.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public final class NumericParameterDomain extends ParameterDomain 
{
	private int lowerBound;
	private int upperBound;
	
	/**
	 * 
	 * @param name
	 */
	protected NumericParameterDomain(String name) {
		super(name, ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
	}
	
	/**
	 * 
	 * @param lb
	 */
	public void setLowerBound(int lb) {
		this.lowerBound = lb;
	}
	
	/**
	 * 
	 * @param ub
	 */
	public void setUpperBound(int ub) {
		this.upperBound = ub;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLowerBound() {
		return lowerBound;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getUpperBound() {
		return upperBound;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[NumericParameterDomain name= " + this.getName() + " values= [" + this.lowerBound + ", " + this.upperBound + "]]";
	}
}
