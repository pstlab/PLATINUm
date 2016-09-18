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
		FlawFilterType.HFF			// dependency-based filter
	}
)
public class HierarchyFlawSelectionHeuristic extends FlawSelectionHeuristic {

	/**
	 * 
	 */
	protected HierarchyFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.HFSH);
	}
}
