package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.reservoir;

import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.resource.costant.ReservoirResource;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentReference;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;

/**
 * 
 * @author anacleto
 *
 */
public class ReservoirResourceSchedulingResolver <T extends ReservoirResource> extends Resolver 
{
	@ComponentReference
	protected T component;
	
	/**
	 * 
	 */
	protected ReservoirResourceSchedulingResolver() {
		super(ResolverType.RESERVOIR_RESOURCE_SCHEDULING_RESOLVER);
	}

	@Override
	protected void doRetract(FlawSolution solution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doApply(FlawSolution solution) throws FlawSolutionApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected List<Flaw> doFindFlaws() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doComputeFlawSolutions(Flaw flaw) throws UnsolvableFlawFoundException {
		// TODO Auto-generated method stub
		
	}
	
	
}
