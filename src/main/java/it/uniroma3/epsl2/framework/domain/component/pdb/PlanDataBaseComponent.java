package it.uniroma3.epsl2.framework.domain.component.pdb;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.uniroma3.epsl2.deliberative.heuristic.filter.ex.HierarchyCycleException;
import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.domain.PlanDataBaseObserver;
import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentFactory;
import it.uniroma3.epsl2.framework.domain.component.DomainComponentType;
import it.uniroma3.epsl2.framework.domain.component.PlanElementStatus;
import it.uniroma3.epsl2.framework.domain.component.ex.DecisionPropagationException;
import it.uniroma3.epsl2.framework.domain.component.ex.FlawSolutionApplicationException;
import it.uniroma3.epsl2.framework.domain.component.ex.RelationPropagationException;
import it.uniroma3.epsl2.framework.lang.ex.ConsistencyCheckException;
import it.uniroma3.epsl2.framework.lang.ex.ConstraintPropagationException;
import it.uniroma3.epsl2.framework.lang.ex.DomainComponentNotFoundException;
import it.uniroma3.epsl2.framework.lang.ex.PlanRefinementException;
import it.uniroma3.epsl2.framework.lang.ex.ProblemInitializationException;
import it.uniroma3.epsl2.framework.lang.ex.SynchronizationCycleException;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.lang.flaw.FlawSolution;
import it.uniroma3.epsl2.framework.lang.plan.Agenda;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Plan;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.lang.plan.SolutionPlan;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.BindParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.EqualParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.NotEqualParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.parameter.ParameterRelation;
import it.uniroma3.epsl2.framework.lang.plan.relations.temporal.TemporalRelation;
import it.uniroma3.epsl2.framework.lang.problem.ParameterProblemConstraint;
import it.uniroma3.epsl2.framework.lang.problem.Problem;
import it.uniroma3.epsl2.framework.lang.problem.ProblemConstraint;
import it.uniroma3.epsl2.framework.lang.problem.ProblemFact;
import it.uniroma3.epsl2.framework.lang.problem.ProblemFluent;
import it.uniroma3.epsl2.framework.lang.problem.ProblemGoal;
import it.uniroma3.epsl2.framework.lang.problem.TemporalProblemConstraint;
import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.DomainComponentConfiguration;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.cfg.PlanDataBaseConfiguration;
import it.uniroma3.epsl2.framework.microkernel.query.ParameterQueryType;
import it.uniroma3.epsl2.framework.microkernel.resolver.Resolver;
import it.uniroma3.epsl2.framework.microkernel.resolver.ResolverType;
import it.uniroma3.epsl2.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.uniroma3.epsl2.framework.parameter.ParameterDataBaseFacadeType;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomain;
import it.uniroma3.epsl2.framework.parameter.lang.ParameterDomainType;
import it.uniroma3.epsl2.framework.parameter.lang.constraints.ParameterConstraint;
import it.uniroma3.epsl2.framework.parameter.lang.query.ComputeSolutionParameterQuery;
import it.uniroma3.epsl2.framework.time.TemporalDataBaseFacadeType;
import it.uniroma3.epsl2.framework.time.lang.TemporalConstraint;
import it.uniroma3.epsl2.framework.time.tn.uncertainty.ex.PseudoControllabilityCheckException;
import it.uniroma3.epsl2.framework.utils.view.component.ComponentViewType;

/**
 * 
 * @author anacleto
 *
 */
@PlanDataBaseConfiguration(
		
		// parameter data manager
		pdb = ParameterDataBaseFacadeType.CSP_PARAMETER_FACADE,
		
		// temporal data manager
		tdb = TemporalDataBaseFacadeType.UNCERTAINTY_TEMPORAL_FACADE
)

@DomainComponentConfiguration(
		
	resolvers = {
			
			// plan refinement resolver
			ResolverType.PLAN_REFINEMENT
	},
	
	view = ComponentViewType.GANTT
)
public class PlanDataBaseComponent extends DomainComponent implements PlanDataBase
{
	// list of observers
	private List<PlanDataBaseObserver> observers;
	
	// see Composite design pattern
	private Map<String, DomainComponent> components;
	private DomainComponentFactory componentFactory;
	
