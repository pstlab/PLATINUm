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
		FlawFilterType.SFF,
		FlawFilterType.HFF,			// hierarchy-based flaw filter
		FlawFilterType.TFF,			// type-baed flaw filter
	}
)
public class SemanticHierarchyFLawSelectionHeuristic extends FlawSelectionHeuristic {

	/**
	 * 
	 */
	protected SemanticHierarchyFLawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.SHFSH);
	}
}