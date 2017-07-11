package it.istc.pst.platinum.framework.domain.component.pdb;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.istc.pst.platinum.deliberative.heuristic.filter.ex.HierarchyCycleException;
import it.istc.pst.platinum.framework.domain.PlanDataBase;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.DomainComponentFactory;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.PlanElementStatus;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.domain.component.ex.FlawSolutionApplicationException;
import it.istc.pst.platinum.framework.domain.component.ex.RelationPropagationException;
import it.istc.pst.platinum.framework.microkernel.ConstraintCategory;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.FrameworkLoggerConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.ParameterFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.deliberative.TemporalFacadeConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.framework.DomainComponentConfiguration;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConsistencyCheckException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ConstraintPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.DomainComponentNotFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.OperatorPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.Flaw;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawSolution;
import it.istc.pst.platinum.framework.microkernel.lang.flaw.FlawType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Decision;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Operator;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Plan;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.parameter.BindParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.parameter.EqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.parameter.NotEqualParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.parameter.ParameterRelation;
import it.istc.pst.platinum.framework.microkernel.lang.plan.relations.temporal.TemporalRelation;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ParameterProblemConstraint;
import it.istc.pst.platinum.framework.microkernel.lang.problem.Problem;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemConstraint;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemFact;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemFluent;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemGoal;
import it.istc.pst.platinum.framework.microkernel.lang.problem.TemporalProblemConstraint;
import it.istc.pst.platinum.framework.microkernel.query.ParameterQueryType;
import it.istc.pst.platinum.framework.microkernel.query.TemporalQueryType;
import it.istc.pst.platinum.framework.microkernel.resolver.Resolver;
import it.istc.pst.platinum.framework.microkernel.resolver.ResolverType;
import it.istc.pst.platinum.framework.microkernel.resolver.ex.UnsolvableFlawFoundException;
import it.istc.pst.platinum.framework.parameter.ParameterFacadeType;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraint;
import it.istc.pst.platinum.framework.parameter.lang.query.ComputeSolutionParameterQuery;
import it.istc.pst.platinum.framework.time.TemporalFacadeType;
import it.istc.pst.platinum.framework.time.lang.TemporalConstraint;
import it.istc.pst.platinum.framework.time.lang.query.ComputeMakespanQuery;
import it.istc.pst.platinum.framework.time.tn.ex.PseudoControllabilityCheckException;
import it.istc.pst.platinum.framework.utils.log.FrameworkLoggingLevel;

/**
 * 
 * @author anacleto
 *
 */
