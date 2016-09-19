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
		FlawFilterType.HFF,			// hierarchy-based flaw filter
		FlawFilterType.TFF,			// type-based flaw filter
		FlawFilterType.SFF			// semantic-based flaw filter
	}
)
public class H1SelectionHeuristic extends FlawSelectionHeuristic {

	/**
	 * 
	 */
	protected H1SelectionHeuristic() {
		super(FlawSelectionHeuristicType.H1);
	}
}
