package it.istc.pst.platinum.framework.microkernel.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.platinum.framework.domain.component.Constraint;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.microkernel.FrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.DomainComponentPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConstraintPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.parameter.ParameterFacade;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.time.TemporalFacade;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;

/**
 * 
 * @author anacleto
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
	protected void doRetract(FlawSolution solution) 
	{
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
	 * @throws RelationPropagationException
	 * @throws DecisionPropagationException
	 */
	protected void doRestore(FlawSolution solution) 
			throws RelationPropagationException, DecisionPropagationException 
	{
		// list of actually activated decisions
		Set<Decision> dCommitted = new HashSet<>();
		// list of actually activated relations
		Set<Relation> rCommitted = new HashSet<>();
		
		try
		{
			// get the list of created decisions
			for (Decision decision : solution.getCreatedDecisions()) {
				// get decision component
				DomainComponent dComp = decision.getComponent();
				// restore decision SILENT -> PENDING
				dComp.restore(decision);
			}
			
			
			// check activated decisions
			for (Decision decision : solution.getActivatedDecisions()) {
				// get decision component
				DomainComponent dComp = decision.getComponent();
				
				// add to committed list
				dCommitted.add(decision);
				// activate decision PENDING -> ACTIVE
				dComp.activate(decision);
			}
			
			// get the list of created relations
			for (Relation relation : solution.getCreatedRelations()) {
				// get reference component
				DomainComponent refComp = relation.getReference().getComponent();
				// restore relation
				refComp.restore(relation);
			}

			// check activated relations
			for (Relation relation : solution.getActivatedRelations()) {
				// add relation to committed list
				rCommitted.add(relation);
				// get reference component
				DomainComponent refComp = relation.getReference().getComponent();
				// activate relation
				refComp.activate(relation);
			}
		}
		catch (DecisionPropagationException ex) 
		{
			// deactivate committed relations
			for (Relation rel : rCommitted) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				// deactivate relation
				refComp.deactivate(rel);
			}
			
			// deactivate committed decisions
			for (Decision dec : dCommitted) {
				// get decision component
				DomainComponent decComp = dec.getComponent();
				// deactivate decision
				decComp.deactivate(dec);
			}
			
			// remove created relations
			for (Relation rel : solution.getCreatedRelations()) {
				// get decision component
				DomainComponent refComp = rel.getReference().getComponent();
				// delete relation
				refComp.delete(rel);
			}
			
			// remove created decision
			for (Decision dec : solution.getCreatedDecisions()) {
				// get component
				DomainComponent comp = dec.getComponent();
				// free decision PENDING -> SILENT
				comp.free(dec);
			}
			
			// throw exception
			throw new DecisionPropagationException(ex.getMessage());
		}
		catch (RelationPropagationException ex) 
		{
			// deactivate committed relations
			for (Relation rel : rCommitted) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				// deactivate relation
				refComp.deactivate(rel);
			}
			
			// deactivate committed decisions
			for (Decision dec : dCommitted) {
				// get decision component
				DomainComponent decComp = dec.getComponent();
				// deactivate decision
				decComp.deactivate(dec);
			}
			
			// remove created relations
			for (Relation rel : solution.getCreatedRelations()) {
				// get decision component
				DomainComponent refComp = rel.getReference().getComponent();
				// delete relation
				refComp.delete(rel);
			}
			
			// remove created decision
			for (Decision dec : solution.getCreatedDecisions()) {
				// get component
				DomainComponent comp = dec.getComponent();
				// free decision PENDING -> SILENT
				comp.free(dec);
			}
			
			// throw exception
			throw new RelationPropagationException(ex.getMessage());
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
