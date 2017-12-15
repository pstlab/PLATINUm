package it.istc.pst.platinum.testing.app.executive.fbt.itia;

import it.istc.pst.platinum.deliberative.app.Planner;
import it.istc.pst.platinum.deliberative.app.PlannerBuilder;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.est.EarliestStartTimeExecutive;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
public class FourByThreeITIAPlannerTest
{
	private static final String DDL = "domains/fourbythree/itia/itia.ddl";
	private static final String PDL = "domains/fourbythree/itia/itia.pdl";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			// build plan database 
			PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(DDL, PDL);
			// build planner
			Planner planner = PlannerBuilder.createAndSet(pdb);
			// start deliberative process 
			SolutionPlan plan = planner.plan();
			System.out.println();
			System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
			System.out.println(plan);
			System.out.println();
			
			// display solution plan
			planner.display();
			
			// create executor
			Executive executor = new EarliestStartTimeExecutive();
			// export solution plan
			executor.initialize(planner.export(plan));
			// start executing the plan
			executor.execute();
		}
		catch (SynchronizationCycleException  | ProblemInitializationException | NoSolutionFoundException ex) {
			System.err.println(ex.getMessage());
		}
		catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
