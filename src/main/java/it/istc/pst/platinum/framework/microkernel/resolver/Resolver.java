package it.istc.pst.platinum.framework.microkernel.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.istc.pst.platinum.framework.domain.component.Constraint;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkContainer;
import it.istc.pst.platinum.framework.microkernel.ApplicationFrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ComponentPlaceholder;
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
import it.istc.pst.platinum.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 * @param <T>
 */
public abstract class Resolver<T extends DomainComponent> extends ApplicationFrameworkObject implements FlawManager 
{
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE_LOGGER)
	protected FrameworkLogger logger;
	
	@TemporalFacadePlaceholder
	protected TemporalFacade tdb;
	
	@ParameterFacadePlaceholder
	protected ParameterFacade pdb;
	
	@ComponentPlaceholder
	protected T component;
	
	protected String label;
	protected FlawType[] flawTypes;
	
	/**
	 * 
	 * @param label
	 * @param flawTypes
	 */
	protected Resolver(String label, FlawType[] flawTypes) {
		super();
		this.label = label;
		this.flawTypes = flawTypes;
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
	public final void restore(FlawSolution solution) 
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
	public final void retract(FlawSolution solution) 
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
		List<Relation> rActivated = solution.getActivatedRelations();
		for (Relation relation : rActivated) {
			// get reference component
			DomainComponent refComp = relation.getReference().getComponent();
			// deactivate relation
			refComp.deactivate(relation);
		}
		
		// get the list of created relations
		List<Relation> rCreated = solution.getCreatedRelations();
		for (Relation relation : rCreated) {
			// get reference component
			DomainComponent refComp = relation.getReference().getComponent();
			// remove relation from the data structure
			refComp.delete(relation);
		}
		
		// get the list of activated decisions
		List<Decision> dActivated = solution.getActivatedDecisisons();
		for (Decision decision : dActivated) {
			// get decision component
			DomainComponent dComp = decision.getComponent();
			// deactivate decision
			dComp.deactivate(decision);
		}
		
		// get the list of created decisions
		List<Decision> dCreated = solution.getCreatedDecisions();
		for (Decision decision : dCreated) {
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
	 * @throws Exception
	 */
	protected void doRestore(FlawSolution solution) 
			throws Exception
	{
		// get the list of created decisions
		List<Decision> dCreated = solution.getCreatedDecisions();
		for (Decision decision : dCreated) {
			// get decision component
			DomainComponent dComp = decision.getComponent();
			dComp.restore(decision);
		}
		
		// get the list of created relations
		List<Relation> rCreated = solution.getCreatedRelations();
		for (Relation relation : rCreated) {
			// get reference component
			DomainComponent refComp = relation.getReference().getComponent();
			// restore relation
			refComp.restore(relation);
		}
		
		
		// list of committed decisions
		Set<Decision> dCommitted = new HashSet<>();
		// get the list of activated decisions
		List<Decision> dActivated = solution.getActivatedDecisisons();
		List<Relation> avoid = new ArrayList<>();
		try
		{
			
			for (Decision decision : dActivated) {
				// get decision component
				DomainComponent dComp = decision.getComponent();
				// activate decision and get the set of related relations that have been activated
				Set<Relation> relations = dComp.activate(decision);
				avoid.addAll(relations);
			}
		}
		catch (DecisionPropagationException ex) {
			// deactivate all "avoid" relations
			for (Relation rel : avoid) {
				// get reference component
				DomainComponent refComp = rel.getReference().getComponent();
				refComp.deactivate(rel);
			}
			
			// deactivate committed decisions
			for (Decision dec : dCommitted) {
				// get decision component
				DomainComponent decComp = dec.getComponent();
				decComp.deactivate(dec);
			}
			
			// throw exception
			throw new DecisionPropagationException(ex.getMessage());
		}
		
 		// list of committed relations
		Set<Relation> rCommitted = new HashSet<>();
		// get the list of activated relations
		List<Relation> rActivated = solution.getActivatedRelations();
		try
		{
			// remove the "avoid" list
			rActivated.removeAll(avoid);
			for (Relation relation : rActivated) {
				// get reference component
				DomainComponent refComp = relation.getReference().getComponent();
				refComp.activate(relation);
				// add to committed relations
				rCommitted.add(relation);
			}
		}
		catch (RelationPropagationException ex) {
			// deactivate committed relations
			for (Relation relation : rCommitted) {
				// get reference component
				DomainComponent refComp = relation.getReference().getComponent();
				refComp.deactivate(relation);
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
