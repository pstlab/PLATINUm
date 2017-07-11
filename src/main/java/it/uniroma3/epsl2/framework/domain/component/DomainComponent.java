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
import it.uniroma3.epsl2.framework.domain.component.ex.DecisionNotFoundException;
import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkContainer;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.FrameworkLoggerPlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.ResolverListPlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.uniroma3.epsl2.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.uniroma3.epsl2.framework.microkernel.lang.ex.ConstraintPropagationException;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.microkernel.lang.flaw.FlawType;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Decision;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.Relation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.parameter.ParameterRelation;
import it.uniroma3.epsl2.framework.microkernel.lang.plan.relations.temporal.TemporalRelation;
import it.uniroma3.epsl2.framework.microkernel.query.TemporalQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.parameter.ParameterFacade;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterCreationException;
import it.uniroma3.epsl2.framework.parameter.ex.ParameterNotFoundException;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.time.TemporalFacade;
import it.uniroma3.epsl2.framework.time.TemporalInterval;
import it.uniroma3.epsl2.framework.time.ex.TemporalIntervalCreationException;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.lang.query.IntervalScheduleQuery;
import it.uniroma3.epsl2.framework.time.tn.ex.PseudoControllabilityCheckException;
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
	@TemporalFacadePlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_TEMPORAL_FACADE)
	protected TemporalFacade tdb;
	
	@ParameterFacadePlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PARAMETER_FACADE)
	protected ParameterFacade pdb;
	
	@FrameworkLoggerPlaceholder(lookup = ApplicationFrameworkContainer.FRAMEWORK_SINGLETON_PLANDATABASE_LOGGER)
	protected FrameworkLogger logger;
	
	@ResolverListPlaceholder
	protected List<Resolver> resolvers;
	protected Map<FlawType, Resolver> flawType2resolver;
	
	// component's name
	protected String name;
	protected DomainComponentType type;
	
	// current (local) plan
	protected Map<PlanElementStatus, Set<Decision>> decisions;
	protected Set<Relation> relations;
	
	// display data concerning this component
	private ComponentView view;
	private static AtomicInteger PREDICATE_COUNTER = new AtomicInteger(0);
	
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
		for (PlanElementStatus status : PlanElementStatus.values()) {
			this.decisions.put(status, new HashSet<>());
		}
		
		// initialize relations of the (local) plan
		this.relations = new HashSet<>();
		// set up the list of resolvers
		this.resolvers = new ArrayList<>();
		this.flawType2resolver = new HashMap<>();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() {
		// set component view
		this.view = new GanttComponentView(this);
		// setup resolver index
		for (Resolver resv : this.resolvers) {
			this.flawType2resolver.put(resv.getFlawType(), resv);
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
	public DomainComponentType getType() {
		return type;
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
	public void clear() 
	{
		// delete all active relations
		for (Relation rel : this.getActiveRelations()) {
			this.delete(rel);
		}
		
		// delete all active decisions
		for (Decision dec : this.getActiveDecisions()) {
			this.delete(dec);
		}
		
		// clear data structures
		this.decisions.clear();
		this.relations.clear();
	}
	
	/**
	 * Compute the makespan of the component
	 *  
	 * @return
	 */
	public double computeMakespan() 
	{
		// initialize the makespan to the temporal origin
		double mk = this.getOrigin();
		// get active decisions
		List<Decision> list = this.getActiveDecisions();
		// check scheduled end-times 
		for (Decision a : list) {
			// compare makespan with the end-time lower bound
			mk = Math.max(mk, a.getEnd()[0]);
		}
		
		// get computed makespan
		return mk;
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
	public List<Decision> getActiveDecisions() 
	{
		// list of active decisions with schedule information
		List<Decision> list = new ArrayList<>();
		// get schedule information
		for (Decision dec : this.decisions.get(PlanElementStatus.ACTIVE)) 
		{
			// create query
			IntervalScheduleQuery query = this.tdb.createTemporalQuery(TemporalQueryType.INTERVAL_SCHEDULE);
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
	public void restore(Decision dec) {
		// check if silent decision
		if (this.decisions.get(PlanElementStatus.SILENT).contains(dec)) {
			// remove from silent set
			this.decisions.get(PlanElementStatus.SILENT).remove(dec);
			// add to pending set
			this.decisions.get(PlanElementStatus.PENDING).add(dec);
		}
	}
	
	/**
	 * The method creates a pending decision of the plan with the given component's value
	 * 
	 * @param value
	 * @return
	 */
	public Decision create(ComponentValue value, String[] labels) {
		// create decision
		return this.create(
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
	public Decision create(ComponentValue value, String[] labels, long[] duration) {
		// create decision
		return this.create(
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
	public Decision create(ComponentValue value, String[] labels, long[] end, long[] duration) {
		// create decision
		return this.create(
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
	public Decision create(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) {
		// initialize decision
		Decision dec = new Decision(value, labels, start, end, duration);
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
	public Set<Relation> add(Decision dec) 
			throws DecisionPropagationException 
	{
		// check if decision exists in the agenda
		if (!this.decisions.get(PlanElementStatus.PENDING).contains(dec)) {
			throw new DecisionPropagationException("Pending decision not found:\n- decision= " + dec + "\n");
		}
		
		// token to create
		Token token = null;
		// list of relations to activate
		Set<Relation> local = new HashSet<>();
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
			// remove decision from agenda
			this.decisions.get(PlanElementStatus.PENDING).remove(dec);
			// add decision to the plan
			this.decisions.get(PlanElementStatus.ACTIVE).add(dec);
		
			// get relations to activate
			local.addAll(this.getToActivateRelations(dec));
			// propagate relations
			this.add(local);
		}
		catch (RelationPropagationException ex) 
		{
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
	 * @throws DecisionNotFoundException
	 */
	public void restoreDecision(Decision dec) 
			throws DecisionNotFoundException
	{
		// check if decision exists in the agenda
		if (!this.decisions.get(PlanElementStatus.SILENT).contains(dec)) {
			throw new DecisionNotFoundException("Decision not found in among SILENTs " + dec);
		}
	}	
	
	/**
	 * 
	 * @param dec
	 */
	public void delete(Decision dec) 
	{
		// check if active
		if (this.isActive(dec)) 
		{
			// get active relations to retract
			for (Relation rel :  this.getActiveRelations(dec)) {
				this.delete(rel);
			}
			
			// delete related token
			Token token = dec.getToken();
			// delete parameters of token predicate
			for (Parameter<?> param : token.getPredicate().getParameters()) 
			{
				try 
				{
					// delete parameter
					this.pdb.deleteParameter(param);
				}
				catch (ParameterNotFoundException ex) {
					this.logger.warning(ex.getMessage());
				}
			}
			
			// delete the temporal interval
			this.tdb.deleteTemporalInterval(token.getInterval());
			// remove decision from active
			this.decisions.get(PlanElementStatus.ACTIVE).remove(dec);
			// add back to pending
			this.decisions.get(PlanElementStatus.PENDING).add(dec);
			// clear decision
			dec.clear();
		}
		else if (this.decisions.get(PlanElementStatus.PENDING).contains(dec)) 
		{
			// delete pending decision
			this.decisions.get(PlanElementStatus.PENDING).remove(dec);
			// add to silent decisions
			this.decisions.get(PlanElementStatus.SILENT).add(dec);
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
	public <T extends Relation> T create(RelationType type, Decision reference, Decision target) {
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
			// add relation
			this.relations.add(rel);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		// get created relation
		return rel;
	}
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	public boolean isSilent(Relation rel)
	{
		// get reference
		Decision reference = rel.getReference();
		// get target
		Decision target = rel.getTarget();
		// check condition
		return (this.isSilent(reference) || this.isSilent(target)) && rel.getConstraint() == null;
	}
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	public boolean isPending(Relation rel) 
	{
		// get reference
		Decision reference = rel.getReference();
		// get target
		Decision target = rel.getTarget();
		// check condition
		return (this.isPending(reference) || this.isPending(target)) && 
				!(this.isSilent(reference) || this.isSilent(target) && 
						rel.getConstraint() == null);
	}
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	public boolean isToActivate(Relation rel)
	{
		// get reference
		Decision reference = rel.getReference();
		// get target
		Decision target = rel.getTarget();
		// check condition
		return this.isActive(reference) && this.isActive(target) && rel.getConstraint() == null;
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public List<Relation> getActiveRelations(Decision dec)
	{
		// list of active relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations)
		{
			// get reference
			Decision reference = rel.getReference();
			// get target 
			Decision target = rel.getTarget();
			// check decisions and relation status
			if ((dec.equals(reference) || dec.equals(target)) && this.isActive(rel)) 
			{
				// add relation
				list.add(rel);
			}
		}
		
		// get the list
		return list;
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @return
	 */
	public List<Relation> getActiveRelations(Decision reference, Decision target)
	{
		// list of active relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations)
		{
			// check decisions and relation status
			if (reference.equals(rel.getReference()) && target.equals(rel.getTarget()) && this.isActive(rel)) 
			{
				// add relation
				list.add(rel);
			}
		}
		
		// get the list
		return list;
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public List<Relation> getActiveRelations()
	{
		// list of active relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations) {
			// check if active relation
			if (this.isActive(rel)) {
				// add relation
				list.add(rel);
			}
		}
		
		// get the list
		return list;
	}
	
	/**
	 * Get the list of relations that concern a particular decision and that 
	 * can be activated.
	 * 
	 * @param dec
	 * @return
	 */
	public List<Relation> getToActivateRelations(Decision dec) 
	{
		// list of relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations) {
			// check decisions and relation status
			if (this.isToActivate(rel) && (dec.equals(rel.getReference()) || dec.equals(rel.getTarget()))) {
				// add pending relation
				list.add(rel);
			}
		}
		
		// get list 
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Relation> getPendingRelations()
	{
		// list of relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations) {
			// check decisions and relation status
			if (this.isPending(rel)) {
				// add pending relation
				list.add(rel);
			}
		}
		// get list 
		return list;
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public List<Relation> getPendingRelations(Decision dec) 
	{
		// list of relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations) 
		{
			// get reference
			Decision reference = rel.getReference();
			// get target
			Decision target = rel.getTarget();
			// check decisions and relation status
			if ((dec.equals(reference) || dec.equals(target)) && 
					(this.isPending(reference) || this.isPending(target)) && 
					!(this.isSilent(reference) || this.isSilent(target)) && 
					rel.getConstraint() == null)
			{
				// add pending relation
				list.add(rel);
			}
		}
		
		// get list 
		return list;
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public List<Relation> getRelations(Decision dec)
	{
		// list of relations
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations) 
		{
			// get reference
			Decision reference = rel.getReference();
			// get target
			Decision target = rel.getTarget();
			// check decisions and relation status
			if (dec.equals(reference) || dec.equals(target)) {
				// add pending relation
				list.add(rel);
			}
		}
		
		// get list 
		return list;		
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Relation> getRelations() 
	{
		return new ArrayList<>(this.relations);
	}
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	public boolean isActive(Relation rel) 
	{
		// get reference
		Decision reference = rel.getReference();
		// get target
		Decision target = rel.getTarget();
		// check condition
		return this.isActive(reference) && this.isActive(target) && rel.getConstraint() != null;
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public boolean isActive(Decision dec) {
		return this.decisions.get(PlanElementStatus.ACTIVE).contains(dec);
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public boolean isPending(Decision dec) {
		return this.decisions.get(PlanElementStatus.PENDING).contains(dec);
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public boolean isSilent(Decision dec) {
		return this.decisions.get(PlanElementStatus.SILENT).contains(dec);
	}
	
	/**
	 * 
	 * @param relation
	 */
	public void free(Relation relation) 
	{
		// check relation
		if (this.relations.contains(relation)) {
			// deactivate relation if necessary
			if (this.isActive(relation)) {
				// deactivate relation
				this.delete(relation);
			}
			
			// completely remove data structure
			this.relations.remove(relation);
		}
	}
	
	/**
	 * Only for debugging 
	 * 
	 * @return
	 */
	public List<Decision> getSilentDecisions() {
		return new ArrayList<>(this.decisions.get(PlanElementStatus.SILENT));
	}
	
	/**
	 * Only for debugging 
	 * 
	 * @return
	 */
	public List<Relation> getSilentRelations() {
		List<Relation> list = new ArrayList<>();
		for (Relation rel : this.relations) {
			if (this.isSilent(rel)) {
				list.add(rel);
			}
		}
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
	public void add(Relation rel) 
			throws RelationPropagationException 
	{
		try
		{
			// check if active decisions
			if (!this.isActive(rel.getReference()) || !this.isActive(rel.getTarget())) {
				// not active decisions
				throw new RelationPropagationException("Trying to propagate local relation between not active decisions\n"
						+ "- reference= " + rel.getReference() + "\n"
						+ "- target= " + rel.getTarget() + "\n");
			}
			else if (!this.isActive(rel)) 
			{
				// check relation type
				switch (rel.getCategory()) 
				{
					// temporal constraint
					case TEMPORAL_CONSTRAINT : 
					{
						// get temporal relation
						TemporalRelation trel = (TemporalRelation) rel;
						// create interval constraint
						TemporalConstraint c = trel.create();
						// propagate constraint
						this.tdb.propagate(c);
					}
					break;
					
					// parameter constraint
					case PARAMETER_CONSTRAINT : 
					{
						// get parameter relation
						ParameterRelation prel = (ParameterRelation) rel;
						// create related constraint
						ParameterConstraint constraint = prel.create();
						// propagate constraint
						this.pdb.propagate(constraint);
					}
					break;
				}
			}
			else {
				// already propagated constraint
				this.logger.debug("Already propagated local relation\n- " + rel + "\n");
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
	public void add(Set<Relation> relations) 
			throws RelationPropagationException 
	{
		// list of committed relations
		List<Relation> committed = new ArrayList<>();
		try 
		{
			// propagate relations
			for (Relation rel : relations) {
				// propagate relation
				this.add(rel);
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
	 * 
	 * @param rel
	 */
	public void delete(Relation rel) 
	{
		// check if relation exists
		if (!this.relations.contains(rel)) {
			// relation not found
			this.logger.warning("Local relation not found\n- relation= " + rel + "\n");
		}
		else if (this.isActive(rel))
		{
			// check relation type
			switch (rel.getCategory()) 
			{
				// temporal constraint
				case TEMPORAL_CONSTRAINT : 
				{
					// get temporal relation
					TemporalRelation trel = (TemporalRelation) rel;
					// retract the related constraint
					this.tdb.retract(trel.getConstraint());
					trel.clear();
				}
				break;
				
				// parameter constraint
				case PARAMETER_CONSTRAINT : 
				{
					// get parameter relation
					ParameterRelation prel = (ParameterRelation) rel;
					// retract the related constraint
					this.pdb.retract(prel.getConstraint());
					prel.clear();
				}
				break;
			}
		}
		else {
			// deleting a not propagated constraint
			this.logger.warning("Trying to delete a not propagated local relation\n- relation= " + rel + "\n");
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
			throws UnsolvableFlawFoundException 
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// call resolvers to detect flaws and possible solutions
		for (Resolver resv : this.resolvers) {
			// add detected flaws
			list.addAll(resv.findFlaws());
		}

		// get the list of detected flaws
		return list;
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws UnsolvableFlawFoundException
	 */
	public List<Flaw> detectFlaws(FlawType type) 
			throws UnsolvableFlawFoundException
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// get resolver capable to handle the desired set of flaws, if any
		if (this.flawType2resolver.containsKey(type))
		{
			// get related resolver
			Resolver resv = this.flawType2resolver.get(type);
			// detect flaws
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
		this.flawType2resolver.get(solution.getFlaw().getType()).apply(solution);
	}
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	public void restore(FlawSolution solution) 
			throws Exception {
		// dispatch the flaw to the correct resolver
		this.flawType2resolver.get(solution.getFlaw().getType()).restore(solution);
	}
	
	/**
	 * 
	 * @param solution
	 */
	public void rollback(FlawSolution solution) {
		// dispatch to the correct resolver
		this.flawType2resolver.get(solution.getFlaw().getType()).retract(solution);
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
			Parameter<?> param = this.pdb.createParameter(labels[index], ph.getDomain());

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