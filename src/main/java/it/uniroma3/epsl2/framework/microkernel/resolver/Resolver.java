package it.uniroma3.epsl2.framework.microkernel.resolver;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.Constraint;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.lang.ex.ConstraintPropagationException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ParameterDataBaseFacadeReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalDataBaseFacadeReference;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacade;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacade;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 * @param <T>
 */
public abstract class Resolver extends ApplicationFrameworkObject implements FlawManager 
{
	@TemporalDataBaseFacadeReference
	protected TemporalDataBaseFacade tdb;
	
	@ParameterDataBaseFacadeReference
	protected ParameterDataBaseFacade pdb;
	
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	protected ResolverType type;
	
	/**
	 * 
	 * @param component
	 * @param type
	 * @param tdb
	 */
	protected Resolver(ResolverType type) {
		super();
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResolverType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawType getFlawType() {
		return this.type.getFlawType();
	}
	
	/**
	 * 
	 */
	@Override
	public List<Flaw> findFlaws() 
			throws UnsolvableFlawFoundException
	{
		// list of flaws on the related component
		List<Flaw> flaws = new ArrayList<>();
		// find flaws on component
		for (Flaw flaw :  this.doFindFlaws()) {
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
			throws FlawSolutionApplicationException {
		// check flaw type
		if (!solution.getFlaw().getType().equals(this.type.getFlawType())) {
			throw new FlawSolutionApplicationException("Impossible to apply solution for flaws of type type " + solution.getFlaw().getType());
		}
		
		// apply flaw solution
		this.doApply(solution);
	}
	
	/**
	 * 
	 */
	@Override
	public final void retract(FlawSolution solution) {
		// check flaw type
		if (!solution.getFlaw().getType().equals(this.type.getFlawType())) {
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
	 * @return
	 */
	protected abstract List<Flaw> doFindFlaws();
	
	/**
	 * 
	 * @param flaw
	 * @throws UnsolvableFlawFoundException
	 */
	protected abstract void doComputeFlawSolutions(Flaw flaw) 
			throws UnsolvableFlawFoundException;
}
