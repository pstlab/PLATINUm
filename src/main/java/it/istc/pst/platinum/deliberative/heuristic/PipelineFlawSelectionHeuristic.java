package it.istc.pst.platinum.deliberative.heuristic;

import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.deliberative.heuristic.filter.FlawFilter;
import it.istc.pst.platinum.deliberative.heuristic.filter.FlawFilterType;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.FlawFilterPipelineModule;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
public class PipelineFlawSelectionHeuristic extends FlawSelectionHeuristic
{
	@FlawFilterPipelineModule(pipeline= {
			FlawFilterType.HFF,
			FlawFilterType.TFF,
	})
	private List<FlawFilter> filters;
	
	/**
	 * 
	 */
	protected PipelineFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.PIPELINE.getLabel());
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
