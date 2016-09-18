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
		FlawFilterType.TFF		// type-based flaw filter
	}
)
public class TypeFlawSelectionHeuristic extends FlawSelectionHeuristic 
{
	/**
	 * 
	 */
	protected TypeFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.TFSH);
	}
}
