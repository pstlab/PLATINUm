package it.uniroma3.epsl2.deliberative.heuristic.shfsh;

import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristic;
import it.uniroma3.epsl2.deliberative.heuristic.FlawSelectionHeuristicType;
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
		FlawFilterType.FFF			// fail-first flaw filter
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
