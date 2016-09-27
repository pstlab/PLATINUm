package it.uniroma3.epsl2.framework.parameter.lang;

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
	NUMERIC_DOMAIN_PARAMETER_TYPE(NumericParameterDomain.class.getName()),
	
	/**
	 * Enumeration parameter domain type
	 */
	ENUMERATION_DOMAIN_PARAMETER_TYPE(EnumerationParameterDomain.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private ParameterDomainType(String cname) {
		this.cname = cname;
	}

	/** 
	 * @return
	 */
	public String getParameterDomainClassName() {
		return cname;
	}
}
