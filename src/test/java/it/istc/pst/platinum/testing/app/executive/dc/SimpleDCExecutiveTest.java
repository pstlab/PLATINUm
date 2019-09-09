package it.istc.pst.platinum.testing.app.executive.dc;

import it.istc.pst.platinum.control.platform.PlatformProxyBuilder;
import it.istc.pst.platinum.control.platform.sim.PlatformSimulator;
import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.ExecutiveBuilder;
import it.istc.pst.platinum.executive.dc.DCExecutive;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author alessandrod
 * 
 * 
 *
 */
public class SimpleDCExecutiveTest 
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		int set = Integer.parseInt(args[0]);					// domain set
		int uncertainty = Integer.parseInt(args[1]);			// domain uncertainty
		int goal = Integer.parseInt(args[2]);					// number of goals
		
		// simulator
		PlatformSimulator simulator = null;
		try
		{
			// build the plan database
			PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(
					"domains/satellite/dc/set" + set + "/satellite_u" + uncertainty + ".ddl", 
					"domains/satellite/dc/set" + set + "/satellite_u" + uncertainty + "_g" + goal + ".pdl");
			
			// create planner 
			Planner planner = PlannerBuilder.createAndSet(pdb);
			System.out.println("Start planning... ");
			// do planning
			SolutionPlan solution = planner.plan();
			System.out.println("Plan found after " + solution.getSolvingTime() + " msecs\n");
			System.out.println(solution.export());
			System.out.println();
			
			
			
			// crate satellite platform simulator
			simulator = PlatformProxyBuilder.build(
					PlatformSimulator.class,
					"etc/platform/dc/satellite/set" + set + "/config_u" + uncertainty + ".xml");
			
			// create executive
			Executive exec  = ExecutiveBuilder.createAndSet(DCExecutive.class, 0, solution.getHorizon());
			// link executive to platform simulator
			exec.link(simulator);
			// export the plan
			exec.initialize(solution.export());
			
			// get start time
			long start  = System.currentTimeMillis();
			// start platform simulator
			System.out.println("Starting platform on configuration file [\"etc/platform/dc/satellite/config_u\"" + uncertainty + "\".xml\"]\n");
			simulator.start();
			// start plan execution
			System.out.println("Starting plan execution...");
			if (exec.execute()) {
				// get execution time
				long execTime = System.currentTimeMillis() - start;
				System.out.println("... plan execution ends after " + (execTime / 1000) + " seconds\n");
			}
			else {
				// get execution time
				long execTime = System.currentTimeMillis() - start;
				System.out.println("... plan execution failure after " + (execTime / 1000) +  " seconds\n"
						+ "\t- failure-type: " + exec.getFailureCause().getType() + "\n"
						+ "\t- failure-tick: " + exec.getFailureCause().getInterruptionTick() + "\n"
						+ "\t- failure-node: " + exec.getFailureCause().getInterruptionNode() + "\n");
			}
			
			// stop simulator
			simulator.stop();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
	
}
