package it.istc.pst.platinum.framework.domain.component.pdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.deliberative.solver.Operator;
import it.istc.pst.platinum.framework.domain.DomainComponentBuilder;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.domain.component.PlanElementStatus;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.domain.knowledge.DomainKnowledge;
import it.istc.pst.platinum.framework.domain.knowledge.DomainKnowledgeType;
import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainKnowledgeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.ParameterFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.TemporalFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.inject.framework.DomainKnowledgePlaceholder;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.DomainComponentNotFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.OperatorPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Plan;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ParameterProblemConstraint;
import it.istc.pst.platinum.framework.microkernel.lang.problem.Problem;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemConstraint;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemFact;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemFluent;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemGoal;
import it.istc.pst.platinum.framework.microkernel.lang.problem.TemporalProblemConstraint;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.BindParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.EqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.NotEqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.parameter.ParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.TemporalRelation;
import it.istc.pst.platinum.framework.microkernel.query.ParameterQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawException;
import it.istc.pst.platinum.framework.parameter.csp.solver.ParameterSolverType;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;
import it.istc.pst.platinum.framework.parameter.lang.query.ComputeSolutionParameterQuery;
import it.istc.pst.platinum.framework.time.solver.TemporalSolverType;
import it.istc.pst.platinum.framework.time.tn.TemporalNetworkType;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

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
@FrameworkLoggerConfiguration(
		
		// set logging level
		level = FrameworkLoggingLevel.DEBUG
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
	protected void init() {
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
		DECISION_COUNTER.set(0);
		// reset relation counter
		RELATION_COUNTER.set(0);
	}
	
	/**
	 * 
	 */
	@Override
	public void setup(Problem problem) 
			throws ProblemInitializationException {
		// setup problem
		this.doSetupProblem(problem);
	}
	
	/**
	 * 
	 */
	@Override
	public void clear() 
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
	public DomainKnowledge getDomainKnowledge() {
		return this.knowledge;
	}
	
	/*
	 * 
	 */
	@Override
	public void addSynchronizationRule(SynchronizationRule rule) 
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
	public SynchronizationRule createSynchronizationRule(ComponentValue value, String[] labels) 
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
	public SolutionPlan getSolutionPlan() 
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
		
		// compute the resulting plan makespan
		double mk = this.computeMakespan();
		plan.setMakespan(mk);
		
		// computer parameter solutions
		ComputeSolutionParameterQuery query = this.pdb.
				createQuery(ParameterQueryType.COMPUTE_SOLUTION);
		// process query
		this.pdb.process(query);
		// get current plan
		return plan;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public Plan getPlan() 
	{
		// initialize the agenda
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
	public Plan getPlan(PlanElementStatus status) { 
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
	public List<DomainComponent> getComponents() {
		return new ArrayList<>(this.components.values());
	}
	
	/**
	 * 
	 */
	@Override
	public DomainComponent getComponentByName(String name) {
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
	public void verify() 
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
	public List<ComponentValue> getValues() {
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
	public ComponentValue getValueByName(String name) {
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
	public <T extends ParameterDomain> T createParameterDomain(String name, ParameterDomainType type) {
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
	public List<ParameterDomain> getParameterDoamins() {
		return new ArrayList<>(this.parameterDomains.values());
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public ParameterDomain getParameterDomainByName(String name) {
		return this.parameterDomains.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public <T extends DomainComponent> T createDomainComponent(String name, DomainComponentType type) 
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
	public void addDomainComponent(DomainComponent component) {
		// add component
		this.components.put(component.getName(), component);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<Decision> getActiveDecisions() {
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
	public List<Decision> getPendingDecisions() {
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
	public Set<Relation> getPendingRelations(Decision dec) 
	{
		// get decision component 
		DomainComponent comp = dec.getComponent();
		return comp.getPendingRelations(dec);
	}
	
	/**
	 * 
	 */
	@Override 
	public void restore(Decision dec) {
		// dispatch request to the related component
		dec.getComponent().restore(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public void restore(Relation rel) {
		// get reference component
		DomainComponent comp = rel.getReference().getComponent();
		comp.restore(rel);
	}
	
	/**
	 * 
	 */
	@Override
	public Decision create(ComponentValue value, String[] labels) {
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
	public Decision create(ComponentValue value, String[] labels, long[] duration) {
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
	public Decision create(ComponentValue value, String[] labels, long[] end, long[] duration) {
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
	public Decision create(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) {
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
	public Set<Relation> activate(Decision dec) 
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
	public Set<Relation> getRelations(Decision dec) 
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
	public Set<Relation> getRelations() {
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
	public Set<Relation> getActiveRelations()
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
	public Set<Relation> getPendingRelations()
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
	public Set<Relation> getActiveRelations(Decision dec) 
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
	public Set<Relation> getToActivateRelations(Decision dec) 
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
	public void free(Decision dec) 
	{
		// get decision component
		DomainComponent comp = dec.getComponent();
		comp.free(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public void deactivate(Decision dec) {
		// get decision component
		DomainComponent comp = dec.getComponent();
		comp.deactivate(dec);;
	}
	
	/**
	 * 
	 * @param relation
	 */
	@Override
	public void delete(Relation relation) 
	{
		// get reference component
		DomainComponent refComp = relation.getReference().getComponent();
		refComp.delete(relation);
	}
	
	/**
	 * Only for debugging
	 */
	@Override
	public List<Decision> getSilentDecisions() {
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
	public Set<Relation> getSilentRelations() {
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
	public boolean isActive(Decision dec) {
		return dec.getComponent().isActive(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isPending(Decision dec) {
		// forward to component
		return dec.getComponent().isPending(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isSilent(Decision dec) {
		// forward to component
		return dec.getComponent().isSilent(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public void activate(Relation rel) 
			throws RelationPropagationException 
	{
		// get reference component
		DomainComponent refComp = rel.getReference().getComponent();
		refComp.activate(rel);
	}
	
	/**
	 * 
	 */
	@Override
	public void deactivate(Relation rel) 
	{
		// get reference component
		DomainComponent refComp = rel.getReference().getComponent();
		refComp.deactivate(rel);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<Flaw> detectFlaws() 
			throws UnsolvableFlawException 
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// simply query the components
		for (DomainComponent comp : this.components.values()) {
			list.addAll(comp.detectFlaws());
		}
		// get the list of detected flaws in the domain
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public void rollback(FlawSolution solution) 
	{ 
		// get component
		DomainComponent comp = solution.getFlaw().getComponent();
		comp.rollback(solution);
	}
	
	/**
	 * Solve a flaw by applying the selected solution
	 * 
	 * @param flaw
	 * @param sol
	 * @throws Exception
	 */
	@Override
	public void commit(FlawSolution solution) 
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
	public void restore(FlawSolution solution) 
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
	public void propagate(Operator operator) 
			throws OperatorPropagationException 
	{
		// get related flaw solution
		FlawSolution solution = operator.getFlawSolution();
		// check if operator has been applied already
		if (!operator.isApplied())
		{
			try
			{
				// commit solution 
				this.commit(solution);
				// set applied
				operator.setApplied();
			}
			catch (FlawSolutionApplicationException ex) {
				// error while applying flaw solution
				warning(ex.getMessage());
				throw new OperatorPropagationException("Error while propagating operator:\n- " + operator + "\n");
			}
		}
		else
		{
			try
			{
				// simply restore flaw solution by leveraging "SILENT" plan
				this.restore(solution);
			} 
			catch (Exception ex) { 
				// error while resetting operator
				throw new OperatorPropagationException("Error while resetting operator status:\n- " + operator + "\n");
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void retract(Operator operator) 
	{
		// get flaw solution
		FlawSolution solution = operator.getFlawSolution();
		// retract flaw solution
		this.rollback(solution);
	}
	
//	/**
//	 * 
//	 */
//	@Override
//	public double computeMakespan() 
//	{
		// query the temporal facade
//		ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
//		// process query
//		this.tdb.process(query);
//		// get computed value
//		return query.getMakespan();
//	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[PlanDataBase components= " + this.components.values() +"]\n";
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
							if (rel.canBeActivated()) {
								// add relation 
								this.activate(rel);
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
							if (rel.canBeActivated()) {
								// add relation
								this.activate(rel);
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
