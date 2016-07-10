package it.uniroma3.epsl2.testing.executive.satellite;

import it.istc.pst.epsl.app.EPSLApplicationBuilder;
import it.istc.pst.epsl.app.run.EPSLRunnableApplicationPlanner;
import it.istc.pst.epsl.microkernel.internal.solver.exception.NoSolutionFoundException;
import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;

/**
 * 
 * @author anacleto
 *
 */
class EPSLSatellitePlanner 
{
	private static final String DDL = "domains/satellite/satellite.ddl";
	private static final String PDL = "domains/satellite/satellite.pdl";
	
	protected EPSLSatellitePlanner() {}
	
	/**
	 * 
	 * @return
	 * @throws NoSolutionFoundException
	 */
	public EPSLPlanDescriptor plan() 
			throws NoSolutionFoundException {
		// build planner
		 EPSLRunnableApplicationPlanner planner = EPSLApplicationBuilder.buildAPSIPlanner(DDL, PDL);
		// run planner
		planner.plan();
		System.out.println("Solution found after " + planner.getSolvingTime() + " msecs");
		// export plan
		return planner.export();
	}
}
