package it.uniroma3.epsl2.deliberative.heuristic.fsh;

import it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;

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
