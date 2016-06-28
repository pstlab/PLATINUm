package it.uniroma3.epsl2.planner.heuristic.fsh;

import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;
import it.uniroma3.epsl2.planner.heuristic.fsh.filter.FlawFilterType;

/**
 * Default heuristic
 * 
 * @author anacleto
 *
 */
@FlawSelectionHeuristicConfiguration(
	
	// set pipeline filters
	pipeline = {
	
		FlawFilterType.TF
	}
)
public class TypePreferencesFlawSelectionHeuristic extends FlawSelectionHeuristic {

	/**
	 * 
	 */
	protected TypePreferencesFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.TFSH);
	}
}
