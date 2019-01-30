package it.istc.pst.platinum.testing.app.executive.fbt.alfa;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.ExecutiveBuilder;
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
public class FourByThreeALFAExecutiveTest
{
	private static final String DDL = "domains/fourbythree/alfa/assigned/alfa.ddl";
	private static final String PDL = "domains/fourbythree/alfa/assigned/alfa.pdl";

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			// create plan database
			PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(DDL, PDL);
			// create planner
			Planner planner = PlannerBuilder.createAndSet(pdb);
			// start deliberative process 
			SolutionPlan plan = planner.plan();
			System.out.println();
			System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
			System.out.println(plan);
			System.out.println();
			
			// display solution plan
			planner.display();
			
			// build executive plan database
			Executive exec = ExecutiveBuilder.createAndSet(Executive.class, pdb.getOrigin(), pdb.getHorizon());
			// export solution plan
			exec.initialize(plan.export());
			// start executing the plan
			exec.execute();
		}
		catch (SynchronizationCycleException  | ProblemInitializationException | NoSolutionFoundException ex) {
			System.err.println(ex.getMessage());
		}
		catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
		
	}
}
