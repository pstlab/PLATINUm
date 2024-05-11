package it.cnr.istc.pst.platinum.ai.deliberative.heuristic.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.deliberative.heuristic.FlawSelectionHeuristic;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.deliberative.PipelineConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.deliberative.PipelinePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.NoFlawFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.utils.reflection.FrameworkReflectionUtils;

/**
 * 
 * @author alessandro
 *
 */
@PipelineConfiguration(pipeline= {
		HierarchicalPlanFlawInspector.class,
//		FailFirstFlawInspector.class
})
public class PipelineFlawSelectionHeuristic extends FlawSelectionHeuristic {
	
	@PipelinePlaceholder
	private List<FlawInspector> inspectors;
	
	/**
	 * 
	 */
	protected PipelineFlawSelectionHeuristic() {
		super("PipelineFlawSelectionHeuristic");
		// set filter list
		this.inspectors = new ArrayList<>();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	private void init() {
		
		// get annotation
		PipelineConfiguration annot = FrameworkReflectionUtils.doFindnAnnotation(this.getClass(), PipelineConfiguration.class);
		// get filter pipeline
		for (Class<? extends FlawInspector> clazz : annot.pipeline()) {
			// create flaw filter 
			FlawInspector filter = this.doCreateFlawFilter(clazz.getName());
			// add filter 
			this.inspectors.add(filter);
		}
	}
	
	/**
	 * 
	 */
//	@Override
//	public Set<Flaw> filter(Set<Flaw> flaws) 
//			throws NoFlawFoundException {
//		
//		// check if any flaw has been found
//		if (flaws.isEmpty()) {
//			// throw exception
//			throw new NoFlawFoundException("No flaw has been found in the current plan");
//		}
//		
//		// set of filtered
//		Set<Flaw> filtered = new HashSet<>(flaws);
//		// filter flaws according to other inspectors of the pipeline
//		for (int index = 0; index < this.inspectors.size(); index++) {
//			// get inspector
//			FlawInspector i = this.inspectors.get(index);
//			// apply inspector and get the subset of flaws
//			filtered = i.filter(filtered);
//		}
//		
//		// get filtered flaws
//		return filtered;
//	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> choose() 
			throws UnsolvableFlawException, NoFlawFoundException {
		
		// take the first inspector to detect flaws
		FlawInspector inspector = this.inspectors.get(0);
		// extract flaws
		Set<Flaw> flaws = inspector.detectFlaws();
		// check if any flaw has been found
		if (flaws.isEmpty()) {
			// throw exception
			throw new NoFlawFoundException("No flaw has been found in the current plan");
		}
		
		// filter flaws according to other inspectors of the pipeline
		for (int index = 1; index < this.inspectors.size(); index++) {
			// get inspector
			FlawInspector i = this.inspectors.get(index);
			// apply inspector and get the subset of flaws
			flaws = i.filter(flaws);
		}
		
		// get "equivalent" flaws to solve
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> check() {
		
		// take the first inspector to detect flaws
		FlawInspector inspector = this.inspectors.get(0);
		// extract flaws
		Set<Flaw> flaws = inspector.check();
		// check flaws to filter
		if (!flaws.isEmpty()) {
			
			// filter flaws according to other inspectors of the pipeline
			for (int index = 1; index < this.inspectors.size(); index++) {
				// get inspector
				FlawInspector i = this.inspectors.get(index);
				// apply inspector and get the subset of flaws
				flaws = i.filter(flaws);
			}
		}
		
		// get "equivalent" flaws to solve
		return flaws;
	}
}
