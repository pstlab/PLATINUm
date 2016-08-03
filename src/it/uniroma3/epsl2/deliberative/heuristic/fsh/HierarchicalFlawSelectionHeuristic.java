package it.uniroma3.epsl2.deliberative.heuristic.fsh;

import it.uniroma3.epsl2.deliberative.heuristic.fsh.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;

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