public class PlanDataBaseComponent extends DomainComponent implements PlanDataBase
{
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
	@TemporalFacadeConfiguration(facade = TemporalFacadeType.UNCERTAINTY_TEMPORAL_FACADE)
	@ParameterFacadeConfiguration(facade = ParameterFacadeType.CSP_PARAMETER_FACADE)
	@FrameworkLoggerConfiguration(level = FrameworkLoggingLevel.DEBUG)
	@DomainComponentConfiguration(resolvers = { 
			// plan refinement resolver
			ResolverType.PLAN_REFINEMENT 
	})
	protected PlanDataBaseComponent(String name) 
	{
		super(name, DomainComponentType.PDB);
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
	public void clear() 
	{
		// remove all active relations
		for (Relation rel : this.getActiveRelations()) {
			this.delete(rel);
		}
		
		// delete all active decisions
		for (Decision dec : this.getActiveDecisions()) {
			this.delete(dec);
		}

		// clear components
		for (DomainComponent component : this.components.values()) {
			// clear component
			component.clear();
		}

		// clear global relations
		this.relations.clear();
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
	public List<Relation> getPendingRelations() 
	{
		// list of relations
		List<Relation> list = new ArrayList<>();
		// get global relations
		for (Relation rel : this.relations) {
			if (this.isPending(rel)) {
				list.add(rel);
			}
		}
		
		// get relations from components
		for (DomainComponent component : this.components.values()) {
			list.addAll(component.getPendingRelations());
		}
		
		// get list
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getPendingRelations(Decision dec) 
	{
		// list of relations
		List<Relation> list = new ArrayList<>();
		// get global relations
		for (Relation rel : this.relations) {
			if ((dec.equals(rel.getReference()) || dec.equals(rel.getTarget())) && this.isPending(rel)) {
				list.add(rel);
			}
		}
		
		// get local relations from component
		DomainComponent component = dec.getComponent();
		list.addAll(component.getPendingRelations(dec));
		
		// get list
		return list;
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
	public Set<Relation> add(Decision dec) 
			throws DecisionPropagationException 
	{
		// get the component the decision belongs to
		DomainComponent c = dec.getComponent();
		// add decision and get the list of local relations propagated
		Set<Relation> local = c.add(dec);
		
		// get global relations to activate
		Set<Relation> global = new HashSet<>();
		for (Relation rel : this.relations) 
		{
			// get reference
			Decision reference = rel.getReference();
			// get target
			Decision target = rel.getTarget();
			// check status of reference and target decisions
			if ((dec.equals(reference) && this.isActive(target)) || 
					(dec.equals(target) && this.isActive(reference)) && 
					rel.getConstraint() == null)
			{
				// add pending relation
				global.add(rel);
			}
		}
		
		try 
		{
			// propagate relations
			this.add(global);
		}
		catch (RelationPropagationException ex) 
		{
			// deactivate local relations
			for (Relation rel : local) {
				c.delete(rel);
			}
			// deactivate decision
			c.delete(dec);
			// throw exception
			throw new DecisionPropagationException(ex.getMessage() + "\nError while propagating global relations");
		}
		
		// get the list of local and global relations propagated
		Set<Relation> set = new HashSet<>(local);
		set.addAll(global);
		// get relations
		return set;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getRelations(Decision dec) 
	{
		// list of relations concerning the decision
		List<Relation> list = new ArrayList<>();
		// add local relations
		list.addAll(dec.getComponent().getRelations(dec));
		// check global relations
		for (Relation rel : this.relations) {
			// get reference 
			Decision reference = rel.getReference();
			Decision target = rel.getTarget();
			if (dec.equals(reference) || dec.equals(target)) {
				list.add(rel);
			}
		}
		// get list
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getRelations() {
		// list of relations
		List<Relation> list = new ArrayList<>(this.relations);
		// add local relations
		for (DomainComponent component : this.components.values()) {
			// add local relations
			list.addAll(component.getRelations());
		}
		// get the list
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getActiveRelations() 
	{
		// list of active decisions 
		List<Relation> list = new ArrayList<>();
		// check global relations
		for (Relation rel : this.relations) {
			// check if active relation
			if (this.isActive(rel)) {
				list.add(rel);
			}
		}
		
		// add local active decisions
		for (DomainComponent component : this.components.values()) {
			// get component active relations
			list.addAll(component.getActiveRelations());
		}
		
		// get list of active relations
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getActiveRelations(Decision dec) 
	{
		// list of active relations
		List<Relation> list = new ArrayList<>();
		// check global relations
		for (Relation rel : this.relations)
		{
			// get reference
			Decision reference = rel.getReference();
			// get target 
			Decision target = rel.getTarget();
			// check decisions and relation status
			if ((dec.equals(reference) || dec.equals(target)) && this.isActive(rel)) {
				// add relation
				list.add(rel);
			}
		}
		
		// get local active relations
		DomainComponent component = dec.getComponent();
		// add local relations
		list.addAll(component.getActiveRelations(dec));
		
		// get list
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Relation> getToActivateRelations(Decision dec) 
	{
		// list of relations
		List<Relation> list = new ArrayList<>();
		// check global relations
		for (Relation rel : this.relations) {
			// check decisions and relation status
			if (this.isToActivate(rel) && (dec.equals(rel.getReference()) || dec.equals(rel.getTarget()))) {
				// activate relation
				list.add(rel);
			}
		}
		
		// add local relations
		DomainComponent component = dec.getComponent();
		list.addAll(component.getToActivateRelations(dec));
		// get list of to activate relations
		return list;
	}
	
	/**
	 * 
	 * @param dec
	 * @throws Exception
	 */
	@Override
	public void delete(Decision dec) 
	{
		// check if active
		if (this.isActive(dec))
		{
			// delete global relations involving decision
			for (Relation rel : this.relations)
			{
				// get reference
				Decision reference = rel.getReference();
				// get target 
				Decision target = rel.getTarget();
				// check decisions and relation status
				if ((dec.equals(reference) || dec.equals(target)) && this.isActive(rel)) 
				{
					// delete (deactivate) global relation
					this.delete(rel);
				}
			}
		}
		
		// get component decision
		DomainComponent component = dec.getComponent();
		// delete decision from component
		component.delete(dec);
	}
	
	/**
	 * 
	 * @param relation
	 */
	public void free(Relation relation) 
	{
		// check if local
		if (relation.isLocal()) {
			// get component
			DomainComponent component = relation.getReference().getComponent();
			component.free(relation);
		}
		else {
			// check if global relation exists
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
	}
	
//	/**
//	 * 
//	 */
//	@Override
//	public String printSilentPlan() {
//		String str = "";
//		str += "- decisions= " + this.getSilentDecisions() + "\n";
//		str += "- relations= " + this.getSilentRelations() + "\n";
//		return str;
//	}
	
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
	public List<Relation> getSilentRelations() {
		List<Relation> list = new ArrayList<>();
		for (DomainComponent component : this.components.values()) {
			list.addAll(component.getSilentRelations());
		}
		// add global relations
		for (Relation rel : this.relations) {
			if (this.isSilent(rel)) {
				list.add(rel);
			}
		}
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Relation> T create(RelationType type, Decision reference, Decision target) 
	{
		// pending relation
		T rel = null;
		// check if local relation
		if (reference.getComponent().equals(target.getComponent())) {
			// dispatch request to the related component
			DomainComponent comp = reference.getComponent();
			// create relation
			rel = comp.create(type, reference, target);
		}
		else 
		{
			// global pending relation
			try 
			{
				Class<T> clazz = (Class<T>) Class.forName(type.getRelationClassName());
				// get constructor
				Constructor<T> c = clazz.getDeclaredConstructor(Decision.class, Decision.class);
				c.setAccessible(true);
				// create instance
				rel = c.newInstance(reference, target);
				// add global relation
				this.relations.add(rel);
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
	public boolean isActive(Decision dec) {
		return dec.getComponent().isActive(dec);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isActive(Relation rel) 
	{
		// flag
		boolean active = false;
		// check if local
		if (rel.isLocal()) {
			// get component 
			DomainComponent comp = rel.getReference().getComponent();
			active = comp.isActive(rel);
		}
		else 
		{
			// get reference
			Decision reference = rel.getReference();
			// get target
			Decision target = rel.getTarget();
			// check condition
			active = reference.getComponent().isActive(reference) && 
					target.getComponent().isActive(target) && 
					rel.getConstraint() != null;
		}
		
		// get result
		return active;
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
	public boolean isPending(Relation rel) 
	{
		// flag
		boolean pending = false;
		// check if local
		if (rel.isLocal()) {
			// get component 
			DomainComponent comp = rel.getReference().getComponent();
			pending = comp.isPending(rel);
		}
		else 
		{
			// get reference
			Decision reference = rel.getReference();
			// get target
			Decision target = rel.getTarget();
			// check condition
			pending = (reference.getComponent().isPending(reference) || target.getComponent().isPending(target)) && 
					!(reference.getComponent().isSilent(reference) || target.getComponent().isSilent(target)) && 
					rel.getConstraint() == null;
		}
		
		// get result
		return pending;
	}
	
	/**
	 * 
	 */
	public boolean isToActivate(Relation rel)
	{
		// flag
		boolean toActivate = false;
		// check if local
		if (rel.isLocal()) {
			// get component 
			DomainComponent comp = rel.getReference().getComponent();
			toActivate = comp.isToActivate(rel);
		}
		else 
		{
			// get reference
			Decision reference = rel.getReference();
			// get target
			Decision target = rel.getTarget();
			// check condition
			toActivate = this.isActive(reference) && this.isActive(target) && rel.getConstraint() == null;
		}
		
		// get result
		return toActivate;
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
	public boolean isSilent(Relation rel) {
		// flag
		boolean active = false;
		// check if local
		if (rel.isLocal()) {
			// get component 
			DomainComponent comp = rel.getReference().getComponent();
			active = comp.isActive(rel);
		}
		else 
		{
			// get reference
			Decision reference = rel.getReference();
			// get target
			Decision target = rel.getTarget();
			// check condition
			active = (reference.getComponent().isSilent(reference) || target.getComponent().isSilent(target)) && 
					rel.getConstraint() == null;
		}
		
		// get result
		return active;
	}
	
	/**
	 * 
	 */
	@Override
	public void add(Relation rel) 
			throws RelationPropagationException 
	{
		// check if local relation
		if (rel.isLocal()) {
			// dispatch request to the component
			DomainComponent comp = rel.getReference().getComponent();
			// add relation
			comp.add(rel);
		}
		else 
		{
			try 
			{
				if (!this.isActive(rel.getReference()) || !this.isActive(rel.getTarget())) {
					// not active decisions
					throw new RelationPropagationException("Trying to propagate global relation between not active decisions\n"
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
							TemporalConstraint constraint = trel.create();
							// propagate constraint
							this.tdb.propagate(constraint);
						}
						break;
						
						// parameter constraint
						case PARAMETER_CONSTRAINT : 
						{
							// get parameter relation
							ParameterRelation prel = (ParameterRelation) rel;
							// create constraint
							ParameterConstraint constraint = prel.create();
							// propagate constraint
							this.pdb.propagate(constraint);
						}
						break;
					}
				}
				else {
					// already propagated constraint
					this.logger.debug("Already propagated global relation\n- " + rel + "\n");
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
	public void delete(Relation rel) 
	{
		// check if local
		if (rel.isLocal()) {
			// forward request to component
			DomainComponent component = rel.getReference().getComponent();
			component.delete(rel);
		}
		else 
		{
			// check global relation
			if (!this.relations.contains(rel)) {
				// relation not found
				this.logger.warning("Global relation not found\n- relation= " + rel + "\n");
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
						TemporalConstraint constraint =  trel.getConstraint();
						this.tdb.retract(constraint);
						trel.clear();
					}
					break;
					
					// parameter constraint
					case PARAMETER_CONSTRAINT : 
					{
						// get parameter relation
						ParameterRelation prel = (ParameterRelation) rel;
						// retract the related constraint
						ParameterConstraint constraint = prel.getConstraint();
						this.pdb.retract(constraint);
						prel.clear();
					}
					break;
				}
			}
			else {
				// deleting a not propagated constraint
				this.logger.warning("Trying to delete a not propagated global relation\n- relation= " + rel + "\n");
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
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
	public List<Flaw> detectFlaws(FlawType type) 
			throws UnsolvableFlawFoundException 
	{
		// list of flaws to solve
		List<Flaw> list = new ArrayList<>();
		// get resolver capable to handle the desired set of flaws, if any
		if (this.flawType2resolver.containsKey(type))
		{
			// get related resolver and detect flaws
			list.addAll(this.flawType2resolver.get(type).findFlaws());
		}
		
		// check components
		for (DomainComponent comp : this.components.values()) {
			// detect flaws on component
			list.addAll(comp.detectFlaws(type));
		}
		
		// get the list of detected flaws
		return list;
	}
	
	/**
	 * 
	 */
	@Override
	public void rollback(FlawSolution solution) 
	{ 
		// check if the request must be dispatched to the correct component
		if (this.flawType2resolver.containsKey(solution.getFlaw().getType())) {
			// solve flaw
			this.flawType2resolver.get(solution.getFlaw().getType()).retract(solution);
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
		if (this.flawType2resolver.containsKey(solution.getFlaw().getType())) {
			// solve flaw
			this.flawType2resolver.get(solution.getFlaw().getType()).apply(solution);
		}
		else {
			// dispatch flaw
			DomainComponent component = solution.getFlaw().getComponent();
			component.commit(solution);
		}
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
		// check if the request must be dispatched to the correct component
		if (this.flawType2resolver.containsKey(solution.getFlaw().getType())) {
			// solve flaw
			this.flawType2resolver.get(solution.getFlaw().getType()).restore(solution);
		}
		else {
			// dispatch flaw
			DomainComponent component = solution.getFlaw().getComponent();
			component.restore(solution);
		}
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
//				// compute the resulting makespan
//				double makespan = this.computeMakespan();
//				// get resulting agenda
//				Agenda agenda = this.getAgenda();
//				// set makespan
//				operator.setMakespan(makespan);
//				// set agenda
//				operator.setAgenda(agenda);
			}
			catch (FlawSolutionApplicationException ex) {
				// error while applying flaw solution
				this.logger.warning(ex.getMessage());
				throw new OperatorPropagationException("Error while propagating operator:\n- " + operator + "\n");
			}
		}
		else
		{
			try
			{
				// simply restore flaw solution
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
	
	/**
	 * 
	 */
	@Override
	public double computeMakespan() 
	{
		// query the temporal facade
		ComputeMakespanQuery query = this.tdb.createTemporalQuery(TemporalQueryType.COMPUTE_MAKESPAN);
		// process query
		this.tdb.process(query);
		// get computed value
		return query.getMakespan();
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
					Decision dec = this.create(
							fact.getValue(), 
							fact.getParameterLabels(), 
							fact.getStart(), 
							fact.getEnd(), 
							fact.getDuration());
					
					// add decision
					this.add(dec);
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
							if (this.isToActivate(rel)) {
								// add relation 
								this.add(rel);
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
							if (this.isToActivate(rel)) {
								// add relation
								this.add(rel);
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
					this.delete(rel);
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