	protected Problem problem;
	
	// domain theory
	private Map<String, ParameterDomain> parameterDomains;
	private Map<DomainComponent, Map<ComponentValue, List<SynchronizationRule>>> rules;
	
	// additional knowledge
	private Map<DomainComponent, Set<DomainComponent>> dg;		// dependency graph (as incident graph on components)
	private Map<ComponentValue, Set<ComponentValue>> tree;		// decomposition tree
	
	/**
	 * 
	 * @param name
	 */
	protected PlanDataBaseComponent(String name) {
		super(name, DomainComponentType.PDB);
		this.observers = new ArrayList<>();
		this.components = new HashMap<>();
		this.parameterDomains = new HashMap<>();
		this.rules = new HashMap<>();
		this.componentFactory = new DomainComponentFactory();
		// initialize additional data
		this.dg = new HashMap<>();
		this.tree = new HashMap<>();
		// initialize problem
		this.problem = null;
	}
	
	/**
	 * 
	 */
	@Override
	public void setup(Problem problem) 
			throws ProblemInitializationException 
	{
		// setup problem
		this.doSetupProblem(problem);
		// analyze synchronization to extract dependencies among components
		this.computeDependencyGraph();
		// print computed dependencies
		String str = "Dependency graph:\n-----------------------------------\n";
		for (DomainComponent key : this.dg.keySet()) {
			str += "- " + key.getName() + ":\n";
			for (DomainComponent target : this.dg.get(key)) {
				str += "\t- " + target.getName() + "\n";
			}
		}
		str += "-----------------------------------";
		// print dependency graph
		this.logger.info(str);
		
		// analyze synchronization to extract the decomposition tree
		this.computeDecompositionTree();
		// print decomposition tree
		str = "Decomposition tree:\n-----------------------------------\n";
		for (ComponentValue val : this.tree.keySet()) {
			str += "- " + val.getLabel() + ":\n";
			for (ComponentValue tar : this.tree.get(val)) {
				str += "\t- " + tar.getLabel() + "\n";
			}
		}
		str += "-----------------------------------";
		// print resulting decomposition tree
		this.logger.info(str);
	}
	
	/**
	 * Get the dependency graph as incident graph on domain components. Each component 
	 * is related to other components it depends on. For example, A -> B means that 
	 * component A is dependent from component B.
	 */
	@Override
	public Map<DomainComponent, Set<DomainComponent>> getDependencyGraph() {
		// get the dependency graph
		return new HashMap<DomainComponent, Set<DomainComponent>>(this.dg);
	}
	
	/**
	 * 
	 */
	@Override
	public Map<ComponentValue, Set<ComponentValue>> getDecompositionTree() {
		// get the decomposition tree
		return new HashMap<ComponentValue, Set<ComponentValue>>(this.tree);
	}
	
	/**
	 * 
	 */
	@Override
	public void subscribe(PlanDataBaseObserver observer) {
		this.observers.add(observer);
	}
	
	/**
	 * 
	 */
	@Override
	public void clear() 
	{
		// remove pending decisions
		for (Decision dec : this.getPendingDecisions()) {
			// delete decision
			this.delete(dec);
		}
		
		// remove active decisions
		for (Decision dec : this.getActiveDecisions()) {
			// delete decision
			this.delete(dec);
		}
		
		// clear problem
		this.problem = null;
	}
	
