package it.uniroma3.epsl2.testing.app.fbt;

import it.istc.pst.epsl.microkernel.internal.solver.exception.NoSolutionFoundException;
import it.istc.pst.epsl.pdb.lang.EPSLPlanDescriptor;
import it.uniroma3.epsl2.executive.Executive;
import it.uniroma3.epsl2.executive.est.EarliestStartTimeExecutive;

/**
 * 
 * @author anacleto
 *
 */
class FourByThreeExecutionTest {

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
			EPSLFourByThreePlanner planner = new EPSLFourByThreePlanner();
			// generate a plan
			EPSLPlanDescriptor plan = planner.plan();
			System.out.println();
			System.out.println(plan);
			System.out.println();
			
			// create executor
			Executive<?, ?,?> executive = new EarliestStartTimeExecutive();
			executive.init(plan);
			// start executing the plan
			executive.execute();
		}
		catch (NoSolutionFoundException ex) {
			System.err.println(ex.getMessage());
		}
		catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
