package it.istc.pst.platinum.testing.aij;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.domain.component.Token;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Timeline;

/**
 * 
 * @author anacleto
 *
 */
public class AIJFbTPlannerExperimentManager extends AIJFbT 
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// experiment generator
		AIJFbTPlanningDomainGenerator generator = new AIJFbTPlanningDomainGenerator(
				DOMAIN_FOLDER,
				TASKS,
				SHARED,
				UNCERTAINTY,
				HORIZON);
		// generate experiment domains
		generator.generate();

		try
		{
			// create data file
			File dataFile = new File(DATA_FOLDER + "/exp_runs_" + System.currentTimeMillis() + ".csv");
			// create file and write the header of the CSV
			try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)))) {
				// write file
				writer.println("cfg;#tasks;%shared;#uncertainty;#hTasks;#rTasks;makespan;time (secs)");
			}
			
			// run experiments
			for (Class cfg : CONFIGURATIONS)
			{
				for (int task : TASKS) 
				{
					for (int shared : SHARED) 
					{
						for (int uncertainty : UNCERTAINTY) 
						{
							// prepare domain name
							String domainName = "AIJ_EXP_T" + task  + "_S" + shared + "_U" + uncertainty;
							// prepare DDL file path
							String DDL = DOMAIN_FOLDER + "/" + domainName + ".ddl";
							// prepare PDL file path
							String PDL = DOMAIN_FOLDER + "/" + domainName + ".pdl";
							// experiment run counter
							double time = 0;
							// average cycle time of the found solutions
							double makespan = 0;
							// number of human tasks
							double hTasks = 0;
							// number of robot tasks
							double rTasks = 0;
							try
							{
								// run experiments and take the average solving time
								for (int run = 0; run < EXPERIMETN_RUNS; run++)
								{
									System.out.println("Run experiemnt on domain: " + domainName + " ... ");
									// build the plan database
									PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(DDL, PDL);
									// initialize a planning instance of the plan database
									Planner planner = PlannerBuilder.createAndSet(cfg, pdb);
			
									// start planning
									SolutionPlan plan = planner.plan();
									
									// get number of human assigned tasks
									for (Timeline tl : plan.getTimelines()) {
										// check human behavior
										if (tl.getComponent().getName().equals("Human")) {
											for (Token tk : tl.getTokens()) {
												// check token 
												if (!tk.getPredicate().getValue().getLabel().contains("Idle")) {
													hTasks++;
												}
											}
										}
										
										// check robot behavior
										if (tl.getComponent().getName().equals("Robot")) {
											for (Token tk : tl.getTokens()) {
												// check token 
												if (!tk.getPredicate().getValue().getLabel().contains("Idle")) {
													rTasks++;
												}
											}
										}
									}
									
									
									// update solving time 
									time +=  plan.getSolvingTime();
									// update average cycle time
									makespan += plan.getMakespan();
									// print solving information
									System.out.println("... problem solved after " + time + " msecs\nSolution plan:\n" + plan + "\n"
											+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
									
									// print resulting plan 
									File planFile = new File(PLAN_FOLDER + "/" + domainName + "_" + cfg.getSimpleName() + "_plan_" + run + ".txt");
									try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(planFile)))) {
										// print the resulting plan 
										writer.println(plan.export());
										writer.println();
										writer.print(plan);
									}
								}
								
								// update number of human and robot tasks
								hTasks = hTasks / EXPERIMETN_RUNS;
								rTasks = rTasks / EXPERIMETN_RUNS; 
								// get the average time
								time = (time / EXPERIMETN_RUNS) / 1000;
								// get average cycle time of the solutions
								makespan = makespan / EXPERIMETN_RUNS;
								// append result to the data file
								try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
									// add data entry
									writer.println(cfg.getSimpleName() + ";" + task + ";" + shared + ";" + uncertainty + ";" + hTasks + ";" + rTasks + ";" + makespan + ";" + time);
								}
							}
							catch (Exception ex) {
								// error while solving planning instance
								System.out.println("... error while solving domain instance: " + domainName + "\n- message: " + ex.getMessage() +"\n"
										+ "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
								
								// log error into the results (use -1 as a special value to indicate failure)
								try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(dataFile, true)))) {
									// add data entry
									writer.println(cfg.getSimpleName() + ";" + task + ";" + shared + ";" + uncertainty + ";-1;-1");
								}
							}
							finally {
								// try to clear memory
								System.gc();
							}
						}
					}
				}
			}
		}
		catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
}

