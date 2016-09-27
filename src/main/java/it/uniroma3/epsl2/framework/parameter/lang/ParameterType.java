package it.uniroma3.epsl2.framework.parameter.lang;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterType 
{
	/**
	 * Constant numeric parameter
	 */
	CONSTANT_NUMERIC_PARAMETER_TYPE(NumericConstantParameter.class.getName(), 
			ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE),
	
	/**
	 * Constant enumeration parameter
	 */
	CONSTANT_ENUMERATION_PARAMETER_TYPE(EnumerationConstantParameter.class.getName(), 
			ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE),
	
	/**
	 * Numeric parameter type
	 */
	NUMERIC_PARAMETER_TYPE(NumericParameter.class.getName(), 
			ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE),
	
	/**
	 * Enumeration parameter type
	 */
	ENUMERATION_PARAMETER_TYPE(EnumerationParameter.class.getName(), 
			ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
	
	private String cname;
	private ParameterDomainType domainType;
	
	/**
	 * 
	 * @param cname
	 * @param type
	 */
	private ParameterType(String cname, ParameterDomainType type) {
		this.cname = cname;
		this.domainType = type;
	}

	/** 
	 * @return
	 */
	public String getParameterClassName() {
		return cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public ParameterDomainType getDomainType() {
		return this.domainType;
	}
}
