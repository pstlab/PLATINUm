package it.uniroma3.epsl2.deliberative;

import it.uniroma3.epsl2.deliberative.solver.Solver;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.PlannerConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.inject.SolverReference;

/**
 * 
 * @author anacleto
 *
 */
@PlannerConfiguration
public class Planner extends ApplicationFrameworkObject {

	@PlanDataBaseReference
	private PlanDataBase pdb;
	
	@SolverReference
	private Solver solver;
	
	/**
	 * 
	 */
	protected Planner() {
		super();
	}
	
	/**
	 * Display the current plan
	 */
	public void display() {
		// display the current plan
		this.pdb.display();
	}
	
	/**
	 * The method starts the planning process and return the solution plan if any.
	 * 
	 * If no solution plan is found the method throws an exception
	 * 
	 * @return
	 * @throws NoSolutionFoundException
	 */
	public SolutionPlan plan() 
			throws NoSolutionFoundException {
		// solve the problem and get the plan
		return this.solver.solve();
	}
	
	/**
	 * The method returns a structure representing the current plan. 
	 * 
	 * @return
	 */
	public SolutionPlan getCurrentPlan() {
		// get current plan
		return this.pdb.getSolutionPlan();
	}
	
	/**
	 * The method returns a text-based description of the current plan
	 * 
	 * @return
	 */
	public String export() {
		return this.getCurrentPlan().export();
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		// get a description of the plan data base
		return this.pdb.toString();
	}
}
