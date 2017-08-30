package it.istc.pst.platinum.framework.microkernel.resolver;

import java.util.ArrayList;
import java.util.List;

import it.istc.pst.platinum.framework.domain.component.Constraint;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConstraintPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.parameter.ParameterFacade;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.time.TemporalFacade;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 * @param <T>
 */
public abstract class Resolver extends ApplicationFrameworkObject implements FlawManager 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE_LOGGER)
	protected FrameworkLogger logger;
	
	@TemporalFacadePlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_TEMPORAL_FACADE)
	protected TemporalFacade tdb;
	
	@ParameterFacadePlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PARAMETER_FACADE)
	protected ParameterFacade pdb;
	
	protected String label;
	protected FlawType flawType;
	
	/**
	 * 
	 * @param label
	 * @param flawType
	 */
	protected Resolver(String label, FlawType flawType) {
		super();
		this.label = label;
		this.flawType = flawType;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawType getFlawType() {
		return this.flawType;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Flaw> findFlaws() 
			throws UnsolvableFlawException
	{
		// list of flaws on the related component
		List<Flaw> flaws = new ArrayList<>();
		// find flaws on component
		for (Flaw flaw :  this.doFindFlaws()) 
		{
			// compute possible solutions
			this.doComputeFlawSolutions(flaw);
			// add flaw
			flaws.add(flaw);
		}
		
		// get detected flaws
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	public final void apply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// check flaw type
		if (!solution.getFlaw().getType().equals(this.flawType)) {
			throw new FlawSolutionApplicationException("Impossible to apply solution for flaws of type type " + solution.getFlaw().getType());
		}
		
		// apply flaw solution
		this.doApply(solution);
	}
	
	/**
	 * 
	 */
	@Override
	public final void restore(FlawSolution solution) 
			throws Exception 
	{
		// check flaw type
		if (!solution.getFlaw().getType().equals(this.flawType)) {
			throw new FlawSolutionApplicationException("Impossible to restore solution for flaws of type type " + solution.getFlaw().getType());
		}
		
		// apply flaw solution
		this.doRestore(solution);
	}
	
	/**
	 * 
	 */
	@Override
	public final void retract(FlawSolution solution) 
	{
		// check flaw type
		if (!solution.getFlaw().getType().equals(this.flawType)) {
			throw new RuntimeException("Impossible to retract solution for flaws of type " + solution.getFlaw().getType());
		}
		
		// retract solution
		this.doRetract(solution);
	}
	
	/**
	 * 
	 * @param solution
	 */
	protected abstract void doRetract(FlawSolution solution); 
	
	/**
	 * 
	 * @param constraint
	 * @throws ConstraintPropagationException
	 */
	protected void doPropagetConstraint(Constraint constraint) 
			throws ConstraintPropagationException 
	{
		// check constraint category
		switch (constraint.getCategory()) 
		{
			// temporal constraint
			case  TEMPORAL_CONSTRAINT : {
				// cast constraint
				TemporalConstraint cons = (TemporalConstraint) constraint;
				// propagate temporal constraint
				this.tdb.propagate(cons);
			}
			break;
			
			// parameter constraint
			case PARAMETER_CONSTRAINT : {
				// cast constraint
				ParameterConstraint  cons = (ParameterConstraint) constraint;
				// propagate parameter constraint
				this.pdb.propagate(cons);
			}
			break;
		}
	}
	
	/**
	 * 
	 * @param constraint
	 */
	protected void doRetractConstraint(Constraint constraint) {
		// check constraint category
		switch (constraint.getCategory()) {
		
			// temporal constraint
			case  TEMPORAL_CONSTRAINT : {
				
				// cast constraint
				TemporalConstraint cons = (TemporalConstraint) constraint;
				// retract temporal constraint
				this.tdb.retract(cons);
			}
			break;
			
			// parameter constraint
			case PARAMETER_CONSTRAINT : {
				
				// cast constraint
				ParameterConstraint cons = (ParameterConstraint) constraint;
				// retract parameter constraint
				this.pdb.retract(cons);
			}
			break;
		}
	}
	
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	protected abstract void doApply(FlawSolution solution) 
			throws FlawSolutionApplicationException;
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	protected abstract void doRestore(FlawSolution solution) 
			throws Exception;
	
	/**
	 * 
	 * @return
	 */
	protected abstract List<Flaw> doFindFlaws();
	
	/**
	 * 
	 * @param flaw
	 * @throws UnsolvableFlawException
	 */
	protected abstract void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawException;
}
