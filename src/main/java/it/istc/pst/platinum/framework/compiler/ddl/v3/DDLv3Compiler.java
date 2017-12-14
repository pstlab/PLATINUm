package it.istc.pst.platinum.framework.compiler.ddl.v3;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;

import it.istc.pst.ddl.v3.parser.DDLComponent;
import it.istc.pst.ddl.v3.parser.DDLComponentDecision;
import it.istc.pst.ddl.v3.parser.DDLComponentType;
import it.istc.pst.ddl.v3.parser.DDLConsumableResourceComponentDecision;
import it.istc.pst.ddl.v3.parser.DDLConsumableResourceComponentType;
import it.istc.pst.ddl.v3.parser.DDLDomain;
import it.istc.pst.ddl.v3.parser.DDLEnumerationParameterConstraint;
import it.istc.pst.ddl.v3.parser.DDLEnumerationParameterConstraint.DDLEnumerationParameterConstraintType;
import it.istc.pst.ddl.v3.parser.DDLEnumerationParameterType;
import it.istc.pst.ddl.v3.parser.DDLInstantiatedComponentDecision;
import it.istc.pst.ddl.v3.parser.DDLNumericParameterConstraint;
import it.istc.pst.ddl.v3.parser.DDLNumericParameterConstraint.DDLNumericParameterConstraintType;
import it.istc.pst.ddl.v3.parser.DDLNumericParameterType;
import it.istc.pst.ddl.v3.parser.DDLParameterConstraint;
import it.istc.pst.ddl.v3.parser.DDLParameterType;
import it.istc.pst.ddl.v3.parser.DDLProblem;
import it.istc.pst.ddl.v3.parser.DDLRenewableResourceComponentDecision;
import it.istc.pst.ddl.v3.parser.DDLRenewableResourceComponentType;
import it.istc.pst.ddl.v3.parser.DDLSimpleGroundStateVariableComponentDecision;
import it.istc.pst.ddl.v3.parser.DDLSingletonStateVariableComponentDecision;
import it.istc.pst.ddl.v3.parser.DDLSingletonStateVariableComponentDecisionType;
import it.istc.pst.ddl.v3.parser.DDLSingletonStateVariableComponentType;
import it.istc.pst.ddl.v3.parser.DDLSingletonStateVariableTransitionConstraint;
import it.istc.pst.ddl.v3.parser.DDLSynchronization;
import it.istc.pst.ddl.v3.parser.DDLTemporalRelation;
import it.istc.pst.ddl.v3.parser.DDLTemporalRelationType;
import it.istc.pst.ddl.v3.parser.DDLTimeline;
import it.istc.pst.ddl.v3.parser.DDLTimelineSynchronization;
import it.istc.pst.ddl.v3.parser.ddl3Lexer;
import it.istc.pst.ddl.v3.parser.ddl3Parser;
import it.istc.pst.platinum.framework.compiler.DomainCompiler;
import it.istc.pst.platinum.framework.compiler.DomainCompilerType;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.pdb.PlanDataBase;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationRule;
import it.istc.pst.platinum.framework.domain.component.pdb.TokenVariable;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.DiscreteResource;
import it.istc.pst.platinum.framework.domain.component.resource.discrete.RequirementResourceValue;
import it.istc.pst.platinum.framework.domain.component.resource.reservoir.ReservoirResource;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariable;
import it.istc.pst.platinum.framework.domain.component.sv.StateVariableValue;
import it.istc.pst.platinum.framework.domain.component.sv.Transition;
import it.istc.pst.platinum.framework.microkernel.lang.ex.DomainComponentNotFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.problem.Problem;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemFact;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemFluent;
import it.istc.pst.platinum.framework.microkernel.lang.problem.ProblemGoal;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.parameter.lang.EnumerationParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.NumericParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;
import it.istc.pst.platinum.framework.parameter.lang.constraints.ParameterConstraintType;

/**
 * 
 * @author anacleto
 *
 */
public class DDLv3Compiler extends DomainCompiler 
{
	private long origin;
	private long horizon;
	
	protected DDLDomain ddl_domain;
    protected DDLProblem ddl_problem;
	
	/**
	 * 
	 * @param ddlFilePath
	 * @param pdlFilePath
	 */
	protected DDLv3Compiler(String ddlFilePath, String pdlFilePath) {
		super(DomainCompilerType.DDLv3, ddlFilePath, pdlFilePath);
	}
	
	/**
	 * 
	 * @param ddlFilePath
	 */
	protected DDLv3Compiler(String ddlFilePath) {
		super(DomainCompilerType.DDLv3, ddlFilePath);
	}
	
	/**
     * 
     * @return
     * @throws SynchronizationCycleException
     */
	@Override
    public PlanDataBase compileDomain() 
    		throws SynchronizationCycleException 
	{
		try 
		{
    		// parse domain
    		this.doParseDDLFile(this.ddlFilePath);
    	} 
    	catch (IOException | RecognitionException ex) {
    		throw new RuntimeException(ex.getMessage());
    	}
    	
		// actually compile domain
    	return this.doCompileDomain();
    }
	
    /**
     * 
     */
	@Override
    public Problem compileProblem(PlanDataBase pdb) 
	{
		try 
		{
    		// parse problem
	    	this.doParseDDLFile(this.pdlFilePath);
    	} 
    	catch (IOException | RecognitionException ex) {
    		throw new RuntimeException(ex.getMessage());
    	}
    	
    	// actually compile problem
    	return this.doCompileProblem(pdb);
    }
	
    /**
     * 
     * @param ddlFilePath
     * @throws IOException
     * @throws RecognitionException
     */
    private void doParseDDLFile(String ddlFilePath) 
    		throws IOException, RecognitionException 
    {
    	// parse domain
    	CharStream dcs = new ANTLRFileStream(ddlFilePath);
		ddl3Lexer lexer = new ddl3Lexer(dcs);
		CommonTokenStream tokens = new CommonTokenStream();
		tokens.setTokenSource(lexer);
		ddl3Parser parser = new ddl3Parser(tokens);
		final ddl3Parser.ddl_return parserResult = parser.ddl();
		Tree ddl = (Tree) parserResult.getTree();
		if (ddl.getChild(0) instanceof DDLDomain) 
		{
		    this.ddl_domain = (DDLDomain) ddl.getChild(0);
		    this.ddl_domain.parse();
		    
		    // parse problem specification
		    if (ddl.getChild(1) instanceof DDLProblem) 
		    {
				this.ddl_problem = (DDLProblem) ddl.getChild(1);
				this.ddl_problem.parse();
		    } 
		    else {
		    	// error while parsing
		    	this.ddl_problem = null;
		    }
		} 
		else 
		{
		    this.ddl_problem = (DDLProblem) ddl.getChild(0);
		    this.ddl_problem.parse();
		    this.ddl_domain = null;
		}
    }

	/**
	 * 
	 */
	private PlanDataBase doCompileDomain() 
			throws SynchronizationCycleException 
	{
		// get parameter
		this.origin = this.ddl_domain.getTemporalModule().getRange().getMin();
		this.horizon = this.ddl_domain.getTemporalModule().getRange().getMax();
		String name = this.ddl_domain.getName();
		
		// create plan data-base
		PlanDataBase pdb = PlanDataBaseBuilder.createAndSet(name, this.origin, this.horizon);
		
		// add parameter domain declaration
		this.addParameterDomains(pdb);
		
		// add data
		this.addComponents(pdb);
		
		// add synchronization rules
		this.addSynchronizationRules(pdb);
		
		// get created plan data-base
		return pdb;
	}
	
	/**
	 * 
	 */
	private Problem doCompileProblem(PlanDataBase pdb) {
		// create problem
		Problem problem = new Problem();
		
		// local cache
		Map<String, ProblemGoal> goals = new HashMap<>();
		Map<String, ProblemFluent> label2fluent = new HashMap<>();
		
		
		// check problem decisions
		for (DDLInstantiatedComponentDecision ddlDec : this.ddl_problem.getComponentDecisions().values()) {
			// get related component
			DomainComponent comp = pdb.getComponentByName(ddlDec.getComponent());
			
			// get related value
			ComponentValue val = comp.getValueByName(ddlDec.getComponentDecision().getName());

			// check if component is external
			if (comp.isExternal()) {
				// add observation
				ProblemFact fact = this.addObservationToProblem(problem, val, ddlDec);
				for (String label : fact.getParameterLabels()) {
					label2fluent.put(label, fact);
				}
			}
			else {
				// check type of decision
				if (ddlDec.getParameters().contains("fact")) {
					// add fact to problem
					ProblemFact fact = this.addFactToProblem(problem, val, ddlDec);
					for (String label : fact.getParameterLabels()) {
						label2fluent.put(label, fact);
					}
				}
				else if (ddlDec.getParameters().contains("goal")) {
					// add goal to problem
					ProblemGoal goal = this.addGoalToProblem(problem, val, ddlDec);
					goals.put(ddlDec.getId(), goal);
					for (String label : goal.getParameterLabels()) {
						label2fluent.put(label, goal);
					}
				}
				else {
					throw new RuntimeException("The decision " + ddlDec + " must be a fact or a goal");
				}
			}
		}
		
		// add constraints between goals
		for (DDLTemporalRelation ddlRel : this.ddl_problem.getTemporalRelations()) {
			// get related goals
			ProblemGoal reference = goals.get(ddlRel.getFrom());
			ProblemGoal target = goals.get(ddlRel.getTo());
			// add constraint between goals
			this.addBinaryGoal(problem, reference, target, ddlRel.getRelationType());
		}
		
		// add parameter constraints
		for (DDLParameterConstraint ddlRel : this.ddl_problem.getParameterConstraints()) {
			// check type
			switch (ddlRel.getConstraintType()) {
				// enumeration constraint
				case ENUMERATION : {
					// get enumeration
					DDLEnumerationParameterConstraint ddlpc = (DDLEnumerationParameterConstraint) ddlRel;
					// get related fluent
					ProblemFluent fluent = label2fluent.get(ddlpc.getLeftTerm());
					// check relation type
					if (ddlpc.getEnumerationConstraintType().equals(DDLEnumerationParameterConstraintType.NEQ)) {
						// FIXME : <----- IMPLEMENTARE
						throw new RuntimeException("Unknown enumeration constraint type " + ddlpc.getEnumerationConstraintType());
					}
					
					// add binding parameter constraint
					problem.addBindingParameterConstraint(fluent, ddlpc.getLeftTerm(), ddlpc.getValue());
				}
				break;
					
				// numeric constraint
				case NUMERIC : {
					// get numeric
					DDLNumericParameterConstraint ddlpc = (DDLNumericParameterConstraint) ddlRel;
					// get reference
					ProblemFluent reference = label2fluent.get(ddlpc.getLeftTerm());
					String referenceLabel = ddlpc.getLeftTerm();
					
					// check if binary constraint
					if (ddlpc.getRightVariables() != null && ddlpc.getRightVariables().get(0) != null) {
						// get relation type
						RelationType relType;
						switch (ddlpc.getNumericConstraintType()) {
							case EQ : {
								// set relation type
								relType = RelationType.EQUAL_PARAMETER;
							}
							break;
						
							case NEQ : {
								// set relation type
								relType = RelationType.NOT_EQUAL_PARAMETER;
							}
							break;
							
							default : {
								// FIXME <<<<<---- IMPLEMENTARE
								throw new RuntimeException("Unknown numeric constraint type " + ddlpc.getNumericConstraintType());
							}
						}
						
						// get targets
						for (String targetLabel : ddlpc.getRightVariables()) {
							// get target fluent
							ProblemFluent target = label2fluent.get(targetLabel);
							// create constraint
							problem.addParameterConstraint(relType, reference, referenceLabel, target, targetLabel);
						}
					}
					else {
						// check relation type
						if (ddlpc.getNumericConstraintType().equals(DDLNumericParameterConstraintType.NEQ)) {
							// FIXME : <<<<<<<<------ IMPLEMENTARE
							throw new RuntimeException("Unknown numeric constraint type " + ddlpc.getNumericConstraintType());
						}
						
						// add constraint
						problem.addBindingParameterConstraint(reference, referenceLabel, Integer.toString(ddlpc.getRightCoefficients().get(0)));
					}
				}
				break;
			}
		}
		
		// get created problem
		return problem;
	}
	
