package it.istc.pst.platinum.testing.app.deliberative.satellite;

import it.istc.pst.platinum.deliberative.app.Planner;
import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
public class SatelliteDeliberative
{
	private static final String DOMAIN_TYPE = "reservoir"; 	// simple, discrete, reservoir
	private static final String DDL = "domains/satellite/" + DOMAIN_TYPE + "/satellite.ddl";
	private static final String PDL = "domains/satellite/" + DOMAIN_TYPE + "/satellite.pdl";
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{ 
		try 
		{
			// create planner
			Planner planner = PlannerBuilder.build(DDL, PDL);	
			// start planning
			SolutionPlan plan = planner.plan();
			// solution found
			System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
			// export plan encoding 
			String enconding = plan.export();
			// print the resulting plan
			System.out.println("Resulting plan:\n" + plan + "\n");
			System.out.println("Exporting encoding:\n" + enconding + "\n");
			
			// display the resulting plant
			planner.display();
		}
		catch (NoSolutionFoundException ex) {
			// no solution found
			System.err.println(ex.getMessage());
		}
		catch (ProblemInitializationException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace(System.err);
		}
	}
}
