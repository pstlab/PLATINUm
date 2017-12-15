package it.istc.pst.platinum.deliberative.solver;

/**
 * 
 * @author anacleto
 *
 */
public enum PlannerSolverType 
{
	/**
	 * This solver tries to generate a pseudo-controllable plan.
	 * 
	 * If no pseudo-controllable plan exists, it provides a warning and 
	 * gets the "best" not pseudo-controllable plan found
	 */
	PSEUDO_CONTROLLABILITY_AWARE(PseudoControllabilityAwareSolver.class.getName(), "pseudo-controllability aware solver"),
	
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
	private PlannerSolverType(String cname, String label) {
		this.cname = cname;
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getClassName() {
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
