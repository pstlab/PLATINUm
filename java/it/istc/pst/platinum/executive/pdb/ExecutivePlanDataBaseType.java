package it.istc.pst.platinum.executive.pdb;

import it.istc.pst.platinum.executive.pdb.epsl.EPSLExecutivePlanDataBase;

/**
 * 
 * @author anacleto
 *
 */
public enum ExecutivePlanDataBaseType 
{
	/**
	 * 
	 */
	PLATINUM(ExecutivePlanDataBase.class.getName()),
	
	/**
	 * 
	 */
	EPSL(EPSLExecutivePlanDataBase.class.getName());
	
	private String className;
	
	/**
	 * 
	 * @param className
	 */
	private ExecutivePlanDataBaseType(String className) {
		this.className = className;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClassName() {
		return className;
	}
	
}
