package it.uniroma3.epsl2.testing.app.executive.fbt;

import it.istc.pst.epsl.app.EPSLApplicationBuilder;
import it.istc.pst.epsl.app.run.EPSLRunnableApplicationPlanner;
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
	private static final String DDL_PATH = "domains/fourbythree/meeting0716/fourbythree.ddl";
	private static final String PDL_PATH = "domains/fourbythree/meeting0716/fourbythree.pdl";
	
	protected EPSLFourByThreePlanner() {}
	
	/**
	 * 
	 * @return
	 * @throws NoSolutionFoundException
	 */
	public EPSLPlanDescriptor plan() 
			throws NoSolutionFoundException, Exception
	{
		// build planner
		EPSLRunnableApplicationPlanner planner = EPSLApplicationBuilder.buildAPSIPlanner(DDL_PATH, PDL_PATH);
		// run planner - default timeout at 60/180 seconds
		planner.plan();
		System.out.println("Solution found after " + planner.getSolvingTime() + " msecs");
		// export plan
		return planner.export();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		EPSLFourByThreePlanner planner = new EPSLFourByThreePlanner();
		try 
		{
			EPSLPlanDescriptor plan = planner.plan();
			System.out.println(plan);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
