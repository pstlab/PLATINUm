package it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.RelationPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.DomainComponentPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacade;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;

/**
 * 
 * @author alessandro
 *
 * @param <T>
 */
public abstract class Resolver<T extends DomainComponent> extends FrameworkObject implements FlawManager 
{
	@TemporalFacadePlaceholder
	protected TemporalFacade tdb;
	
	@ParameterFacadePlaceholder
	protected ParameterFacade pdb;
	
	@DomainComponentPlaceholder
	protected T component;
	
	protected String label;
	protected FlawType[] flawTypes;

	// static information
	protected static final AtomicInteger FLAW_COUNTER = new AtomicInteger(0);
	
	
	/**
	 * 
	 * @param label
	 * @param flawTypes
	 */
	protected Resolver(String label, FlawType[] flawTypes) {
		super();
		this.label = label;
		this.flawTypes = flawTypes;
		
		// reset counter if necessary
		FLAW_COUNTER.set(0);
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
	public FlawType[] getFlawTypes() {
		return this.flawTypes;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized List<Flaw> checkFlaws() {
		// get detected flaws
		return new ArrayList<>(this.doFindFlaws());
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized List<Flaw> findFlaws() 
			throws UnsolvableFlawException {
		
		// list of flaws on the related component
		List<Flaw> flaws = new ArrayList<>();
		// find flaws on component
		for (Flaw flaw :  this.doFindFlaws()) {
			
			// compute possible solutions
			this.doComputeFlawSolutions(flaw);
			// add flaw
			flaws.add(flaw);
		}
		
		
		// randomly select a flaw if any 
//		List<Flaw> list = new ArrayList<>();
//		if (flaws.size() > 1) {
//			
//			// get the flaw with the lowest number of solutions - fail first principle
//			Collections.sort(flaws, new Comparator<Flaw>() {
//				
//				/**
//				 * 
//				 */
//				@Override
//				public int compare(Flaw o1, Flaw o2) {
//					
//					// compare available solutions
//					return o1.getSolutions().size() < o2.getSolutions().size() ? -1 : 
//						o1.getSolutions().size() > o2.getSolutions().size() ? 1 : 0;
//				}
//			});
//			
//			// get the hardest flaw to solve
//			list.add(flaws.get(0));
//
//		} else {
//			
//			list = flaws;
//		}
		
		// get detected flaws
		return flaws;
	}
	
	/**
	 * 
	 */
	@Override
	public final synchronized void apply(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// check flaw type
		if (!Arrays.asList(this.flawTypes).contains(solution.getFlaw().getType())) {
			throw new FlawSolutionApplicationException("Impossible to apply solution for flaws of type type " + solution.getFlaw().getType());
		}
		
		// apply flaw solution
		this.doApply(solution);
	}
	
	/**
	 * 
	 */
	@Override
	public final synchronized void restore(FlawSolution solution) 
			throws Exception 
	{
		// check flaw type
		if (!Arrays.asList(this.flawTypes).contains(solution.getFlaw().getType())) {
			throw new FlawSolutionApplicationException("Impossible to restore solution for flaws of type type " + solution.getFlaw().getType());
		}
		
		// apply flaw solution
		this.doRestore(solution);
	}
	
	/**
	 * 
	 */
	@Override
	public final synchronized void retract(FlawSolution solution) 
	{
		// check flaw type
		if (!Arrays.asList(this.flawTypes).contains(solution.getFlaw().getType())) {
			throw new RuntimeException("Impossible to retract solution for flaws of type " + solution.getFlaw().getType());
		}
		
		// retract solution
		this.doRetract(solution);
	}
	
	/**
	 * 
	 * @param solution
	 */
	protected void doRetract(FlawSolution solution)  {
		
		// get the list of activated relations
		for (Relation relation : solution.getActivatedRelations()) {
			// get reference component
			DomainComponent refComp = relation.getReference().getComponent();
			// deactivate relation
			refComp.deactivate(relation);
		}
		
		// get the list of created relations
		for (Relation relation : solution.getCreatedRelations()) {
			// get reference component
			DomainComponent refComp = relation.getReference().getComponent();
			// remove relation from the data structure
			refComp.delete(relation);
		}
		
		// get the list of activated decisions
		for (Decision decision : solution.getActivatedDecisions()) {
			// get decision component
			DomainComponent dComp = decision.getComponent();
			// deactivate decision
			dComp.deactivate(decision);
		}
		
		// get the list of created decisions
		for (Decision decision : solution.getCreatedDecisions()) {
			// get decision component
			DomainComponent dComp = decision.getComponent();
			// delete decision from pending "list" 
			dComp.free(decision);
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
	 * @throws RelationPropagationException
	 * @throws DecisionPropagationException
	 */
	protected void doRestore(FlawSolution solution) 
			throws RelationPropagationException, DecisionPropagationException {
		
		// list of restored decisions
		Set<Decision> dRestored = new HashSet<>();
		// list of activated decisions
		Set<Decision> dActivated = new HashSet<>();
		// list of restored relations
		Set<Relation> rRestored = new HashSet<>();
		// list of activated relations
		Set<Relation> rActivated = new HashSet<>();
		
		try {
			
			// get the list of created decisions
			for (Decision decision : solution.getCreatedDecisions()) {
				// get decision component
				DomainComponent dComp = decision.getComponent();
				// restore decision SILENT -> PENDING
				dComp.restore(decision);
				// add 
				dRestored.add(decision);
			}
			
			
			// check activated decisions
			for (Decision decision : solution.getActivatedDecisions()) {
				// get decision component
				DomainComponent dComp = decision.getComponent();
				// activate decision PENDING -> ACTIVE
				dComp.activate(decision);
				// add
				dActivated.add(decision);
			}
			
			// get the list of created relations
			for (Relation relation : solution.getCreatedRelations()) {
				// get reference component
				DomainComponent refComp = relation.getReference().getComponent();
				// restore relation
				refComp.restore(relation);
				// add
				rRestored.add(relation);
			}

			// check activated relations
			for (Relation relation : solution.getActivatedRelations()) {
				// get reference component
				DomainComponent refComp = relation.getReference().getComponent();
				// activate relation
				refComp.activate(relation);
				// add relation to committed list
				rActivated.add(relation);
			}
			
			// check consistency
			this.tdb.verify();
			this.pdb.verify();
			
		} catch (DecisionPropagationException | ConsistencyCheckException ex) {
			
			// deactivate activated relations
			for (Relation rel : rActivated) {
				
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				// deactivate relation
				refComp.deactivate(rel);
			}
			
			// remove restored relations
			for (Relation rel : rRestored) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				// deactivate relation
				refComp.delete(rel);
			}
			
			// deactivate decisions
			for (Decision dec : dActivated) {
				// get decision component
				DomainComponent decComp = dec.getComponent();
				// deactivate decision
				decComp.deactivate(dec);
			}
			
			// remove restored decisions
			for (Decision dec: dRestored) {
				// get decision component
				DomainComponent decComp = dec.getComponent();
				// free decision
				decComp.free(dec);
			}
			
			// throw exception
			throw new DecisionPropagationException(ex.getMessage());
		}
	}
	
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
