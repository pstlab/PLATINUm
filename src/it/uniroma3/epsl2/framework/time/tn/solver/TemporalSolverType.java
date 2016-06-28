package it.uniroma3.epsl2.framework.time.tn.solver;

import it.uniroma3.epsl2.framework.time.tn.solver.apsp.APSPTemporalSolver;

/**
 * 
 * @author anacleto
 *
 */
public enum TemporalSolverType {

	/**
	 * All-Pair-Shortest-Path temporal reasoning engine
	 */
	APSP(APSPTemporalSolver.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private TemporalSolverType(String cname) {
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
