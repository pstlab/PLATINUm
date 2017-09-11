package it.istc.pst.platinum.deliberative.app.launcher;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.istc.pst.platinum.deliberative.app.Planner;
import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.PlanControllabilityType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * This program takes as input the paths to DDL and PDL files respectively and
 * launch the planner.
 * 
 * The resulting plan (if any) is printed on the standard output and a related
 * file is created in the output directory of the planner ("plans" directory).
 * 
 *  The names of the file describing generated plans either end with "p" if the related
 *  plan is "pseudo-controllable" or they end with "np" if not.
 *  
 *  
 * @author anacleto
 *
 */
public class DeliberativeLauncher 
{
	/**
	 * The main method of the launcher program  
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// check arguments
		if (args.length >= 2) 
		{
			// create output directory if it does not exists
			File out = new File("plans");
			// check if exists
			if (!out.exists()) {
				out.mkdir();
			}
			
			// get date formatter
			SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
			long now = System.currentTimeMillis();
			// get time format
			String time = timeFormatter.format(new Date(now));
			
			try 
			{
				// get file paths
				String ddlPath = args[0].trim();
				String pdlPath = args[1].trim();
				
				// get domain name
				String[] splits = ddlPath.split(File.separator);
				String domainName = splits[splits.length - 1].replace(".ddl", "");
				// get problem name
				splits = pdlPath.split(File.separator);
				String problemName = splits[splits.length - 1].replace(".pdl", "");
				
				// build a planner instance
				Planner planner = PlannerBuilder.build(ddlPath, pdlPath);
				try 
				{
					// start planning
					System.out.println("Running PLATINUm planner on domain= " + domainName + " and problem= " + problemName + "...");
					// start planning
					SolutionPlan plan = planner.plan();
					// solution found
					System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
					// print the resulting plan
					System.out.println(plan);
					
					// writer sub-directory if it does not exists
					File subdir = new File(out.getPath() + File.separator + domainName);
					if (!subdir.exists()) {
						subdir.mkdir();
					}
					
					// check if resulting plan is pseudo-controllable
					String pseudo = plan.getPlanControllabilityType().
							equals(PlanControllabilityType.PSEUDO_CONTROLLABILITY) ? 
									"p" :	// pseudo-controllable 
									"np";
					
					// export the plan to the output file
					try (PrintWriter writer = new PrintWriter(new File(
							subdir.getPath() + File.separator + time + "_" + problemName + "_" + pseudo))) 
					{
						// export
						writer.println(plan.export());
						writer.flush();
					}
					
					// display the resulting plant
					planner.display();
				}
				catch (NoSolutionFoundException ex) {
					// no solution found
					System.err.println(ex.getMessage());
				}
			}
			catch (SynchronizationCycleException | ProblemInitializationException ex) {
				System.err.println(ex.getMessage());
				ex.printStackTrace(System.err);
			}
			catch (Exception ex) {
				System.err.println(ex.getMessage());
				ex.printStackTrace(System.err);
			}
		}
		else {
			System.err.println("Specify two valid paths for the DDL and PDL files resepctively");
		}
	}
}



