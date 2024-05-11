package it.cnr.istc.pst.platinum.ai.framework.domain.component;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNodeStatus;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.RelationPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb.SynchronizationRule;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.FrameworkObject;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.ParameterFacadePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.ResolverListPlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.TemporalFacadePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConstraintPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.ParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.TemporalQueryType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.Resolver;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ParameterFacade;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ex.ParameterCreationException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.ex.ParameterNotFoundException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.Parameter;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.constraints.ParameterConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalFacade;
import it.cnr.istc.pst.platinum.ai.framework.time.TemporalInterval;
import it.cnr.istc.pst.platinum.ai.framework.time.ex.TemporalIntervalCreationException;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.TemporalConstraint;
import it.cnr.istc.pst.platinum.ai.framework.time.lang.query.IntervalScheduleQuery;
import it.cnr.istc.pst.platinum.ai.framework.utils.view.component.ComponentView;
import it.cnr.istc.pst.platinum.ai.framework.utils.view.component.gantt.GanttComponentView;

/**
 * 
 * @author alessandro
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
	
	// rules from the planning domain
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
		
		// set decisions of the (local) plan
		this.decisions = new HashMap<>();
		for (PlanElementStatus status : PlanElementStatus.values()) {
			this.decisions.put(status, new HashSet<>());
		}
		
		// set relations of the (local) plan
		this.localRelations = new HashSet<>();
		// set up the list of resolvers
		this.resolvers = new ArrayList<>();
		this.flawType2resolver = new HashMap<>();
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected synchronized void init() {
		
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
	 * Clear component data structure
	 */
	public synchronized void clear() 
	{
		// delete all active relations
		for (Relation relation : this.getActiveRelations()) {
			// deactivate relation
			this.deactivate(relation);
			// delete relation data
			this.delete(relation);
		}
		
		// delete all active decisions
		for (Decision decision : this.getActiveDecisions()) {
			// deactivate decision
			this.deactivate(decision);
			// free decision
			this.free(decision);
			// delete decision
			this.delete(decision);
		}
		
		// clear component data structures
		this.decisions.clear();
		this.localRelations.clear();
	}
	
	/**
	 * Compute the makespan of a component as minimal and maximal duration of its activities
	 *  
	 * @return
	 */
	public synchronized double[] getMakespan() 
	{
		// set makespan initial values
		double[] makespan = new double[] {
			0,
			0
		};
		
		// get active decisions
		List<Decision> list = this.getActiveDecisions();
		// check scheduled end-times 
		for (Decision a : list) {
			// increment minimal makespan
			makespan[0] = Math.max(makespan[0], a.getEnd()[0]);
			// increment maximal makespan
			makespan[1] = Math.max(makespan[1], a.getEnd()[1]);
		}
		
		// get computed makespan
		return makespan;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized double[] getBehaviorDuration() {
		// set duration initial values
		double[] duration = new double[] {
			0,
			0
		};
		
		// get active decisions
		List<Decision> list = this.getActiveDecisions();
		// check scheduled end-times 
		for (Decision a : list) {
			// increment minimal duration
			duration[0] += a.getDuration()[0];
			// increment maximal duration
			duration[1] += a.getDuration()[1];
		}
		
		// get computed makespan
		return duration;
	}
	
	/**
	 * 
	 */
	public void display() {
		
		// check view 
		if (this.view == null) {
			// create component view
			this.view = new GanttComponentView(this);
		}
		
		// display component's data
		this.view.display();
	}

	/**
	 * 
	 * @return
	 */
	public final List<SynchronizationRule> getSynchronizationRules() {
		// get all rules
		List<SynchronizationRule> list = new ArrayList<>();
		synchronized (rules) {
			for (DomainComponent comp : rules.keySet()) {
				// check if a rule has been defined on the component
				if (rules.containsKey(comp)) {
					for (ComponentValue v : rules.get(comp).keySet()) {
						// add rules
						list.addAll(rules.get(comp).get(v));
					}
				}
			}
		}
		
		// get rules
		return list;
	}
	
	/**
	 * 
	 */
	public final  List<SynchronizationRule> getSynchronizationRules(ComponentValue value) 
	{
		// list of rules
		List<SynchronizationRule> list = new ArrayList<>();
		synchronized (rules) {
			// check domain specification
			if (rules.containsKey(value.getComponent()) && rules.get(value.getComponent()).containsKey(value)) {
				list.addAll(rules.get(value.getComponent()).get(value));
			}
		}
		
		// get rules
		return list;
	}
	
	/**
	 * 
	 */
	public final List<SynchronizationRule> getSynchronizationRules(DomainComponent component) 
	{
		// list of rules
		List<SynchronizationRule> list = new ArrayList<>();
		synchronized (rules) {
			// check domain specification
			if (rules.containsKey(component)) {
				for (ComponentValue value : rules.get(component).keySet()) {
					list.addAll(rules.get(component).get(value));
				}
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
	public static final Set<Relation> getGlobalActiveRelations() {
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
	public static final Set<Relation> getGlobalRelations() {
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
	public static final Set<Relation> getGlobalPendingRelations() {
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
	public synchronized List<Decision> getActiveDecisions() 
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
	public synchronized List<Decision> getPendingDecisions() {
		return new ArrayList<>(this.decisions.get(PlanElementStatus.PENDING));
	}
	
	/**
	 * Restore the selected decision as pending into the component
	 * 
	 * @param dec
	 */
	public synchronized void restore(Decision dec) 
	{
		// check decision component
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Trying to restore a not local decision on component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// if active something goes wrong
		if (this.isActive(dec)) {
			// unexpected behavior
			throw new RuntimeException("Trying to restore an active decision:\n- decision: " + dec + "\n");
		}
		
		// check if pending
		if (this.isPending(dec)) {
			// warning information
			warning("Trying to restore an already pending decision:\n- decision: " + dec + "\n");
//			throw new RuntimeException("Trying to restore an already pending decision:\n- decision: " + dec + "\n");
		}
		
		// check if silent decision
		if (this.isSilent(dec)) {
			// remove from silent set
			this.decisions.get(PlanElementStatus.SILENT).remove(dec);
			// add to pending set
			this.decisions.get(PlanElementStatus.PENDING).add(dec);
		}
	}
	
	/**
	 * 
	 * @param rel
	 */
	public synchronized void restore(Relation rel) 
	{
		// get reference component
		DomainComponent refComp = rel.getReference().getComponent();
		DomainComponent targetComp = rel.getTarget().getComponent();
		// check components
		if (!refComp.equals(this) && !targetComp.equals(this)) {
			// unknown relation - global "external" relation
			throw new RuntimeException("Trying to restore a relation \"unknown\" to component:\n- component: " + this.name + "\n- relation: " + rel + "\n");
		}
		
		// restore local relation
		if (rel.isLocal()) 
		{
			// check if still present in the data structure
			if (this.localRelations.contains(rel)) 
			{
				// check also if active 
				if (rel.getConstraint() != null) {
					// warning trying to restore an active relation
					warning("Trying to restore an ACTIVE relation!");
					// deactivate relation
					this.deactivate(rel);
				}
			}
			else {
				// add "back" local relation
				this.localRelations.add(rel);
			}
		}
		else 
		{
			// restore global relation
			synchronized (globalRelations) {
				// check if still present in the data structure
				if (globalRelations.contains(rel)) 
				{
					// check also if active 
					if (rel.getConstraint() != null) {
						// warning trying to restore an active relation
						warning("Trying to restore an ACTIVE relation!");
						// deactivate relation
						this.deactivate(rel);
					}
				}
				else {
					// add "back" global relation
					globalRelations.add(rel);
				}
			}
		}
	}
	
	/**
	 * The method creates a pending decision of the plan with the given component's value
	 * 
	 * @param value
	 * @return
	 */
	public synchronized Decision create(ComponentValue value, String[] labels) 
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
	public synchronized Decision create(ComponentValue value, String[] labels, long[] duration) {
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
	public synchronized Decision create(ComponentValue value, String[] labels, long[] end, long[] duration) {
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
	public synchronized Decision create(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) 
	{
		// check if value is known to the component
		if (!value.getComponent().equals(this)) {
			throw new RuntimeException("Trying to add a decision with a value unknown to the component:\n- component: " + this.name + "\n- value: " + value + "\n");
		}
		
		// set decision
		Decision dec = new Decision(DecisionIdCounter.getAndIncrement(), value, labels, start, end, duration);
		// add decision the the agenda
		this.decisions.get(PlanElementStatus.PENDING).add(dec);
		// get decision
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
	public synchronized Decision create(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration, ExecutionNodeStatus status) 
	{
		// check if value is known to the component
		if (!value.getComponent().equals(this)) {
			throw new RuntimeException("Trying to add a decision with a value unknown to the component:\n- component: " + this.name + "\n- value: " + value + "\n");
		}
		
		// set decision
		Decision dec = new Decision(DecisionIdCounter.getAndIncrement(), value, labels, start, end, duration, status);
		// add decision the the agenda
		this.decisions.get(PlanElementStatus.PENDING).add(dec);
		// get set decision
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
	public synchronized Set<Relation> activate(Decision dec) 
			throws DecisionPropagationException {
		
		// check decision component
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Trying to add a not local decision to a component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// list of relations to activate
		Set<Relation> rels = new HashSet<>();
		// check if already active
		if (this.isActive(dec)) {
			// warning information
			warning("Trying to activate an already ACTIVE decision:\n- decision: " + dec + "\n");
			
		} else {
			
			// flag in case of roll-back
			boolean free = false;
			// check if decision is silent
			if (this.isSilent(dec)) {
				// restore decision 
				this.restore(dec);
				// set free flag
				free = true;
			}
			
			// check if decision is pending
			if (this.isPending(dec)) 
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
				
					// get local and global relations to activate
					for (Relation rel : this.getToActivateRelations(dec)) {
						// propagate relations
						this.activate(rel);
						// add relation to the list
						rels.add(rel);
					}
				}
				catch (RelationPropagationException ex) 
				{
					// deactivate relations
					for (Relation rel : rels) {
						this.deactivate(rel);
					}
					
					// deactivate decision ACTIVE -> PENDING
					this.deactivate(dec);
					
					// reset SILENT decision if necessary
					if (free) {
						// PENDING -> FREE
						this.free(dec);
					}
					
					// throw exception
					throw new DecisionPropagationException(ex.getMessage());
				}
				catch (TemporalIntervalCreationException | ParameterCreationException ex) {
					
					// reset SILENT decision if necessary
					if (free) {
						
						// PENDING -> FREE
						this.free(dec);
					}
					
					// throw exception
					throw new DecisionPropagationException(ex.getMessage());
				}
			}
		}
		
		// get list of "local" activated relations
		return rels;
	}

	/**
	 * 
	 * @param dec
	 */
	public synchronized Set<Relation> deactivate(Decision dec) {
		
		// check decision component
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Trying to delete a not local decision from a component:\n"
					+ "- Component: " + this.name + "\n"
					+ "- Decision: " + dec + "\n");
		}
		
		// check if already pending
		if (this.isSilent(dec)) {
			// restore silent decision to set it as "pending" 
			this.restore(dec);
			// warning information
			warning("Trying to deactivate a SILENT decision:\n"
					+ "- Decision: " + dec + "\n");	
		}
		
		// check if already pending
		if (this.isPending(dec)) {
			// warning information
			warning("Trying to deactivate an already PENDING decision:\n"
					+ "- Decision: " + dec + "\n");
		}
		
		// set of deactivated relations
		Set<Relation> rDeactivated = new HashSet<>(); 
		// check if active
		if (this.isActive(dec)) {
			
			// get active relations to retract
			for (Relation rel :  this.getActiveRelations(dec)) {
				
				// deactivate relations
				this.deactivate(rel);
				// add 
				rDeactivated.add(rel);
			}
			
			// delete related token
			Token token = dec.getToken();
			// delete parameters of token predicate
			for (Parameter<?> param : token.getPredicate().getParameters()) {
				
				try {
					
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
		
		// get deactivated relations
		return rDeactivated;
	}
	
	/**
	 * 
	 * @param dec
	 */
	public synchronized void free(Decision dec) 
	{
		// check decision component
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Trying to free a not local decision from a component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// check if already SILENT
		if (this.isSilent(dec)) {
			// warning information
			warning("Trying to free an already SILENT decision:\n- decision: " + dec + "\n");
		}
		
		// check if ACTIVE
		if (this.isActive(dec)) {
			// check as this could be a potential bug in the backtracking procedure when "restoring" plan states 
			throw new RuntimeException("Trying to free an ACTIVE decision:\n- decision: " + dec + "\n");
		}
		
		// complete transition PENDING -> SILENT
		if (this.isPending(dec)) {
			// delete pending decision
			this.decisions.get(PlanElementStatus.PENDING).remove(dec);
			// add to silent decisions
			this.decisions.get(PlanElementStatus.SILENT).add(dec);
		}
	}
	
	/**
	 * Force delete of the decision from the component
	 * 
	 * @param dec
	 */
	public void delete(Decision dec)
	{
		// check decision component
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Trying to free a not local decision from a component:\n- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// check if active ACTIVE -> PENDING
		if (this.isActive(dec)) {
			// deactivate decision and associated active relations
			this.deactivate(dec);
		}
		
		// complete transition PENDING -> SILENT
		if (this.isPending(dec)) {
			// delete pending decision
			this.decisions.get(PlanElementStatus.PENDING).remove(dec);
			// add to silent decisions
			this.decisions.get(PlanElementStatus.SILENT).add(dec);
		}
		
		if (this.isSilent(dec)) {
			// remove decision from silent set
			this.decisions.get(PlanElementStatus.SILENT).remove(dec);
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
	public synchronized <T extends Relation> T create(RelationType type, Decision reference, Decision target) 
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
	public synchronized Set<Relation> getActiveRelations(Decision dec)
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
			if ((rel.getReference().equals(dec) || 
					rel.getTarget().equals(dec)) 
					&& rel.isActive()) 
			{
				// add relation
				set.add(rel);
			}
		}
		
		// check global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				// check related decision and relation status
				if ((rel.getReference().equals(dec) || 
						rel.getTarget().equals(dec)) && 
						rel.isActive()) 
				{
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
	public synchronized Set<Relation> getActiveRelations(Decision reference, Decision target)
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
			if (rel.getReference().equals(reference) && 
					rel.getTarget().equals(target) && 
					rel.isActive()) {
				
				// add relation
				set.add(rel);
			}
		}
		
		// check global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				// check decisions and relation status
				if (rel.getReference().equals(reference) && 
						rel.getTarget().equals(target) && 
						rel.isActive()) {
					
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
	public synchronized Set<Relation> getActiveRelations()
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
	public synchronized Set<Relation> getPendingRelations()
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
	public synchronized Set<Relation> getRelations() {
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
	public synchronized Set<Relation> getToActivateRelations(Decision dec) 
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
			if ((rel.getReference().equals(dec) || 
					rel.getTarget().equals(dec)) && 
					rel.canBeActivated()) 
			{
				// add pending local relation
				set.add(rel);
			}
		}
		
		// check pending global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				
				// check reference and target decisions
				if ((rel.getReference().equals(dec) || 
						rel.getTarget().equals(dec)) && 
						rel.canBeActivated()) 
				{
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
	public synchronized Set<Relation> getPendingRelations(Decision dec) 
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
			if ((rel.getReference().equals(dec) || 
					rel.getTarget().equals(dec)) && 
					rel.isPending()) {
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
	public synchronized Set<Relation> getRelations(Decision dec)
	{
		// check if local decision
		if (!dec.getComponent().equals(this)) {
			throw new RuntimeException("Unknown decision to component:- component: " + this.name + "\n- decision: " + dec + "\n");
		}
		
		// list local of relations
		Set<Relation> set = new HashSet<>();
		for (Relation rel : this.localRelations) {
			// check decisions and relation status
			if (dec.equals(rel.getReference()) || 
					dec.equals(rel.getTarget())) {
				// add relation
				set.add(rel);
			}
		}
		
		// check global relations
		synchronized (globalRelations) {
			// get also global relation
			for (Relation rel : globalRelations) {
				if (dec.equals(rel.getReference()) || 
						dec.equals(rel.getTarget())) {
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
	public synchronized boolean isActive(Decision dec) {
		return this.decisions.get(PlanElementStatus.ACTIVE) != null && 
				decisions.get(PlanElementStatus.ACTIVE).contains(dec);
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public synchronized boolean isPending(Decision dec) {
		return this.decisions.get(PlanElementStatus.PENDING) != null &&
				this.decisions.get(PlanElementStatus.PENDING).contains(dec);
	}
	
	/**
	 * 
	 * @param dec
	 * @return
	 */
	public synchronized boolean isSilent(Decision dec) {
		return this.decisions.get(PlanElementStatus.SILENT) != null &&
				this.decisions.get(PlanElementStatus.SILENT).contains(dec);
	}
	
	/**
	 * 
	 * @param relation
	 */
	public synchronized void delete(Relation relation) 
	{
		// check reference and target components
		DomainComponent refComp = relation.getReference().getComponent();
		DomainComponent tarComp = relation.getTarget().getComponent();
		if (!refComp.equals(this) && !tarComp.equals(this)) {
			// "external" relation
			throw new RuntimeException("Trying to free an \"external\" relation for component:\n- component: " + this.name + "\n- relation: " + relation + "\n");
		}
		
		// deactivate relation if necessary
		this.deactivate(relation);
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
	
	/**
	 * Only for debugging 
	 * 
	 * @return
	 */
	public synchronized List<Decision> getSilentDecisions() {
		return new ArrayList<>(this.decisions.get(PlanElementStatus.SILENT));
	}
	
	/**
	 * Get the list of silent local relations
	 * 
	 * Only for debugging 
	 * 
	 * @return
	 */
	public synchronized Set<Relation> getSilentRelations() {
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
	public synchronized boolean activate(Relation rel) 
			throws RelationPropagationException 
	{
		// check reference and target components
		DomainComponent refComp = rel.getReference().getComponent();
		DomainComponent tarComp = rel.getTarget().getComponent();
		if (!refComp.equals(this) && !tarComp.equals(this)) {
			// "external" relation
			throw new RuntimeException("Trying to add an \"external\" relation for component:\n- component: " + this.name + "\n- relation: " + rel+ "\n");
		}
		
		// check if can be activated
		boolean canBeActivated = rel.canBeActivated();
		// check no constraint is associated and if related decisions are active 
		if (canBeActivated) 
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
				// note that the relation is still "pending"
				throw new RelationPropagationException(ex.getMessage());
			}
		}
		else {
			// debug information
			debug("The decision you want to activate is already active or the related decision are not active yet:\n- " + rel + "\n");
		}
		
		// get flag
		return canBeActivated;
	}
	
	/**
	 * Propagate pending relations 
	 * 
	 * @param relations
	 */
	public synchronized void activate(Set<Relation> relations) 
			throws RelationPropagationException 
	{
		// list of committed relations
		List<Relation> committed = new ArrayList<>();
		try 
		{
			// propagate relations
			for (Relation rel : relations) {
				// propagate relation
				if (this.activate(rel)) {
					// add to committed
					committed.add(rel);
				}
			}
		} 
		catch (RelationPropagationException ex) {
			
			// error while propagating relations
			for (Relation rel : committed) {
				// deactivated committed relations
				this.deactivate(rel);
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
	public synchronized void deactivate(Relation rel) 
	{
		// check reference and target components
		DomainComponent refComp = rel.getReference().getComponent();
		DomainComponent tarComp = rel.getTarget().getComponent();
		if (!refComp.equals(this) && !tarComp.equals(this)) {
			// "external" relation
			throw new RuntimeException("Trying to delete an \"external\" relation for component:\n- component: " + this.name + "\n- relation: " + rel+ "\n");
		}
		
		// check underlying constraint
		if (rel.getConstraint() != null)
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
					// clear relation
					rel.clear();
				}
				break;
				
				// parameter constraint
				case PARAMETER_CONSTRAINT : 
				{
					// get parameter relation
					ParameterRelation prel = (ParameterRelation) rel;
					// retract the related constraint
					this.pdb.retract(prel.getConstraint());
					// clear relation
					rel.clear();
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
	 */
	public synchronized List<Flaw> checkFlaws() 
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// call resolvers to check flaws 
		for (Resolver<?> resv : this.resolvers) {
			// add detected flaws
			list.addAll(resv.checkFlaws());
		}

		// get the list of detected flaws
		return list;
	}
	
	/**
	 * 
	 * @param types
	 * @return
	 */
	public synchronized List<Flaw> checkFlaws(FlawType[] types)
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		for (FlawType fType : types) {
			// check if exists
			if (this.flawType2resolver.containsKey(fType)) {
				// get resolver associated to the specified types
				Resolver<?> resv = this.flawType2resolver.get(fType);
				// add detected flaws
				list.addAll(resv.checkFlaws());
			}
		}

		// get the list of detected flaws
		return list;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws UnsolvableFlawException
	 */
	public synchronized List<Flaw> detectFlaws() 
			throws UnsolvableFlawException 
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// flag about existence of unsolvable flaws
		boolean unsolvable = false;
		// call resolvers to detect flaws and possible solutions
		for (Resolver<?> resv : this.resolvers) 
		{
			try 
			{
				// add detected flaws
				list.addAll(resv.findFlaws());
			}
			catch (UnsolvableFlawException ex) {
				// set unsolvable flag
				unsolvable = true;
				// warning 
				warning("Component [" + this.name + "] detects flaws with not solutions:\n"
						+ "- message= " + ex.getMessage() + "\n");
			}
		}

		// check flaws exist and are all unsolvable
		if (unsolvable) {
			// throw exception if no solvable flaw was found by resolvers
			throw new UnsolvableFlawException("Component [" + this.name + "] detects unsolvable flaws only!");
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
	public synchronized List<Flaw> detectFlaws(FlawType type) 
			throws UnsolvableFlawException {
		
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// get resolver capable to handle the desired set of flaws, if any
		if (this.flawType2resolver.containsKey(type)) {
			
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
	public synchronized void commit(FlawSolution solution) 
			throws FlawSolutionApplicationException {
		// dispatch the flaw to the correct resolver
		this.flawType2resolver.get(solution.getFlaw().getType()).apply(solution);
	}
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	public synchronized void restore(FlawSolution solution) 
			throws Exception {
		// dispatch the flaw to the correct resolver
		this.flawType2resolver.get(solution.getFlaw().getType()).restore(solution);
	}
	
	/**
	 * 
	 * @param solution
	 */
	public synchronized void rollback(FlawSolution solution) {
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
		return "{ \"name\": \"" + this.name + "\" }";
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
	public synchronized Token createToken(int id, ComponentValue value, String[] labels, long[] start, long[] end, long[] duration, ExecutionNodeStatus state) 
			throws TemporalIntervalCreationException, ParameterCreationException {
		
		// create a temporal interval
		TemporalInterval interval = this.tdb.createTemporalInterval(
						start,
						end, 
						duration, 
						value.isControllable());
		
		// create predicate
		Predicate predicate = new Predicate(PREDICATE_COUNTER.getAndIncrement(), value);
		// check parameter labels
		if (labels != null && labels.length > 0) {
			
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
		}
		
		// create token
		return new Token(id, this, interval, predicate, state);
	}
}