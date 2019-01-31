package it.istc.pst.platinum.framework.domain.component;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationRule;
import it.istc.pst.platinum.framework.microkernel.FrameworkObject;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.ResolverListPlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConstraintPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.ParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.parameter.ParameterFacade;
import it.istc.pst.platinum.framework.parameter.ex.ParameterCreationException;
import it.istc.pst.platinum.framework.parameter.ex.ParameterNotFoundException;
import it.istc.pst.platinum.framework.parameter.lang.Parameter;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.time.TemporalFacade;
import it.istc.pst.platinum.framework.time.TemporalInterval;
import it.istc.pst.platinum.framework.time.ex.TemporalIntervalCreationException;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.IntervalScheduleQuery;
import it.istc.pst.platinum.framework.utils.view.component.ComponentView;
import it.istc.pst.platinum.framework.utils.view.component.gantt.GanttComponentView;

/**
 * 
 * @author anacleto
 *
 */
public abstract class DomainComponent extends FrameworkObject
{
	@TemporalFacadePlaceholder
	protected TemporalFacade tdb;
	
	@ParameterFacadePlaceholder
	protected ParameterFacade pdb;
	
	@ResolverListPlaceholder
	protected List<Resolver<?>> resolvers;
	protected Map<FlawType, Resolver<?>> flawType2resolver;
	
	// component's name
	protected String name;
	protected DomainComponentType type;
	
	// current (local) plan
	protected Map<PlanElementStatus, Set<Decision>> decisions;
	protected Set<Relation> localRelations;
	
	// display data concerning this component
	private ComponentView view;
	
	// static information
	
