package it.uniroma3.epsl2.planner.heuristic.fsh;

import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;
import it.uniroma3.epsl2.planner.heuristic.fsh.filter.FlawFilterType;

/**
 * 
 * @author anacleto
 *
 */
@FlawSelectionHeuristicConfiguration(
	
	// set pipeline of filters
	pipeline = {
		
		FlawFilterType.DgF
	}
)
public class DependencyGraphFlawSelectionHeuristic extends FlawSelectionHeuristic {

	/**
	 * 
	 */
	protected DependencyGraphFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.DgFSH);
	}
}
