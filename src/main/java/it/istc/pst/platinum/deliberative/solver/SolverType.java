package it.istc.pst.platinum.deliberative.solver;

/**
 * 
 * @author anacleto
 *
 */
public enum SolverType 
{
	/**
	 * This solver tries to generate a pseudo-controllable plan.
	 * 
	 * If no pseudo-controllable plan exists, it provides a warning and 
	 * gets the "best" not pseudo-controllable plan found
	 */
	PSEUDO_CONTROLLABILITY_AWARE(PseudoControllabilityAwareSolver.class.getName(), "Pseud-Controllability aware solver"),
	
	/**
	 * 
	 */
	BEST_FIRST(BestFirstSolver.class.getName(), "Best First solver");
	
	private String cname;
	private String label;
	
	/**
	 * 
	 * @param cname
	 * @param label
	 */
	private SolverType(String cname, String label) {
		this.cname = cname;
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSolverClassName() {
		return this.cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
}
