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
		FlawFilterType.TKFF,	// temporal knowledge-based flaw filter
		FlawFilterType.HFF,		// hierarchy-based flaw filter
		FlawFilterType.TFF		// type-based flaw filter
	}
)
public class KnowledgeHierarchyTypeFlawSelectionHeuristic extends FlawSelectionHeuristic 
{
	/**
	 * 
	 */
	protected KnowledgeHierarchyTypeFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.KHTFSH);
	}
}
