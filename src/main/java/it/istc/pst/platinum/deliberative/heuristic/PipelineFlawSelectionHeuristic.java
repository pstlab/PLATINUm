package it.istc.pst.platinum.deliberative.heuristic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.deliberative.heuristic.filter.FlawFilter;
import it.istc.pst.platinum.deliberative.heuristic.filter.FlawFilterType;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.PipelineConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.deliberative.PipelinePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoFlawFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author anacleto
 *
 */
@PipelineConfiguration(pipeline= {
		FlawFilterType.TFF,
		FlawFilterType.HFF,
		FlawFilterType.DFF
})
public class PipelineFlawSelectionHeuristic extends FlawSelectionHeuristic
{
	@PipelinePlaceholder
	private List<FlawFilter> filters;
	
	/**
	 * 
	 */
	protected PipelineFlawSelectionHeuristic() {
		super(FlawSelectionHeuristicType.PIPELINE.getLabel());
		// initialize filter list
		this.filters = new ArrayList<>();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	private void init() 
	{
		// get annotation
		PipelineConfiguration annot = FrameworkReflectionUtils.doFindnAnnotation(this.getClass(), PipelineConfiguration.class);
		// get filter pipeline
		for (FlawFilterType type : annot.pipeline()) {
			// create flaw filter 
			FlawFilter filter = this.doCreateFlawFilter(type.getClassName());
			// add filter 
			this.filters.add(filter);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> choose() 
			throws UnsolvableFlawException, NoFlawFoundException 
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
