package it.uniroma3.epsl2.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public class NumericParameter extends Parameter
{
	private int[] domainBounds;
	private int[] value;
	
	/**
	 * 
	 * @param label
	 * @param domain
	 */
	protected NumericParameter(String label, NumericParameterDomain domain) {
		super(label, ParameterType.NUMERIC_PARAMETER_TYPE, domain);
		// set domain bound
		this.domainBounds = new int[] {
			domain.getLowerBound(),
			domain.getUpperBound()
		};
		// initialize value bound
		this.value = new int[] {
				domain.getLowerBound(),
				domain.getUpperBound()
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDomainLowerBound() {
		return this.domainBounds[0];
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDomainUpperBound() {
		return this.domainBounds[1];
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLowerBound() {
		return this.value[0];
	}
	
	/**
	 * 
	 * @return
	 */
	public int getUpperBound() {
		return this.value[1];
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getValue() {
		return this.value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setValue(int[] value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @param lb
	 */
	public void setLowerBound(int lb) {
		this.value[0] = lb;
	}
	
	
	/**
	 * 
	 * @param ub
	 */
	public void setUpperBound(int ub) {
		this.value[1] = ub;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[NumericParameter label= " + this.getLabel() + " value= "
				+ "[" + this.value[0] + ", " + this.value[1] + "] domain= " + this.domain + "]";
	}
}
