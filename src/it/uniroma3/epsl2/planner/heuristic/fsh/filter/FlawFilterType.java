package it.uniroma3.epsl2.planner.heuristic.fsh.filter;

/**
 * 
 * @author anacleto
 *
 */
public enum FlawFilterType {

	/**
	 * Filter flaws according to their type
	 */
	TF(TypePreferencesFlawFilter.class.getName()),
	
	/**
	 * Filter flaws according to the component they belong to and the related
	 * dependency level
	 */
	DgF(DependencyGraphFlawFilter.class.getName());
	
	private String cname;

	/**
	 * 
	 * @param cname
	 */
	private FlawFilterType(String cname) {
		this.cname = cname;
	}
	
	/*
	 * 
	 */
	public String getFilterClassName() {
		return cname;
	}
}
