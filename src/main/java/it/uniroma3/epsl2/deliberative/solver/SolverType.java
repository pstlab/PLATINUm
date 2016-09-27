package it.uniroma3.epsl2.deliberative.solver;

/**
 * 
 * @author anacleto
 *
 */
public enum SolverType {

	/**
	 * This solver tries to generate a pseudo-controllable plan.
	 * 
	 * If no pseudo-controllable plan exists, it provides a warning and 
	 * gets the "best" not pseudo-controllable plan found
	 */
	PSEUDO_CONTROLLABILITY_AWARE(PseudoControllabilityAwareSolver.class.getName()),
	
	/**
	 * 
	 */
	BEST_FIRST(BestFirstSolver.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private SolverType(String cname) {
		this.cname = cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSolverClassName() {
		return this.cname;
	}
}
