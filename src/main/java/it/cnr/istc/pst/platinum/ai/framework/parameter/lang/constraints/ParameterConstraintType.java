package it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints;

import it.cnr.istc.pst.platinum.ai.framework.microkernel.ConstraintCategory;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterConstraintType 
{
	/**
	 * Equal constraint between parameters
	 */
	EQUAL("=", EqualParameterConstraint.class.getName(),
			ConstraintCategory.PARAMETER_CONSTRAINT),
	
	/**
	 * NotEqual constraint between parameters
	 */
	NOT_EQUAL("!=", NotEqualParameterConstraint.class.getName(),
			ConstraintCategory.PARAMETER_CONSTRAINT),
	
	/**
	 * Bind a variable to a specific value
	 */
	BIND("=", BindParameterConstraint.class.getName(),
			ConstraintCategory.PARAMETER_CONSTRAINT),
	
	/**
	 * Exclude a value from the set of those allowed for a variable
	 */
	EXCLUDE("!=", ExcludeParameterConstraint.class.getName(),
			ConstraintCategory.PARAMETER_CONSTRAINT);
	
	private String symbol;
	private String cname;
	private ConstraintCategory category;
	
	/**
	 * 
	 * @param symbol
	 * @param cname
	 * @param category
	 */
	private ParameterConstraintType(String symbol, String cname, ConstraintCategory category) {
		this.symbol = symbol;
		this.cname = cname;
		this.category = category;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSymbol() {
		return symbol;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getConstraintClassName() {
		return this.cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public ConstraintCategory getCategory() {
		return category;
	}
}
