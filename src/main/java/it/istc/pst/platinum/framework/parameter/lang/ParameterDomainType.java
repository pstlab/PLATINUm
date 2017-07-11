package it.istc.pst.platinum.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterDomainType 
{
	/**
	 * Numeric parameter domain type
	 */
	NUMERIC_DOMAIN_PARAMETER_TYPE(NumericParameterDomain.class.getName(),
			ParameterType.NUMERIC_PARAMETER_TYPE),
	
	/**
	 * Enumeration parameter domain type
	 */
	ENUMERATION_DOMAIN_PARAMETER_TYPE(EnumerationParameterDomain.class.getName(),
			ParameterType.ENUMERATION_PARAMETER_TYPE);
	
	private String cname;
	private ParameterType parameterType;
	
	/**
	 * 
	 * @param cname
	 * @param paramType
	 */
	private ParameterDomainType(String cname, ParameterType paramType) {
		this.cname = cname;
		this.parameterType = paramType;
	}

	/** 
	 * @return
	 */
	public String getParameterDomainClassName() {
		return cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public ParameterType getParameterType() {
		return parameterType;
	}
}
