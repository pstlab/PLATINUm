package it.uniroma3.epsl2.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public class NumericParameter extends Parameter<NumericParameterDomain>
{
	private int lowerBound;
	private int upperBound;
	
	/**
	 * 
	 * @param label
	 * @param domain
	 */
	protected NumericParameter(String label, NumericParameterDomain domain) {
		super(label, ParameterType.NUMERIC_PARAMETER_TYPE, domain);
		this.lowerBound = domain.getLowerBound();
		this.upperBound = domain.getUpperBound();
	}
	
	/**
	 * 
	 * @param dom
	 */
	protected NumericParameter(NumericParameterDomain dom) {
		super(ParameterType.NUMERIC_PARAMETER_TYPE, dom);
		this.lowerBound = domain.getLowerBound();
		this.upperBound = domain.getUpperBound();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLowerBound() {
		return this.lowerBound;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getUpperBound() {
		return this.upperBound;
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
	 */
	@Override
	public String toString() {
		return "[NumericParameter label= " + this.getLabel() + " value= "
				+ "[" + this.lowerBound + ", " + this.upperBound +"] domain= " + this.domain + "]";
	}
}
