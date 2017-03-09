package it.uniroma3.epsl2.framework.microkernel.resolver.scheduling.reservoir;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.domain.component.resource.costant.ReservoirResource;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
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
	
	
	/**
	 * 
	 */
	@Override
	protected void doRestore(FlawSolution solution) 
			throws Exception 
	{
		// get created decisions 
		List<Decision> dCreated = solution.getCreatedDecisions();
		// restore created decisions
		for (Decision dec : dCreated) {
			// restore decision
			this.component.restore(dec);
		}
		
		// get activated decisions
		List<Decision> dActivated = solution.getActivatedDecisisons();
		List<Decision> commitDecs = new ArrayList<>();
		// activate decisions
		for (Decision dec : dActivated) 
		{
			try
			{
				// activate decision
				this.component.add(dec);
				commitDecs.add(dec);
			}
			catch (DecisionPropagationException ex) 
			{
				// deactivate committed decisions
				for (Decision d : commitDecs) {
					// deactivate decision
					this.component.delete(d);
				}

				// error while resetting flaw solution
				throw new Exception("Error while resetting flaw solution:\n- " + solution + "\n");
			}
		}
		
		// get activated relations
		List<Relation> rActivated = solution.getActivatedRelations();
		// list of committed relations
		List<Relation> commitRels = new ArrayList<>();
		// activate relations
		for (Relation rel : rActivated) 
		{
			try
			{
				// activate relation
				this.component.add(rel);
				commitRels.add(rel);
			}
			catch (RelationPropagationException ex) {
				// deactivate committed relations
				for (Relation r : commitRels) {
					// deactivate relation
					this.component.delete(r);
				}
				
				// deactivate committed decisions
				for (Decision d : commitDecs) {
					// deactivate 
					this.component.delete(d);
				}
				
				throw new Exception("Error while resetting flaw solution:\n- " + solution + "\n");
			}
		}
	}
	
}