	// synchronization rules from the planning domain
	protected static final Map<DomainComponent, Map<ComponentValue, List<SynchronizationRule>>> rules = new HashMap<>();
	// current (global) relations
	protected static final Set<Relation> globalRelations = new HashSet<>();
	// predicate ID counter
	protected static final AtomicInteger PREDICATE_COUNTER = new AtomicInteger(0);
	protected static final AtomicInteger DecisionIdCounter = new AtomicInteger(0);
	protected static final AtomicInteger RELATION_COUNTER = new AtomicInteger(0);
	
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
		this.localRelations = new HashSet<>();
//		// initialize global relations
//		if (globalRelations == null) {
//			globalRelations = new HashSet<>();
//		}
//		// initialize rules 
//		if (rules == null) {
//			rules = new HashMap<>();
//		}
		
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
		for (Resolver<?> resv : this.resolvers) {
			for (FlawType ft : resv.getFlawTypes()) {
				this.flawType2resolver.put(ft, resv);
			}
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
		for (Relation relation : this.getActiveRelations()) {
			this.deactivate(relation);
		}
		
		// delete all active decisions
		for (Decision decision : this.getActiveDecisions()) {
			this.deactivate(decision);
		}
		
		// clear component data structures
		this.decisions.clear();
		this.localRelations.clear();
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
	
//	/**
//	 * Check pseudo-controllability of the component and 
//	 * return the list of "squeezed" uncontrollable tokens 
//	 * if any
//	 * 
//	 * @throws ConsistencyCheckException
//	 */
//	public abstract void verify() 
//			throws ConsistencyCheckException;
	
	/**
	 * 
	 * @return
	 */
	public final synchronized List<SynchronizationRule> getSynchronizationRules() {
		// get all rules
		List<SynchronizationRule> list = new ArrayList<>();
		for (DomainComponent comp : rules.keySet()) {
			// check if a synchronization has been defined on the component
			if (rules.containsKey(comp)) {
				for (ComponentValue v : rules.get(comp).keySet()) {
					// add rules
					list.addAll(rules.get(comp).get(v));
				}
			}
		}
		
		// get rules
		return list;
	}
	
	/**
	 * 
	 */
	public final synchronized  List<SynchronizationRule> getSynchronizationRules(ComponentValue value) 
	{
		// list of rules
		List<SynchronizationRule> list = new ArrayList<>();
		// check domain specification
		if (rules.containsKey(value.getComponent()) && rules.get(value.getComponent()).containsKey(value)) {
			list.addAll(rules.get(value.getComponent()).get(value));
		}
		// get rules
		return list;
	}
	
	/**
	 * 
	 */
	public final synchronized List<SynchronizationRule> getSynchronizationRules(DomainComponent component) 
	{
		// list of rules
		List<SynchronizationRule> list = new ArrayList<>();
		// check domain specification
		if (rules.containsKey(component)) {
			for (ComponentValue value : rules.get(component).keySet()) {
				list.addAll(rules.get(component).get(value));
			}
		}
		// get rules
		return list;
	}
	
	/**
	 * Get the set of active global relations
	 * 
	 * @return
	 */
	public final Set<Relation> getGlobalActiveRelations() {
		// set of active global relations
		Set<Relation> set = new HashSet<>();
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				if (rel.isActive()) {
					set.add(rel);
				}
			}
		}
		// get the list of global active relations
		return set;
	}
	
	/**
	 * Get the set of global relations
	 * @return
	 */
	public final Set<Relation> getGlobalRelations() {
		// set of active global relations
		Set<Relation> set = new HashSet<>();
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				set.add(rel);
			}
		}
		// get the list of global active relations
		return set;
	}
	
	/**
	 * Get the set of pending global relations
	 * @return
	 */
	public final Set<Relation> getGlobalPendingRelations() {
		// set of active global relations
		Set<Relation> set = new HashSet<>();
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				if (rel.isPending()) {
					set.add(rel);
				}
			}
		}
		// get the list of global active relations
		return set;
	}
	
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

		// sort decisions
		Collections.sort(list);
		// get sorted list of active decisions
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
		// check decision component
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Trying to restore a not local decision on component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// check if silent decision
		if (this.decisions.get(PlanElementStatus.SILENT).contains(dec)) {
			// remove from silent set
			this.decisions.get(PlanElementStatus.SILENT).remove(dec);
			// add to pending set
			this.decisions.get(PlanElementStatus.PENDING).add(dec);
		}
		else {
			// debug information
			warning("The decision you want to restore is not \"silent\":\n- decision: " + dec + "\n");
		}
	}
	
	/**
	 * 
	 * @param rel
	 */
	public void restore(Relation rel) 
	{
		// get reference component
		DomainComponent refComp = rel.getReference().getComponent();
		DomainComponent targetComp = rel.getTarget().getComponent();
		// check components
		if (!refComp.equals(this) && !targetComp.equals(this)) {
			// unknown relation - global "external" relation
			throw new RuntimeException("Trying to restore a relation \"unknown\" to component:\n- component: " + this.name + "\n- relation: " + rel + "\n");
		}
		
		// check if local relation
		if (rel.isLocal()) {
			// add-back local relation
			this.localRelations.add(rel);
		}
		else {
			// add-back global relation
			synchronized (globalRelations) {
				globalRelations.add(rel);
			}
		}
	}
	
	/**
	 * The method creates a pending decision of the plan with the given component's value
	 * 
	 * @param value
	 * @return
	 */
	public Decision create(ComponentValue value, String[] labels) 
	{
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
	public Decision create(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) 
	{
		// check if value is known to the component
		if (!value.getComponent().equals(this)) {
			throw new RuntimeException("Trying to add a decision with a value unknown to the component:\n- component: " + this.name + "\n- value: " + value + "\n");
		}
		
		// initialize decision
		Decision dec = new Decision(DecisionIdCounter.getAndIncrement(), value, labels, start, end, duration);
		// add decision the the agenda
		this.decisions.get(PlanElementStatus.PENDING).add(dec);
		// get initialized decision
		return dec;
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
	public Decision create(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration, ExecutionNodeStatus status) 
	{
		// check if value is known to the component
		if (!value.getComponent().equals(this)) {
			throw new RuntimeException("Trying to add a decision with a value unknown to the component:\n- component: " + this.name + "\n- value: " + value + "\n");
		}
		
		// initialize decision
		Decision dec = new Decision(DecisionIdCounter.getAndIncrement(), value, labels, start, end, duration, status);
		// add decision the the agenda
		this.decisions.get(PlanElementStatus.PENDING).add(dec);
		// get initialized decision
		return dec;
	}
	
	/**
	 * The method adds a pending decision to the current plan. The status of the decision 
	 * changes from PENDING to ACTIVE.
	 * 
	 * The method returns the set of local and global relations propagate during decision activation.
	 * 
	 * @param dec
	 * @return
	 * @throws DecisionPropagationException
	 * 
	 */
	public Set<Relation> activate(Decision dec) 
			throws DecisionPropagationException 
	{
		// check decision component
		if (dec.getComponent().equals(dec)) {
			throw new RuntimeException("Trying to add a not local decision to a component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// list of relations to activate
		Set<Relation> local = new HashSet<>();
		// check if decision is pending
		if (this.decisions.get(PlanElementStatus.PENDING).contains(dec)) 
		{
			// token to create
			Token token = null;
			try 
			{
				// create a token
				token = this.createToken(
						dec.getId(),
						dec.getValue(),
						dec.getParameterLabels(),
						dec.getStart(), 
						dec.getEnd(), 
						dec.getNominalDuration(),
						dec.getStartExecutionState());
				
				// set token to decision 
				dec.setToken(token);
				// remove decision from agenda
				this.decisions.get(PlanElementStatus.PENDING).remove(dec);
				// add decision to the plan
				this.decisions.get(PlanElementStatus.ACTIVE).add(dec);
			
				// get relations to activate
				local.addAll(this.getToActivateRelations(dec));
				// propagate relations
				this.activate(local);
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
		}
		else {
			// debug information
			warning("Trying to activate a non pending decision:\n- decision: " + dec + "\n");
		}
		
		// get list of "local" activated relations
		return local;
	}

	/**
	 * 
	 * @param dec
	 */
	public void deactivate(Decision dec) 
	{
		// check decision component
		if (dec.getComponent().equals(dec)) {
			throw new RuntimeException("Trying to delete a not local decision from a component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// check if active
		if (this.isActive(dec)) 
		{
			// get active relations to retract
			for (Relation rel :  this.getActiveRelations(dec)) {
				this.deactivate(rel);
			}
			
			// delete related token
			Token token = dec.getToken();
			// delete parameters of token predicate
			for (Parameter<?> param : token.getPredicate().getParameters()) 
			{
				try 
				{
					// delete parameter variable
					this.pdb.deleteParameter(param);
				}
				catch (ParameterNotFoundException ex) {
					warning(ex.getMessage());
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
		else {
			// debug information
			debug("The decision you want to deactivate is not active:\n- decision: " + dec + "\n");
		}
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void free(Decision dec) 
	{
		// check decision component
		if (dec.getComponent().equals(dec)) {
			throw new RuntimeException("Trying to delete a not local decision from a component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// check if active
		if (!this.isActive(dec)) {
			// delete pending decision
			this.decisions.get(PlanElementStatus.PENDING).remove(dec);
			// add to silent decisions
			this.decisions.get(PlanElementStatus.SILENT).add(dec);
		}
		else {
			// debug information 
			debug("The decision you want to delete is not \"pending\":\n- decision: " + dec +"\n");
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
	public <T extends Relation> T create(RelationType type, Decision reference, Decision target) 
	{
		// get reference component
		DomainComponent refComp = reference.getComponent();
		// get target component
		DomainComponent targetComp = target.getComponent();
		if (!refComp.equals(this) && !targetComp.equals(this)) {
			// "external" relation
			throw new RuntimeException("Trying to create an \"external\" relation for component:\n- component: " + this.name + "\n- reference: " + reference + "\n- target: " + target + "\n");
		}
		
		// relation 
		T rel = null;
		try 
		{
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getRelationClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor(Integer.TYPE, Decision.class, Decision.class);
			c.setAccessible(true);
			// create instance
			rel = c.newInstance(RELATION_COUNTER.getAndIncrement(), reference, target);
			
			// check if local relation
			if (rel.isLocal()) {
				// add to local relations
				this.localRelations.add(rel);
			} else {
				// mutually access global relations
				synchronized (globalRelations) {
					// add to global relations
					globalRelations.add(rel);
				}
			}
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get created relation
		return rel;
	}
	
	/**
	 * Get the set of local and global active relations concerning a particular decision
	 * 
	 * @param dec
	 * @return
	 */
	public Set<Relation> getActiveRelations(Decision dec)
	{
		// check if local relation
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Unknown decision to component:- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// list of active relations
		Set<Relation> set = new HashSet<>();
		// check local relations
		for (Relation rel : this.localRelations) {
			// check related decisions and relation status
			if ((rel.getReference().equals(dec) || rel.getTarget().equals(dec)) && rel.isActive()) {
				// add relation
				set.add(rel);
			}
		}
		
		// check global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				// check related decision and relation status
				if ((rel.getReference().equals(dec) || rel.getTarget().equals(dec)) && rel.isActive()) {
					// add relation
					set.add(rel);
				}
			}
		}
		
		// get the list
		return set;
	}
	
	/**
	 * Get the set of local and global active relations concerning a particular decision
	 * 
	 * @param reference
	 * @param target
	 * @return
	 */
	public Set<Relation> getActiveRelations(Decision reference, Decision target)
	{
		// check if local relation
		if (!reference.getComponent().equals(this) && !target.getComponent().equals(this)) {
			throw new RuntimeException("Unknown decisions to component:- component: " + this.name + "\n- reference: " + reference + "\n- target: " + target + "\n");
		}
		
		// list of active relations
		Set<Relation> set = new HashSet<>();
		// check local relations
		for (Relation rel : this.localRelations) {
			// check decisions and relation status
			if (rel.getReference().equals(reference) && rel.getTarget().equals(target) && rel.isActive()) {
				// add relation
				set.add(rel);
			}
		}
		
		// check global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				// check decisions and relation status
				if (rel.getReference().equals(reference) && rel.getTarget().equals(target) && rel.isActive()) {
					// add relation
					set.add(rel);
				}
			}
		}
		
		// get the list
		return set;
	}
	
	/**
	 * Get the set of local active relations on a component
	 *  
	 * @param dec
	 * @return
	 */
	public Set<Relation> getActiveRelations()
	{
		// list of active relations
		Set<Relation> set = new HashSet<>();
		// check local relations
		for (Relation rel : this.localRelations) {
			// check if active relation
			if (rel.isActive()) {
				// add relation
				set.add(rel);
			}
		}
		
		// get the list
		return set;
	}
	
	/**
	 * Get the list of local pending relations
	 * 
	 * @return
	 */
	public Set<Relation> getPendingRelations()
	{
		// list of active relations
		Set<Relation> set = new HashSet<>();
		// check local relations
		for (Relation rel : this.localRelations) {
			// check if active relation
			if (rel.isPending()) {
				// add relation
				set.add(rel);
			}
		}
		
		// get the list
		return set;
	}
	
	/**
	 * Get the list of both pending and active local relations
	 * 
	 * @return
	 */
	public Set<Relation> getRelations() {
		// list of active relations
		Set<Relation> set = new HashSet<>();
		// check local relations
		for (Relation rel : this.localRelations) {
			// add relation
			set.add(rel);
		}
		
		// get the list
		return set;
	}
	
	/**
	 * Get the set of not activated relations that concern a particular decision 
	 * 
	 * @param dec
	 * @return
	 */
	public Set<Relation> getToActivateRelations(Decision dec) 
	{
		// check decision component
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Unknown decision to component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// list of relations
		Set<Relation> set = new HashSet<>();
		// check pending local relations
		for (Relation rel : this.localRelations) {
			// check decisions and relation status
			if ((rel.getReference().equals(dec) || rel.getTarget().equals(dec)) && rel.canBeActivated()) {
				// add pending local relation
				set.add(rel);
			}
		}
		
		// check pending global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				// check reference and target decisions
				if ((rel.getReference().equals(dec) || rel.getTarget().equals(dec)) && rel.canBeActivated()) {
					// add pending global relation
					set.add(rel);
				}
			}
		}
		
		// get list 
		return set;
	}
	
	/**
	 * Get the set of local and global pending relations concerning the decision 
	 * @param dec
	 * @return
	 */
	public Set<Relation> getPendingRelations(Decision dec) 
	{
		// check if local decision
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Unknown decision to component:- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// list of relations
		Set<Relation> set = new HashSet<>();
		// check local relations
		for (Relation rel : this.localRelations) 
		{
			// check decisions and relation status
			if ((rel.getReference().equals(dec) || rel.getTarget().equals(dec)) && rel.isPending()) {
				// add pending relation
				set.add(rel);
			}
		}
		
		// check global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				// check decisions and relation status
				if ((rel.getReference().equals(dec) || rel.getTarget().equals(dec)) && rel.isPending()) {
					// add pending relation
					set.add(rel);
				}
			}
		}
		
		// get list 
		return set;
	}
	
	/**
	 * Get the set of all local and global relations concerning a decision
	 * 
	 * @param dec
	 * @return
	 */
	public Set<Relation> getRelations(Decision dec)
	{
		// check if local decision
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Unknown decision to component:- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// list local of relations
		Set<Relation> set = new HashSet<>();
		for (Relation rel : this.localRelations) {
			// check decisions and relation status
			if (dec.equals(rel.getReference()) || dec.equals(rel.getTarget())) {
				// add relation
				set.add(rel);
			}
		}
		
		// check global relations
		synchronized (globalRelations) {
			// get also global relation
			for (Relation rel : globalRelations) {
				if (dec.equals(rel.getReference()) || dec.equals(rel.getTarget())) {
					// add relation
					set.add(rel);
				}
	 		}
		}
		
		// get list 
		return set;		
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
	public void delete(Relation relation) 
	{
		// check reference and target components
		DomainComponent refComp = relation.getReference().getComponent();
		DomainComponent tarComp = relation.getTarget().getComponent();
		if (!refComp.equals(this) && !tarComp.equals(this)) {
			// "external" relation
			throw new RuntimeException("Trying to free an \"external\" relation for component:\n- component: " + this.name + "\n- relation: " + relation + "\n");
		}
		
		// check if relation is active
		if (!relation.isActive()) 
		{
			// check if local relation
			if (this.localRelations.contains(relation)) {
				// remove relation from component 
				this.localRelations.remove(relation);
			}
			
			// check global relation
			synchronized (globalRelations) {
				if (globalRelations.contains(relation)) {
					// remove from global relations
					globalRelations.remove(relation);
				}
			}
		}
		else {
			// debugging information
			warning("The relation you want to delete is active:\n- relation: " + relation + "\n");
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
	 * Get the list of silent local relations
	 * 
	 * Only for debugging 
	 * 
	 * @return
	 */
	public Set<Relation> getSilentRelations() {
		// relations
		Set<Relation> set = new HashSet<>();
		// check local relations
		for (Relation rel : this.localRelations) {
			// check status
			if (rel.isSilent()) {
				set.add(rel);
			}
		}
		// get the set
		return set;
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
	public void activate(Relation rel) 
			throws RelationPropagationException 
	{
		// check reference and target components
		DomainComponent refComp = rel.getReference().getComponent();
		DomainComponent tarComp = rel.getTarget().getComponent();
		if (!refComp.equals(this) && !tarComp.equals(this)) {
			// "external" relation
			throw new RuntimeException("Trying to add an \"external\" relation for component:\n- component: " + this.name + "\n- relation: " + rel+ "\n");
		}
		
		// check if relation can be activated - check no constraint is associated and if related decisions are active 
		if (rel.canBeActivated()) 
		{
			try
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
			catch (ConstraintPropagationException ex) {
				// clear relation
				rel.clear();
				// not that the relation is still "pending"
				throw new RelationPropagationException(ex.getMessage());
			}
		}
		else {
			// debug information
			warning("The decision you want to activate is already active or the related decision are not active yet:\n- " + rel + "\n");
		}
	}
	
	/**
	 * Propagate pending relations 
	 * 
	 * @param relations
	 */
	public void activate(Set<Relation> relations) 
			throws RelationPropagationException 
	{
		// list of committed relations
		List<Relation> committed = new ArrayList<>();
		try 
		{
			// propagate relations
			for (Relation rel : relations) {
				// propagate relation
				this.activate(rel);
				// add to committed
				committed.add(rel);
			}
		} 
		catch (RelationPropagationException ex) {
			// error while propagating relations
			for (Relation toRetract : committed) {
				// delete relation
				this.deactivate(toRetract);
			}
			// forward exception
			throw new RelationPropagationException(ex.getMessage());
		}
	}
	
	/**
	 * Deactivate a relation by removing the related constraint if any. The relation remains into the component data 
	 * structure as a "pending" relation
	 * 
	 * @param rel
	 */
	public void deactivate(Relation rel) 
	{
		// check reference and target components
		DomainComponent refComp = rel.getReference().getComponent();
		DomainComponent tarComp = rel.getTarget().getComponent();
		if (!refComp.equals(this) && !tarComp.equals(this)) {
			// "external" relation
			throw new RuntimeException("Trying to delete an \"external\" relation for component:\n- component: " + this.name + "\n- relation: " + rel+ "\n");
		}
		
		// check if relation must be deactivated
		if (rel.isActive())
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
	}
	
	/**
	 * 
	 * @return
	 */
	public abstract <V extends ComponentValue> List<V> getValues();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public abstract ComponentValue getValueByName(String name);
	
	/**
	 * 
	 * @return
	 * @throws UnsolvableFlawException
	 */
	public List<Flaw> detectFlaws() 
			throws UnsolvableFlawException 
	{
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
	 * 
	 * @param type
	 * @return
	 * @throws UnsolvableFlawException
	 */
	public List<Flaw> detectFlaws(FlawType type) 
			throws UnsolvableFlawException
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// get resolver capable to handle the desired set of flaws, if any
		if (this.flawType2resolver.containsKey(type))
		{
			// get related resolver
			Resolver<?> resv = this.flawType2resolver.get(type);
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
	 * @param id
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param duration
	 * @param state
	 * @return
	 * @throws TemporalIntervalCreationException
	 * @throws ParameterCreationException
	 */
	public Token createToken(int id, ComponentValue value, String[] labels, long[] start, long[] end, long[] duration, ExecutionNodeStatus state) 
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
		return new Token(id, this, interval, predicate, state);
	}
}