# PLATINUm  
**PLanning and Acting with TImeliNes under Uncertainty**

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)]()
[![Java](https://img.shields.io/badge/Java-8%2B-orange)]()

PLATINUm is an open-source framework for **timeline-based AI planning and execution under temporal uncertainty**.  
It provides a modular architecture for defining domains, synthesizing flexible plans, and executing them while handling uncertain durations and dynamic environments.

The framework is designed for **robotics**, **human-robot collaboration**, **manufacturing**, **logistics**, and **autonomous systems**.

---

## Key Features

- **Timeline-Based Planning**: Model domains with state variables, tokens, timelines, and synchronization rules.  
- **Execution Under Uncertainty**: Dynamic dispatch with flexibility based on temporal networks.  
- **Heuristics & Search Algorithms**: Built-in strategies for efficient plan generation.  
- **Extensible Architecture**: Add new heuristics, planners, temporal reasoners, or resource models.  
- **Resource Management**: Support for discrete and reservoir resources.  
- **Open Source (Apache 2.0)**

---

# Architecture Overview

The diagram below summarizes PLATINUm’s high-level architecture and module interactions.

                       +-----------------------------+
                       |        User / Domain        |
                       |  - Domain Models            |
                       |  - Synchronization Rules    |
                       |  - Resources                |
                       +---------------+-------------+
                                       |
                                       v
               +-----------------------+-----------------------+
               |                    Planner                    |
               |-----------------------------------------------|
               |  Search Engine     |   Heuristics             |
               |  Temporal Checkers |   Resource Constraints   |
               +-----------+-------------------------+---------+
                           |                         |
                           v                         v
              +------------+-----------+   +---------+-------------+
              |   Temporal Reasoner    |   |   Resource Manager    |
              | (STN, flexible bounds) |   | (discrete/reservoir)  |
              +------------+-----------+   +---------+-------------+
                           |                         |
                           +-----------+-------------+
                                       |
                                       v
                      +----------------+-----------------+
                      |               Plan               |
                      |  - Timelines                     |
                      |  - Tokens & ordering             |
                      |  - Temporal flexibility windows  |
                      +----------------+-----------------+
                                       |
                                       v
                 +---------------------+----------------------+
                 |                    Executor                |
                 |--------------------------------------------|
                 |  Dispatcher | Monitoring | Reactions       |
                 |  (executes tokens respecting temporal      |
                 |   flexibility and uncertain durations)     |
                 +--------------------------------------------+

---

# Getting Started

## Requirements
- **Java 8+**
- **Maven 3.6+**


## Installation

```bash
git clone https://github.com/pstlab/PLATINUm.git
cd PLATINUm
mvn clean install

```

### Using PLATINUm in your project 

You you can include PLATINUm library into your project as Maven package. 

Add the following dependency to the ```pom.xml``` file of your project to get the latest distributed version

```
<dependency>
    <groupId>it.uniroma3.platinum</groupId>
    <artifactId>platinum</artifactId>
    <version>[1.0.0,)</version>
</dependency>
```

---

## Project Structure

```
PLATINUm
├── etc
│   ├── agent.properties
│   ├── deliberative.properties
│   └── executive.properties
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── it
    │           └── cnr
    │               └── istc
    │                   └── pst
    │                       └── platinum
    │                           ├── ai
    │                           │   ├── deliberative
    │                           │   │   ├── heuristic
    │                           │   │   │   ├── CompleteFlawSelectionHeuristic.java
    │                           │   │   │   ├── FlawSelectionHeuristic.java
    │                           │   │   │   ├── HierarchicalFlawSelectionHeuristic.java
    │                           │   │   │   ├── pipeline
    │                           │   │   │   │   ├── FailFirstFlawInspector.java
    │                           │   │   │   │   ├── FlawInspector.java
    │                           │   │   │   │   ├── HierarchicalPlanFlawInspector.java
    │                           │   │   │   │   ├── HierarchyFlawInspector.java
    │                           │   │   │   │   ├── PipelineFlawSelectionHeuristic.java
    │                           │   │   │   │   ├── PreferenceFlawInspector.java
    │                           │   │   │   │   └── ReverseHierarchyFlawInspector.java
    │                           │   │   │   ├── RandomFlawSelectionHeuristic.java
    │                           │   │   │   └── ReverseHierarchicalFlawSelectionHeuristic.java
    │                           │   │   ├── PlannerBuilder.java
    │                           │   │   ├── Planner.java
    │                           │   │   ├── solver
    │                           │   │   │   ├── Operator.java
    │                           │   │   │   ├── PseudoControllabilityAwareSolver.java
    │                           │   │   │   ├── SearchSpaceNode.java
    │                           │   │   │   └── Solver.java
    │                           │   │   └── strategy
    │                           │   │       ├── CostDepthSearchStrategy.java
    │                           │   │       ├── DepthFirstSearchStrategy.java
    │                           │   │       ├── ex
    │                           │   │       │   └── EmptyFringeException.java
    │                           │   │       ├── GreedyDepthSearchStrategy.java
    │                           │   │       ├── SearchStrategy.java
    │                           │   │       ├── StandardDeviationMinimizationSearchStrategy.java
    │                           │   │       └── WeightedAStarSearchStrategy.java
    │                           │   ├── executive
    │                           │   │   ├── AtomicClockManager.java
    │                           │   │   ├── ClockManager.java
    │                           │   │   ├── dispatcher
    │                           │   │   │   ├── ConditionCheckingDispatcher.java
    │                           │   │   │   └── Dispatcher.java
    │                           │   │   ├── ExecutionManager.java
    │                           │   │   ├── ExecutionStatus.java
    │                           │   │   ├── ExecutiveBuilder.java
    │                           │   │   ├── Executive.java
    │                           │   │   ├── lang
    │                           │   │   │   ├── ex
    │                           │   │   │   │   ├── ExecutionException.java
    │                           │   │   │   │   ├── ExecutionPreparationException.java
    │                           │   │   │   │   ├── NodeDispatchingException.java
    │                           │   │   │   │   ├── NodeExecutionErrorException.java
    │                           │   │   │   │   └── NodeObservationException.java
    │                           │   │   │   ├── ExecutionFeedback.java
    │                           │   │   │   ├── ExecutionFeedbackType.java
    │                           │   │   │   └── failure
    │                           │   │   │       ├── ExecutionFailureCause.java
    │                           │   │   │       ├── ExecutionFailureCauseType.java
    │                           │   │   │       ├── NodeDurationOverflow.java
    │                           │   │   │       ├── NodeExecutionError.java
    │                           │   │   │       ├── NodeStartOverflow.java
    │                           │   │   │       └── PlanRepairInformation.java
    │                           │   │   ├── monitor
    │                           │   │   │   ├── ConditionCheckingMonitor.java
    │                           │   │   │   └── Monitor.java
    │                           │   │   ├── pdb
    │                           │   │   │   ├── ControllabilityType.java
    │                           │   │   │   ├── ExecutionNode.java
    │                           │   │   │   ├── ExecutionNodeStatus.java
    │                           │   │   │   ├── ExecutivePlanDataBaseBuilder.java
    │                           │   │   │   ├── ExecutivePlanDataBase.java
    │                           │   │   │   ├── ExecutivePlanDataBaseType.java
    │                           │   │   │   └── NodePredicate.java
    │                           │   │   └── PlanExecutionObserver.java
    │                           │   ├── framework
    │                           │   │   ├── domain
    │                           │   │   │   ├── component
    │                           │   │   │   │   ├── ComponentValue.java
    │                           │   │   │   │   ├── ComponentValueType.java
    │                           │   │   │   │   ├── Constraint.java
    │                           │   │   │   │   ├── Decision.java
    │                           │   │   │   │   ├── DomainComponent.java
    │                           │   │   │   │   ├── DomainComponentType.java
    │                           │   │   │   │   ├── ex
    │                           │   │   │   │   │   ├── DecisionNotFoundException.java
    │                           │   │   │   │   │   ├── DecisionPropagationException.java
    │                           │   │   │   │   │   ├── FlawSolutionApplicationException.java
    │                           │   │   │   │   │   ├── RelationPropagationException.java
    │                           │   │   │   │   │   ├── ResourceProfileComputationException.java
    │                           │   │   │   │   │   └── TransitionNotFoundException.java
    │                           │   │   │   │   ├── ParameterPlaceHolder.java
    │                           │   │   │   │   ├── pdb
    │                           │   │   │   │   │   ├── DecisionVariable.java
    │                           │   │   │   │   │   ├── ParameterSynchronizationConstraint.java
    │                           │   │   │   │   │   ├── PlanDataBaseComponent.java
    │                           │   │   │   │   │   ├── PlanDataBaseEvent.java
    │                           │   │   │   │   │   ├── PlanDataBaseEventType.java
    │                           │   │   │   │   │   ├── PlanDataBaseObserver.java
    │                           │   │   │   │   │   ├── SynchronizationConstraint.java
    │                           │   │   │   │   │   ├── SynchronizationRule.java
    │                           │   │   │   │   │   ├── TemporalSynchronizationConstraint.java
    │                           │   │   │   │   │   └── TokenVariable.java
    │                           │   │   │   │   ├── PlanDataBase.java
    │                           │   │   │   │   ├── PlanElementStatus.java
    │                           │   │   │   │   ├── Predicate.java
    │                           │   │   │   │   ├── resource
    │                           │   │   │   │   │   ├── discrete
    │                           │   │   │   │   │   │   ├── DiscreteResource.java
    │                           │   │   │   │   │   │   ├── DiscreteResourceProfile.java
    │                           │   │   │   │   │   │   ├── RequirementResourceEvent.java
    │                           │   │   │   │   │   │   ├── RequirementResourceProfileSample.java
    │                           │   │   │   │   │   │   └── RequirementResourceValue.java
    │                           │   │   │   │   │   ├── reservoir
    │                           │   │   │   │   │   │   ├── ConsumptionResourceEvent.java
    │                           │   │   │   │   │   │   ├── ProductionResourceEvent.java
    │                           │   │   │   │   │   │   ├── ReservoirResource.java
    │                           │   │   │   │   │   │   ├── ReservoirResourceProfile.java
    │                           │   │   │   │   │   │   ├── ResourceConsumptionValue.java
    │                           │   │   │   │   │   │   ├── ResourceProductionValue.java
    │                           │   │   │   │   │   │   ├── ResourceUsageProfileSample.java
    │                           │   │   │   │   │   │   └── ResourceUsageValue.java
    │                           │   │   │   │   │   ├── ResourceEvent.java
    │                           │   │   │   │   │   ├── ResourceEventType.java
    │                           │   │   │   │   │   ├── Resource.java
    │                           │   │   │   │   │   ├── ResourceProfile.java
    │                           │   │   │   │   │   └── ResourceProfileSample.java
    │                           │   │   │   │   ├── sv
    │                           │   │   │   │   │   ├── ExternalStateVariable.java
    │                           │   │   │   │   │   ├── FunctionalStateVariable.java
    │                           │   │   │   │   │   ├── PrimitiveStateVariable.java
    │                           │   │   │   │   │   ├── RequirementStateVariableResourceEvent.java
    │                           │   │   │   │   │   ├── StateVariable.java
    │                           │   │   │   │   │   ├── StateVariableResourceProfile.java
    │                           │   │   │   │   │   ├── StateVariableValue.java
    │                           │   │   │   │   │   ├── Transition.java
    │                           │   │   │   │   │   └── ValuePath.java
    │                           │   │   │   │   └── Token.java
    │                           │   │   │   ├── DomainComponentBuilder.java
    │                           │   │   │   ├── knowledge
    │                           │   │   │   │   ├── DomainKnowledge.java
    │                           │   │   │   │   ├── DomainKnowledgeType.java
    │                           │   │   │   │   ├── ex
    │                           │   │   │   │   │   └── HierarchyCycleException.java
    │                           │   │   │   │   └── StaticDomainKnowledge.java
    │                           │   │   │   └── PlanDataBaseBuilder.java
    │                           │   │   ├── microkernel
    │                           │   │   │   ├── annotation
    │                           │   │   │   │   ├── cfg
    │                           │   │   │   │   │   ├── deliberative
    │                           │   │   │   │   │   │   ├── FlawSelectionHeuristicsConfiguration.java
    │                           │   │   │   │   │   │   ├── PipelineConfiguration.java
    │                           │   │   │   │   │   │   ├── PlannerSolverConfiguration.java
    │                           │   │   │   │   │   │   └── SearchStrategyConfiguration.java
    │                           │   │   │   │   │   ├── executive
    │                           │   │   │   │   │   │   ├── DispatcherConfiguration.java
    │                           │   │   │   │   │   │   └── MonitorConfiguration.java
    │                           │   │   │   │   │   ├── framework
    │                           │   │   │   │   │   │   ├── DomainComponentConfiguration.java
    │                           │   │   │   │   │   │   ├── DomainKnowledgeConfiguration.java
    │                           │   │   │   │   │   │   ├── ParameterFacadeConfiguration.java
    │                           │   │   │   │   │   │   └── TemporalFacadeConfiguration.java
    │                           │   │   │   │   │   └── FrameworkLoggerConfiguration.java
    │                           │   │   │   │   ├── inject
    │                           │   │   │   │   │   ├── deliberative
    │                           │   │   │   │   │   │   ├── FlawSelectionHeuristicPlaceholder.java
    │                           │   │   │   │   │   │   ├── PipelinePlaceholder.java
    │                           │   │   │   │   │   │   ├── PlannerPlaceholder.java
    │                           │   │   │   │   │   │   ├── PlannerSolverPlaceholder.java
    │                           │   │   │   │   │   │   └── SearchStrategyPlaceholder.java
    │                           │   │   │   │   │   ├── executive
    │                           │   │   │   │   │   │   ├── DispatcherPlaceholder.java
    │                           │   │   │   │   │   │   ├── ExecutivePlaceholder.java
    │                           │   │   │   │   │   │   ├── ExecutivePlanDataBasePlaceholder.java
    │                           │   │   │   │   │   │   └── MonitorPlaceholder.java
    │                           │   │   │   │   │   ├── framework
    │                           │   │   │   │   │   │   ├── DomainComponentPlaceholder.java
    │                           │   │   │   │   │   │   ├── DomainKnowledgePlaceholder.java
    │                           │   │   │   │   │   │   ├── ParameterFacadePlaceholder.java
    │                           │   │   │   │   │   │   ├── ParameterSolverPlaceholder.java
    │                           │   │   │   │   │   │   ├── PlanDataBasePlaceholder.java
    │                           │   │   │   │   │   │   ├── ResolverListPlaceholder.java
    │                           │   │   │   │   │   │   ├── TemporalFacadePlaceholder.java
    │                           │   │   │   │   │   │   ├── TemporalNetworkPlaceholder.java
    │                           │   │   │   │   │   │   └── TemporalSolverPlaceholder.java
    │                           │   │   │   │   │   └── FrameworkLoggerPlaceholder.java
    │                           │   │   │   │   └── lifecycle
    │                           │   │   │   │       └── PostConstruct.java
    │                           │   │   │   ├── ConstraintCategory.java
    │                           │   │   │   ├── ExecutiveObject.java
    │                           │   │   │   ├── FrameworkObject.java
    │                           │   │   │   ├── lang
    │                           │   │   │   │   ├── ex
    │                           │   │   │   │   │   ├── ConsistencyCheckException.java
    │                           │   │   │   │   │   ├── ConstraintPropagationException.java
    │                           │   │   │   │   │   ├── DomainComponentNotFoundException.java
    │                           │   │   │   │   │   ├── NoFlawFoundException.java
    │                           │   │   │   │   │   ├── NoSolutionFoundException.java
    │                           │   │   │   │   │   ├── OperatorPropagationException.java
    │                           │   │   │   │   │   ├── PlanRefinementException.java
    │                           │   │   │   │   │   ├── ProblemInitializationException.java
    │                           │   │   │   │   │   └── SynchronizationCycleException.java
    │                           │   │   │   │   ├── flaw
    │                           │   │   │   │   │   ├── FlawCategoryType.java
    │                           │   │   │   │   │   ├── Flaw.java
    │                           │   │   │   │   │   ├── FlawSolution.java
    │                           │   │   │   │   │   └── FlawType.java
    │                           │   │   │   │   ├── plan
    │                           │   │   │   │   │   ├── Agenda.java
    │                           │   │   │   │   │   ├── PlanControllabilityType.java
    │                           │   │   │   │   │   ├── Plan.java
    │                           │   │   │   │   │   ├── Profile.java
    │                           │   │   │   │   │   ├── SolutionPlan.java
    │                           │   │   │   │   │   └── Timeline.java
    │                           │   │   │   │   ├── problem
    │                           │   │   │   │   │   ├── ParameterProblemConstraint.java
    │                           │   │   │   │   │   ├── ProblemConstraint.java
    │                           │   │   │   │   │   ├── ProblemFact.java
    │                           │   │   │   │   │   ├── ProblemFluent.java
    │                           │   │   │   │   │   ├── ProblemFluentType.java
    │                           │   │   │   │   │   ├── ProblemGoal.java
    │                           │   │   │   │   │   ├── Problem.java
    │                           │   │   │   │   │   └── TemporalProblemConstraint.java
    │                           │   │   │   │   └── relations
    │                           │   │   │   │       ├── parameter
    │                           │   │   │   │       │   ├── BindParameterRelation.java
    │                           │   │   │   │       │   ├── EqualParameterRelation.java
    │                           │   │   │   │       │   ├── NotEqualParameterRelation.java
    │                           │   │   │   │       │   └── ParameterRelation.java
    │                           │   │   │   │       ├── Relation.java
    │                           │   │   │   │       ├── RelationType.java
    │                           │   │   │   │       └── temporal
    │                           │   │   │   │           ├── AfterRelation.java
    │                           │   │   │   │           ├── BeforeRelation.java
    │                           │   │   │   │           ├── ContainsRelation.java
    │                           │   │   │   │           ├── DuringRelation.java
    │                           │   │   │   │           ├── EndEndRelation.java
    │                           │   │   │   │           ├── EndsDuringRelation.java
    │                           │   │   │   │           ├── EqualsRelation.java
    │                           │   │   │   │           ├── MeetsRelation.java
    │                           │   │   │   │           ├── MetByRelation.java
    │                           │   │   │   │           ├── StartsDuringRelation.java
    │                           │   │   │   │           ├── StartStartRelation.java
    │                           │   │   │   │           └── TemporalRelation.java
    │                           │   │   │   ├── query
    │                           │   │   │   │   ├── ParameterQueryFactory.java
    │                           │   │   │   │   ├── ParameterQuery.java
    │                           │   │   │   │   ├── ParameterQueryType.java
    │                           │   │   │   │   ├── Query.java
    │                           │   │   │   │   ├── QueryManager.java
    │                           │   │   │   │   ├── TemporalQueryFactory.java
    │                           │   │   │   │   ├── TemporalQuery.java
    │                           │   │   │   │   └── TemporalQueryType.java
    │                           │   │   │   └── resolver
    │                           │   │   │       ├── ex
    │                           │   │   │       │   ├── InvalidBehaviorException.java
    │                           │   │   │       │   ├── NotFeasibleExpansionException.java
    │                           │   │   │       │   ├── NotFeasibleGapCompletionException.java
    │                           │   │   │       │   ├── NotFeasibleUnificationException.java
    │                           │   │   │       │   └── UnsolvableFlawException.java
    │                           │   │   │       ├── FlawManager.java
    │                           │   │   │       ├── plan
    │                           │   │   │       │   ├── GoalExpansion.java
    │                           │   │   │       │   ├── Goal.java
    │                           │   │   │       │   ├── GoalJustification.java
    │                           │   │   │       │   ├── GoalSchedule.java
    │                           │   │   │       │   ├── GoalUnification.java
    │                           │   │   │       │   ├── PlanRefinementResolver.java
    │                           │   │   │       │   └── TimelineAwarePlanRefinementResolver.java
    │                           │   │   │       ├── ResolverBuilder.java
    │                           │   │   │       ├── Resolver.java
    │                           │   │   │       ├── ResolverType.java
    │                           │   │   │       ├── resource
    │                           │   │   │       │   ├── discrete
    │                           │   │   │       │   │   ├── CriticalSet.java
    │                           │   │   │       │   │   ├── DiscreteResourceSchedulingResolver.java
    │                           │   │   │       │   │   └── PrecedenceConstraint.java
    │                           │   │   │       │   └── reservoir
    │                           │   │   │       │       ├── ReservoirOverflow.java
    │                           │   │   │       │       ├── ReservoirResourceSchedulingResolver.java
    │                           │   │   │       │       └── ResourceEventSchedule.java
    │                           │   │   │       └── timeline
    │                           │   │   │           ├── behavior
    │                           │   │   │           │   ├── checking
    │                           │   │   │           │   │   ├── IncompleteBehavior.java
    │                           │   │   │           │   │   ├── InvalidTransition.java
    │                           │   │   │           │   │   ├── MissingObservation.java
    │                           │   │   │           │   │   ├── ObservationBehaviorCheckingResolver.java
    │                           │   │   │           │   │   └── TimelineBehaviorCheckingResolver.java
    │                           │   │   │           │   └── planning
    │                           │   │   │           │       ├── GapCompletion.java
    │                           │   │   │           │       ├── Gap.java
    │                           │   │   │           │       ├── GapType.java
    │                           │   │   │           │       └── TimelineBehaviorPlanningResolver.java
    │                           │   │   │           └── scheduling
    │                           │   │   │               ├── OverlappingSet.java
    │                           │   │   │               ├── OverlappingSetSchedule.java
    │                           │   │   │               ├── PrecedenceConstraint.java
    │                           │   │   │               └── TimelineSchedulingResolver.java
    │                           │   │   ├── parameter
    │                           │   │   │   ├── csp
    │                           │   │   │   │   ├── event
    │                           │   │   │   │   │   ├── AddConstraintParameterNotification.java
    │                           │   │   │   │   │   ├── AddParameterNotification.java
    │                           │   │   │   │   │   ├── DelConstraintParameterNotification.java
    │                           │   │   │   │   │   ├── DelParameterNotification.java
    │                           │   │   │   │   │   ├── ParameterNotificationFactory.java
    │                           │   │   │   │   │   ├── ParameterNotification.java
    │                           │   │   │   │   │   ├── ParameterNotificationObserver.java
    │                           │   │   │   │   │   └── ParameterNotificationType.java
    │                           │   │   │   │   └── solver
    │                           │   │   │   │       ├── choco
    │                           │   │   │   │       │   └── v4
    │                           │   │   │   │       │       └── ChocoSolver.java
    │                           │   │   │   │       ├── ParameterSolver.java
    │                           │   │   │   │       └── ParameterSolverType.java
    │                           │   │   │   ├── ex
    │                           │   │   │   │   ├── ParameterConsistencyException.java
    │                           │   │   │   │   ├── ParameterConstraintNotFoundException.java
    │                           │   │   │   │   ├── ParameterConstraintPropagationException.java
    │                           │   │   │   │   ├── ParameterCreationException.java
    │                           │   │   │   │   └── ParameterNotFoundException.java
    │                           │   │   │   ├── lang
    │                           │   │   │   │   ├── constraints
    │                           │   │   │   │   │   ├── BinaryParameterConstraint.java
    │                           │   │   │   │   │   ├── BindParameterConstraint.java
    │                           │   │   │   │   │   ├── EqualParameterConstraint.java
    │                           │   │   │   │   │   ├── ExcludeParameterConstraint.java
    │                           │   │   │   │   │   ├── NotEqualParameterConstraint.java
    │                           │   │   │   │   │   ├── ParameterConstraintFactory.java
    │                           │   │   │   │   │   ├── ParameterConstraint.java
    │                           │   │   │   │   │   └── ParameterConstraintType.java
    │                           │   │   │   │   ├── EnumerationParameterDomain.java
    │                           │   │   │   │   ├── EnumerationParameter.java
    │                           │   │   │   │   ├── NumericParameterDomain.java
    │                           │   │   │   │   ├── NumericParameter.java
    │                           │   │   │   │   ├── ParameterDomain.java
    │                           │   │   │   │   ├── ParameterDomainType.java
    │                           │   │   │   │   ├── Parameter.java
    │                           │   │   │   │   ├── ParameterType.java
    │                           │   │   │   │   └── query
    │                           │   │   │   │       ├── CheckValuesParameterQuery.java
    │                           │   │   │   │       └── ComputeSolutionParameterQuery.java
    │                           │   │   │   ├── ParameterFacadeBuilder.java
    │                           │   │   │   └── ParameterFacade.java
    │                           │   │   ├── protocol
    │                           │   │   │   ├── lang
    │                           │   │   │   │   ├── ParameterDescriptor.java
    │                           │   │   │   │   ├── ParameterTypeDescriptor.java
    │                           │   │   │   │   ├── PlanProtocolDescriptor.java
    │                           │   │   │   │   ├── ProtocolLanguageFactory.java
    │                           │   │   │   │   ├── relation
    │                           │   │   │   │   │   ├── AfterRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── BeforeRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── ContainsRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── DuringRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── EndEndRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── EndsDuringRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── EqualsRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── MeetsRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── MetByRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── OverlappedByRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── RelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── RelationProtocolLanguageFactory.java
    │                           │   │   │   │   │   ├── StartsDuringRelationProtocolDescriptor.java
    │                           │   │   │   │   │   ├── StartsRelationProtocolDescriptor.java
    │                           │   │   │   │   │   └── StartStartRelationProtocolDescriptor.java
    │                           │   │   │   │   ├── TimelineProtocolDescriptor.java
    │                           │   │   │   │   ├── TokenProtocolDescriptor.java
    │                           │   │   │   │   └── UnallocatedTokenDescription.java
    │                           │   │   │   └── query
    │                           │   │   │       ├── get
    │                           │   │   │       │   ├── GetFlexibleTimelinesProtocolQuery.java
    │                           │   │   │       │   ├── GetPlanProtocolQuery.java
    │                           │   │   │       │   └── GetSingleFlexibleTimelineProtocolQuery.java
    │                           │   │   │       ├── ProtocolQueryFactory.java
    │                           │   │   │       ├── ProtocolQuery.java
    │                           │   │   │       ├── ProtocolQueryType.java
    │                           │   │   │       └── show
    │                           │   │   │           └── ShowComponentProtocolQuery.java
    │                           │   │   ├── time
    │                           │   │   │   ├── ex
    │                           │   │   │   │   ├── InconsistentIntervaEndTimeException.java
    │                           │   │   │   │   ├── InconsistentIntervalDurationException.java
    │                           │   │   │   │   ├── InconsistentIntervalStartTimeException.java
    │                           │   │   │   │   ├── PseudoControllabilityException.java
    │                           │   │   │   │   ├── TemporalConsistencyException.java
    │                           │   │   │   │   ├── TemporalConstraintPropagationException.java
    │                           │   │   │   │   ├── TemporalIntervalCreationException.java
    │                           │   │   │   │   └── TimePointCreationException.java
    │                           │   │   │   ├── lang
    │                           │   │   │   │   ├── allen
    │                           │   │   │   │   │   ├── AfterIntervalConstraint.java
    │                           │   │   │   │   │   ├── BeforeIntervalConstraint.java
    │                           │   │   │   │   │   ├── ContainsIntervalConstraint.java
    │                           │   │   │   │   │   ├── DuringIntervalConstraint.java
    │                           │   │   │   │   │   ├── EndsDuringIntervalConstraint.java
    │                           │   │   │   │   │   ├── EqualsIntervalConstraint.java
    │                           │   │   │   │   │   ├── MeetsIntervalConstraint.java
    │                           │   │   │   │   │   ├── MetByIntervalConstraint.java
    │                           │   │   │   │   │   └── StartsDuringIntervalConstraint.java
    │                           │   │   │   │   ├── BinaryTemporalConstraint.java
    │                           │   │   │   │   ├── FixIntervalDurationConstraint.java
    │                           │   │   │   │   ├── FixTimePointConstraint.java
    │                           │   │   │   │   ├── query
    │                           │   │   │   │   │   ├── IntervalDistanceQuery.java
    │                           │   │   │   │   │   ├── IntervalOverlapQuery.java
    │                           │   │   │   │   │   ├── IntervalPseudoControllabilityQuery.java
    │                           │   │   │   │   │   ├── IntervalScheduleQuery.java
    │                           │   │   │   │   │   └── TemporalIntervalQuery.java
    │                           │   │   │   │   ├── TemporalConstraintFactory.java
    │                           │   │   │   │   ├── TemporalConstraint.java
    │                           │   │   │   │   ├── TemporalConstraintType.java
    │                           │   │   │   │   └── UnaryTemporalConstraint.java
    │                           │   │   │   ├── solver
    │                           │   │   │   │   ├── apsp
    │                           │   │   │   │   │   ├── APSPTemporalSolver.java
    │                           │   │   │   │   │   └── DistanceGraph.java
    │                           │   │   │   │   ├── TemporalSolver.java
    │                           │   │   │   │   └── TemporalSolverType.java
    │                           │   │   │   ├── TemporalFacadeBuilder.java
    │                           │   │   │   ├── TemporalFacade.java
    │                           │   │   │   ├── TemporalInterval.java
    │                           │   │   │   └── tn
    │                           │   │   │       ├── ex
    │                           │   │   │       │   ├── DistanceConstraintNotFoundException.java
    │                           │   │   │       │   ├── InconsistentDistanceConstraintException.java
    │                           │   │   │       │   ├── InconsistentTpValueException.java
    │                           │   │   │       │   ├── IntervalDisjunctionException.java
    │                           │   │   │       │   ├── NotCompatibleConstraintsFoundException.java
    │                           │   │   │       │   ├── TemporalNetworkTransactionFailureException.java
    │                           │   │   │       │   ├── TimePointNotFoundException.java
    │                           │   │   │       │   └── UnableToHandleContingentConstraintsException.java
    │                           │   │   │       ├── lang
    │                           │   │   │       │   ├── event
    │                           │   │   │       │   │   ├── AddRelationTemporalNetworkNotification.java
    │                           │   │   │       │   │   ├── AddTimePointTemporalNetworkNotification.java
    │                           │   │   │       │   │   ├── DelRelationTemporalNetworkNotification.java
    │                           │   │   │       │   │   ├── DelTimePointTemporalNetworkNotification.java
    │                           │   │   │       │   │   ├── ex
    │                           │   │   │       │   │   │   └── NotificationPropagationFailureException.java
    │                           │   │   │       │   │   ├── InitializationTemporalNetworkNotifaction.java
    │                           │   │   │       │   │   ├── TemporalNetworkNotificationFactory.java
    │                           │   │   │       │   │   ├── TemporalNetworkNotification.java
    │                           │   │   │       │   │   ├── TemporalNetworkNotificationTypes.java
    │                           │   │   │       │   │   └── TemporalNetworkObserver.java
    │                           │   │   │       │   └── query
    │                           │   │   │       │       ├── TimePointDistanceQuery.java
    │                           │   │   │       │       ├── TimePointDistanceToHorizonQuery.java
    │                           │   │   │       │       ├── TimePointQuery.java
    │                           │   │   │       │       └── TimePointScheduleQuery.java
    │                           │   │   │       ├── SimpleTemporalNetworkWithUncertainty.java
    │                           │   │   │       ├── TemporalData.java
    │                           │   │   │       ├── TemporalNetwork.java
    │                           │   │   │       ├── TemporalNetworkType.java
    │                           │   │   │       ├── TimePointDistanceConstraint.java
    │                           │   │   │       └── TimePoint.java
    │                           │   │   └── utils
    │                           │   │       ├── log
    │                           │   │       │   ├── FrameworkLogger.java
    │                           │   │       │   └── FrameworkLoggingLevel.java
    │                           │   │       ├── properties
    │                           │   │       │   └── FilePropertyReader.java
    │                           │   │       ├── reflection
    │                           │   │       │   └── FrameworkReflectionUtils.java
    │                           │   │       └── view
    │                           │   │           ├── component
    │                           │   │           │   ├── ComponentViewFactory.java
    │                           │   │           │   ├── ComponentView.java
    │                           │   │           │   ├── ComponentViewType.java
    │                           │   │           │   └── gantt
    │                           │   │           │       └── GanttComponentView.java
    │                           │   │           └── executive
    │                           │   │               └── ExecutiveWindow.java
    │                           │   └── lang
    │                           │       └── ddl
    │                           │           ├── DomainCompilerFactory.java
    │                           │           ├── DomainCompiler.java
    │                           │           ├── DomainCompilerType.java
    │                           │           ├── ex
    │                           │           │   └── PDLFileMissingException.java
    │                           │           └── v3
    │                           │               ├── ddl3.g
    │                           │               ├── ddl3.tokens
    │                           │               ├── DDLv3Compiler.java
    │                           │               └── parser
    │                           │                   ├── ddl3Lexer.java
    │                           │                   ├── ddl3Parser.java
    │                           │                   ├── DDLComponentDecision.java
    │                           │                   ├── DDLComponentDecisionType.java
    │                           │                   ├── DDLComponent.java
    │                           │                   ├── DDLComponentType.java
    │                           │                   ├── DDLConsumableResourceComponentDecision.java
    │                           │                   ├── DDLConsumableResourceComponentType.java
    │                           │                   ├── DDLDomain.java
    │                           │                   ├── DDLEnumerationParameterConstraint.java
    │                           │                   ├── DDLEnumerationParameterType.java
    │                           │                   ├── DDLGenerator.java
    │                           │                   ├── DDLInstantiatedComponentDecision.java
    │                           │                   ├── DDLNumericParameterConstraint.java
    │                           │                   ├── DDLNumericParameterType.java
    │                           │                   ├── DDLParameterConstraint.java
    │                           │                   ├── DDLParameterConstraintType.java
    │                           │                   ├── DDLParameterSet.java
    │                           │                   ├── DDLParameterType.java
    │                           │                   ├── DDLProblem.java
    │                           │                   ├── DDLRange.java
    │                           │                   ├── DDLRenewableResourceComponentDecision.java
    │                           │                   ├── DDLRenewableResourceComponentType.java
    │                           │                   ├── DDLSimpleGroundStateVariableComponentDecision.java
    │                           │                   ├── DDLSimpleGroundStateVariableComponentDecisionType.java
    │                           │                   ├── DDLSimpleGroundStateVariableComponentType.java
    │                           │                   ├── DDLSimpleGroundStateVariableTransitionConstraint.java
    │                           │                   ├── DDLSingletonStateVariableComponentDecision.java
    │                           │                   ├── DDLSingletonStateVariableComponentDecisionType.java
    │                           │                   ├── DDLSingletonStateVariableComponentType.java
    │                           │                   ├── DDLSingletonStateVariableTransitionConstraint.java
    │                           │                   ├── DDLSynchronization.java
    │                           │                   ├── DDLTemporalModule.java
    │                           │                   ├── DDLTemporalRelation.java
    │                           │                   ├── DDLTemporalRelationType.java
    │                           │                   ├── DDLTimeline.java
    │                           │                   ├── DDLTimelineSynchronization.java
    │                           │                   └── PDLGenerator.java
    │                           ├── control
    │                           │   ├── acting
    │                           │   │   ├── ActingAgentStatus.java
    │                           │   │   ├── ContingencyHandlerProcess.java
    │                           │   │   ├── DeliberativeProcess.java
    │                           │   │   ├── ExecutiveProcess.java
    │                           │   │   └── GoalOrientedActingAgent.java
    │                           │   ├── lang
    │                           │   │   ├── AgentTaskDescription.java
    │                           │   │   ├── ex
    │                           │   │   │   ├── PlatformCommunicationException.java
    │                           │   │   │   └── PlatformException.java
    │                           │   │   ├── Goal.java
    │                           │   │   ├── GoalStatus.java
    │                           │   │   ├── PlatformCommandDescription.java
    │                           │   │   ├── PlatformCommand.java
    │                           │   │   ├── PlatformFeedback.java
    │                           │   │   ├── PlatformFeedbackType.java
    │                           │   │   ├── PlatformMessage.java
    │                           │   │   ├── PlatformObservation.java
    │                           │   │   └── TokenDescription.java
    │                           │   └── platform
    │                           │       ├── PlatformObserver.java
    │                           │       ├── PlatformProxyBuilder.java
    │                           │       ├── PlatformProxy.java
    │                           │       └── RunnablePlatformProxy.java
    │                           ├── executive
    │                           │   └── dc
    │                           │       ├── DCDispatcher.java
    │                           │       ├── DCExecutive.java
    │                           │       ├── DCMonitor.java
    │                           │       ├── DCResult.java
    │                           │       ├── DCResultType.java
    │                           │       ├── DispatchDCResult.java
    │                           │       ├── FailureDCResult.java
    │                           │       ├── PlanExecutionStatus.java
    │                           │       ├── strategy
    │                           │       │   ├── clock
    │                           │       │   │   ├── ClockRelation.java
    │                           │       │   │   └── ClockSet.java
    │                           │       │   ├── ListStrategy.java
    │                           │       │   ├── loader
    │                           │       │   │   └── StrategyLoader.java
    │                           │       │   ├── result
    │                           │       │   │   ├── Action.java
    │                           │       │   │   ├── Transition.java
    │                           │       │   │   └── Wait.java
    │                           │       │   ├── State.java
    │                           │       │   ├── StateSet.java
    │                           │       │   ├── StateStrategy.java
    │                           │       │   ├── Strategy.java
    │                           │       │   ├── TreeNodeState.java
    │                           │       │   └── TreeStrategy.java
    │                           │       └── WaitDCResult.java
    │                           └── stats
    │                               ├── ModelDataset.java
    │                               ├── mongo
    │                               │   └── MongoModelDataset.java
    │                               └── TokenExecutionData.java
    └── test
        └── java
            └── it
                └── cnr
                    └── istc
                        └── pst
                            └── platinum
                                ├── domain
                                │   └── component
                                │       ├── pdb
                                │       │   └── PlanDataBaseTestCase.java
                                │       ├── resource
                                │       │   └── DiscreteResourceComponentTestCase.java
                                │       └── sv
                                │           ├── ExternalStateVariableComponentTestCase.java
                                │           └── StateVariableComponentTestCase.java
                                ├── parameter
                                │   ├── csp
                                │   │   └── v4
                                │   │       ├── ChochoSolverUnitTest.java
                                │   │       └── ParameterTestFactory.java
                                │   └── facade
                                │       └── CSPParameterDataBaseFacadeTest.java
                                ├── stats
                                │   └── mongo
                                │       └── MongoModelDatasetTest.java
                                ├── testing
                                │   └── executive
                                │       └── dc
                                │           └── strategy
                                │               ├── TestListStrategy.java
                                │               └── TestTreeStrategy.java
                                └── time
                                    ├── facade
                                    │   └── UncertaintyTemporalDataBaseFacadeTestCase.java
                                    ├── reasoner
                                    │   └── apsp
                                    │       └── APSPSolverTestCase.java
                                    └── tn
                                        ├── SimpleTemporalNetworkTestCase.java
                                        └── SimpleTemporalNetworkWithUncertaintyTestCaseTest.java

```
