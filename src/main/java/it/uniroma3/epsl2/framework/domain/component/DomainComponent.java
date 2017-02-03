package it.uniroma3.epsl2.framework.domain.component;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.epsl.microkernel.internal.engine.exception.UnsolvableFlawException;
import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.lang.ex.ConstraintPropagationException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.ParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.temporal.TemporalRelation;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ComponentViewReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ParameterDataBaseFacadeReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.ResolverReferences;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.TemporalDataBaseFacadeReference;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.lifecycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryFactory;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacade;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterCreationException;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacade;
import it.uniroma3.epsl2.framework.time.TemporalInterval;
import it.uniroma3.epsl2.framework.time.ex.TemporalIntervalCreationException;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.query.CheckIntervalScheduleQuery;
import it.uniroma3.epsl2.framework.time.tn.uncertainty.ex.PseudoControllabilityCheckException;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;
import it.uniroma3.epsl2.framework.utils.view.component.ComponentView;
import it.uniroma3.epsl2.framework.utils.view.component.gantt.GanttComponentView;

/**
 * 
 * @author anacleto
 *
 */
public abstract class DomainComponent extends ApplicationFrameworkObject 
{
	private static AtomicInteger PREDICATE_COUNTER = new AtomicInteger(0);
	
	// component's name
	protected String name;
	protected DomainComponentType type;
	
	// query factory
	protected TemporalQueryFactory queryFactory;
	
	// current (local) plan
	protected Map<PlanElementStatus, Set<Decision>> decisions;
	protected Map<PlanElementStatus, Set<Relation>> relations;
	
	@ComponentViewReference
	private ComponentView view;
	
	@TemporalDataBaseFacadeReference
	protected TemporalDataBaseFacade tdb;
	
	@ParameterDataBaseFacadeReference
	protected ParameterDataBaseFacade pdb;
	
