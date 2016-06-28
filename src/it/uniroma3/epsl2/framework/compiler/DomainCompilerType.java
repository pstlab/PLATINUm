package it.uniroma3.epsl2.framework.compiler;

import it.uniroma3.epsl2.framework.compiler.ddl.v3.DDLv3Compiler;

/**
 * 
 * @author anacleto
 *
 */
public enum DomainCompilerType 
{
	/**
	 * 
	 */
	DDLv3(DDLv3Compiler.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private DomainCompilerType(String cname) {
		this.cname = cname;
	}

	/**
	 * 
	 * @return
	 */
	public String getCompilerClassName() {
		return this.cname;
	}
}
