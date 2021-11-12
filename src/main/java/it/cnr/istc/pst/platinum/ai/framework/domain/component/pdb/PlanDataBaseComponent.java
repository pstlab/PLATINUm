package it.cnr.istc.pst.platinum.ai.framework.domain.component.pdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.ai.deliberative.solver.Operator;
import it.cnr.istc.pst.platinum.ai.framework.domain.DomainComponentBuilder;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponentType;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.PlanDataBase;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.PlanElementStatus;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.FlawSolutionApplicationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.RelationPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.domain.knowledge.DomainKnowledge;
import it.cnr.istc.pst.platinum.ai.framework.domain.knowledge.DomainKnowledgeType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.ConstraintCategory;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.DomainKnowledgeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.inject.framework.DomainKnowledgePlaceholder;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.DomainComponentNotFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.OperatorPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.ProblemInitializationException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.Flaw;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawSolution;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw.FlawType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.Plan;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.SolutionPlan;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.ParameterProblemConstraint;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.Problem;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.ProblemConstraint;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.ProblemFact;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.ProblemFluent;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.ProblemGoal;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.problem.TemporalProblemConstraint;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.Relation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.RelationType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.EqualParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.NotEqualParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.parameter.ParameterRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.query.ParameterQueryType;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.cnr.istc.pst.platinum.ai.framework.parameter.csp.solver.ParameterSolverType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomain;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.ParameterDomainType;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.query.ComputeSolutionParameterQuery;
import it.cnr.istc.pst.platinum.ai.framework.time.solver.TemporalSolverType;
import it.cnr.istc.pst.platinum.ai.framework.time.tn.TemporalNetworkType;

/**
 * 
 * @author anacleto
 *
 */
@DomainKnowledgeConfiguration(
		
		// set domain knowledge
		knowledge = DomainKnowledgeType.STATIC
)
@TemporalFacadeConfiguration(
		
		// set temporal network 
		network = TemporalNetworkType.STNU, 
		
		// set planner solver
		solver = TemporalSolverType.APSP
)
@ParameterFacadeConfiguration(
		
		// set parameter reasoner
		solver = ParameterSolverType.CHOCHO_SOLVER
)
public final class PlanDataBaseComponent extends DomainComponent implements PlanDataBase
{
	@DomainKnowledgePlaceholder
	protected DomainKnowledge knowledge;
	
	// see Composite design pattern
	private Map<String, DomainComponent> components;
	
	// the planning problem
	protected Problem problem;
	
	// domain theory
	private Map<String, ParameterDomain> parameterDomains;
		
	/**
	 * 
	 * @param name
	 */
	
