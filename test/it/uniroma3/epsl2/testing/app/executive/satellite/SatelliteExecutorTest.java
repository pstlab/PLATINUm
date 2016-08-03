package it.uniroma3.epsl2.testing.app.executive.satellite;

import it.istc.pst.epsl.microkernel.internal.solver.exception.NoSolutionFoundException;
import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.uniroma3.epsl2.executive.Executive;
import it.uniroma3.epsl2.executive.est.EarliestStartTimeExecutive;

/**
 * 
 * @author anacleto
 *
 */
class SatelliteExecutorTest {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			System.out.println("Start plannig...");
			// create planner
			EPSLSatellitePlanner planner = new EPSLSatellitePlanner();
			// generate a plan
			EPSLPlanDescriptor plan = planner.plan();

			System.out.println();
			System.out.println(plan);
			System.out.println();
			
			// create executor
			Executive executor = new EarliestStartTimeExecutive();
			executor.initialize(plan);
			// start executing the plan
			executor.execute();
		}
		catch (NoSolutionFoundException ex) {
			System.err.println(ex.getMessage());
		}
		catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