	@ResolverReferences
	protected List<Resolver<?>> resolvers;
	protected Map<FlawType, Resolver<?>> index;
	
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	protected DomainComponent(String name, DomainComponentType type) {
		super();
		this.type = type;
		this.name = name;
		
		// initialize decisions of the (local) plan
		this.decisions = new HashMap<>();
		this.decisions.put(PlanElementStatus.PENDING, new HashSet<Decision>());
		this.decisions.put(PlanElementStatus.ACTIVE, new HashSet<Decision>());
		// initialize relations of the (local) plan
		this.relations = new HashMap<>();
		this.relations.put(PlanElementStatus.PENDING, new HashSet<Relation>());
		this.relations.put(PlanElementStatus.ACTIVE, new HashSet<Relation>());
		
		
		// set up the list of resolvers
		this.resolvers = new ArrayList<>();
		this.index = new HashMap<>();
		
		// complete initialization
		this.queryFactory = TemporalQueryFactory.getInstance();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// set component view
		this.view = new GanttComponentView(this);
		// setup resolver index
		for (Resolver<? extends DomainComponent> resv : this.resolvers) {
			this.index.put(resv.getFlawType(), resv);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isExternal() {
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getOrigin() {
		return this.tdb.getOrigin();
	}
	
	/**
	 * 
	 * @return
	 */
	public long getHorizon() {
		return this.tdb.getHorizon();
	}
	
	/**
	 * 
	 */
	public void display() {
		this.view.display();
	}
	
	/**
	 * Check pseudo-controllability of the component and 
	 * return the list of "squeezed" uncontrollable tokens 
	 * if any
	 * 
	 * @throws PseudoControllabilityCheckException
	 */
	public abstract void checkPseudoControllability() 
			throws PseudoControllabilityCheckException;
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getActiveDecisions() {
		// list of active decisions with schedule information
		List<Decision> list = new ArrayList<>();
		// get schedule information
		for (Decision dec : this.decisions.get(PlanElementStatus.ACTIVE)) {
			// create query
			CheckIntervalScheduleQuery query = this.queryFactory.create(TemporalQueryType.CHECK_SCHEDULE);
			// set related temporal interval
			query.setInterval(dec.getToken().getInterval());
			// process 
			this.tdb.process(query);
			// add the updated token to the list
			list.add(dec);
		}
		// get list
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Decision> getPendingDecisions() {
		return new ArrayList<>(this.decisions.get(PlanElementStatus.PENDING));
	}
	
	/**
	 * Restore the selected decision as pending into the component
	 * 
	 * @param dec
	 */
	public void restorePendingDecision(Decision dec) {
		this.decisions.get(PlanElementStatus.PENDING).add(dec);
	}
	
	/**
	 * The method creates a pending decision of the plan with the given component's value
	 * 
	 * @param value
	 * @return
	 */
	public Decision createDecision(ComponentValue value, String[] labels) {
		// create decision
		return this.createDecision(
				value,
				labels,
				new long[] {this.tdb.getOrigin(), this.tdb.getHorizon()}, 
				new long[] {this.tdb.getOrigin(), this.tdb.getHorizon()}, 
				value.getDurationBounds());
	}
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param duration
	 * @return
	 */
	public Decision createDecision(ComponentValue value, String[] labels, long[] duration) {
		// create decision
		return this.createDecision(
				value,
				labels,
				new long[] {this.tdb.getOrigin(), this.tdb.getHorizon()}, 
				new long[] {this.tdb.getOrigin(), this.tdb.getHorizon()}, 
				duration);
	}
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param end
	 * @param duration
	 * @return
	 */
	public Decision createDecision(ComponentValue value, String[] labels, long[] end, long[] duration) {
		// create decision
		return this.createDecision(
				value,
				labels,
				new long[] {this.tdb.getOrigin(), this.tdb.getHorizon()}, 
				end, 
				duration);
	}
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param duration
	 * @return
	 */
	public Decision createDecision(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) {
		// initialize decision
		Decision dec = new Decision(value,labels,  start, end, duration);
		// add decision the the agenda
		this.decisions.get(PlanElementStatus.PENDING).add(dec);
		// get initialized decision
		return dec;
	}
	
	/**
	 * The method adds a pending decision to the current plan. The status of the decision 
	 * changes from PENDING to ACTIVE.
	 * 
	 * The method returns the list of relations propagate during decision activation.
	 * 
	 * @param dec
	 * @return
	 * @throws DecisionPropagationException
	 * 
	 */
	public List<Relation> addDecision(Decision dec) 
			throws DecisionPropagationException 
	{
		// check if decision exists in the agenda
		if (!this.decisions.get(PlanElementStatus.PENDING).contains(dec)) {
			throw new DecisionPropagationException("Decision not found in agenda " + dec);
		}
		
		// token to create
		Token token = null;
		// list of relations to activate
		List<Relation> local = new ArrayList<>();
		try 
		{
			// create a token
			token = this.createToken(
					dec.getId(),
					dec.getValue(),
					dec.getParameterLabels(),
					dec.getStart(), 
					dec.getEnd(), 
					dec.getNominalDuration());
			
			// set token to decision 
			dec.setToken(token);
		
			// get decision related relations to activate
			for (Relation rel : this.getPendingRelations(dec)) {
				// check if can be activate
				if (rel.getReference().equals(dec) && rel.getTarget().isActive() || 
						rel.getTarget().equals(dec) && rel.getReference().isActive() ||
						// activate reflexive relations also
						rel.getTarget().equals(dec) && rel.getReference().equals(dec)) {
					// add relation
					local.add(rel);
				}
			}
			
			// activate related relations
			this.addRelations(local);
			
			// remove decision from agenda
			this.decisions.get(PlanElementStatus.PENDING).remove(dec);
			// add decision to the plan
			this.decisions.get(PlanElementStatus.ACTIVE).add(dec);
		}
		catch (RelationPropagationException ex) {
			// roll-back decision's created token
			this.tdb.deleteTemporalInterval(token.getInterval());
			// the decision is still pending and the related token is removed
			dec.clear();
			// note the decision is still pending
			// forward exception
			throw new DecisionPropagationException(ex.getMessage());
		}
		catch (TemporalIntervalCreationException | ParameterCreationException ex) {
			throw new DecisionPropagationException(ex.getMessage());
		}
		
		// get list of "local" activated relations
		return local;
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void delete(Decision dec) 
	{
		// check if active
		if (this.decisions.get(PlanElementStatus.ACTIVE).contains(dec)) 
		{
			// get related active relations to delete
			for (Relation rel : this.getActiveRelations(dec)) {
				// delete (deactivate) relation
				this.delete(rel);
			}
			
			// delete related token
			Token token = dec.getToken();
			// delete the temporal interval
			this.tdb.deleteTemporalInterval(token.getInterval());
			// remove decision from active
			this.decisions.get(PlanElementStatus.ACTIVE).remove(dec);
			// add back to pending
			this.decisions.get(PlanElementStatus.PENDING).add(dec);
			// clear decision
			dec.clear();
		}
		else if (this.decisions.get(PlanElementStatus.PENDING).contains(dec)) {
			// get related pending relations to delete
			for (Relation rel : this.getPendingRelations(dec)) {
				// delete pending relation
				this.delete(rel);
			}
			
			// delete pending decision
			this.decisions.get(PlanElementStatus.PENDING).remove(dec);
		}
		else {
			// decision not found in the plan
			this.logger.debug("Decision not found in the plan " + dec);
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Relation> T createRelation(RelationType type, Decision reference, Decision target) {
		// relation 
		T rel = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getRelationClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor(Decision.class, Decision.class);
			c.setAccessible(true);
			// create instance
			rel = c.newInstance(reference, target);
			// add pending relation
			this.relations.get(PlanElementStatus.PENDING).add(rel);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get created relation
		return rel;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Relation> getPendingRelations() {
		return new ArrayList<>(this.relations.get(PlanElementStatus.PENDING));
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Relation> getActiveRelations() {
		return new ArrayList<>(this.relations.get(PlanElementStatus.ACTIVE));
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 */
	public List<Relation> getExistingRelations(Decision reference, Decision target) {
		// list of relation concerning the decisions
		List<Relation> list = new ArrayList<>();
		// check pending relations
		for (Relation rel : this.relations.get(PlanElementStatus.PENDING)) {
			// check related decisions
			if (rel.getReference().equals(reference) && rel.getTarget().equals(target)) {
				list.add(rel);
			}
		}
		
		// check active relations
		for (Relation rel : this.relations.get(PlanElementStatus.ACTIVE)) {
			// check related decisions
			if (rel.getReference().equals(reference) && rel.getTarget().equals(target)) {
				list.add(rel);
			}
		}
		
		// get list
		return list;
	}
	
	/**
	 * Get the list of pending relations concerning a particular decision
	 * 
	 * @param dec
	 * @return
	 */
	public List<Relation> getPendingRelations(Decision dec) {
		// list of relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations.get(PlanElementStatus.PENDING)) {
			// check reference and target
			if (rel.getReference().equals(dec) || rel.getTarget().equals(dec)) {
				// add relation
				list.add(rel);
			}
		}
		// get list
		return list;
	}
	
	/**
	 * Get the list of active relations concerning a particular decision. It means that
	 * also the decision is active
	 * 
	 * @param dec
	 * @return
	 */
	public List<Relation> getActiveRelations(Decision dec) {
		// list of relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations.get(PlanElementStatus.ACTIVE)) {
			// check reference and target
			if (rel.getReference().equals(dec) || rel.getTarget().equals(dec)) {
				// add relation
				list.add(rel);
			}
		}
		// get list
		return list;
	}
	
	/**
	 * Propagate a local temporal relation.
	 * 
	 * Precondition: The related temporal intervals must be activated, i.e. they must have
	 * temporal intervals associated
	 * 
	 * @param reference
	 * @param target
	 * @param constraint
	 * @return
	 * @throws RelationPropagationException
	 */
	public void addRelation(Relation rel) 
			throws RelationPropagationException 
	{
		// check if the relation is actually pending
		if (!this.relations.get(PlanElementStatus.PENDING).contains(rel)) {
			throw new RelationPropagationException("Trying to propagate a not pending relation " + rel);
		}
		
		try
		{
			// check relation type
			switch (rel.getCategory()) 
			{
				// temporal constraint
				case TEMPORAL_CONSTRAINT : {
					// get temporal relation
					TemporalRelation trel = (TemporalRelation) rel;
					// create interval constraint
					TemporalConstraint c = trel.create();
					// propagate constraint
					this.tdb.propagate(c);
					// set relation as activated
					this.relations.get(PlanElementStatus.PENDING).remove(rel);
					this.relations.get(PlanElementStatus.ACTIVE).add(rel);
				}
				break;
				
				// parameter constraint
				case PARAMETER_CONSTRAINT : {
					// get parameter relation
					ParameterRelation prel = (ParameterRelation) rel;
					// create related constraint
					ParameterConstraint constraint = prel.create();
					// propagate constraint
					this.pdb.propagate(constraint);
					// set relation as activated
					this.relations.get(PlanElementStatus.PENDING).remove(rel);
					this.relations.get(PlanElementStatus.ACTIVE).add(rel);
					
				}
				break;
			}
		}
		catch (ConstraintPropagationException ex) {
			// clear relation
			rel.clear();
			// not that the relation is still "pending"
			throw new RelationPropagationException(ex.getMessage());
		}
	}
	
	/**
	 * Propagate pending relations 
	 * 
	 * @param relations
	 */
	public void addRelations(List<Relation> relations) 
			throws RelationPropagationException 
	{
		// list of committed relations
		List<Relation> committed = new ArrayList<>();
		try {
			// propagate relations
			for (Relation rel : relations) {
				// propagate relation
				this.addRelation(rel);
				// add to committed
				committed.add(rel);
			}
		} 
		catch (RelationPropagationException ex) {
			// error while propagating relations
			for (Relation toRetract : committed) {
				// delete relation
				this.delete(toRetract);
			}
			// forward exception
			throw new RelationPropagationException(ex.getMessage());
		}
	}
	
	/**
	 * This method completely remove a relation from the plan whatever state 
	 * the relation belongs to
	 * 
	 * @param rel
	 */
	public void free(Relation rel) {
		// retract constraint if active
		if (this.relations.get(PlanElementStatus.ACTIVE).contains(rel)) {
			// check relation type
			switch (rel.getCategory()) 
			{
				// retract temporal constraint
				case TEMPORAL_CONSTRAINT : {
					// get temporal relation
					TemporalRelation trel = (TemporalRelation) rel;
					// retract constraint
					this.tdb.retract(trel.getConstraint());
					trel.clear();
				}
				break;
				
				// retract parameter constraint
				case PARAMETER_CONSTRAINT : {
					// get parameter relation
					ParameterRelation prel = (ParameterRelation) rel;
					// retract constraint
					this.pdb.retract(prel.getConstraint());
					prel.clear();
				}
				break;
			}
			
			// remove from active relations
			this.relations.get(PlanElementStatus.ACTIVE).remove(rel);
		}
		else if (this.relations.get(PlanElementStatus.PENDING).contains(rel)) {
			// remove from pending relations
			this.relations.get(PlanElementStatus.PENDING).remove(rel);
		}
		else {
			// relation not found
			this.logger.debug("Relation not found in the plan " + rel);
		}
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void delete(Relation rel) 
	{
		// check if active relation
		if (this.relations.get(PlanElementStatus.ACTIVE).contains(rel)) {
			// check relation type
			switch (rel.getCategory()) 
			{
				// temporal constraint
				case TEMPORAL_CONSTRAINT : {
					// get temporal relation
					TemporalRelation trel = (TemporalRelation) rel;
					// retract the related constraint
					this.tdb.retract(trel.getConstraint());
					trel.clear();
					// remove relation
					this.relations.get(PlanElementStatus.ACTIVE).remove(trel);
					// add back to pending
					this.relations.get(PlanElementStatus.PENDING).add(trel);
				}
				break;
				
				// parameter constraint
				case PARAMETER_CONSTRAINT : {
					// get parameter relation
					ParameterRelation prel = (ParameterRelation) rel;
					// retract the related constraint
					this.pdb.retract(prel.getConstraint());
					prel.clear();
					// remove relation
					this.relations.get(PlanElementStatus.ACTIVE).remove(prel);
					// add back to pending
					this.relations.get(PlanElementStatus.PENDING).add(prel);
				}
				break;
			}
		}
		else if (this.relations.get(PlanElementStatus.PENDING).contains(rel)) {
			// remove pending relation
			this.relations.get(PlanElementStatus.PENDING).remove(rel);
		}
		else {
			// relation not found
			this.logger.debug("Relation not found in the plan " + rel);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract List<ComponentValue> getValues();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public abstract ComponentValue getValueByName(String name);
	
	/**
	 * 
	 * @return
	 * @throws UnsolvableFlawFoundException
	 * @throws UnsolvableFlawException
	 */
	public List<Flaw> detectFlaws() 
			throws UnsolvableFlawFoundException {
		
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// call resolvers to detect flaws and possible solutions
		for (Resolver<?> resv : this.resolvers) {
			// add detected flaws
			list.addAll(resv.findFlaws());
		}

		// get the list of detected flaws
		return list;
	}
	
	/**
	 * Solve a flaw by applying the selected solution
	 * 
	 * @param flaw
	 * @param sol
	 * @throws FlawSolutionApplicationException
	 */
	public void commit(FlawSolution solution) 
			throws FlawSolutionApplicationException {
		// dispatch the flaw to the correct resolver
		this.index.get(solution.getFlaw().getType()).apply(solution);
	}
	
	/**
	 * 
	 * @param solution
	 */
	public void rollback(FlawSolution solution) {
		// dispatch to the correct resolver
		this.index.get(solution.getFlaw().getType()).retract(solution);
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomainComponent other = (DomainComponent) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[DomainComponent label= " + this.name + "]";
	}
	
	/**
	 * 
	 * @param value
	 * @param start
	 * @param end
	 * @param duration
	 * @return
	 * @throws TemporalIntervalCreationException
	 * @throws ParameterCreationException
	 */
	protected Token createToken(int id, ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) 
			throws TemporalIntervalCreationException, ParameterCreationException
	{
		// create a temporal interval
		TemporalInterval interval = this.tdb.
				createTemporalInterval(
						start,
						end, 
						duration, 
						value.isControllable());
		
		// create predicate
		Predicate predicate = new Predicate(PREDICATE_COUNTER.getAndIncrement(), value);
		
		// get place holders
		for (int index = 0; index < labels.length; index++) 
		{
			// get value's parameter place holder
			ParameterPlaceHolder ph = value.getParameterPlaceHolderByIndex(index);
			
			// create a parameter
			Parameter<?> param = this.pdb.createParameter(labels[index], ph.getType(), ph.getDomain());

			// add parameter to predicate
			
			// add parameter
			this.pdb.addParameter(param);
			
			// add parameter to the predicate at the specified position
			predicate.setParameter(index, labels[index], param);
		}
		
		// create token
		return new Token(id, this, interval, predicate);
	}
}