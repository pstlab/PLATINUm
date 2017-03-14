package it.uniroma3.epsl2.deliberative.heuristic;

import java.util.List;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilter;
import it.uniroma3.epsl2.deliberative.heuristic.filter.FlawFilterType;
import it.uniroma3.epsl2.framework.lang.ex.NoFlawFoundException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.cfg.FlawSelectionHeuristicConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.planner.inject.FilterPipelineReference;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
@FlawSelectionHeuristicConfiguration(
	// set pipeline of filters
	pipeline = {
		FlawFilterType.TFF,			// hierarchy-based flaw filter
		FlawFilterType.HFF,			// type-based flaw filter
	}
)
public class PipelineFlawSelectionHeuristic extends FlawSelectionHeuristic
{
	@FilterPipelineReference
	private List<FlawFilter> filters;
	
	/**
	 * 
	 */
	protected PipelineFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.PIPELINE);
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> choose() 
			throws UnsolvableFlawFoundException, NoFlawFoundException 
	{
		// set of detected flaws
		Set<Flaw> flaws = null;
		// iteratively find and filter flaws
		for (FlawFilter ff : this.filters) 
		{
			// check if first filter to be applied
			if (flaws == null) {
				// apply filter
				flaws = ff.filter();
			}
			else {
				// apply filter
				flaws = ff.filter(flaws);
			}
		}
		
		// check if any flaw has been found
		if (flaws == null || flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		
		// get "equivalent" flaws to solve
		return flaws;
	}
}
