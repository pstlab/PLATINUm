package it.uniroma3.epsl2.deliberative.heuristic;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;

/**
 * 
 * @author anacleto
 *
 */
@FlawSelectionHeuristicConfiguration(
	// set pipeline of filters
	pipeline = {
		FlawFilterType.SFF,			// semantic-based flaw filter
		FlawFilterType.HFF,			// hierarchy-based flaw filter
	}
)
public class H2SelectionHeuristic extends FlawSelectionHeuristic {

	/**
	 * 
	 */
	protected H2SelectionHeuristic() {
		super(FlawSelectionHeuristicType.H2);
	}
}
