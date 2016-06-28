package it.uniroma3.epsl2.testing.executive.fourByThree;

import it.istc.pst.epsl.app.EPSLApplicationBuilder;
import it.istc.pst.epsl.app.run.EPSLApplicationPlanner;
import it.istc.pst.epsl.microkernel.internal.solver.exception.NoSolutionFoundException;
import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;

/**
 * 
 * @author alessandroumbrico
 *
 */
class EPSLFourByThreePlanner 
{
	// DDL file path
	private static final String DDL_PATH = "opt/fourbythree/fourbythree.ddl";
	private static final String PDL_PATH = "opt/fourbythree/fourbythree.pdl";
	
	protected EPSLFourByThreePlanner() {}
	
	/**
	 * 
	 * @return
	 * @throws NoSolutionFoundException
	 */
	public EPSLPlanDescriptor plan() 
			throws NoSolutionFoundException {
		// build planner
		EPSLApplicationPlanner planner = EPSLApplicationBuilder.buildAPSIPlanner(DDL_PATH, PDL_PATH);
		// run planner - default timeout at 60/180 seconds
		planner.plan();
		System.out.println("Solution found after " + planner.getSolvingTime() + " msecs");
		// export plan
		return planner.export();
	}
}
