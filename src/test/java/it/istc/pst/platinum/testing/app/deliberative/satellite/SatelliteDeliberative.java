package it.istc.pst.platinum.testing.app.deliberative.satellite;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
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
	// simple, discrete, reservoir
	private static final String DOMAIN_TYPE = "discrete";
	private static final String DOMAIN_HOME = "domains/satellite/battery/domains/" + DOMAIN_TYPE;
	private static final String DDL = DOMAIN_HOME  + "/satellite.ddl";

	private static final String PROBLEM_HOME = "domains/satellite/battery/problems";
	private static final int NUMBER_OF_RECHARGING_WINDOW = 3;
	private static final int NUMBER_OF_COMMUNICATION_WINDOW = 3;
	private static final int NUMBER_OF_SCIENCE_OPERATION = 5;
	
	private static final String PDL = PROBLEM_HOME + "/satellite_"
			+ "" + DOMAIN_TYPE + "_"
			+ "" + NUMBER_OF_COMMUNICATION_WINDOW + "_"
			+ "" + NUMBER_OF_RECHARGING_WINDOW + "_"
			+ "" + NUMBER_OF_SCIENCE_OPERATION + ".pdl";
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{ 
		try 
		{
			// build the plan database
			PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(DDL, PDL);
			// initialize a planning instance of the plan database
			Planner planner = PlannerBuilder.createAndSet(pdb);

			// start planning
			SolutionPlan plan = planner.plan();
			// solution found
			System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
			// export plan encoding 
			String enconding = plan.export().toString();
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
