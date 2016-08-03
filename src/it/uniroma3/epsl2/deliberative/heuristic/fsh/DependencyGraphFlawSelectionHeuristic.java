package it.uniroma3.epsl2.deliberative.heuristic.fsh;

import it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;

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
