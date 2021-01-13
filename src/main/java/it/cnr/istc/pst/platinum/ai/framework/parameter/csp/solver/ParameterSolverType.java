package it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver;

import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.choco.v4.ChocoSolver;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterSolverType {

	/**
	 * CHOCHO wrapper for CSP constraints manager 
	 */
	CHOCHO_SOLVER(ChocoSolver.class.getName());
	
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
	public String getClassName() {
		return cname;
	}
}
