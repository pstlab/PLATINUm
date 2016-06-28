package it.uniroma3.epsl2.app.fourbythree;

import it.uniroma3.epsl2.framework.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.PlannerConfiguration;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;
import it.uniroma3.epsl2.planner.Planner;
import it.uniroma3.epsl2.planner.PlannerBuilder;
import it.uniroma3.epsl2.planner.heuristic.fsh.FlawSelectionHeuristicType;
import it.uniroma3.epsl2.planner.search.SearchStrategyType;
import it.uniroma3.epsl2.planner.solver.SolverType;

/**
 * 
 * @author anacleto
 *
 */
@PlannerConfiguration(
		
		// set the solving strategy the planner applies
		solver = SolverType.PSEUDO_CONTROLLABILITY_AWARE,
		
		// set the flaw selection heuristic
		heuristic = FlawSelectionHeuristicType.HFSH,
		
		// set search strategy
		strategy = SearchStrategyType.DFD,

		// set logging level
		logging = FrameworkLoggingLevel.OFF
)
public class FourByThreePlanner extends Planner {

	private static final String DDL_FILE = "domains/fourbythree/test/fourbythree_large.ddl";
	private static final String PDL_FILE = "domains/fourbythree/test/fourbythree.pdl";
	
	/**
	 * 
	 */
	protected FourByThreePlanner() {
		super();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			// create the planner
			FourByThreePlanner planner = PlannerBuilder.build(FourByThreePlanner.class.getName(), DDL_FILE, PDL_FILE);
			
			System.out.println("*******************************************************************");
			System.out.println("Test: [ddl= " + DDL_FILE + ", pdl= " + PDL_FILE + "]");
			System.out.println("Start planning... ");
			try {
				
				// start planning
				Plan plan = planner.plan();
				System.out.println("... solution found after " + plan.getSolvingTime() + " msecs");
				System.out.println();
				// print final plan
				System.out.println(plan);
				System.out.println();
				// display resulting plan
				planner.display();
			}
			catch (NoSolutionFoundException ex) {
				System.out.println("... no solutions have been found");
			}
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
