package it.uniroma3.epsl2.planner.heuristic.fsh;

import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;

/**
 * 
 * @author anacleto
 *
 */
@FlawSelectionHeuristicConfiguration(
	
	// set the pipeline of filters to apply
	pipeline = {}
)
public class BlindFlawSelectionHeuristic extends FlawSelectionHeuristic {

	/**
	 * 
	 */
	protected BlindFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.BLIND);
	}
}