	@DomainComponentConfiguration(resolvers = {
			// no resolver is needed
	})
	protected PlanDataBaseComponent(String name) 
	{
		super(name, DomainComponentType.PLAN_DATABASE);
		// initialize data structures
		this.components = new HashMap<>();
		this.parameterDomains = new HashMap<>();
		this.problem = null;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected synchronized void init() {
		super.init();
		
		// clear static (global) information
		synchronized (rules) {
			// clear if needed global synchronization rules
			rules.clear();
		}
		
		synchronized(globalRelations) {
			// clear if needed global relations
			globalRelations.clear();
		}
	
		// reset predicate counter
		PREDICATE_COUNTER.set(0);
		// reset decision counter
		DecisionIdCounter.set(0);
		// reset relation counter
		RELATION_COUNTER.set(0);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void setup(Problem problem) 
			throws ProblemInitializationException {
		// setup problem
		this.doSetupProblem(problem);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void clear() 
	{
		// clear components
		for (DomainComponent component : this.components.values()) {
			// clear component
			component.clear();
		}

		// clear local relations
		this.localRelations.clear();
		// clear global relations
		synchronized (globalRelations) {
			// clear global active relations
			for (Relation rel : globalRelations) {
				// delete global relation
				this.deactivate(rel);
			}
			
			// clear global relations
			globalRelations.clear();
		}
		// clear problem
		this.problem = null;
		// clear domain knowledge
		this.knowledge = null;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized DomainKnowledge getDomainKnowledge() {
		return this.knowledge;
	}
	
	/*
	 * 
	 */
	@Override
	public synchronized void addSynchronizationRule(SynchronizationRule rule) 
			throws SynchronizationCycleException
	{
		// get head value
		ComponentValue value = rule.getTriggerer().getValue();
		// set the trigger as complex
		value.setComplex();
				
		// check data
		if (!rules.containsKey(value.getComponent())) {
			rules.put(value.getComponent(), new HashMap<ComponentValue, List<SynchronizationRule>>());
		}
		if (!rules.get(value.getComponent()).containsKey(value)) {
			// initialize
			rules.get(value.getComponent()).put(value, new ArrayList<SynchronizationRule>());
		}
		
		// look for cycles
		for (TokenVariable var : rule.getTokenVariables()) 
		{
			// get value 
			ComponentValue v = var.getValue();
			// check if this value is trigger of other synchronizations
			if (rules.containsKey(v.getComponent()) && rules.get(v.getComponent()).containsKey(v)) {
				// get synchronizations
				List<SynchronizationRule> existingRules = rules.get(v.getComponent()).get(v);
				for (SynchronizationRule existingRule : existingRules) {
					// get rule trigger
					TokenVariable existingRuleTrigger = existingRule.getTriggerer();
					// check constraint
					for (SynchronizationConstraint cons : existingRule.getConstraints()) {
						// consider temporal constraint for cycle detection
						if (cons.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT)) {
							// get constraint target
							TokenVariable target = cons.getTarget();
							if (!target.equals(existingRuleTrigger) && target.getValue().equals(value)) { 
								// we've got a cycle
								throw new SynchronizationCycleException("A cycle has been detected after the introduction of synchronization rule " + rule);
							}
						}
					}
				}
			}
		}
		
		// add rule if no cycle is detected
		rules.get(value.getComponent()).get(value).add(rule);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public synchronized SynchronizationRule createSynchronizationRule(ComponentValue value, String[] labels) 
			throws DomainComponentNotFoundException 
	{
		// check if related component exists
		if (!this.getComponents().contains(value.getComponent())) {
			throw new DomainComponentNotFoundException("Value's component not found " + value);
		}
		
		// set value as complex
		value.setComplex();
		// create synchronization rule
		return new SynchronizationRule(value, labels);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized SolutionPlan getSolutionPlan() 
	{
		// create a plan
		SolutionPlan plan = new SolutionPlan(this.name, this.getHorizon());
		// set components
		for (DomainComponent component : this.components.values()) {
			// add a component to the plan
			plan.add(component);
		}
		
		// add active relations
		for (Relation rel : this.getActiveRelations()) {
			// add relation
			plan.add(rel);
		}
		
		// computer parameter solutions
		ComputeSolutionParameterQuery query = this.pdb.
				createQuery(ParameterQueryType.COMPUTE_SOLUTION);
		
		// process query
		this.pdb.process(query);
		// get current plan
		return plan;
	}
	
	/**
	 * Compute the duration of a plan as the minimum and maximal average duration of the activities
	 * of its components 
	 */
	@Override
	public synchronized double[] getMakespan() 
	{
		// set the initial minimal and maximal values
		double[] makespan = new double[] {
				0,
				0
			};
		
		
		// get the list of primitive components
		Set<DomainComponent> primset = new HashSet<>();
		// compute "local" makespan for each component
		for (DomainComponent comp : this.components.values()) 
		{
			// check type 
			if (comp.getType().equals(DomainComponentType.SV_PRIMITIVE)) 
			{
				// get local makespan
				double[] local = comp.getMakespan();
				// update "global" minimum makespan
				makespan[0] += local[0];
				// update "global" maximum makespan
				makespan[1] += local[1];
				// add component to primset
				primset.add(comp);
			}
		}
		
		// compute average values
		makespan[0] = makespan[0] / primset.size();
		makespan[1] = makespan[1] / primset.size();
		// get the makespan
		return makespan;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized double[] getBehaviorDuration() 
	{
		// set the initial minimal and maximal values
		double[] duration = new double[] {
				0,
				0
			};
		
		
		// get the list of primitive components
		Set<DomainComponent> primset = new HashSet<>();
		// compute "local" makespan for each component
		for (DomainComponent comp : this.components.values()) 
		{
			// check type 
			if (comp.getType().equals(DomainComponentType.SV_PRIMITIVE)) 
			{
				// get local makespan
				double[] local = comp.getBehaviorDuration();
				// update "global" minimum makespan
				duration[0] += local[0];
				// update "global" maximum makespan
				duration[1] += local[1];
				// add component to primset
				primset.add(comp);
			}
		}
		
		// compute average values
		duration[0] = duration[0] / primset.size();
		duration[1] = duration[1] / primset.size();
		// get the duration
		return duration;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public synchronized Plan getPlan() 
	{
		// initialize the plan
		Plan plan = new Plan();
		// get decisions
		for (Decision goal : this.getActiveDecisions()) {
			plan.add(goal);
		}
		
		// get relations
		for (Relation rel : this.getActiveRelations()) {
			plan.add(rel);
		}
		
		// get the plan
		return plan;
	}

	
	/**
	 * 
	 */
	@Override
	public synchronized Plan getPlan(PlanElementStatus status) 
	{ 
		// prepare the plan
		Plan plan = new Plan();
		// check desired level
		switch (status) 
		{
			// get the plan
			case ACTIVE : {
				// get currently active plan
				plan = this.getPlan();
			}
			break;
			
			// get the pending plan
			case PENDING : {
				// get pending decisions
				for (Decision goal : this.getPendingDecisions()) {
					plan.add(goal);
				}
				// get pending relations
				for (Relation rel : this.getPendingRelations()) {
					plan.add(rel);
				}
			}
			break;
			
			// get silent plan
			case SILENT : {
				// get silent decisions
				for (Decision dec : this.getSilentDecisions()) {
					plan.add(dec);
				}
				// get silent relations
				for (Relation rel : this.getSilentRelations()) {
					plan.add(rel);
				}
			}
			break;
		}
		
		return plan;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized List<DomainComponent> getComponents() {
		return new ArrayList<>(this.components.values());
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized DomainComponent getComponentByName(String name) {
		if (!this.components.containsKey(name)) {
			throw new RuntimeException("Component with name " + name + " does not exist");
		}
		// get component
		return this.components.get(name);
	}
	
	/**
	 * Verify the temporal consistency of the plan.
	 * 
	 * If the underlying network is an STNU, then the 
	 * procedure checks also the pseudo-controllability
	 * of the plan. If the network is not pseudo-controllable
	 * the exception reports information concerning the values
	 * that have been "squeezed" during the solving process 
	 * 
	 * @throws ConsistencyCheckException
	 */
	@Override
	public synchronized void verify() 
			throws ConsistencyCheckException 
	{
		// check temporal consistency of the network
		this.tdb.verify();
		// check parameter consistency
		this.pdb.verify();
	}

	/**
	 * The method returns the list of all available domain values
	 */
	@Override
	public synchronized List<ComponentValue> getValues() {
		List<ComponentValue> values = new ArrayList<>();
		for (DomainComponent component : this.components.values()) {
			values.addAll(component.getValues());
		}
		// get all domain values
		return values;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized ComponentValue getValueByName(String name) {
		ComponentValue value = null;
		for (DomainComponent comp : this.components.values()) {
			for (ComponentValue v : comp.getValues()) {
				if (v.getLabel().equals(name)) {
					value = v;
					break;
				}
			}
			
			if (value != null) {
				break;
			}
		}
		
		// check if value has been found
		if (value == null) {
			throw new RuntimeException("Value " + name + " not found");
		}
		
		// get value
		return value;
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 */
	@Override
	public synchronized <T extends ParameterDomain> T createParameterDomain(String name, ParameterDomainType type) {
		// create parameter domain
		T pd = this.pdb.createParameterDomain(name, type);
		// add parameter domain
		this.parameterDomains.put(name, pd);
		return pd;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public synchronized List<ParameterDomain> getParameterDoamins() {
		return new ArrayList<>(this.parameterDomains.values());
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public synchronized ParameterDomain getParameterDomainByName(String name) {
		return this.parameterDomains.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public synchronized <T extends DomainComponent> T createDomainComponent(String name, DomainComponentType type) 
	{
		// check if a component already exist
		if (this.components.containsKey(name)) {
			throw new RuntimeException("A component with name " + name + " already exists");
		}
	
		// create domain component
		T c = DomainComponentBuilder.createAndSet(name, type, this.tdb, this.pdb);
		// get created component
		return c;
	}
	
	/**
	 * 
	 * @param component
	 */
	@Override
	public synchronized void addDomainComponent(DomainComponent component) {
		// add component
		this.components.put(component.getName(), component);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public synchronized List<Decision> getActiveDecisions() {
		// list of active decisions with schedule information
		List<Decision> list = new ArrayList<>();
		// get schedule information from components
		for (DomainComponent comp : this.components.values()) {
			list.addAll(comp.getActiveDecisions());
		}
		// get list
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public synchronized List<Decision> getPendingDecisions() {
		// list of pending decisions
		List<Decision> list = new ArrayList<>();
		for (DomainComponent comp : this.components.values()) {
			list.addAll(comp.getPendingDecisions());
		}
		// get list of pending decisions
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Set<Relation> getPendingRelations(Decision dec) 
	{
		// get decision component 
		DomainComponent comp = dec.getComponent();
		return comp.getPendingRelations(dec);
	}
	
	/**
	 * 
	 */
	@Override 
	public synchronized void restore(Decision dec) {
		// dispatch request to the related component
		dec.getComponent().restore(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void restore(Relation rel) {
		// get reference component
		DomainComponent comp = rel.getReference().getComponent();
		comp.restore(rel);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Decision create(ComponentValue value, String[] labels) {
		// get the component the value belongs to
		DomainComponent comp = value.getComponent();
		// create decision
		Decision dec = comp.create(value, labels);
		// get created decision
		return dec;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Decision create(ComponentValue value, String[] labels, long[] duration) {
		// get the component the value belongs to
		DomainComponent comp = value.getComponent();
		// create decision
		Decision dec = comp.create(value, labels, duration);
		// get created decision
		return dec;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Decision create(ComponentValue value, String[] labels, long[] end, long[] duration) {
		// get the component the value belongs to
		DomainComponent comp = value.getComponent();
		// create decision
		Decision dec = comp.create(value, labels, end, duration);
		// get created decision
		return dec;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Decision create(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) {
		// get the component the value belongs to
		DomainComponent comp = value.getComponent();
		// create decision
		Decision dec = comp.create(value, labels, start, end, duration);
		// get created decision
		return dec;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Set<Relation> activate(Decision dec) 
			throws DecisionPropagationException 
	{
		// get the component the decision belongs to
		DomainComponent c = dec.getComponent();
		// add decision and get the list of local and global relations propagated
		Set<Relation> set = c.activate(dec);
		// get global and local relations propagated
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Set<Relation> getRelations(Decision dec) 
	{
		// list of relations concerning the decision
		Set<Relation> set = new HashSet<>();
		// get decision component
		DomainComponent comp = dec.getComponent();
		set.addAll(comp.getRelations(dec));
		// get the set
		return set;
	}
	
	/**
	 * Get the set of both active and pending local and global relations on components
	 */
	@Override
	public synchronized Set<Relation> getRelations() {
		// list of relations
		Set<Relation> set = new HashSet<>();
		for (DomainComponent comp : this.components.values()) {
			set.addAll(comp.getRelations());
		}
		
		// add global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				set.add(rel);
			}
		}
		
		// get the set
		return set;
	}
	
	/**
	 * Get the set of local active relations and local active relations on components 
	 *  
	 * @param dec
	 * @return
	 */
	@Override
	public synchronized Set<Relation> getActiveRelations()
	{
		// list of active relations
		Set<Relation> set = new HashSet<>();
		// check local relations
		for (DomainComponent component : this.components.values()) {
			// add active local relations
			set.addAll(component.getActiveRelations());
		}
		
		// add global active relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				if (rel.isActive()) {
					set.add(rel);
				}
			}
		}
		// get the list
		return set;
	}
	
	/**
	 * Get the set of pending global relations and pending local relations on components
	 * 
	 * @return
	 */
	public synchronized Set<Relation> getPendingRelations()
	{
		// set of relations
		Set<Relation> set = new HashSet<>();
		for (DomainComponent comp : this.components.values()) {
			set.addAll(comp.getPendingRelations());
		}
		
		// add pending global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				if (rel.isPending()) {
					set.add(rel);
				}
			}
		}
		// get the set
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Set<Relation> getActiveRelations(Decision dec) 
	{
		// list of active relations
		Set<Relation> set = new HashSet<>();
		// get decision component
		DomainComponent comp = dec.getComponent();
		set.addAll(comp.getActiveRelations(dec));
		// get the set
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Set<Relation> getToActivateRelations(Decision dec) 
	{
		// list of relations
		Set<Relation> set = new HashSet<>();
		// get decision component
		DomainComponent comp = dec.getComponent();
		set.addAll(comp.getToActivateRelations(dec));
		// get the set
		return set;
	}
	
	/**
	 * 
	 * @param dec
	 * @throws Exception
	 */
	@Override
	public synchronized void free(Decision dec) 
	{
		// get decision component
		DomainComponent comp = dec.getComponent();
		comp.free(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized Set<Relation> deactivate(Decision dec) {
		// get decision component
		DomainComponent comp = dec.getComponent();
		return new HashSet<>(comp.deactivate(dec));
	}
	
	/**
	 * 
	 * @param relation
	 */
	@Override
	public synchronized void delete(Relation relation) 
	{
		// get reference component
		DomainComponent refComp = relation.getReference().getComponent();
		refComp.delete(relation);
	}
	
	/**
	 * Only for debugging
	 */
	@Override
	public synchronized List<Decision> getSilentDecisions() {
		List<Decision> list = new ArrayList<>();
		for (DomainComponent component : this.components.values()) {
			list.addAll(component.getSilentDecisions());
		}
		return list;
	}
	
	/**
	 * Only for debugging
	 */
	@Override
	public synchronized Set<Relation> getSilentRelations() {
		// set of relations
		Set<Relation> set = new HashSet<>();
		for (DomainComponent component : this.components.values()) {
			set.addAll(component.getSilentRelations());
		}
		
		// check global relations
		synchronized (globalRelations) {
			for (Relation rel : globalRelations) {
				if (rel.isSilent()) {
					set.add(rel);
				}
			}
		}
		// get the set
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized boolean isActive(Decision dec) {
		return dec.getComponent().isActive(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized boolean isPending(Decision dec) {
		// forward to component
		return dec.getComponent().isPending(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized boolean isSilent(Decision dec) {
		// forward to component
		return dec.getComponent().isSilent(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized boolean activate(Relation rel) 
			throws RelationPropagationException  {
		
		// get reference component
		DomainComponent refComp = rel.getReference().getComponent();
		return refComp.activate(rel);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void deactivate(Relation rel) 
	{
		// get reference component
		DomainComponent refComp = rel.getReference().getComponent();
		refComp.deactivate(rel);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized List<Flaw> checkFlaws() {
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// simply query the components
		for (DomainComponent comp : this.components.values()) {
			// query each COMPOSITE component for flaws
			List<Flaw> flaws = comp.checkFlaws();
			list.addAll(flaws);
		}
		// get the list of detected flaws in the domain
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized List<Flaw> checkFlaws(FlawType[] types) {
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// simply query the components
		for (DomainComponent comp : this.components.values()) {
			// query each COMPOSITE component for flaws
			List<Flaw> flaws = comp.checkFlaws(types);
			list.addAll(flaws);
		}
		// get the list of detected flaws in the domain
		return list;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public synchronized List<Flaw> detectFlaws() 
			throws UnsolvableFlawException 
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// simply query the components
		for (DomainComponent comp : this.components.values()) {
			// query each COMPOSITE component for flaws
			List<Flaw> flaws = comp.detectFlaws();
			list.addAll(flaws);
		}
		// get the list of detected flaws in the domain
		return list;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 * @throws UnsolvableFlawException
	 */
	@Override
	public synchronized List<Flaw> detectFlaws(FlawType type) 
			throws UnsolvableFlawException {
		
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// simply query the components
		for (DomainComponent comp : this.components.values()) {
			// get the list of flaws
			List<Flaw> flaws = comp.detectFlaws(type);
			list.addAll(flaws);
		}
		
		// get the list of detected flaws
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void rollback(FlawSolution solution) 
	{ 
		// get component
		DomainComponent comp = solution.getFlaw().getComponent();
		comp.rollback(solution);
	}
	
	/**
	 * Solve a flaw by applying the selected solution. 
	 * 
	 * Commit the effect of a flaw solution to the underlying component
	 * 
	 * @param flaw
	 * @param sol
	 * @throws Exception
	 */
	@Override
	public synchronized void commit(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// get component
		DomainComponent comp = solution.getFlaw().getComponent();
		comp.commit(solution);
	}
	
	/**
	 * 
	 * @param solution
	 * @throws Exception
	 */
	@Override
	public synchronized void restore(FlawSolution solution) 
			throws Exception
	{
		// get component
		DomainComponent comp = solution.getFlaw().getComponent();
		comp.restore(solution);
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void propagate(Operator operator) 
			throws OperatorPropagationException 
	{
		// check if operator has been applied already
		if (!operator.isApplied()) {
			
			try {
				
				// commit solution 
				this.commit(operator.getFlawSolution());
				// set applied
				operator.setApplied();
			}
			catch (FlawSolutionApplicationException ex) {
				// throw exception
				throw new OperatorPropagationException("Error while propagating operator:\n"
						+ "- Operator: " + operator + "\n");
			}
		}
		else {
			
			try {
				
				// simply restore flaw solution by leveraging "SILENT" plan
				this.restore(operator.getFlawSolution());
			} 
			catch (Exception ex) {
				
				// error while resetting operator
				throw new OperatorPropagationException("Error while restoring operator status:\n"
						+ "- Operator: " + operator + "\n");
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public synchronized void retract(Operator operator) 
	{
		// get flaw solution
		FlawSolution solution = operator.getFlawSolution();
		// retract flaw solution
		this.rollback(solution);
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "{ \"components\": " + this.components.values() +" }";
	}
	
	/**
	 * 
	 */
	@Override
	public <T extends Relation> T create(RelationType type, Decision reference, Decision target) {
		// get reference component
		DomainComponent refComp = reference.getComponent();
		// create relation
		return refComp.create(type, reference, target);
	}
	
	/**
	 * 
	 * @param problem
	 * @throws ProblemInitializationException
	 */
	private void doSetupProblem(Problem problem) 
			throws ProblemInitializationException
	{
		// check if a problem has been already set up
		if (this.problem == null) 
		{
			// list of committed decisions
			List<Decision> committedDecisions = new ArrayList<>();
			// list of committed relations
			List<Relation> committedRelations = new ArrayList<>();
			// index fluent to added decisions
			Map<ProblemFluent, Decision> fluent2decisions = new HashMap<>();
			
			try 
			{
				// get facts 
				for (ProblemFact fact : problem.getFacts()) {
					// create decision
					Decision dec = this.create(
							fact.getValue(), 
							fact.getParameterLabels(), 
							fact.getStart(), 
							fact.getEnd(), 
							fact.getDuration());
					
					// add decision
					this.activate(dec);
					// add committed decision
					committedDecisions.add(dec);
					// add entry
					fluent2decisions.put(fact, dec);
				}
			}
			catch (Exception ex) {
				// roll-back committed decisions
				for (Decision dec : committedDecisions) {
					try {
						// retract decision
						this.free(dec);
					} catch (Exception exx) {
						throw new RuntimeException(exx.getMessage());
					}
				}
				// throw exception
				throw new ProblemInitializationException(ex.getMessage());
			}
			
			// create goals
			for (ProblemGoal goal : problem.getGoals()) {
				// create related decisions
				Decision dec = this.create(
						goal.getValue(), 
						goal.getParameterLabels(), 
						goal.getStart(), 
						goal.getEnd(), 
						goal.getDuration());
				
				// set mandatory expansion
				dec.setMandatoryExpansion();
				// add entry
				fluent2decisions.put(goal, dec);
			}
			
			try 
			{
				// check constraints
				for (ProblemConstraint constraint : problem.getConstraints()) 
				{
					// get related decisions
					Decision reference = fluent2decisions.get(constraint.getReference());
					Decision target = fluent2decisions.get(constraint.getTarget());
					
					// check relation type
					switch (constraint.getCategory()) 
					{
						// temporal constraint
						case TEMPORAL_CONSTRAINT : 
						{
							// get temporal constraint
							TemporalProblemConstraint tc = (TemporalProblemConstraint) constraint;
							// create relation
							TemporalRelation rel = this.create(constraint.getType(), reference, target);
							rel.setBounds(tc.getBounds());
							// check if relation can be activated
							if (this.activate(rel)) {
								committedRelations.add(rel);
							}
						}
						break;
						
						// parameter constraint
						case PARAMETER_CONSTRAINT : 
						{
							// get parameter constraint
							ParameterProblemConstraint pc = (ParameterProblemConstraint) constraint;
							// create relation
							ParameterRelation rel = this.create(constraint.getType(), reference, target);
							// set labels
							rel.setReferenceParameterLabel(pc.getReferenceParameterLabel());
							
							// check relation type
							switch (rel.getType())
							{
								// bind parameter relation
								case BIND_PARAMETER :
								{
									// get relation
									BindParameterRelation bind = (BindParameterRelation) rel;
									// set the binding value
									bind.setValue(pc.getTargetParameterLabel());
								}
								break;
								
								// equal parameter relation
								case EQUAL_PARAMETER :  
								{
									// get relation
									EqualParameterRelation eq = (EqualParameterRelation) rel;
									// set target label
									eq.setTargetParameterLabel(pc.getTargetParameterLabel());
								}
								break; 
								
								// not equal parameter relation
								case NOT_EQUAL_PARAMETER : 
								{
									// get relation
									NotEqualParameterRelation neq = (NotEqualParameterRelation) rel;
									// set also the target label
									neq.setTargetParameterLabel(pc.getTargetParameterLabel());
								}
								break;
								
								default : {
									throw new RuntimeException("Unknown parameter relation type - " + rel.getType());
								}
							}
							
							// check if relation can be activated
							if (this.activate(rel)) {
								committedRelations.add(rel);
							}
						}
						break;
					}
				}
			}
			catch (RelationPropagationException ex) {
				// roll-back committed relations
				for (Relation rel : committedRelations) {
					// retract 
					this.deactivate(rel);
				}
				
				// throw exception
				throw new ProblemInitializationException(ex.getMessage());
			}
			
			try {
				
				// check unsolvable flaws
				this.detectFlaws();
			} 
			catch (UnsolvableFlawException ex) {
				// unsolvable flaws found
				throw new ProblemInitializationException("Inconsistent Problem description\n- Unsolvable flaws have been found\n" + ex.getMessage());
			}
			
			// set problem 
			this.problem = problem;
		}
		else {
			// a problem already exists
			throw new ProblemInitializationException("A problem instace has been already set up... try clear() before setting up a new problem instance");
		}
	}
}
