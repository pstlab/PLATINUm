package it.istc.pst.platinum.testing.app.executive.fbt.etfa16;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
import it.istc.pst.platinum.framework.domain.component.Token;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Timeline;

/**
 * 
 * @author anacleto
 *
 */
public class FourByThreeExperimentManager 
{
	private static final String PLANNER_CLASS = FourByThreePlanner.class.getName();
	private static final String FIELD_SEPARATOR = ";";
	// data folder
	private static final String DATA_FOLDER = "data/fourbythree";
	// testing parameter: domain type
	private static final String[] PROCESS_SIZE = new String[] {
			"small", "medium", "large"
	};
	
	// testing parameter: effort
	private static final String[] EFFORT = new String[] {
		"30", "50", "70"	
	};
	
	// planning problem
	private static final String PDL_FILE = "domains/fourbythree/etfa16/fourbythree.pdl";
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			// setup data file 
			try (PrintWriter writer = new PrintWriter(new File(DATA_FOLDER + "/experiments.csv"))) {
				
				// print header
				writer.println("process-size-effort" + FIELD_SEPARATOR
						+ "planning-time (msecs)" + FIELD_SEPARATOR 
						+ "process-duration-lb" + FIELD_SEPARATOR
						+ "process-duration-ub" + FIELD_SEPARATOR
						+ "#totalTasks" + FIELD_SEPARATOR
						+ "#assignableTasks" + FIELD_SEPARATOR
						+ "#robot-tasks" + FIELD_SEPARATOR
						+ "#human-tasks" + FIELD_SEPARATOR
						+ "horizon" + FIELD_SEPARATOR
						+ "robot-idle-time (%)" + FIELD_SEPARATOR
						+ "human-idle-time (%)");
				
				
				// generate data for the different process sizes and efforts
				for (String size : PROCESS_SIZE) {
					for (String effort : EFFORT) {
						
						// set DDL file
						String DDL_FILE = "domains/fourbythree/etfa16/fourbythree_" + size +  "_" + effort + "effort.ddl";
						// get the planner
						FourByThreePlanner planner = PlannerBuilder.build(PLANNER_CLASS, DDL_FILE, PDL_FILE);
						try {
			
							System.out.println("*******************************************************************");
							System.out.println("Test: [ddl= " + DDL_FILE + ", pdl= " + PDL_FILE + "]");
							System.out.println("Start planning... ");
							// start planning
							SolutionPlan plan = planner.plan();
							System.out.println("... solution found after " + plan.getSolvingTime() + " msecs");
							System.out.println();
							// print final plan
							System.out.println(plan);
							System.out.println();
			
							// get process duration
							long[] duration = getProcessDuration(plan);
							// compute the number of process's tasks
							int processTasks = computeNumberOfProcessTasks(plan);
							// compute the number of shared tasks
							int sharedTasks = computeNumberOfSharedTasks(plan);
							// compute the number of robot's tasks
							int robotTasks = computeNumberOfRobotTasks(plan);
							// compute number of human's tasks
							int humanTasks = computeNumberOfHumanTasks(plan);
							// compute robot idle time
							double robotIdleTime = computeIdleTimeOfComponent("RobotController", plan);
							// compute human idle time
							double humanIdleTime = computeIdleTimeOfComponent("Human", plan);
							
							System.out.println("ALFA Pilot statistics:"); 
							System.out.println("- Assemby process duration [" + duration[0] + ", " + duration[1] + "] over plan's horizon H= " + plan.getHorizon());
							System.out.println("\tThe Assembly process is composed by " + processTasks + " \"high-level tasks\" for a total of " + (robotTasks + humanTasks) + " agents' tasks");
							System.out.println("\tThe Assembly process is composed by " + sharedTasks + " \"assignable\" tasks");
							
							System.out.println("\tHuman committment in the Assembly process: " + ((humanTasks * 100) / (robotTasks + humanTasks)) + "% (" + humanTasks + " human tasks)");
							System.out.println("\tHuman Idle Time: " + humanIdleTime + "% of the plan's horizon");
							
							System.out.println("\tRobot committment in the Assembly process: " + ((robotTasks * 100) / (robotTasks + humanTasks)) + "% (" + robotTasks + " robot tasks)");				
							System.out.println("\tRobot Idle Time: " + robotIdleTime + "% of the plan's horizon");
							
							
							// print data
							writer.println(size + "-" + effort + FIELD_SEPARATOR +
									"" + plan.getSolvingTime() + FIELD_SEPARATOR + 
									"" + duration[0] + FIELD_SEPARATOR + 
									"" + duration[1] + FIELD_SEPARATOR +
									"" + (robotTasks + humanTasks) + FIELD_SEPARATOR +
									"" + sharedTasks + FIELD_SEPARATOR +
									"" + robotTasks + FIELD_SEPARATOR +
									"" + humanTasks + FIELD_SEPARATOR +
									"" + plan.getHorizon() + FIELD_SEPARATOR +
									"" + robotIdleTime + FIELD_SEPARATOR +
									"" + humanIdleTime);
							
							// flush stream
							writer.flush();
						}
						catch (NoSolutionFoundException ex) {
							// no solution found
							System.out.println(ex.getMessage());
						}
					}
				}
			}
		}
		catch (SynchronizationCycleException | ProblemInitializationException ex) {
			System.err.println(ex.getMessage());
		}
		catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param plan
	 * @return
	 */
	private static int computeNumberOfRobotTasks(SolutionPlan plan) {
		
		// initialize task counter
		int tasks = 0;
		for (Timeline tl : plan.getTimelines()) {
			
			// check component
			if (tl.getComponent().getName().equals("RobotController")) {
				
				// counter number of tasks
				for (Token token : tl.getTokens()) {
					
					// check value
					if (!token.getPredicate().getValue().getLabel().equals("Idle")) {
						// update counter
						tasks++;
					}
				}
			}
		}
		
		// get number of tasks
		return tasks;
	}
	
	/**
	 * 
	 * @param plan
	 * @return
	 */
	private static int computeNumberOfHumanTasks(SolutionPlan plan) {
		
		// initialize task counter
		int tasks = 0;
		for (Timeline tl : plan.getTimelines()) {
			
			// check component
			if (tl.getComponent().getName().equals("Human")) {
				
				// counter number of tasks
				for (Token token : tl.getTokens()) {
					
					// check value
					if (!token.getPredicate().getValue().getLabel().equals("Idle")) {
						// update counter
						tasks++;
					}
				}
			}
		}
		
		// get number of tasks
		return tasks;
	}
	
	/**
	 * 
	 * @param plan
	 * @return
	 */
	private static int computeNumberOfSharedTasks(SolutionPlan plan) {
		
		int tasks = 0;
		for (Timeline tl : plan.getTimelines()) {
			
			// check component
			if (tl.getComponent().getName().equals("RobotController") || 
					tl.getComponent().getName().equals("Human")) {
				
				// check task
				for (Token token : tl.getTokens()) {
					
					// check "shared" task
					if (token.getPredicate().getValue().getLabel().contains("Screw") ||
							token.getPredicate().getValue().getLabel().contains("Unscrew")) {
						tasks++;
					}
				}
			}
		}
		
		return tasks;
	}
	
	/**
	 * 
	 * @param plan
	 * @return
	 */
	private static int computeNumberOfProcessTasks(SolutionPlan plan) {
		
		// initialize task counter
		int tasks = 0;
		for (Timeline tl : plan.getTimelines()) {
			
			// check component
			if (tl.getComponent().getName().equals("AssemblyProcess")) {
				
				// counter number of tasks
				for (Token token : tl.getTokens()) {
					
					// check value
					if (!token.getPredicate().getValue().getLabel().equals("None")) {
						// update counter
						tasks++;
					}
				}
			}
		}
		
		// get number of tasks
		return tasks;
	}
	
	/**
	 * 
	 * @param plan
	 * @return
	 */
	private static long[] getProcessDuration(SolutionPlan plan) {
		
		// processe's duration
		long[] duration = new long[2];
		// check the plan
		for (Timeline tl : plan.getTimelines()) {
			
			// check component
			if (tl.getComponent().getName().equals("ALFA")) {
				
				// check assembly process
				for (Token token : tl.getTokens()) {
					
					// check value
					if (token.getPredicate().getValue().getLabel().equals("Assembly")) {
						
						// set duration
						duration = new long[] {
								token.getInterval().getDurationLowerBound(),
								token.getInterval().getDurationUpperBound()
						};
					}
				}
			}
		}
		
		// get process's duration
		return duration;
	}
	
	/**
	 * 
	 * @param componentName
	 * @param plan
	 * @return
	 */
	private static double computeIdleTimeOfComponent(String componentName, SolutionPlan plan) {
		
		// get horizon
		long horizon = plan.getHorizon();
		double idleTime = 0;
		// check the plan
		for (Timeline tl : plan.getTimelines()) {
			
			// check component
			if (tl.getComponent().getName().equals(componentName)) {
				
				// compute idle time
				long count = 0;
				for (Token token : tl.getTokens()) {
					
					// check value
					if (!token.getPredicate().getValue().getLabel().equals("Idle")) {
						
						// get duration lower bound
						count += token.getInterval().getDurationLowerBound();
					}
					
				}

				// check the the idle time with respect to the planning horizon
				idleTime = ((horizon - count) * 100) / horizon;
			}
		}
		
		// get computed idle time
		return idleTime;
	}
}
