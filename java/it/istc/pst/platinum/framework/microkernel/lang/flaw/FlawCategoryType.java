package it.istc.pst.platinum.framework.microkernel.lang.flaw;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawCategoryType 
{
	/**
	 * Category of flaws that entail only planning decisions to be solved
	 */
	PLANNING,
	
	/**
	 * Category of flaws that entail only scheduling decisions to be solved
	 */
	SCHEDULING,
	
	/**
	 * Category of flaws that entail both planning and scheduling decisions to be solved 
	 */
	PLANNING_SCHEDULING,
	
	/**
	 * Category of flaws that cannot be solved
	 */
	UNSOLVABLE;
}
