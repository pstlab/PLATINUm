package it.uniroma3.epsl2.planner.heuristic.fsh;

import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;
import it.uniroma3.epsl2.planner.heuristic.fsh.filter.FlawFilterType;

/**
 * 
 * @author anacleto
 *
 */
@FlawSelectionHeuristicConfiguration(
	
	// set the pipeline of filters to apply
	pipeline = {
			
		FlawFilterType.DgF,			// flaw dependency filter
			
		FlawFilterType.TF			// flaw type filter
	}
)
public class HierarchicalFlawSelectionHeuristic extends FlawSelectionHeuristic {

	/**
	 * 
	 */
	protected HierarchicalFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.HFSH);
	}
}
