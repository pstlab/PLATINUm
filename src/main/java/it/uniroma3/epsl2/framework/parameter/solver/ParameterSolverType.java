package it.uniroma3.epsl2.framework.parameter.solver;

import it.uniroma3.epsl2.framework.parameter.solver.csp.chocho3.Chocho3CSPSolver;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterSolverType {

	/**
	 * CHOCHO wrapper for CSP constraints manager 
	 */
	CHOCHO_SOLVER(Chocho3CSPSolver.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private ParameterSolverType(String cname) {
		this.cname = cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getParameterSolverClassName() {
		return cname;
	}
}