	/**
	 * 
	 */
	@Override
	public SolutionPlan getSolutionPlan() 
	{
		// create a plan
		SolutionPlan plan = new SolutionPlan(this.name, this.getHorizon());
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
		this.pdb.process(query);
		// get current plan
		return plan;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public Agenda getAgenda() 
	{
		// initialize the agenda
		Agenda agenda = new Agenda();
		// get pending decisions
		for (Decision goal : this.getPendingDecisions()) {
			agenda.add(goal);
		}
		// get pending relations
		for (Relation rel : this.getPendingRelations()) {
			agenda.add(rel);
		}
		
		// get the agenda
		return agenda;
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
	 * Check the temporal consistency of the plan.
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
	public void check() 
			throws ConsistencyCheckException 
	{
		// check temporal consistency of the network
		this.tdb.checkConsistency();
		// check parameter consistency
		this.pdb.checkConsistency();
		// check pseudo-controllability of components
		this.checkPseudoControllability();
	}

	/**
	 * Check components to get information about the specific 
	 * tokens that are not pseudo-controllable
	 * 
	 * @return
	 */
	@Override
	public void checkPseudoControllability() 
			throws PseudoControllabilityCheckException {
		// list of squeezed tokens
		Map<DomainComponent, List<Decision>> squeezed = new HashMap<>();
		// check pseudo-controllability of components
		for (DomainComponent component : this.components.values()) {
			try {
				// check pseudo-controllability
				component.checkPseudoControllability();
			}
			catch (PseudoControllabilityCheckException ex) {
				// get controllability issues
				Map<DomainComponent, List<Decision>> issues = ex.getPseudoControllabilityIssues();
				for (DomainComponent c : issues.keySet()) {
					for (Decision issue : issues.get(c)) {
						// add pseudo-controllability issues
						if (!squeezed.containsKey(c)) {
							squeezed.put(c, new ArrayList<Decision>());
						}
						// add issue
						squeezed.get(c).add(issue);
					}
				}
			}
		}
		
		// check if some issues have been found
		if (!squeezed.isEmpty()) {
			// create exception
			PseudoControllabilityCheckException ex = new PseudoControllabilityCheckException("Some pseudo-controllability issues have been found");
			ex.setPseudoControllabilityIssues(squeezed);
			// throw exception
			throw ex;
		}
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
	public <T extends DomainComponent> T createDomainComponent(String name, DomainComponentType type) {
		// check if a component already exist
		if (this.components.containsKey(name)) {
			throw new RuntimeException("A component with name " + name + " already exists");
		}
		// create component
		T c = this.componentFactory.create(name, type);
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
	 * @param value
	 * @return
	 */
	@Override
	public SynchronizationRule createSynchronizationRule(ComponentValue value, String[] labels) 
			throws DomainComponentNotFoundException {
		// check if related component exists
		if (!this.components.containsKey(value.getComponent().getName())) {
			throw new DomainComponentNotFoundException("Value's component not found " + value);
		}
		// create synchronization rule
		return new SynchronizationRule(value, labels);
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
		// check data
		if (!this.rules.containsKey(value.getComponent())) {
			this.rules.put(value.getComponent(), new HashMap<ComponentValue, List<SynchronizationRule>>());
		}
		if (!this.rules.get(value.getComponent()).containsKey(value)) {
			// initialize
			this.rules.get(value.getComponent()).put(value, new ArrayList<SynchronizationRule>());
		}
		
		// look for cycles
		for (TokenVariable var : rule.getTokenVariables()) {
			// get value 
			ComponentValue v = var.getValue();
			// check if this value is trigger of other synchronizations
			if (this.rules.containsKey(v.getComponent()) && this.rules.get(v.getComponent()).containsKey(v)) {
				// get synchronizations
				List<SynchronizationRule> existingRules = this.rules.get(v.getComponent()).get(v);
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
		this.rules.get(value.getComponent()).get(value).add(rule);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<SynchronizationRule> getSynchronizationRules() {
		// get all rules
		List<SynchronizationRule> list = new ArrayList<>();
		for (DomainComponent comp : this.components.values()) {
			// check if a synchronization has been defined on the component
			if (this.rules.containsKey(comp)) {
				for (ComponentValue v : this.rules.get(comp).keySet()) {
					// add rules
					list.addAll(this.rules.get(comp).get(v));
				}
			}
		}
		
		// get rules
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<SynchronizationRule> getSynchronizationRules(ComponentValue value) {
		// list of rules
		List<SynchronizationRule> rules = new ArrayList<>();
		// check domain specification
		if (this.rules.containsKey(value.getComponent()) && this.rules.get(value.getComponent()).containsKey(value)) {
			rules.addAll(this.rules.get(value.getComponent()).get(value));
		}
		// get rules
		return rules;
	}
	
	/**
	 * 
	 */
	@Override
	public List<SynchronizationRule> getSynchronizationRules(DomainComponent component) {
		// list of rules
		List<SynchronizationRule> rules = new ArrayList<>();
		// check domain specification
		if (this.rules.containsKey(component)) {
			for (ComponentValue value : this.rules.get(component).keySet()) {
				rules.addAll(this.rules.get(component).get(value));
			}
		}
		// get rules
		return rules;
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
	public void restorePendingDecision(Decision dec) {
		// dispatch request to the related component
		dec.getComponent().restorePendingDecision(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public Decision createDecision(ComponentValue value, String[] labels) {
		// get the component the value belongs to
		DomainComponent comp = value.getComponent();
		// create decision
		Decision dec = comp.createDecision(value, labels);
		// get created decision
		return dec;
	}
	
	/**
	 * 
	 */
	@Override
	public Decision createDecision(ComponentValue value, String[] labels, long[] duration) {
		// get the component the value belongs to
		DomainComponent comp = value.getComponent();
		// create decision
		Decision dec = comp.createDecision(value, labels, duration);
		// get created decision
		return dec;
	}
	
	/**
	 * 
	 */
	@Override
	public Decision createDecision(ComponentValue value, String[] labels, long[] end, long[] duration) {
		// get the component the value belongs to
		DomainComponent comp = value.getComponent();
		// create decision
		Decision dec = comp.createDecision(value, labels, end, duration);
		// get created decision
		return dec;
	}
	
	/**
	 * 
	 */
	@Override
	public Decision createDecision(ComponentValue value, String[] labels, long[] start, long[] end, long[] duration) {
		// get the component the value belongs to
		DomainComponent comp = value.getComponent();
		// create decision
		Decision dec = comp.createDecision(value, labels, start, end, duration);
		// get created decision
		return dec;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> addDecision(Decision dec) 
			throws DecisionPropagationException 
	{
		// get the component the decision belongs to
		DomainComponent c = dec.getComponent();
		// add decision and get the list of local  relations propagated
		List<Relation> local = c.addDecision(dec);
		
		// get global pending relations to propagate
		List<Relation> global = new ArrayList<>();
		for (Relation rel : this.getPendingRelations(dec)) {
			// check relations to activate
			if (rel.getReference().equals(dec) && rel.getTarget().isActive() || 
					rel.getTarget().equals(dec) && rel.getReference().isActive() ||
					// check reflexive relations
					rel.getReference().equals(dec) && rel.getTarget().equals(dec)) {
				// add relation to propagate
				global.add(rel);
			}
		}
		
		try {
			// propagate relations
			this.addRelations(global);
		}
		catch (RelationPropagationException ex) {
			// deactivate decision
			c.delete(dec);
			// throw exception
			throw new DecisionPropagationException(ex.getMessage() + "\nError while propagating global relations");
		}
		
		// get the list of local and global relations propagated
		List<Relation> list = new ArrayList<>();
		list.addAll(local);
		list.addAll(global);
		return list;
	}
	
	/**
	 * 
	 * @param dec
	 * @throws Exception
	 */
	@Override
	public void delete(Decision dec) {
		// list of related relations to delete
		List<Relation> toDelete = new ArrayList<>();
		// check decision status
		if (dec.isActive()) {
			// deactivate global decisions
			for (Relation rel : this.relations.get(PlanElementStatus.ACTIVE)) {
				if (rel.getReference().equals(dec) || rel.getTarget().equals(dec)) {
					// delete global active relation
					toDelete.add(rel);
				}
			}
		}
		else {
			// delete pending global decisions
			for (Relation rel : this.relations.get(PlanElementStatus.PENDING)) {
				if (rel.getReference().equals(dec) || rel.getTarget().equals(dec)) {
					// delete global pending relation
					toDelete.add(rel);
				}
			}
		}
		
		// delete relations
		for (Relation rel : toDelete) {
			// delete relation
			this.delete(rel);
		}
		
		// manage locally to the component
		dec.getComponent().delete(dec);
	}
	
	/**
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Relation> T createRelation(RelationType type, Decision reference, Decision target) 
	{
		// pending relation
		T rel = null;
		// check if local relation
		if (reference.getComponent().equals(target.getComponent())) {
			// dispatch request to the related component
			DomainComponent comp = reference.getComponent();
			// create relation
			rel = comp.createRelation(type, reference, target);
		}
		else {
			// global pending relation
			try {
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
		}
		// get pending relation
		return rel;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getPendingRelations() {
		// list of pending relations
		List<Relation> list = new ArrayList<>();
		// get all local pending relations
		for (DomainComponent comp : this.components.values()) {
			list.addAll(comp.getPendingRelations());
		}
		// get all global pending relation
		list.addAll(this.relations.get(PlanElementStatus.PENDING));
		// get list of pending relations
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getActiveRelations() {
		// list of pending relations
		List<Relation> list = new ArrayList<>();
		// get all local pending relations
		for (DomainComponent comp : this.components.values()) {
			list.addAll(comp.getActiveRelations());
		}
		// get all global pending relation
		list.addAll(this.relations.get(PlanElementStatus.ACTIVE));
		// get list of pending relations
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getExistingRelations(Decision reference, Decision target) {
		// list of existing relations
		List<Relation> list = new ArrayList<>();
		for (DomainComponent comp : this.components.values()) {
			// check local relations
			list.addAll(comp.getExistingRelations(reference, target));
		}
		
		// check pending global relations
		for (Relation rel : this.relations.get(PlanElementStatus.PENDING)) {
			if (rel.getReference().equals(reference) && rel.getTarget().equals(target)) {
				list.add(rel);
			}
		}
		
		// check active global relations
		for (Relation rel : this.relations.get(PlanElementStatus.ACTIVE)) {
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
	@Override
	public List<Relation> getPendingRelations(Decision dec) {
		
		// list of pending relations
		List<Relation> list = new ArrayList<>();
		// get local pending relations
		list.addAll(dec.getComponent().getPendingRelations(dec));
		
		// add global pending relation
		for (Relation rel : this.relations.get(PlanElementStatus.PENDING)) {
			// check reference and target
			if (rel.getReference().equals(dec) || rel.getTarget().equals(dec)) {
				// add relation
				list.add(rel);
			}
		}
		// get list of pending relations
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public void addRelation(Relation rel) 
			throws RelationPropagationException 
	{
		// check if local relation
		if (rel.isLocal()) {
			// dispatch request to the component
			DomainComponent comp = rel.getReference().getComponent();
			// add relation
			comp.addRelation(rel);
		}
		else {
			// global relation
			if (!this.relations.get(PlanElementStatus.PENDING).contains(rel)) {
				throw new RuntimeException("Trying to propagate a not pending relation " + rel);
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
							TemporalConstraint constraint = trel.create();
							// propagate constraint
							this.tdb.propagate(constraint);
							// set relation as activated
							this.relations.get(PlanElementStatus.PENDING).remove(rel);
							this.relations.get(PlanElementStatus.ACTIVE).add(rel);
					}
					break;
					
					// parameter constraint
					case PARAMETER_CONSTRAINT : {
						// get parameter relation
						ParameterRelation prel = (ParameterRelation) rel;
							// create constraint
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
				// note that the relation is still "pending"
				throw new RelationPropagationException(ex.getMessage());
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void free(Relation rel) {
		// get decisions
		Decision reference = rel.getReference();
		Decision target = rel.getTarget();
		// check if local relation
		if (reference.getComponent().equals(target.getComponent())) {
			// dispatch request
			DomainComponent comp = reference.getComponent();
			comp.delete(rel);
		}
		else {
			// retract global relation if active
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
	}
	
	/**
	 * 
	 */
	@Override
	public void delete(Relation rel) {
		// get decisions
		Decision reference = rel.getReference();
		Decision target = rel.getTarget();
		// check if local relation
		if (reference.getComponent().equals(target.getComponent())) {
			// dispatch request
			DomainComponent comp = reference.getComponent();
			comp.delete(rel);
		}
		else {
			// global relation
			if (this.relations.get(PlanElementStatus.ACTIVE).contains(rel)) {
				// check relation type
				switch (rel.getCategory()) 
				{
					// temporal constraint
					case TEMPORAL_CONSTRAINT : {
						// get temporal relation
						TemporalRelation trel = (TemporalRelation) rel;
						// retract the related constraint
						TemporalConstraint constraint =  trel.getConstraint();
						this.tdb.retract(constraint);
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
						ParameterConstraint constraint = prel.getConstraint();
						this.pdb.retract(constraint);
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
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public List<Flaw> detectFlaws() 
			throws UnsolvableFlawFoundException {
		
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// call resolvers to detect flaws and possible solutions
		for (Resolver resv : this.resolvers) {
			// add detected flaws
			list.addAll(resv.findFlaws());
		}
		
		// check components
		for (DomainComponent comp : this.components.values()) {
			// detect flaws on component
			list.addAll(comp.detectFlaws());
		}
		
		// get the list of detected flaws in the domain
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public void rollback(FlawSolution solution) { 
		// check if the request must be dispatched to the correct component
		if (this.index.containsKey(solution.getFlaw().getType())) {
			// solve flaw
			this.index.get(solution.getFlaw().getType()).retract(solution);
		}
		else {
			// dispatch flaw
			DomainComponent component = solution.getFlaw().getComponent();
			component.rollback(solution);
		}
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
		// check if the request must be dispatched to the correct component
		if (this.index.containsKey(solution.getFlaw().getType())) {
			// solve flaw
			this.index.get(solution.getFlaw().getType()).apply(solution);
		}
		else {
			// dispatch flaw
			DomainComponent component = solution.getFlaw().getComponent();
			component.commit(solution);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void propagete(FlawSolution solution) 
			throws PlanRefinementException 
	{
		try 
		{
			// apply solution
			this.commit(solution);
			// notify update to observers
			for (PlanDataBaseObserver observer : this.observers) {
				// create event
				PlanDataBaseEvent event = new PlanDataBaseEvent(PlanDataBaseEventType.PROPAGATE, solution);
				// notify event
				observer.notify(event);
			}
		} 
		catch (FlawSolutionApplicationException ex) {
			throw new PlanRefinementException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void retract(FlawSolution solution) 
			throws FlawSolutionApplicationException 
	{
		// roll-back flaw solution
		this.rollback(solution);
		// notify update to observers
		for (PlanDataBaseObserver observer : this.observers) {
			// create event
			PlanDataBaseEvent event = new PlanDataBaseEvent(PlanDataBaseEventType.RETRACT, solution);
			// notify retraction
			observer.notify(event);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		String str = "[PlanDataBase components=\n";
		for (DomainComponent comp : this.components.values()) { 
			str += "\t" + comp.getName() + "\n";
			if (this.rules.containsKey(comp)) {
				str += "\tsynchronization-rules=\n";
				for (SynchronizationRule rule : this.getSynchronizationRules(comp)) {
					str += "\t\t" + rule + "\n";
				}
			}
		}
		str += "]";
		return str;
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
			// initialize incident graph
			this.dg = new HashMap<>();
			// initialize the decomposition tree
			this.tree = new HashMap<>();
			
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
					Decision dec = this.createDecision(
							fact.getValue(), 
							fact.getParameterLabels(), 
							fact.getStart(), 
							fact.getEnd(), 
							fact.getDuration());
					
					// add decision
					this.addDecision(dec);
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
						this.delete(dec);
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
				Decision dec = this.createDecision(
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
							TemporalRelation rel = this.createRelation(constraint.getType(), reference, target);
							rel.setBounds(tc.getBounds());
							
							// check if relation can be activated
							if (reference.isActive() && target.isActive()) {
								// add relation 
								this.addRelation(rel);
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
							ParameterRelation rel = this.createRelation(constraint.getType(), reference, target);
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
							if (reference.isActive() && target.isActive()) {
								// add relation
								this.addRelation(rel);
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
					this.free(rel);
				}
				
				// throw exception
				throw new ProblemInitializationException(ex.getMessage());
			}
			
			try {
				// check unsolvable flaws
				this.detectFlaws();
			} 
			catch (UnsolvableFlawFoundException ex) {
				// unsolvable flaws found
				throw new ProblemInitializationException("Inconsistent Problem description\n- Unsolvable flaws have been found\n" + ex.getMessage());
			}
			
			this.problem = problem;
		}
		else {
			// a problem already exists
			throw new ProblemInitializationException("A problem instace has been already set up... try clear() before setting up a new problem instance");
		}
	}
	
	/**
	 * Compute an acyclic Dependency Graph by analyzing the temporal constraints
	 * of the synchronization rules in the domain specification.
	 * 
	 * The dependency graph represents a relaxed view of the temporal dependencies 
	 * among domain components. Namely, if a cycle exists it is ignored by the
	 * dependency graph representation. Thus, only the sub-set of acyclic relations
	 * are considered for inferring dependencies between components.
	 */
	private void computeDependencyGraph() 
	{
		// initialize the dependency graph
		for (DomainComponent node : this.getComponents()) {
			// initialize DG 
			this.dg.put(node, new HashSet<DomainComponent>());
		}
			
		// check synchronization and build the graph as "incident" matrix
		for (SynchronizationRule rule : this.getSynchronizationRules()) 
		{
			// check constraints
			for (SynchronizationConstraint ruleConstraint : rule.getConstraints()) 
			{
				// consider only temporal constraint to build the dependency graph
				if (ruleConstraint.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT)) 
				{
					// get related token variables
					TokenVariable source = ruleConstraint.getSource();
					TokenVariable target = ruleConstraint.getTarget();
					// check related values' components
					DomainComponent master = source.getValue().getComponent();
					DomainComponent slave = target.getValue().getComponent();
					// check if "external" constraint
					if (!master.equals(slave)) 
					{
						try 
						{
							// update the dependency graph - recall: the dg is an incident graph
							this.dg.get(slave).add(master);
							// check hierarchy cycle
							this.checkHiearchyCycle(slave, master);
						}
						catch (HierarchyCycleException ex) {
							// a cycle into the hierarchy has been found
							this.logger.warning(ex.getMessage() + "\nIgnore dependency relation between component=" + master + " and component=" + slave);
							// remove dependency relation
							this.dg.get(slave).remove(master);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param reference
	 * @param target
	 * @throws HierarchyCycleException
	 */
	private void checkHiearchyCycle(DomainComponent reference, DomainComponent target) 
			throws HierarchyCycleException
	{
		// check direct cycle
		if (this.dg.get(reference).contains(target) &&
				this.dg.get(target).contains(reference)) {
			// cycle detected
			throw new HierarchyCycleException("A direct cycle has been detected in the Dependency Graph between component= " + reference + " and component= " + target);
		}
		else {
			// check paths
			this.findCycle(reference, new HashSet<DomainComponent>());
		}
	}
	
	/**
	 * 
	 * @param comp
	 * @param visited
	 * @throws HierarchyCycleException
	 */
	private void findCycle(DomainComponent comp, Set<DomainComponent> visited) 
			throws HierarchyCycleException
	{
		// add component to visited
		visited.add(comp);
		// check component's successors
		for (DomainComponent next : this.dg.get(comp)) {
			// check if visited
			if (!visited.contains(next)) {
				// recursive call
				this.findCycle(next, new HashSet<DomainComponent>(visited));
			}
			else {
				// throw exception
				throw new HierarchyCycleException("A cycle has been introduced in the Dependency Graph between component= " + comp + " and component= " + next);
			}
		}
	}
	
	/**
	 * Analyze synchronization rule constraints to generate and extract the 
	 * decomposition tree of the domain
	 */
	private void computeDecompositionTree()
	{
		// initialize the decomposition tree
		for (DomainComponent component : this.getComponents()) {
			// check component values
			for (ComponentValue value : component.getValues()) {
				// add entry to the tree
				this.tree.put(value, new HashSet<>());
			}
		}
		
		// get synchronization domains
		for (SynchronizationRule rule : this.getSynchronizationRules()) 
		{
			// get trigger value 
			ComponentValue reference = rule.getTriggerer().getValue();
			// check synchronization target
			for (SynchronizationConstraint constraint : rule.getConstraints())
			{
				// get target value
				ComponentValue target = constraint.getTarget().getValue();
				// avoid reflexive references
				if (!reference.equals(target)) {
					this.tree.get(reference).add(target);
				}
			}
		}
	}
}
