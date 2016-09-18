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
		FlawFilterType.HFF,			// flaw dependency filter
		FlawFilterType.TFF			// flaw type filter
	}
)
public class HierarchyTypeFlawSelectionHeuristic extends FlawSelectionHeuristic 
{
	/**
	 * 
	 */
	protected HierarchyTypeFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.HTFSH);
	}
}
