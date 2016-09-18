package it.uniroma3.epsl2.testing.app.executive.fbt.etfa16;

import it.uniroma3.epsl2.deliberative.Planner;
import it.uniroma3.epsl2.deliberative.PlannerBuilder;
import it.uniroma3.epsl2.deliberative.heuristic.fsh.FlawSelectionHeuristicType;
import it.uniroma3.epsl2.deliberative.search.SearchStrategyType;
import it.uniroma3.epsl2.deliberative.solver.SolverType;
import it.uniroma3.epsl2.framework.lang.ex.NoSolutionFoundException;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.PlannerConfiguration;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
@PlannerConfiguration(
		
		// set the solving strategy the planner applies
		solver = SolverType.PSEUDO_CONTROLLABILITY_AWARE,
		
		// set the flaw selection heuristic
		heuristic = FlawSelectionHeuristicType.HTFSH,
		
		// set search strategy
		strategy = SearchStrategyType.DFD,

		// set logging level
		logging = FrameworkLoggingLevel.OFF
)
public class FourByThreePlanner extends Planner {

	private static final String DDL_FILE = "domains/fourbythree/beta/fourbythree.ddl";
	private static final String PDL_FILE = "domains/fourbythree/beta/fourbythree.pdl";
	
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
			FourByThreePlanner planner = PlannerBuilder.
					build(FourByThreePlanner.class.getName(), DDL_FILE, PDL_FILE);
			
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
