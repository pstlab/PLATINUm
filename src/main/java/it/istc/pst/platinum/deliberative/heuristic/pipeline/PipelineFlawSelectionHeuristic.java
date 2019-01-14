package it.istc.pst.platinum.deliberative.heuristic.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.deliberative.heuristic.FlawSelectionHeuristic;
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
		PlanningFlawInspector.class,
		HierarchyFlawInspector.class,
		DegreeFlawInspector.class
})
public class PipelineFlawSelectionHeuristic extends FlawSelectionHeuristic
{
	@PipelinePlaceholder
	private List<FlawInspector> inspectors;
	private FlawInspector behaviorBuilder;
	
	/**
	 * 
	 */
	protected PipelineFlawSelectionHeuristic() {
		super("Heuristics:Pipeline");
		// initialize filter list
		this.inspectors = new ArrayList<>();
		// initialize behavior builder
		this.behaviorBuilder = null;
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
		for (Class<? extends FlawInspector> clazz : annot.pipeline()) {
			// create flaw filter 
			FlawInspector filter = this.doCreateFlawFilter(clazz.getName());
			// add filter 
			this.inspectors.add(filter);
		}
		
		// create flaw filter
		this.behaviorBuilder = this.doCreateFlawFilter(BehaviorFlawInspector.class.getName());
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> choose() 
			throws UnsolvableFlawException, NoFlawFoundException 
	{
		// take the first inspector to detect flaws
		FlawInspector inspector = this.inspectors.get(0);
		// extract flaws
		Set<Flaw> flaws = inspector.detectFlaws();
		// filter flaws according to other inspectors of the pipeline
		for (int index = 1; index < this.inspectors.size(); index++) {
			// get inspector
			FlawInspector i = this.inspectors.get(index);
			// apply inspector and get the subset of flaws
			flaws = i.filter(flaws);
		}
		
		// check the plan has been solved
		if (flaws.isEmpty()) {
			// build behaviors
			flaws = this.behaviorBuilder.detectFlaws();
		}
		
		// check if any flaw has been found
		if (flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		
		// get "equivalent" flaws to solve
		return flaws;
	}
}