	/**
	 * 
	 * @param problem
	 * @param val
	 * @param ddlDec
	 * @return
	 */
	private ProblemGoal addGoalToProblem(Problem problem, ComponentValue val, DDLInstantiatedComponentDecision ddlDec) {
		// set start bound
		long[] start = new long[] {this.origin, this.horizon};
		// check start bounds
		if (ddlDec.getStart() != null) {
			start = new long[] {
				ddlDec.getStart().getMin(),
				ddlDec.getStart().getMax() > this.horizon ? 
						this.horizon : 
						ddlDec.getStart().getMax()
			};
		}
		
		// set end bound 
		long[] end = new long[] {this.origin, this.horizon};
		if (ddlDec.getEnd() != null) {
			end = new long[] {
					ddlDec.getEnd().getMin(),
					ddlDec.getEnd().getMax() > this.horizon ? 
							this.horizon : 
							ddlDec.getEnd().getMax()
				};
		}
		
		// parameter labels
		String[] labels = new String[] {};
		if (ddlDec.getComponentDecision() instanceof DDLSingletonStateVariableComponentDecision) {
			// get decision
			DDLSingletonStateVariableComponentDecision ddlSingleton = (DDLSingletonStateVariableComponentDecision) ddlDec.getComponentDecision();
			labels = ddlSingleton.getParameterNames().toArray(new String[ddlSingleton.getParameterNames().size()]);
		}
		// add goal
		return problem.addGoal(val, labels, start, end);
	}

	/**
	 * 
	 * @param problem
	 * @param val
	 * @param ddlDec
	 * @return
	 */
	private ProblemFact addFactToProblem(Problem problem, ComponentValue val, DDLInstantiatedComponentDecision ddlDec) {
		// set start bound
		long[] start = new long[] {this.origin, this.horizon};
		// check start bounds
		if (ddlDec.getStart() != null) {
			start = new long[] {
				ddlDec.getStart().getMin(),
				ddlDec.getStart().getMax() > this.horizon ? 
						this.horizon : 
						ddlDec.getStart().getMax()
			};
		}
		
		// set end bound 
		long[] end = new long[] {this.origin, this.horizon};
		if (ddlDec.getEnd() != null) {
			end = new long[] {
					ddlDec.getEnd().getMin(),
					ddlDec.getEnd().getMax() > this.horizon ? 
							this.horizon : 
							ddlDec.getEnd().getMax()
				};
		}
		
		// parameter labels
		String[] labels = new String[] {};
		if (ddlDec.getComponentDecision() instanceof DDLSingletonStateVariableComponentDecision) {
			// get decision
			DDLSingletonStateVariableComponentDecision ddlSingleton = (DDLSingletonStateVariableComponentDecision) ddlDec.getComponentDecision();
			labels = ddlSingleton.getParameterNames().toArray(new String[ddlSingleton.getParameterNames().size()]);
		}
		
		// add a fact
		return problem.addFact(val, labels, start, end);
	}

	/**
	 * 
	 * @param problem
	 * @param val
	 * @param ddlDec
	 * @return
	 */
	private ProblemFact addObservationToProblem(Problem problem, ComponentValue val, DDLInstantiatedComponentDecision ddlDec) 
	{
		// set start bound
		long[] start = new long[] {this.origin, this.horizon};
		// check start bounds
		if (ddlDec.getStart() != null) {
			start = new long[] {
				ddlDec.getStart().getMin(),
				ddlDec.getStart().getMax() > this.horizon ? 
						this.horizon : 
						ddlDec.getStart().getMax()
			};
		}
		
		// set end bound 
		long[] end = new long[] {this.origin, this.horizon};
		if (ddlDec.getEnd() != null) {
			end = new long[] {
					ddlDec.getEnd().getMin(),
					ddlDec.getEnd().getMax() > this.horizon ? 
							this.horizon : 
							ddlDec.getEnd().getMax()
				};
		}
		
		// set duration bound
		long[] duration = new long[] {1, this.horizon};
		if (ddlDec.getDuration() != null) {
			duration = new long[] {
					ddlDec.getDuration().getMin(),
					ddlDec.getDuration().getMax() > this.horizon ? 
							this.horizon : 
							ddlDec.getDuration().getMax()
				};
		}
		
		// parameter labels
		String[] labels = new String[] {};
		if (ddlDec.getComponentDecision() instanceof DDLSingletonStateVariableComponentDecision) {
			// get decision
			DDLSingletonStateVariableComponentDecision ddlSingleton = (DDLSingletonStateVariableComponentDecision) ddlDec.getComponentDecision();
			labels = ddlSingleton.getParameterNames().toArray(new String[ddlSingleton.getParameterNames().size()]);
		}
		// add observation
		return problem.addObservation(val, labels, start, end, duration);
	}

