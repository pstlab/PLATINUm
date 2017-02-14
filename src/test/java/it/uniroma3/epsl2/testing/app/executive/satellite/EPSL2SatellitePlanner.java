package it.uniroma3.epsl2.testing.app.executive.satellite;

import it.uniroma3.epsl2.deliberative.Planner;
import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristicType;
import it.uniroma3.epsl2.deliberative.search.SearchStrategyType;
import it.uniroma3.epsl2.deliberative.solver.SolverType;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.PlannerConfiguration;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
@PlannerConfiguration(

	// set solver
	solver = SolverType.PSEUDO_CONTROLLABILITY_AWARE,
		
	// set heuristic
	heuristic = FlawSelectionHeuristicType.HFSH,
	
	// set strategy
	strategy = SearchStrategyType.DFS,
	
	// log level
	logging = FrameworkLoggingLevel.OFF
)
public class EPSL2SatellitePlanner extends Planner
{
	/**
	 * 
	 */
	protected EPSL2SatellitePlanner() {
		super();
	}
	
//	/**
//	 * 
//	 * @param args
//	 */
//	public static void main(String[] args) 
//	{ 
//		try 
//		{
//			Planner planner = PlannerBuilder.build(EPSL2SatellitePlanner.class.getName(), DDL, PDL);	
//			// start planning
//			SolutionPlan plan = planner.plan();
//			// solution found
//			System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
//			// print the resulting plan
//			System.out.println(plan);
//			
//			// display the resulting plant
//			planner.display();
//		}
//		catch (NoSolutionFoundException ex) {
//			// no solution found
//			System.err.println(ex.getMessage());
//		}
//		catch (ProblemInitializationException ex) {
//			System.err.println(ex.getMessage());
//			ex.printStackTrace(System.err);
//		}
//		catch (Exception ex) {
//			System.err.println(ex.getMessage());
//			ex.printStackTrace(System.err);
//		}
//	}
}
