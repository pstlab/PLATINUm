package it.istc.pst.platinum.testing.sir;

import java.io.BufferedWriter;
import java.io.FileWriter;

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
public class SirPlannerTester 
{
	private static final String DDL = "domains/sir/hospital_drugs/drugs_delivery.ddl";
	private static final String PDL = "domains/sir/hospital_drugs/drugs_delivery.pdl";
	private static final String OUT = "plans";
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{ 
		// get DDL and PDL files
		String ddl = args.length >= 2 && args[0] != null ? args[0] : DDL;
		String pdl = args.length >= 2 && args[1] != null ? args[1] : PDL;
		
		try 
		{
			// build the plan database
			PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(ddl, pdl);
			// set a planning instance of the plan database
			Planner planner = PlannerBuilder.createAndSet(pdb);

			// start planning
			SolutionPlan plan = planner.plan();
			// solution found
			System.out.println("----------------------------------\nSolution found after " + plan.getSolvingTime() + " msecs\n"
					+ "Solution plan:\n" + plan + "\n----------------------------------\n");

			// display the resulting plant
			planner.display();
			
			// print plan to file 
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUT + "/plan.txt"))) { 
				// export plan encoding 
				String enconding = plan.export().toString();
				// write exported file
				writer.write(enconding);
				// print the resulting plan
				System.out.println("Output plan file: \"" + OUT + "/pan.txt"  + "\"");
			}
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