	/**
	 * 
	 * @param problem
	 * @param reference
	 * @param target
	 * @param ddlRelType
	 */
	private void addBinaryGoal(Problem problem, ProblemGoal reference, ProblemGoal target, DDLTemporalRelationType ddlRelType) {
		// check relation type
		if (ddlRelType.getText().equalsIgnoreCase("meets")) {
			// create meets constraint
			problem.addTemporalConstraint(RelationType.MEETS, reference, target, new long[][] {});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("met-by")) {
			// create met-by constraint
			problem.addTemporalConstraint(RelationType.MET_BY, reference, target, new long[][] {});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("during")) { 
			// create during constraint
			problem.addTemporalConstraint(RelationType.DURING, reference, target, 
				new long[][] {
					new long[] {
						ddlRelType.getFirstRange().getMin(),
						ddlRelType.getFirstRange().getMax() > this.horizon ? 
								this.horizon :
								ddlRelType.getFirstRange().getMax()
					},
					new long[] {
						ddlRelType.getSecondRange().getMin(),
						ddlRelType.getSecondRange().getMax() > this.horizon ? 
								this.horizon :
								ddlRelType.getSecondRange().getMax()
					}
				});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("equals")) {
			// create equals constraint
			problem.addTemporalConstraint(RelationType.EQUALS, reference, target, new long[][] {});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("contains")) {
			// create contains constraint
			problem.addTemporalConstraint(RelationType.CONTAINS, reference, target, 
				new long[][] {
					new long[] {
						ddlRelType.getFirstRange().getMin(),
						ddlRelType.getFirstRange().getMax() > this.horizon ? 
								this.horizon : 
								ddlRelType.getFirstRange().getMax()
					},
					new long[] {
						ddlRelType.getSecondRange().getMin(),
						ddlRelType.getSecondRange().getMax() > this.horizon ?
								this.horizon : 
								ddlRelType.getSecondRange().getMax()
					}
				});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("before")) {
			problem.addTemporalConstraint(RelationType.BEFORE, reference, target, 
				new long[][] {
					new long[] {
						ddlRelType.getFirstRange().getMin(),
						ddlRelType.getFirstRange().getMax() > this.horizon ? 
								this.horizon :
								ddlRelType.getFirstRange().getMax()
					}
				});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("after")) {
			problem.addTemporalConstraint(RelationType.AFTER, reference, target, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(),
							ddlRelType.getFirstRange().getMax() > this.horizon ? 
									this.horizon :
									ddlRelType.getFirstRange().getMax()
						}
					});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("starts-during")) {
			problem.addTemporalConstraint(RelationType.STARTS_DURING, reference, target, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(),
							ddlRelType.getFirstRange().getMax() > this.horizon ? 
									this.horizon :
									ddlRelType.getFirstRange().getMax()
						},
						new long[] {
							ddlRelType.getSecondRange().getMin(),
							ddlRelType.getSecondRange().getMax() > this.horizon ? 
									this.horizon : 
									ddlRelType.getSecondRange().getMax()
						}
					});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("ends-during")) {
			problem.addTemporalConstraint(RelationType.ENDS_DURING, reference, target, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(),
							ddlRelType.getFirstRange().getMax() > this.horizon ? 
									this.horizon :
									ddlRelType.getFirstRange().getMax()
						},
						new long[] {
								ddlRelType.getSecondRange().getMin(),
								ddlRelType.getSecondRange().getMax() > this.horizon ? 
										this.horizon : 
										ddlRelType.getSecondRange().getMax()
							}
					});
		}
		else {
			/*
			 * TODO : <<<<----- CHECK OTHER TYPES OF TEMPORAL RELATIONS
			 */
			throw new RuntimeException("Unknown temporal relation " + ddlRelType);
		}
	}
	
	/**
	 * 
	 * @param pdb
	 */
	private void addParameterDomains(PlanDataBase pdb) {
		// add parameter domains 
		for (String name : this.ddl_domain.getParameterTypes().keySet()) {
			// get parameter declaration
			DDLParameterType pDomType = this.ddl_domain.getParameterTypes().get(name);
			// check type
			switch (pDomType.getDomainType()) 
			{
				// numeric parameter
				case NUMERIC : {
					// get numeric domain
					DDLNumericParameterType numeric = (DDLNumericParameterType) pDomType;
					// create domain parameter
					NumericParameterDomain dom  = pdb.createParameterDomain(name, ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE);
					// set bounds
					dom.setLowerBound(numeric.getLowerBound());
					dom.setUpperBound(numeric.getUpperBound());
				}
				break;
				
				// enumeration parameter
				case ENUMERATION : {
					// get enumeration domain
					DDLEnumerationParameterType enumeration = (DDLEnumerationParameterType) pDomType;
					// create domain parameter
					EnumerationParameterDomain dom = pdb.createParameterDomain(name, ParameterDomainType.ENUMERATION_DOMAIN_PARAMETER_TYPE);
					// set values
					dom.setValues(enumeration.getEnums().toArray(new String[enumeration.getEnums().size()]));
				}
				break;
			}
		}
	}
	
	/**
	 * 
	 * @param pdb
	 */
	private void addComponents(PlanDataBase pdb) 
	{
		// get domain components
		for (DDLComponent ddlComponent : this.ddl_domain.getComponents().values()) 
		{
			// get component type
			DDLComponentType ctype = this.ddl_domain.getComponentTypes().get(ddlComponent.getComponentType());
			// check state variable type
			if (ctype instanceof DDLSingletonStateVariableComponentType) 
			{
				// add state variable
				this.addStateVariable(pdb, ddlComponent);
			}
			
			// check discrete resource type
			if (ctype instanceof DDLRenewableResourceComponentType)
			{
				// add discrete resource
				this.addDiscreteResource(pdb, ddlComponent);
			}
			
			// check reservoir resource type
			if (ctype instanceof DDLConsumableResourceComponentType)
			{
				// add reservoir resource
				this.addReservoirResource(pdb, ddlComponent);
			}
		}
	}
	
	/**
	 * 
	 * @param pdb
	 * @throws SynchronizationCycleException
	 */
	private void addSynchronizationRules(PlanDataBase pdb) 
			throws SynchronizationCycleException 
	{
		try 
		{
			// add synchronization on components
			for (DDLTimelineSynchronization ddlComponentSynch : this.ddl_domain.getTimelineSynchronizations()) 
			{
				// get reference component
				DomainComponent comp = pdb.getComponentByName(ddlComponentSynch.getComponent());
				
				// check component's synchronizations
				for (DDLSynchronization ddlSynch : ddlComponentSynch.getSynchronizations()) 
				{
					// get synchronization's reference
					DDLComponentDecision ddlReference = ddlSynch.getReference();
					// check type
					switch (ddlReference.getDecisionType()) 
					{
						// singleton decision
						case SINGLETON : {
							// create synchronization 
							this.doCreateSynchronizationFromSingletonDecision(ddlSynch, comp, pdb);
						}
						break;
						
						// ground decision
						case GROUND : {
							// create synchronization
							this.doCreateSynchronizationFromGroundDecision(ddlSynch, comp, pdb);
						}
						break;
						
						// reservoir resource usage
						case CONSUMABLE_RESOURCE_REQUIREMENT : {
							// create synchronization
							this.doCreateSynchronizationFromConsumableResourceValue(ddlSynch, (ReservoirResource) comp, pdb);
						}
						break;
						
						// discrete resource usage
						case RENEWABLE_RESOURCE_REQUIREMENT : {
							 // create synchronization
							this.doCreateSynchronizationFromRenewableResourceValue(ddlSynch, (DiscreteResource) comp, pdb);
						}
						
						default : {
							throw new RuntimeException("Synchronization on unknown type of value " + ddlReference.getDecisionType());
						}
					}
				}
			}
		}
		catch (DomainComponentNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	/**
	 * 
	 * @param ddlSynch
	 * @param comp
	 * @param pdb
	 * @throws DomainComponentNotFoundException
	 * @throws SynchronizationCycleException
	 */
	private void doCreateSynchronizationFromRenewableResourceValue(DDLSynchronization ddlSynch, DiscreteResource comp, PlanDataBase pdb) 
			throws DomainComponentNotFoundException, SynchronizationCycleException
	{
		// setup auxiliary data structures
		Map<String, TokenVariable> label2variable = new HashMap<>();
		Map<String, TokenVariable> decId2variable = new HashMap<>();
		
		// get reference decision
		DDLRenewableResourceComponentDecision ddlDec = (DDLRenewableResourceComponentDecision) ddlSynch.getReference();
		
		// get trigger value
		RequirementResourceValue valTrigger = comp.getRequirementValue();
		
		// create synchronization
		SynchronizationRule rule = pdb.createSynchronizationRule(valTrigger, new String[] {ddlDec.getRequirementName()}); 
		// add entry to parameter label index
		for (String label : rule.getTriggerer().getParameterLabels()) {
			label2variable.put(label, rule.getTriggerer());
		}
		
		// check target values
		for (DDLInstantiatedComponentDecision ddlTarget : ddlSynch.getTargets().values()) 
		{
			// get target component
			DomainComponent targetComponent = pdb.getComponentByName(ddlTarget.getComponent());
			// check target component type
			switch (targetComponent.getType())
			{
				// state variable component
				case SV_EXTERNAL : 
				case SV_FUNCTIONAL : 
				case SV_PRIMITIVE : 
				{
					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// check decision type
					switch (ddlTargetDecision.getDecisionType())
					{
						// decision with parameters
						case SINGLETON :
						{
							// cast decision
							DDLSingletonStateVariableComponentDecision ddlSingletonTarget = (DDLSingletonStateVariableComponentDecision) ddlTargetDecision;
							
							// get target value
							ComponentValue targetValue = targetComponent.getValueByName(ddlSingletonTarget.getName());
							
							// add token variable
							TokenVariable target = rule.addTokenVariable(targetValue, 
									ddlSingletonTarget.getParameterNames().toArray(new String[ddlSingletonTarget.getParameterNames().size()]));
							
							// check solving knowledge
							if (!ddlTarget.getParameters().isEmpty()) {
								// check mandatory expansion
								if (ddlTarget.getParameters().contains("!")) {
									// set knowledge
									target.setMandatoryExpansion();
								}
								
								// check mandatory unification
								if (ddlTarget.getParameters().contains("?")) {
									target.setMandatoryUnification();
								}
							}
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						// ground decision
						case GROUND : 
						{
							// cast decision
							DDLSimpleGroundStateVariableComponentDecision ddlGroundTarget = (DDLSimpleGroundStateVariableComponentDecision) ddlTargetDecision;
							// get target value
							ComponentValue targetValue = targetComponent.getValueByName(ddlGroundTarget.getName());
							
							// add token variable with no parameter labels
							TokenVariable target = rule.addTokenVariable(targetValue, new String[] {}); 
							
							// check solving knowledge
							if (!ddlTarget.getParameters().isEmpty()) {
								// check mandatory expansion
								if (ddlTarget.getParameters().contains("!")) {
									// set knowledge
									target.setMandatoryExpansion();
								}
								
								// check mandatory unification
								if (ddlTarget.getParameters().contains("?")) {
									target.setMandatoryUnification();
								}
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						default : {
							throw new RuntimeException("Unknown state variable decision type - " + ddlTargetDecision.getDecisionType());
						}
					}
					
					
				}
				break;
				
				// discrete resource component
				case RESOURCE_DISCRETE : 
				{
					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// cast decision
					DDLRenewableResourceComponentDecision ddlRequirementTarget = (DDLRenewableResourceComponentDecision) ddlTargetDecision;
					// get target component
					DiscreteResource resource = (DiscreteResource) targetComponent;
					 
					// get target value 
					ComponentValue targetValue = resource.getRequirementValue();
					// add token variable
					TokenVariable target = rule.addTokenVariable(targetValue,
							new String[] {ddlRequirementTarget.getRequirementName()});
					
					// add entry to index
					for (String label : target.getParameterLabels()) {
						// add entry
						label2variable.put(label, target);
					}
					
					// get synchronization decision's ID
					String id = ddlTarget.getId();
					// check if target id already exists 
					if (decId2variable.containsKey(id)) {
						throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
					}
					
					// add entry 
					decId2variable.put(id, target);
				}
				break;
				
				// reservoir resource component
				case RESOURCE_RESERVOIR : 
				{
					// get target component
					ReservoirResource resource = (ReservoirResource) targetComponent;

					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// cast decision
					DDLConsumableResourceComponentDecision ddlConsumableTarget = (DDLConsumableResourceComponentDecision) ddlTargetDecision;
					// check decision type
					switch (ddlConsumableTarget.getComponentDecisionType())
					{
						// resource consumption
						case Consumption : 
						{
							// get target value 
							ComponentValue consumption = resource.getConsumptionValue();
							// add token variable
							TokenVariable target = rule.addTokenVariable(consumption,
									new String[] {ddlConsumableTarget.getParameterName()});
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						// resource production 
						case Production : 
						{
							// get target value 
							ComponentValue production = resource.getProductionValue();
							// add token variable
							TokenVariable target = rule.addTokenVariable(production,
									new String[] {ddlConsumableTarget.getParameterName()});
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
					}
					
					
				}
				break;
				
				default : {
					// unknown target component type
					throw new RuntimeException("Unknownw target component found into synchronization constraint" + ddlTarget);
				}
			}
		}
		
		// set temporal relations
		for (DDLTemporalRelation ddlRel : ddlSynch.getTemporalRelations()) 
		{
			// check relation type
			DDLTemporalRelationType ddlRelType = ddlRel.getRelationType();
			
			// get constraint reference
			TokenVariable cref;
			// check reference's ID
			if (ddlRel.getFrom() == null) {
				cref = rule.getTriggerer();
			}
			else {
				// check if reference exists
				if (!decId2variable.containsKey(ddlRel.getFrom())) {
					throw new RuntimeException("Unknown decision ID found <" + ddlRel.getFrom() + "> on synchronization " + rule);
				}
				
				// get reference decision
				cref = decId2variable.get(ddlRel.getFrom());
			}
			
			// check if target exists
			if (!decId2variable.containsKey(ddlRel.getTo())) {
				throw new RuntimeException("Unknown decision ID found <" + ddlRel.getTo() + "> on synchronization " + rule);
			}
			
			// get constraint target
			TokenVariable ctarget = decId2variable.get(ddlRel.getTo());
			
			// create constraint
			this.addConstraintToSynchronization(rule, cref, ctarget, ddlRelType);
		}
		
		// set parameter relations
		List<DDLParameterConstraint> ddlParameterConstraints = ddlSynch.getParameterConstraints();
		for (DDLParameterConstraint ddlpc : ddlParameterConstraints) 
		{
			// check constraint type
			switch (ddlpc.getConstraintType()) 
			{
				// enumeration parameter constraint
				case ENUMERATION : 
				{
					// get enumeration constraint
					DDLEnumerationParameterConstraint ddlEnumerationConstraint = (DDLEnumerationParameterConstraint) ddlpc;
					// get reference variable
					String leftTerm = ddlEnumerationConstraint.getLeftTerm();
					
					// check if parameter exists
					if (!label2variable.containsKey(leftTerm)) {
						throw new RuntimeException("Unknown reference parameter found <" + leftTerm + "> on synchronization " + rule);
					}
					
					// get reference
					TokenVariable pcReference = label2variable.get(leftTerm);
					
					// get value
					String value = ddlEnumerationConstraint.getValue();
					// get relation type
					RelationType relType;
					switch (ddlEnumerationConstraint.getEnumerationConstraintType()) {
						// equal constraint
						case EQ : {
							// set relation type
							relType = RelationType.BIND_PARAMETER;
						}
						break;
						
						default : {
							// FIXME : <------- MANAGE MISSING CASES
							throw new RuntimeException("Unknownw enumeration constraint type " + ddlEnumerationConstraint.getEnumerationConstraintType());
						}
					}
					
					// create binding constraint
					rule.addBindingConstraint(pcReference, relType, leftTerm, value);
				}
				break;
				
				// binary parameter constraint
				case NUMERIC : 
				{
					// get numeric constraint
					DDLNumericParameterConstraint ddlNumericConstraint = (DDLNumericParameterConstraint) ddlpc;
					// get reference variable
					String leftTerm = ddlNumericConstraint.getLeftTerm();
					
					// check if parameter exists
					if (!label2variable.containsKey(leftTerm)) {
						throw new RuntimeException("Unknown reference parameter found <" + leftTerm + "> on synchronization " + rule);
					}
					
					// get reference
					TokenVariable pcReference = label2variable.get(leftTerm);
					
					// check if binary
					if (ddlNumericConstraint.isBinary()) 
					{
						// get relation type
						RelationType relType;
						switch (ddlNumericConstraint.getNumericConstraintType()) {
							// equal constraint
							case EQ : {
								// set relation type
								relType = RelationType.EQUAL_PARAMETER;
							}
							break;
							
							// not equal constraint
							case NEQ : {
								// set relation type
								relType = RelationType.NOT_EQUAL_PARAMETER;
							}
							break;
							
							default : {
								// FIXME : <------- MANAGE MISSING CASES
								throw new RuntimeException("Unknownw numeric constraint type " + ddlNumericConstraint.getNumericConstraintType());
							}
						}
						
						// check targets
						for (String rightTerm : ddlNumericConstraint.getRightVariables()) {
							// check if parameter exists
							if (!label2variable.containsKey(rightTerm)) {
								throw new RuntimeException("Unknown target found <" + rightTerm + "> on synchronization " + rule);
							}
							
							// get target variable
							TokenVariable pcTarget = label2variable.get(rightTerm);
							
							// create constraint
							rule.addParameterConstraint(pcReference, pcTarget, relType, leftTerm, rightTerm);
						}
					}
					else {
						// not binary constraint, set bind constraint
						int value = ddlNumericConstraint.getRightCoefficients().get(0);
						// get relation type
						RelationType relType;
						switch (ddlNumericConstraint.getNumericConstraintType()) 
						{
							// equal constraint
							case EQ : 
							{
								// set relation type
								relType = RelationType.BIND_PARAMETER;
							}
							break;
							
							default : {
								// FIXME : <------- MANAGE MISSING CASES
								throw new RuntimeException("Unknownw numeric constraint type " + ddlNumericConstraint.getNumericConstraintType());
							}
						}
						
						// create constraint
						rule.addBindingConstraint(pcReference, relType, leftTerm, Integer.toString(value));
					}
				}
			}
		}
		
		// add synchronization rule
		pdb.addSynchronizationRule(rule);
	}
	
	/**
	 * 
	 * @param ddlSynch
	 * @param comp
	 * @param pdb
	 * @throws DomainComponentNotFoundException
	 * @throws SynchronizationCycleException
	 */
	private void doCreateSynchronizationFromConsumableResourceValue(DDLSynchronization ddlSynch, ReservoirResource comp, PlanDataBase pdb) 
			throws DomainComponentNotFoundException, SynchronizationCycleException
	{
		// setup auxiliary data structures
		Map<String, TokenVariable> label2variable = new HashMap<>();
		Map<String, TokenVariable> decId2variable = new HashMap<>();
		
		// get reference decision
		DDLConsumableResourceComponentDecision ddlDec = (DDLConsumableResourceComponentDecision) ddlSynch.getReference();
		
		// get trigger value
		ComponentValue valTrigger = null;
		// check decision type 
		switch (ddlDec.getComponentDecisionType())
		{
			case Consumption : {
				// set trigger value to consumption
				valTrigger = comp.getConsumptionValue();
			}
			break;
			
			case Production : {
				// set trigger value to production
				valTrigger = comp.getProductionValue();
			}
			break;
		}
		
		// create synchronization
		SynchronizationRule rule = pdb.createSynchronizationRule(valTrigger, new String[] {ddlDec.getParameterName()}); 
		// add entry to parameter label index
		for (String label : rule.getTriggerer().getParameterLabels()) {
			label2variable.put(label, rule.getTriggerer());
		}
		
		// check target values
		for (DDLInstantiatedComponentDecision ddlTarget : ddlSynch.getTargets().values()) 
		{
			// get target component
			DomainComponent targetComponent = pdb.getComponentByName(ddlTarget.getComponent());
			// check target component type
			switch (targetComponent.getType())
			{
				// state variable component
				case SV_EXTERNAL : 
				case SV_FUNCTIONAL : 
				case SV_PRIMITIVE : 
				{
					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// check decision type
					switch (ddlTargetDecision.getDecisionType())
					{
						// decision with parameters
						case SINGLETON :
						{
							// cast decision
							DDLSingletonStateVariableComponentDecision ddlSingletonTarget = (DDLSingletonStateVariableComponentDecision) ddlTargetDecision;
							
							// get target value
							ComponentValue targetValue = targetComponent.getValueByName(ddlSingletonTarget.getName());
							
							// add token variable
							TokenVariable target = rule.addTokenVariable(targetValue, 
									ddlSingletonTarget.getParameterNames().toArray(new String[ddlSingletonTarget.getParameterNames().size()]));
							
							// check solving knowledge
							if (!ddlTarget.getParameters().isEmpty()) {
								// check mandatory expansion
								if (ddlTarget.getParameters().contains("!")) {
									// set knowledge
									target.setMandatoryExpansion();
								}
								
								// check mandatory unification
								if (ddlTarget.getParameters().contains("?")) {
									target.setMandatoryUnification();
								}
							}
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						// ground decision
						case GROUND : 
						{
							// cast decision
							DDLSimpleGroundStateVariableComponentDecision ddlGroundTarget = (DDLSimpleGroundStateVariableComponentDecision) ddlTargetDecision;
							// get target value
							ComponentValue targetValue = targetComponent.getValueByName(ddlGroundTarget.getName());
							
							// add token variable with no parameter labels
							TokenVariable target = rule.addTokenVariable(targetValue, new String[] {}); 
							
							// check solving knowledge
							if (!ddlTarget.getParameters().isEmpty()) {
								// check mandatory expansion
								if (ddlTarget.getParameters().contains("!")) {
									// set knowledge
									target.setMandatoryExpansion();
								}
								
								// check mandatory unification
								if (ddlTarget.getParameters().contains("?")) {
									target.setMandatoryUnification();
								}
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						default : {
							throw new RuntimeException("Unknown state variable decision type - " + ddlTargetDecision.getDecisionType());
						}
					}
					
					
				}
				break;
				
				// discrete resource component
				case RESOURCE_DISCRETE : 
				{
					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// cast decision
					DDLRenewableResourceComponentDecision ddlRequirementTarget = (DDLRenewableResourceComponentDecision) ddlTargetDecision;
					// get target component
					DiscreteResource resource = (DiscreteResource) targetComponent;
					 
					// get target value 
					ComponentValue targetValue = resource.getRequirementValue();
					// add token variable
					TokenVariable target = rule.addTokenVariable(targetValue,
							new String[] {ddlRequirementTarget.getRequirementName()});
					
					// add entry to index
					for (String label : target.getParameterLabels()) {
						// add entry
						label2variable.put(label, target);
					}
					
					// get synchronization decision's ID
					String id = ddlTarget.getId();
					// check if target id already exists 
					if (decId2variable.containsKey(id)) {
						throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
					}
					
					// add entry 
					decId2variable.put(id, target);
				}
				break;
				
				// reservoir resource component
				case RESOURCE_RESERVOIR : 
				{
					// get target component
					ReservoirResource resource = (ReservoirResource) targetComponent;

					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// cast decision
					DDLConsumableResourceComponentDecision ddlConsumableTarget = (DDLConsumableResourceComponentDecision) ddlTargetDecision;
					// check decision type
					switch (ddlConsumableTarget.getComponentDecisionType())
					{
						// resource consumption
						case Consumption : 
						{
							// get target value 
							ComponentValue consumption = resource.getConsumptionValue();
							// add token variable
							TokenVariable target = rule.addTokenVariable(consumption,
									new String[] {ddlConsumableTarget.getParameterName()});
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						// resource production 
						case Production : 
						{
							// get target value 
							ComponentValue production = resource.getProductionValue();
							// add token variable
							TokenVariable target = rule.addTokenVariable(production,
									new String[] {ddlConsumableTarget.getParameterName()});
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
					}
					
					
				}
				break;
				
				default : {
					// unknown target component type
					throw new RuntimeException("Unknownw target component found into synchronization constraint" + ddlTarget);
				}
			}
		}
		
		// set temporal relations
		for (DDLTemporalRelation ddlRel : ddlSynch.getTemporalRelations()) 
		{
			// check relation type
			DDLTemporalRelationType ddlRelType = ddlRel.getRelationType();
			
			// get constraint reference
			TokenVariable cref;
			// check reference's ID
			if (ddlRel.getFrom() == null) {
				cref = rule.getTriggerer();
			}
			else {
				// check if reference exists
				if (!decId2variable.containsKey(ddlRel.getFrom())) {
					throw new RuntimeException("Unknown decision ID found <" + ddlRel.getFrom() + "> on synchronization " + rule);
				}
				
				// get reference decision
				cref = decId2variable.get(ddlRel.getFrom());
			}
			
			// check if target exists
			if (!decId2variable.containsKey(ddlRel.getTo())) {
				throw new RuntimeException("Unknown decision ID found <" + ddlRel.getTo() + "> on synchronization " + rule);
			}
			
			// get constraint target
			TokenVariable ctarget = decId2variable.get(ddlRel.getTo());
			
			// create constraint
			this.addConstraintToSynchronization(rule, cref, ctarget, ddlRelType);
		}
		
		// set parameter relations
		List<DDLParameterConstraint> ddlParameterConstraints = ddlSynch.getParameterConstraints();
		for (DDLParameterConstraint ddlpc : ddlParameterConstraints) 
		{
			// check constraint type
			switch (ddlpc.getConstraintType()) 
			{
				// enumeration parameter constraint
				case ENUMERATION : 
				{
					// get enumeration constraint
					DDLEnumerationParameterConstraint ddlEnumerationConstraint = (DDLEnumerationParameterConstraint) ddlpc;
					// get reference variable
					String leftTerm = ddlEnumerationConstraint.getLeftTerm();
					
					// check if parameter exists
					if (!label2variable.containsKey(leftTerm)) {
						throw new RuntimeException("Unknown reference parameter found <" + leftTerm + "> on synchronization " + rule);
					}
					
					// get reference
					TokenVariable pcReference = label2variable.get(leftTerm);
					
					// get value
					String value = ddlEnumerationConstraint.getValue();
					// get relation type
					RelationType relType;
					switch (ddlEnumerationConstraint.getEnumerationConstraintType()) {
						// equal constraint
						case EQ : {
							// set relation type
							relType = RelationType.BIND_PARAMETER;
						}
						break;
						
						default : {
							// FIXME : <------- MANAGE MISSING CASES
							throw new RuntimeException("Unknownw enumeration constraint type " + ddlEnumerationConstraint.getEnumerationConstraintType());
						}
					}
					
					// create binding constraint
					rule.addBindingConstraint(pcReference, relType, leftTerm, value);
				}
				break;
				
				// binary parameter constraint
				case NUMERIC : 
				{
					// get numeric constraint
					DDLNumericParameterConstraint ddlNumericConstraint = (DDLNumericParameterConstraint) ddlpc;
					// get reference variable
					String leftTerm = ddlNumericConstraint.getLeftTerm();
					
					// check if parameter exists
					if (!label2variable.containsKey(leftTerm)) {
						throw new RuntimeException("Unknown reference parameter found <" + leftTerm + "> on synchronization " + rule);
					}
					
					// get reference
					TokenVariable pcReference = label2variable.get(leftTerm);
					
					// check if binary
					if (ddlNumericConstraint.isBinary()) 
					{
						// get relation type
						RelationType relType;
						switch (ddlNumericConstraint.getNumericConstraintType()) {
							// equal constraint
							case EQ : {
								// set relation type
								relType = RelationType.EQUAL_PARAMETER;
							}
							break;
							
							// not equal constraint
							case NEQ : {
								// set relation type
								relType = RelationType.NOT_EQUAL_PARAMETER;
							}
							break;
							
							default : {
								// FIXME : <------- MANAGE MISSING CASES
								throw new RuntimeException("Unknownw numeric constraint type " + ddlNumericConstraint.getNumericConstraintType());
							}
						}
						
						// check targets
						for (String rightTerm : ddlNumericConstraint.getRightVariables()) {
							// check if parameter exists
							if (!label2variable.containsKey(rightTerm)) {
								throw new RuntimeException("Unknown target found <" + rightTerm + "> on synchronization " + rule);
							}
							
							// get target variable
							TokenVariable pcTarget = label2variable.get(rightTerm);
							
							// create constraint
							rule.addParameterConstraint(pcReference, pcTarget, relType, leftTerm, rightTerm);
						}
					}
					else {
						// not binary constraint, set bind constraint
						int value = ddlNumericConstraint.getRightCoefficients().get(0);
						// get relation type
						RelationType relType;
						switch (ddlNumericConstraint.getNumericConstraintType()) 
						{
							// equal constraint
							case EQ : 
							{
								// set relation type
								relType = RelationType.BIND_PARAMETER;
							}
							break;
							
							default : {
								// FIXME : <------- MANAGE MISSING CASES
								throw new RuntimeException("Unknownw numeric constraint type " + ddlNumericConstraint.getNumericConstraintType());
							}
						}
						
						// create constraint
						rule.addBindingConstraint(pcReference, relType, leftTerm, Integer.toString(value));
					}
				}
			}
		}
		
		// add synchronization rule
		pdb.addSynchronizationRule(rule);
	}
	
	/**
	 * 
	 * @param ddlSynch
	 * @param comp
	 * @param pdb
	 * @throws DomainComponentNotFoundException
	 * @throws SynchronizationCycleException
	 */
	private void doCreateSynchronizationFromGroundDecision(DDLSynchronization ddlSynch, DomainComponent comp, PlanDataBase pdb) 
			throws DomainComponentNotFoundException, SynchronizationCycleException
	{ 
		// setup auxiliary data structures
		Map<String, TokenVariable> label2variable = new HashMap<>();
		Map<String, TokenVariable> decId2variable = new HashMap<>();
		
		// get reference decision
		DDLSimpleGroundStateVariableComponentDecision ddlDec = (DDLSimpleGroundStateVariableComponentDecision) ddlSynch.getReference();
		
		// get trigger value
		ComponentValue valTrigger = comp.getValueByName(ddlDec.getName());
		
		// create synchronization
		SynchronizationRule rule = pdb.createSynchronizationRule(valTrigger, new String[] {});
		// add entry to parameter label index
		for (String label : rule.getTriggerer().getParameterLabels()) {
			label2variable.put(label, rule.getTriggerer());
		}
		
		// check target values
		for (DDLInstantiatedComponentDecision ddlTarget : ddlSynch.getTargets().values()) 
		{
			// get target component
			DomainComponent targetComponent = pdb.getComponentByName(ddlTarget.getComponent());
			// check target decision
			DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
			// check type
			switch (ddlTargetDecision.getDecisionType()) 
			{
				// consumable resource
				case CONSUMABLE_RESOURCE_REQUIREMENT :
				{
					// get target value
					ReservoirResource resource = (ReservoirResource) targetComponent;
					// cast decision
					DDLConsumableResourceComponentDecision ddlDecisionTarget = (DDLConsumableResourceComponentDecision) ddlTargetDecision;
					// target token variable
					TokenVariable target = null;
					// check decision type
					switch (ddlDecisionTarget.getComponentDecisionType())
					{
						case Consumption : {
							// add consumption token variable
							target = rule.addTokenVariable(resource.getConsumptionValue(), new String[] {
								ddlDecisionTarget.getParameterName()	
							});
						}
						break;
						
						case Production : {
							// add production token variable
							target = rule.addTokenVariable(resource.getProductionValue(), new String[] {
								ddlDecisionTarget.getParameterName()
							});
						}
						break;
					}
					
					// set as mandatory expansion
					target.setMandatoryExpansion();
					// add entry to index
					for (String label : target.getParameterLabels()) {
						// add entry
						label2variable.put(label, target);
					}
					
					// get synchronization decision's ID
					String id = ddlTarget.getId();
					// check if target id already exists 
					if (decId2variable.containsKey(id)) {
						throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
					}
					
					// add entry 
					decId2variable.put(id, target);
				}
				break;
			
				// discrete resource target
				case RENEWABLE_RESOURCE_REQUIREMENT :
				{
					// get target value
					ComponentValue targetValue = ((DiscreteResource) targetComponent).getRequirementValue();
					// cast decision
					DDLRenewableResourceComponentDecision ddlRequirementTarget = (DDLRenewableResourceComponentDecision) ddlTargetDecision;
					// add token variable
					TokenVariable target = rule.addTokenVariable(targetValue, new String[] {
							ddlRequirementTarget.getRequirementName()
					});
					
					// set as mandatory expansion
					target.setMandatoryExpansion();
					// add entry to index
					for (String label : target.getParameterLabels()) {
						// add entry
						label2variable.put(label, target);
					}
					
					// get synchronization decision's ID
					String id = ddlTarget.getId();
					// check if target id already exists 
					if (decId2variable.containsKey(id)) {
						throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
					}
					
					// add entry 
					decId2variable.put(id, target);
				}
				break;
			
				// singleton target decision
				case SINGLETON : 
				{
					// get target value
					ComponentValue targetValue = targetComponent.getValueByName(ddlTarget.getComponentDecision().getName());
					// cast decision
					DDLSingletonStateVariableComponentDecision ddlSingletonTarget = (DDLSingletonStateVariableComponentDecision) ddlTargetDecision;
					// add token variable
					TokenVariable target = rule.addTokenVariable(targetValue, 
							ddlSingletonTarget.getParameterNames().toArray(new String[ddlSingletonTarget.getParameterNames().size()]));
					
					// check solving knowledge
					if (!ddlTarget.getParameters().isEmpty()) {
						// check mandatory expansion
						if (ddlTarget.getParameters().contains("!")) {
							// set knowledge
							target.setMandatoryExpansion();
						}
						
						// check mandatory unification
						if (ddlTarget.getParameters().contains("?")) {
							target.setMandatoryUnification();
						}
					}
					
					// add entry to index
					for (String label : target.getParameterLabels()) {
						// add entry
						label2variable.put(label, target);
					}
					
					// get synchronization decision's ID
					String id = ddlTarget.getId();
					// check if target id already exists 
					if (decId2variable.containsKey(id)) {
						throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
					}
					
					// add entry 
					decId2variable.put(id, target);
				}
				break;
				
				// ground target decision
				case GROUND : 
				{
					// get target value
					ComponentValue targetValue = targetComponent.getValueByName(ddlTarget.getComponentDecision().getName());
					// add token variable
					TokenVariable target = rule.addTokenVariable(targetValue, new String[] {});
					
					// check solving knowledge
					if (!ddlTarget.getParameters().isEmpty()) {
						// check mandatory expansion
						if (ddlTarget.getParameters().contains("!")) {
							// set knowledge
							target.setMandatoryExpansion();
						}
						
						// check mandatory unification
						if (ddlTarget.getParameters().contains("?")) {
							target.setMandatoryUnification();
						}
					}
					
					// get synchronization decision's ID
					String id = ddlTarget.getId();
					// check if target id already exists 
					if (decId2variable.containsKey(id)) {
						throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
					}
					
					// add entry 
					decId2variable.put(id, target);
				}
				break;
				
				default : {
					// FIXME <<<<----- IMPLEMENTARE
					throw new RuntimeException("Unknownw target decision found into synchronization " + ddlTarget);
				}
			}
		}
		
		// set temporal relations
		for (DDLTemporalRelation ddlRel : ddlSynch.getTemporalRelations()) {
			// check relation type
			DDLTemporalRelationType ddlRelType = ddlRel.getRelationType();
			
			// get constraint reference
			TokenVariable cref;
			// check reference's ID
			if (ddlRel.getFrom() == null) {
				cref = rule.getTriggerer();
			}
			else {
				// check if reference exists
				if (!decId2variable.containsKey(ddlRel.getFrom())) {
					throw new RuntimeException("Unknown decision ID found <" + ddlRel.getFrom() + "> on synchronization " + rule);
				}
				
				// set reference
				cref = decId2variable.get(ddlRel.getFrom());
			}
			
			// check if target exists
			if (!decId2variable.containsKey(ddlRel.getTo())) {
				throw new RuntimeException("Unknown decision ID found <" + ddlRel.getTo() + "> on synchronization " + rule);
			}
			
			// get constraint target
			TokenVariable ctarget = decId2variable.get(ddlRel.getTo());
			
			// create constraint
			this.addConstraintToSynchronization(rule, cref, ctarget, ddlRelType);
		}
		
		// set parameter relations
		List<DDLParameterConstraint> ddlParameterConstraints = ddlSynch.getParameterConstraints();
		for (DDLParameterConstraint ddlpc : ddlParameterConstraints) 
		{
			// check constraint type
			switch (ddlpc.getConstraintType()) 
			{
				// enumeration parameter constraint
				case ENUMERATION : 
				{
					// get enumeration constraint
					DDLEnumerationParameterConstraint ddlEnumerationConstraint = (DDLEnumerationParameterConstraint) ddlpc;
					// get reference variable
					String leftTerm = ddlEnumerationConstraint.getLeftTerm();
					
					// check if label exists
					if (!label2variable.containsKey(leftTerm)) {
						throw new RuntimeException("Unknown parameter label found <" + leftTerm +"> on synchronization " + rule);
					}
					
					TokenVariable pcReference = label2variable.get(leftTerm);
					// get value
					String value = ddlEnumerationConstraint.getValue();
					// get relation type
					RelationType relType;
					switch (ddlEnumerationConstraint.getEnumerationConstraintType()) {
						// equal constraint
						case EQ : {
							// set relation type
							relType = RelationType.BIND_PARAMETER;
						}
						break;
						
						default : {
							// FIXME : <------- MANAGE MISSING CASES
							throw new RuntimeException("Unknownw enumeration constraint type " + ddlEnumerationConstraint.getEnumerationConstraintType());
						}
					}
					
					// create binding constraint
					rule.addBindingConstraint(pcReference, relType, leftTerm, value);
				}
				break;
				
				// binary parameter constraint
				case NUMERIC : 
				{
					// get numeric constraint
					DDLNumericParameterConstraint ddlNumericConstraint = (DDLNumericParameterConstraint) ddlpc;
					// get reference variable
					String leftTerm = ddlNumericConstraint.getLeftTerm();
					
					// check if label exists
					if (!label2variable.containsKey(leftTerm)) {
						throw new RuntimeException("Unknown parameter label found <" + leftTerm +"> on synchronization " + rule);
					}
					
					TokenVariable pcReference = label2variable.get(leftTerm);
					
					// check if binary
					if (ddlNumericConstraint.isBinary()) 
					{
						// get relation type
						RelationType relType;
						switch (ddlNumericConstraint.getNumericConstraintType()) {
							// equal constraint
							case EQ : {
								// set relation type
								relType = RelationType.EQUAL_PARAMETER;
							}
							break;
							
							// not equal constraint
							case NEQ : {
								// set relation type
								relType = RelationType.NOT_EQUAL_PARAMETER;
							}
							break;
							
							default : {
								// FIXME : <------- MANAGE MISSING CASES
								throw new RuntimeException("Unknownw numeric constraint type " + ddlNumericConstraint.getNumericConstraintType());
							}
						}
						
						// check targets
						for (String rightTerm : ddlNumericConstraint.getRightVariables()) {
							// check if label exists
							if (!label2variable.containsKey(rightTerm)) {
								throw new RuntimeException("Unknown parameter label found <" + rightTerm +"> on synchronization " + rule);
							}
							
							// get target variable
							TokenVariable pcTarget = label2variable.get(rightTerm);
							
							// create constraint
							rule.addParameterConstraint(pcReference, pcTarget, relType, leftTerm, rightTerm);
						}
					}
					else {
						// not binary constraint, set bind constraint
						int value = ddlNumericConstraint.getRightCoefficients().get(0);
						// get relation type
						RelationType relType;
						switch (ddlNumericConstraint.getNumericConstraintType()) {
							// equal constraint
							case EQ : {
								// set relation type
								relType = RelationType.BIND_PARAMETER;
							}
							break;
							
							default : {
								// FIXME : <------- MANAGE MISSING CASES
								throw new RuntimeException("Unknownw numeric constraint type " + ddlNumericConstraint.getNumericConstraintType());
							}
						}
						
						// create constraint
						rule.addBindingConstraint(pcReference, relType, leftTerm, Integer.toString(value));
					}
				}
			}
		}
		
		// add synchronization rule
		pdb.addSynchronizationRule(rule);
	}
	
	/**
	 * 
	 * @param ddlSynch
	 * @param comp
	 * @param pdb
	 * @throws DomainComponentNotFoundException
	 * @throws SynchronizationCycleException
	 */
	private void doCreateSynchronizationFromSingletonDecision(DDLSynchronization ddlSynch, DomainComponent comp, PlanDataBase pdb) 
			throws DomainComponentNotFoundException, SynchronizationCycleException
	{ 
		// setup auxiliary data structures
		Map<String, TokenVariable> label2variable = new HashMap<>();
		Map<String, TokenVariable> decId2variable = new HashMap<>();
		
		// get reference decision
		DDLSingletonStateVariableComponentDecision ddlDec = (DDLSingletonStateVariableComponentDecision) ddlSynch.getReference();
		
		// get trigger value
		ComponentValue valTrigger = comp.getValueByName(ddlDec.getName());
		
		// create synchronization
		SynchronizationRule rule = pdb.createSynchronizationRule(valTrigger, 
				ddlDec.getParameterNames().toArray(new String[ddlDec.getParameterNames().size()]));
		// add entry to parameter label index
		for (String label : rule.getTriggerer().getParameterLabels()) {
			label2variable.put(label, rule.getTriggerer());
		}
		
		// check target values
		for (DDLInstantiatedComponentDecision ddlTarget : ddlSynch.getTargets().values()) 
		{
			// get target component
			DomainComponent targetComponent = pdb.getComponentByName(ddlTarget.getComponent());
			// check target component type
			switch (targetComponent.getType())
			{
				// state variable component
				case SV_EXTERNAL : 
				case SV_FUNCTIONAL : 
				case SV_PRIMITIVE : 
				{
					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// check decision type
					switch (ddlTargetDecision.getDecisionType())
					{
						// decision with parameters
						case SINGLETON :
						{
							// cast decision
							DDLSingletonStateVariableComponentDecision ddlSingletonTarget = (DDLSingletonStateVariableComponentDecision) ddlTargetDecision;
							
							// get target value
							ComponentValue targetValue = targetComponent.getValueByName(ddlSingletonTarget.getName());
							
							// add token variable
							TokenVariable target = rule.addTokenVariable(targetValue, 
									ddlSingletonTarget.getParameterNames().toArray(new String[ddlSingletonTarget.getParameterNames().size()]));
							
							// check solving knowledge
							if (!ddlTarget.getParameters().isEmpty()) {
								// check mandatory expansion
								if (ddlTarget.getParameters().contains("!")) {
									// set knowledge
									target.setMandatoryExpansion();
								}
								
								// check mandatory unification
								if (ddlTarget.getParameters().contains("?")) {
									target.setMandatoryUnification();
								}
							}
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						// ground decision
						case GROUND : 
						{
							// cast decision
							DDLSimpleGroundStateVariableComponentDecision ddlGroundTarget = (DDLSimpleGroundStateVariableComponentDecision) ddlTargetDecision;
							// get target value
							ComponentValue targetValue = targetComponent.getValueByName(ddlGroundTarget.getName());
							
							// add token variable with no parameter labels
							TokenVariable target = rule.addTokenVariable(targetValue, new String[] {}); 
							
							// check solving knowledge
							if (!ddlTarget.getParameters().isEmpty()) {
								// check mandatory expansion
								if (ddlTarget.getParameters().contains("!")) {
									// set knowledge
									target.setMandatoryExpansion();
								}
								
								// check mandatory unification
								if (ddlTarget.getParameters().contains("?")) {
									target.setMandatoryUnification();
								}
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						default : {
							throw new RuntimeException("Unknown state variable decision type - " + ddlTargetDecision.getDecisionType());
						}
					}
					
					
				}
				break;
				
				// discrete resource component
				case RESOURCE_DISCRETE : 
				{
					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// cast decision
					DDLRenewableResourceComponentDecision ddlRequirementTarget = (DDLRenewableResourceComponentDecision) ddlTargetDecision;
					// get target component
					DiscreteResource resource = (DiscreteResource) targetComponent;
					 
					// get target value 
					ComponentValue targetValue = resource.getRequirementValue();
					// add token variable
					TokenVariable target = rule.addTokenVariable(targetValue,
							new String[] {ddlRequirementTarget.getRequirementName()});
					
					// add entry to index
					for (String label : target.getParameterLabels()) {
						// add entry
						label2variable.put(label, target);
					}
					
					// get synchronization decision's ID
					String id = ddlTarget.getId();
					// check if target id already exists 
					if (decId2variable.containsKey(id)) {
						throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
					}
					
					// add entry 
					decId2variable.put(id, target);
				}
				break;
				
				// reservoir resource component
				case RESOURCE_RESERVOIR : 
				{
					// get target component
					ReservoirResource resource = (ReservoirResource) targetComponent;

					// check target decision
					DDLComponentDecision ddlTargetDecision = ddlTarget.getComponentDecision();
					// cast decision
					DDLConsumableResourceComponentDecision ddlConsumableTarget = (DDLConsumableResourceComponentDecision) ddlTargetDecision;
					// check decision type
					switch (ddlConsumableTarget.getComponentDecisionType())
					{
						// resource consumption
						case Consumption : 
						{
							// get target value 
							ComponentValue consumption = resource.getConsumptionValue();
							// add token variable
							TokenVariable target = rule.addTokenVariable(consumption,
									new String[] {ddlConsumableTarget.getParameterName()});
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
						
						// resource production 
						case Production : 
						{
							// get target value 
							ComponentValue production = resource.getProductionValue();
							// add token variable
							TokenVariable target = rule.addTokenVariable(production,
									new String[] {ddlConsumableTarget.getParameterName()});
							
							// add entry to index
							for (String label : target.getParameterLabels()) {
								// add entry
								label2variable.put(label, target);
							}
							
							// get synchronization decision's ID
							String id = ddlTarget.getId();
							// check if target id already exists 
							if (decId2variable.containsKey(id)) {
								throw new RuntimeException("Duplicated decision ID <" + id + "> on synchronization " + rule);
							}
							
							// add entry 
							decId2variable.put(id, target);
						}
						break;
					}
					
					
				}
				break;
				
				default : {
					// unknown target component type
					throw new RuntimeException("Unknownw target component found into synchronization constraint" + ddlTarget);
				}
			}
		}
		
		// set temporal relations
		for (DDLTemporalRelation ddlRel : ddlSynch.getTemporalRelations()) 
		{
			// check relation type
			DDLTemporalRelationType ddlRelType = ddlRel.getRelationType();
			
			// get constraint reference
			TokenVariable cref;
			// check reference's ID
			if (ddlRel.getFrom() == null) {
				cref = rule.getTriggerer();
			}
			else {
				// check if reference exists
				if (!decId2variable.containsKey(ddlRel.getFrom())) {
					throw new RuntimeException("Unknown decision ID found <" + ddlRel.getFrom() + "> on synchronization " + rule);
				}
				
				// get reference decision
				cref = decId2variable.get(ddlRel.getFrom());
			}
			
			// check if target exists
			if (!decId2variable.containsKey(ddlRel.getTo())) {
				throw new RuntimeException("Unknown decision ID found <" + ddlRel.getTo() + "> on synchronization " + rule);
			}
			
			// get constraint target
			TokenVariable ctarget = decId2variable.get(ddlRel.getTo());
			
			// create constraint
			this.addConstraintToSynchronization(rule, cref, ctarget, ddlRelType);
		}
		
		// set parameter relations
		List<DDLParameterConstraint> ddlParameterConstraints = ddlSynch.getParameterConstraints();
		for (DDLParameterConstraint ddlpc : ddlParameterConstraints) 
		{
			// check constraint type
			switch (ddlpc.getConstraintType()) 
			{
				// enumeration parameter constraint
				case ENUMERATION : 
				{
					// get enumeration constraint
					DDLEnumerationParameterConstraint ddlEnumerationConstraint = (DDLEnumerationParameterConstraint) ddlpc;
					// get reference variable
					String leftTerm = ddlEnumerationConstraint.getLeftTerm();
					
					// check if parameter exists
					if (!label2variable.containsKey(leftTerm)) {
						throw new RuntimeException("Unknown reference parameter found <" + leftTerm + "> on synchronization " + rule);
					}
					
					// get reference
					TokenVariable pcReference = label2variable.get(leftTerm);
					
					// get value
					String value = ddlEnumerationConstraint.getValue();
					// get relation type
					RelationType relType;
					switch (ddlEnumerationConstraint.getEnumerationConstraintType()) {
						// equal constraint
						case EQ : {
							// set relation type
							relType = RelationType.BIND_PARAMETER;
						}
						break;
						
						default : {
							// FIXME : <------- MANAGE MISSING CASES
							throw new RuntimeException("Unknownw enumeration constraint type " + ddlEnumerationConstraint.getEnumerationConstraintType());
						}
					}
					
					// create binding constraint
					rule.addBindingConstraint(pcReference, relType, leftTerm, value);
				}
				break;
				
				// binary parameter constraint
				case NUMERIC : 
				{
					// get numeric constraint
					DDLNumericParameterConstraint ddlNumericConstraint = (DDLNumericParameterConstraint) ddlpc;
					// get reference variable
					String leftTerm = ddlNumericConstraint.getLeftTerm();
					
					// check if parameter exists
					if (!label2variable.containsKey(leftTerm)) {
						throw new RuntimeException("Unknown reference parameter found <" + leftTerm + "> on synchronization " + rule);
					}
					
					// get reference
					TokenVariable pcReference = label2variable.get(leftTerm);
					
					// check if binary
					if (ddlNumericConstraint.isBinary()) 
					{
						// get relation type
						RelationType relType;
						switch (ddlNumericConstraint.getNumericConstraintType()) {
							// equal constraint
							case EQ : {
								// set relation type
								relType = RelationType.EQUAL_PARAMETER;
							}
							break;
							
							// not equal constraint
							case NEQ : {
								// set relation type
								relType = RelationType.NOT_EQUAL_PARAMETER;
							}
							break;
							
							default : {
								// FIXME : <------- MANAGE MISSING CASES
								throw new RuntimeException("Unknownw numeric constraint type " + ddlNumericConstraint.getNumericConstraintType());
							}
						}
						
						// check targets
						for (String rightTerm : ddlNumericConstraint.getRightVariables()) {
							// check if parameter exists
							if (!label2variable.containsKey(rightTerm)) {
								throw new RuntimeException("Unknown target found <" + rightTerm + "> on synchronization " + rule);
							}
							
							// get target variable
							TokenVariable pcTarget = label2variable.get(rightTerm);
							
							// create constraint
							rule.addParameterConstraint(pcReference, pcTarget, relType, leftTerm, rightTerm);
						}
					}
					else {
						// not binary constraint, set bind constraint
						int value = ddlNumericConstraint.getRightCoefficients().get(0);
						// get relation type
						RelationType relType;
						switch (ddlNumericConstraint.getNumericConstraintType()) 
						{
							// equal constraint
							case EQ : 
							{
								// set relation type
								relType = RelationType.BIND_PARAMETER;
							}
							break;
							
							default : {
								// FIXME : <------- MANAGE MISSING CASES
								throw new RuntimeException("Unknownw numeric constraint type " + ddlNumericConstraint.getNumericConstraintType());
							}
						}
						
						// create constraint
						rule.addBindingConstraint(pcReference, relType, leftTerm, Integer.toString(value));
					}
				}
			}
		}
		
		// add synchronization rule
		pdb.addSynchronizationRule(rule);
	}
	
	/**
	 * 
	 * @param rule
	 * @param cref
	 * @param ctarget
	 * @param ddlRelType
	 */
	private void addConstraintToSynchronization(SynchronizationRule rule, TokenVariable reference, TokenVariable target, DDLTemporalRelationType ddlRelType) 
	{
		// check relation type
		if (ddlRelType.getText().equalsIgnoreCase("meets")) {
			// create meets constraint
			rule.addTemporalConstraint(reference, target, RelationType.MEETS, new long[][] {});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("met-by")) {
			// create met-by constraint
			rule.addTemporalConstraint(reference, target, RelationType.MET_BY, new long[][] {});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("during")) { 
			// create during constraint
			rule.addTemporalConstraint(reference, target, 
					RelationType.DURING, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(), 
							ddlRelType.getFirstRange().getMax() > this.horizon ? 
									this.horizon : 
									ddlRelType.getFirstRange().getMax()
						},
						new long[] {
							ddlRelType.getSecondRange().getMin(),
							ddlRelType.getSecondRange().getMax() > this.horizon ? 
									this.horizon :
									ddlRelType.getSecondRange().getMax()
						}
				});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("equals")) {
			// create equals constraint
			rule.addTemporalConstraint(reference, target, RelationType.EQUALS, new long[][] {});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("contains")) {
			// create contains constraint
			rule.addTemporalConstraint(reference, target, 
					RelationType.CONTAINS, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(),
							ddlRelType.getFirstRange().getMax() > this.horizon ?
									this.horizon :
									ddlRelType.getFirstRange().getMax()
						},
						new long[] {
							ddlRelType.getSecondRange().getMin(),
							ddlRelType.getSecondRange().getMax() > this.horizon ?
									this.horizon :
									ddlRelType.getSecondRange().getMax()
						}
				});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("before")) {
			// create before constraint
			rule.addTemporalConstraint(reference, target, 
					RelationType.BEFORE, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(),
							ddlRelType.getFirstRange().getMax() > this.horizon ?
									this.horizon : 
									ddlRelType.getFirstRange().getMax()
						}
				});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("after")) {
			// create before constraint
			rule.addTemporalConstraint(reference, target, 
					RelationType.AFTER, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(),
							ddlRelType.getFirstRange().getMax() > this.horizon ?
									this.horizon : 
									ddlRelType.getFirstRange().getMax()
						}
				});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("starts-during")) {
			rule.addTemporalConstraint(reference, target,
					RelationType.STARTS_DURING, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(),
							ddlRelType.getFirstRange().getMax() > this.horizon ? 
									this.horizon :
									ddlRelType.getFirstRange().getMax()
						},
						new long[] {
							ddlRelType.getSecondRange().getMin(),
							ddlRelType.getSecondRange().getMax() > this.horizon ? 
									this.horizon : 
									ddlRelType.getSecondRange().getMax()
						}
					});
		}
		else if (ddlRelType.getText().equalsIgnoreCase("ends-during")) {
			rule.addTemporalConstraint(reference, target,
					RelationType.ENDS_DURING, 
					new long[][] {
						new long[] {
							ddlRelType.getFirstRange().getMin(),
							ddlRelType.getFirstRange().getMax() > this.horizon ? 
									this.horizon :
									ddlRelType.getFirstRange().getMax()
						},
						new long[] {
								ddlRelType.getSecondRange().getMin(),
								ddlRelType.getSecondRange().getMax() > this.horizon ? 
										this.horizon : 
										ddlRelType.getSecondRange().getMax()
							}
					});
		}
		else {
			/*
			 * TODO : <<<<<------ CHECK OTHER TYPES OF TEMPORAL RELATIONS
			 */
			throw new RuntimeException("Unknown temporal relation " + ddlRelType);
		}
	}
	
	/**
	 * 
	 * @param pdb
	 * @param ddlComponent
	 */
	private void addDiscreteResource(PlanDataBase pdb, DDLComponent ddlComponent)
	{
		// get component name
		String name = ddlComponent.getName();
		// cast component
		DDLRenewableResourceComponentType renewable = (DDLRenewableResourceComponentType) this.ddl_domain.getComponentTypes().get(ddlComponent.getComponentType());
		// get resource capacity
		int capacity = renewable.getCapacity();
		
		// create discrete resource
		DiscreteResource resource = pdb.createDomainComponent(name, DomainComponentType.RESOURCE_DISCRETE);
		// set capacity bounds
		resource.setMinCapacity(0);
		resource.setMaxCapacity(capacity);
		resource.setInitialCapacity(capacity);
		// add component 
		pdb.addDomainComponent(resource);
	}
	
	/**
	 * 
	 * @param pdb
	 * @param ddlComponent
	 */
	private void addReservoirResource(PlanDataBase pdb, DDLComponent ddlComponent)
	{
		// get component name 
		String name = ddlComponent.getName();
		// cast component
		DDLConsumableResourceComponentType consumable = (DDLConsumableResourceComponentType) this.ddl_domain.getComponentTypes().get(ddlComponent.getComponentType());
		// get minimum capacity
		int min = consumable.getMinCapacity();
		int max = consumable.getCapacity();
		// create reservoir resource
		ReservoirResource resource = pdb.createDomainComponent(name, DomainComponentType.RESOURCE_RESERVOIR);
		// set capacity bounds
		resource.setMinCapacity(min);
		resource.setMaxCapacity(max);
		resource.setInitialCapacity(max);
		// add component
		pdb.addDomainComponent(resource);
	}

	/**
	 * 
	 * @param pdb
	 * @param ddlComponent
	 */
	private void addStateVariable(PlanDataBase pdb, DDLComponent ddlComponent) 
	{
		// get instances
		for (DDLTimeline ddlTimeline : ddlComponent.getTimelines()) 
		{
			// component's name
			String name = ddlComponent.getName();
			// set state variable 
			StateVariable sv;
			// check state variable type
			if (ddlTimeline.getParameters().contains("uncontrollable")) {
				// external variable
				sv = pdb.createDomainComponent(name, DomainComponentType.SV_EXTERNAL);
			}
			else if (ddlTimeline.getParameters().contains("functional")) {
				// functional variable
				sv = pdb.createDomainComponent(name, DomainComponentType.SV_FUNCTIONAL);
			}
			else {
				// primitive variable
				sv = pdb.createDomainComponent(name, DomainComponentType.SV_PRIMITIVE);
			}

			// get component type
			DDLSingletonStateVariableComponentType ddlComponentType = (DDLSingletonStateVariableComponentType) this.ddl_domain.getComponentTypes().get(ddlComponent.getComponentType());
			
			// value index
			Map<String, StateVariableValue> vindex = new HashMap<>();
			// creating allowed values
			for (DDLSingletonStateVariableComponentDecisionType ddlValueType : ddlComponentType.getAllowedValues().values()) 
			{
				// add value
				String vname = ddlValueType.getName();
				// check controllability property of the value
				boolean controllable = !vname.startsWith("_");
				
				// set duration constraints
				for (DDLSingletonStateVariableTransitionConstraint ddlTransition : ddlComponentType.getTransitionConstraints()) 
				{
					// check transition on current value
					if (ddlTransition.getFrom().getName().equals(vname)) {
						// get duration bounds
						long dmin = ddlTransition.getRange().getMin();
						long dmax = ddlTransition.getRange().getMax() > pdb.getHorizon() ? 
								pdb.getHorizon() : 
								ddlTransition.getRange().getMax();
								
						
						// add value to the variable
						StateVariableValue val = sv.addStateVariableValue(vname, new long[] {dmin, dmax}, controllable);
						
						// add parameter place-holders to the value
						for (String pname : ddlValueType.getParameterTypes()) {
							// get domain
							ParameterDomain dom = pdb.getParameterDomainByName(pname);
							// add parameter place holder
							val.addParameterPlaceHolder(dom);
						}
						
						// add to local index
						vindex.put(vname, val);
						break;
					}
				}
			}
			
			// set transition constraints
			for (DDLSingletonStateVariableTransitionConstraint ddlTransition : ddlComponentType.getTransitionConstraints()) 
			{
				// transition's parameter index
				Map<String, Map<String, DDLNumericParameterConstraintType>> labels2constraint = new HashMap<>();
				// get transition parameter constraints
				List<DDLParameterConstraint> paramConstraints = ddlTransition.getConstraints();
				for (DDLParameterConstraint cons : paramConstraints) 
				{
					// check constraint type
					switch (cons.getConstraintType()) 
					{
						// enumeration parameter constraint
						case ENUMERATION : 
						{
							// not binary constraint
							throw new RuntimeException("Only binary parameter constraints can be specifeid for variables' transitions");
						}
						
						// numeric parameter constraint
						case NUMERIC : 
						{
							// get numeric parameter constraint
							DDLNumericParameterConstraint numConstraint = (DDLNumericParameterConstraint) cons;
							// check if binary
							if (numConstraint.isBinary()) {
								// get labels
								String referenceLabel = numConstraint.getLeftTerm();
								// initialize structure
								if (!labels2constraint.containsKey(referenceLabel)) {
									labels2constraint.put(referenceLabel, new HashMap<String, DDLNumericParameterConstraintType>());
								}
								
								// check targets
								for (String targetLabel : numConstraint.getRightVariables()) {
									// add entry 
									labels2constraint.get(referenceLabel).put(targetLabel, numConstraint.getNumericConstraintType());
									// add also "reverse" entry
									if (!labels2constraint.containsKey(targetLabel)) {
										labels2constraint.put(targetLabel, new HashMap<String, DDLNumericParameterConstraintType>());
									}
									labels2constraint.get(targetLabel).put(referenceLabel, numConstraint.getNumericConstraintType());
								}
							}
							else {
								// not binary constraint
								throw new RuntimeException("Only binary parameter constraints can be specified for variables' transitions");
							}
						}
						break;
							
						default : {
							// unknown parameter type
							throw new RuntimeException("Unknown parameter constraint type on transition " + cons.getConstraintType());
						}
					}
				}
				
				
				// get related values
				DDLSingletonStateVariableComponentDecision source = ddlTransition.getFrom();
				StateVariableValue from = vindex.get(source.getName());
				for (DDLSingletonStateVariableComponentDecision target : ddlTransition.getTo()) 
				{
					// get target value
					StateVariableValue to = vindex.get(target.getName());
					// add transition constraint
					Transition t = sv.addValueTransition(from, to);
					
					// add parameter constraints
					for (String referenceParName : source.getParameterNames()) 
					{
						// check target parameters
						for (String targetParName : target.getParameterNames()) 
						{
							// check if constraints exists
							if (labels2constraint.containsKey(referenceParName) && labels2constraint.get(referenceParName).containsKey(targetParName)) 
							{
								// get parameter
								DDLNumericParameterConstraintType ddlConsType = labels2constraint.get(referenceParName).get(targetParName);
								// get parameters's indexes
								int referenceIndex = source.getParameterIndex(referenceParName);
								int targetIndex = target.getParameterIndex(targetParName);
								
								// get type of constraint
								ParameterConstraintType consType;
								// check constraint type
								switch (ddlConsType) 
								{
									case EQ : {
										// equals constraint
										consType = ParameterConstraintType.EQUAL;
									}
									break;
									
									case NEQ : {
										// not equal constraint
										consType = ParameterConstraintType.NOT_EQUAL;
									}
									break;
									
									case GE : 
									case GT : 
									case LE : 
									case LT : {
										
										/*
										 * FIXME -> VERIFICARE GESTIONE PARAMETRI "bind" 
										 */
										
										throw new RuntimeException("Manage GE|GT|LE|LT parameter constraint");
									}
									
									default : {
										// unknown parameter constraint type
										throw new RuntimeException("Unknown parameter constraint type into transition " + ddlConsType);
									}
								}
								
								// add parameter constraint
								t.addParameterConstraint(referenceIndex, targetIndex, consType);
							}
						}
					}
				}
			}
			
			// add state variable to plan data-base
			pdb.addDomainComponent(sv);
		}
	}
}
