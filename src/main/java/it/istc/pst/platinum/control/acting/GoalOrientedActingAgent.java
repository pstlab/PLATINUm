package it.istc.pst.platinum.control.acting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.control.lang.AgentTaskDescription;
import it.istc.pst.platinum.control.lang.Goal;
import it.istc.pst.platinum.control.lang.GoalStatus;
import it.istc.pst.platinum.control.lang.TokenDescription;
import it.istc.pst.platinum.control.platform.PlatformProxy;
import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.executive.lang.ex.DurationOverflow;
import it.istc.pst.platinum.executive.lang.ex.PlanRepairInformation;
import it.istc.pst.platinum.executive.lang.ex.StartOverflow;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.Decision;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.domain.component.ex.DecisionPropagationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;
import it.istc.pst.platinum.framework.microkernel.lang.relations.Relation;
import it.istc.pst.platinum.framework.microkernel.lang.relations.RelationType;
import it.istc.pst.platinum.framework.microkernel.lang.relations.temporal.MeetsRelation;

/**
 * 
 * @author anacleto
 *
 */
public class GoalOrientedActingAgent
{
	private final Object lock;								// lock state;
	private ActingAgentStatus status;						// agent status

	private final Map<GoalStatus, List<Goal>> queue;		// goal queue
	
	private String ddl;										// path to the domain specification file
	private PlanDataBase pdb;								// internal plan database representation
	
	private List<Thread> processes;							// goal oriented processes
	private DeliberativeProcess deliberative;				// internal deliberative process
	private ExecutiveProcess executive;						// internal executive process
	private ContingencyHandlerProcess contingencyHandler;	// internal contingency handler process
	
	private PlatformProxy proxy;
	
	/**
	 * 
	 */
	public GoalOrientedActingAgent() 
	{
		// initialize lock and status
		this.lock = new Object(); 
		this.status = ActingAgentStatus.OFFLINE;
		// initialize goal buffer
		this.queue = new HashMap<>();
		// initialize goal queue
		for (GoalStatus s : GoalStatus.values()) {
			this.queue.put(s, new LinkedList<>());
		}
		
		// initialize internal plan database representation
		this.pdb = null;
		// initialize the list of processes
		this.processes = new ArrayList<>();
		// initialize goal listener thread
		this.processes.add(new Thread(new Runnable() {

			/**
			 * 
			 */
			@Override
			public void run() {
				boolean running = true;
				while(running)
				{
					try
					{
						// check buffered goals
						Goal goal = waitGoal(GoalStatus.BUFFERED);
						System.out.println("Selecting goal.. \n" + goal + "\n");
						// simply select the extracted goal
						select(goal);
					}
					catch (InterruptedException ex) {
						running = false;
					}
				}
			}
		}));
		
		// initialize goal deliberative
		this.deliberative = new DeliberativeProcess(this);
		this.processes.add(new Thread(this.deliberative));
	
		// initialize goal executive
		this.executive = new ExecutiveProcess(this);
		this.processes.add(new Thread(this.executive));
	
		// initialize goal failure handler
		this.contingencyHandler = new ContingencyHandlerProcess(this);
		this.processes.add(new Thread(this.contingencyHandler));
		
		// initialize proxy reference
		this.proxy = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized ActingAgentStatus getStatus() {
		return status;
	}
	
	/**
	 * Trigger acting process by buffering a description of a goal to plan and execute for
	 * 
	 * @param description
	 */
	public void buffer(AgentTaskDescription description) {
		// protect access to the queue
		synchronized (this.queue) {
			System.out.println("receiving task ...\n" + description + "\n");
			// create goal 
			Goal goal = new Goal(description);
			// set goal status
			goal.setStatus(GoalStatus.BUFFERED);
			// add a goal to the queue
			this.queue.get(goal.getStatus()).add(goal);
			// send signal
			this.queue.notifyAll();
		}
	}
	
	/**
	 * Blocking call returning a list of finished or aborted goals.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public List<Goal> getResults() 
			throws InterruptedException
	{
		// wait some finished or aborted goal
		List<Goal> goals = new ArrayList<>();
		synchronized (this.queue) {
			while (this.queue.get(GoalStatus.ABORTED).isEmpty() && 
					this.queue.get(GoalStatus.FINISHED).isEmpty()) {
				// wait
				this.queue.wait();
			}
			
			// take aborted goals
			goals.addAll(this.queue.get(GoalStatus.ABORTED));
			// clear queue
			this.queue.get(GoalStatus.ABORTED).clear();
			// take finished goals
			goals.addAll(this.queue.get(GoalStatus.FINISHED));
			// clear queue
			this.queue.get(GoalStatus.FINISHED).clear();
			
			// send signal
			this.queue.notifyAll();
		}
		
		// get finished and aborted goals
		return goals;
	}
	
	/**
	 * 
	 */
	protected void select(Goal goal) {
		// protect access to the queue
		synchronized (this.queue) {
			// remove goal form the current queue
			this.queue.get(goal.getStatus()).remove(goal);
			// set goal status
			goal.setStatus(GoalStatus.SELECTED);
			// add goal to the queue
			this.queue.get(goal.getStatus()).add(goal);
			// send signal
			this.queue.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	protected void commit(Goal goal) 
	{
		// protect access to the queue
		synchronized (this.queue) {
			// remove goal form the current queue
			this.queue.get(goal.getStatus()).remove(goal);
			// set goal status
			goal.setStatus(GoalStatus.COMMITTED);
			// add goal to the queue
			this.queue.get(goal.getStatus()).add(goal);
			// send signal
			this.queue.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	protected void suspend(Goal goal) {
		// protect access to the queue
		synchronized (this.queue) {
			// remove goal form the current queue
			this.queue.get(goal.getStatus()).remove(goal);
			// set goal status
			goal.setStatus(GoalStatus.SUSPENDED);
			// add goal to the queue
			this.queue.get(goal.getStatus()).add(goal);
			// send signal
			this.queue.notifyAll();
		}
	}

	/**
	 * 
	 */
	protected void finish(Goal goal) {
		// protect access to the queue
		synchronized (this.queue) {
			// remove goal form the current queue
			this.queue.get(goal.getStatus()).remove(goal);
			// set goal status
			goal.setStatus(GoalStatus.FINISHED);
			// add goal to the queue
			this.queue.get(goal.getStatus()).add(goal);
			// send signal
			this.queue.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	protected void abort(Goal goal) {
		// protect access to the queue
		synchronized (this.queue) {
			// remove goal form the current queue
			this.queue.get(goal.getStatus()).remove(goal);
			// set goal status
			goal.setStatus(GoalStatus.ABORTED);
			// add goal to the queue
			this.queue.get(goal.getStatus()).add(goal);
			// send signal
			this.queue.notifyAll();
		}		
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void start() 
			throws InterruptedException
	{
		synchronized (this.lock) {
			while (!this.status.equals(ActingAgentStatus.OFFLINE)) {
				// wait 
				this.lock.wait();
			}
			
			// change status
			this.status = ActingAgentStatus.STARTING;
			// send signal
			this.lock.notifyAll();
		}
		
		
		// start all internal processes
		for (Thread p : this.processes) {
			p.start();
		}

		synchronized (this.lock) {
			// change status
			this.status = ActingAgentStatus.RUNNING;
			// notify all
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void stop() 
			throws InterruptedException, PlatformException
	{
		synchronized (this.lock) {
			while (!this.status.equals(ActingAgentStatus.READY) && 
					!this.status.equals(ActingAgentStatus.RUNNING)) {
				// wait 
				this.lock.wait();
			}
			
			// change status
			this.status = ActingAgentStatus.STOPPING;
			// send signal
			this.lock.notifyAll();
		}
		
		
		// interrupt internal processes and wait termination
		for (Thread p : this.processes) {
			p.interrupt();
			p.join();
		}
		
//		// stop proxy
//		if (this.proxy != null) {
//			this.proxy.stop();
//		}

		synchronized (this.lock) {
			// change status
			this.status = ActingAgentStatus.OFFLINE;
			// notify all
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 * @param ddl
	 * @throws InterruptedException
	 * @throws SynchronizationCycleException
	 * @throws PlatformException
	 */
	public void initialize(PlatformProxy proxy, String ddl) 
			throws InterruptedException, SynchronizationCycleException, PlatformException
	{
		synchronized (this.lock) {
			while(!this.status.equals(ActingAgentStatus.RUNNING)) {
				// wait a signal
				this.lock.wait();
			}
			
			// change status
			this.status = ActingAgentStatus.INITIALIZING;
			// send signal 
			this.lock.notifyAll();
		}
		
		// set platform proxy
		this.proxy = proxy;
		// set domain specification file
		this.ddl = ddl;
		// initialize plan database on the given planning domain
		this.pdb = PlanDataBaseBuilder.createAndSet(this.ddl);
		
		
		synchronized (this.lock) {
			// change status
			this.status = ActingAgentStatus.READY;
			// send signal
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void clear() 
			throws InterruptedException
	{
		synchronized (this.lock) {
			while (!this.status.equals(ActingAgentStatus.FAILURE) && 
					!this.status.equals(ActingAgentStatus.READY)) {
				// wait
				this.lock.wait();
			}
			
			// change status
			this.status = ActingAgentStatus.CLEARNING;
			// send signal 
			this.lock.notifyAll();
		}
		
		// clear queue
		this.queue.clear();
		// clear domain file specification
		this.ddl = null;
		// clear plan database 
		this.pdb = null;
		// clear proxy
		this.proxy = null;
		
		synchronized (this.lock) {
			// change status
			this.status = ActingAgentStatus.RUNNING;
			// send signal
			this.lock.notifyAll();
		}
 	}

	/**
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws NoSolutionFoundException 
	 */
	protected boolean plan(Goal goal) 
			throws InterruptedException
	{
		// wait when planning can be actually performed if necessary
		synchronized (this.lock) {
			while (!this.status.equals(ActingAgentStatus.READY)) {
				// wait 
				this.lock.wait();
			}
			
			// change status
			this.status = ActingAgentStatus.DELIBERATING;
			// send signal
			this.lock.notifyAll();
		}
		
		// planning process result
		boolean success = true;
		
		// list of goal decisions
		List<Decision> goals = new ArrayList<>();
		// list of fact decisions
		List<Decision> facts = new ArrayList<>();
		try
		{
			// get task description
			AgentTaskDescription task = goal.getTaskDescription();
			// set known information concerning components
			for (TokenDescription f : task.getFacts()) 
			{
				// get domain component
				DomainComponent component = this.pdb.getComponentByName(f.getComponent());
				// get goal referred value
				ComponentValue value = component.getValueByName(f.getValue());
				// check start time bound
				long[] start = f.getStart();
				if (start == null) {
					start = new long[] {
						this.pdb.getOrigin(),
						this.pdb.getHorizon()
					};
				}
				
				// check end time bound
				long[] end = f.getEnd();
				if (end == null) {
					end = new long[] {
						this.pdb.getOrigin(),
						this.pdb.getHorizon()
					};
				}
				
				// check duration bound
				long[] duration = f.getDuration();
				if (duration == null) {
					duration = new long[] {
						value.getDurationLowerBound(),
						value.getDurationUpperBound()
					};
				}
				
				// check labels
				String[] labels = f.getLabels();
				if (labels == null) {
					labels = new String[] {};
				}
				
				// create fact decision
				Decision decision = component.create(
						value, 
						labels,
						start,
						end,
						duration
						);
				
				// also activate fact decision
				component.activate(decision);
				// add decision to fact list
				facts.add(decision);
			}
			
			// set planning goals 
			for (TokenDescription g : task.getGoals()) 
			{
				// get domain component
				DomainComponent component = this.pdb.getComponentByName(g.getComponent());
				// get goal referred value
				ComponentValue value = component.getValueByName(g.getValue());
				// check start time bound
				long[] start = g.getStart();
				if (start == null) {
					start = new long[] {
						this.pdb.getOrigin(),
						this.pdb.getHorizon()
					};
				}
				
				// check end time bound
				long[] end = g.getEnd();
				if (end == null) {
					end = new long[] {
						this.pdb.getOrigin(),
						this.pdb.getHorizon()
					};
				}
				
				// check duration bound
				long[] duration = g.getDuration();
				if (duration == null) {
					duration = new long[] {
						value.getDurationLowerBound(),
						value.getDurationUpperBound()
					};
				}
				
				// check labels
				String[] labels = g.getLabels();
				if (labels == null) {
					labels = new String[] {};
				}
				
				// create goal decision
				Decision decision = component.create(
						value, 
						labels,
						start,
						end,
						duration
						);
				
				// add decision to goal list
				goals.add(decision);
			}
			
			
			// start planning time
			long now = System.currentTimeMillis();
			try
			{
				// deliberate on the current status of the plan database
				SolutionPlan plan = this.deliberative.doPlan(this.pdb);
				// set generated plan
				goal.setPlan(plan);
			}
			catch (NoSolutionFoundException ex) {
				// failure - no plan can be found
				success = false;
				// remove and deactivate facts
				for (Decision f : facts) {
					f.getComponent().deactivate(f);
					f.getComponent().free(f);
				}
				
				// remove and deactivate goals
				for (Decision g : goals) {
					g.getComponent().deactivate(g);
					g.getComponent().free(g);
				}
			}
			finally 
			{
				// compute actual planning time
				long time = System.currentTimeMillis() - now;
				// add planning time attempt to the goal
				goal.addPlanningAttempt(time);
			}
		}
		catch (DecisionPropagationException ex) {
			// problem setup error 
			success = false;
			// remove and deactivate facts
			for (Decision f : facts) {
				f.getComponent().deactivate(f);
				f.getComponent().free(f);
			}
			
			// remove and deactivate goals
			for (Decision g : goals) {
				g.getComponent().deactivate(g);
				g.getComponent().free(g);
			}
			
			// print an error message
			System.err.println("Error while propagating intial facts from task description:\n"
					+ "\t- message: " + ex.getMessage() + "\n");
		}
		
		
		// update agent status
		synchronized (this.lock) {
			// update status according to the result of the planning process
			if (success) {
				this.status = ActingAgentStatus.READY;
			}
			else {
				// failure
				this.status = ActingAgentStatus.FAILURE;
			}
			
			// send signal
			this.lock.notifyAll();
		}
		
		// return planning process result
		return success;
	}
	
	/**
	 * 
	 * @param goal
	 * @return
	 * @throws InterruptedException
	 */
	protected boolean execute(Goal goal) 
			throws InterruptedException
	{
		synchronized (this.lock) {
			while (!this.status.equals(ActingAgentStatus.READY)) {
				// wait
				this.lock.wait();
			}
			
			// update status
			this.status = ActingAgentStatus.EXECUTING;
			// send signal
			this.lock.notifyAll();
 		}
		
		// execution result
		boolean complete = true;
		// start execution time
		long now = System.currentTimeMillis();
		try 
		{
			// initialize the executive on proxy
			this.executive.initialize(this.proxy);
			// execution the plan synthesized for the goal
			this.executive.doExecute(goal);
		}
		catch (Exception ex) {
			// execution failure
			complete = false;
		}
		finally 
		{
			// compute actual execution time
			long time = System.currentTimeMillis() - now;
			// add execution attempt time
			goal.addExecutionAttempt(time);
		}
		
		// update agent status
		synchronized (this.lock) {
			// update status according to the execution results
			if (complete) {
				this.status = ActingAgentStatus.READY;
			}
			else {
				this.status = ActingAgentStatus.SUSPENDED;
			}
			
			// send signal
			this.lock.notifyAll();
		}
		
		// return execution result
		return complete;
	}
	
	/**
	 * 
	 * @param goal
	 * @return
	 * @throws InterruptedException
	 */
	protected boolean repair(Goal goal) 
			throws InterruptedException
	{
		synchronized (this.lock) {
			while (!this.status.equals(ActingAgentStatus.SUSPENDED)) {
				// wait
				this.lock.wait();
			}
			
			// update status
			this.status = ActingAgentStatus.DELIBERATING;
			// send signal
			this.lock.notifyAll();
 		}
		
		
		// list of activated facts
		List<Decision> facts = new ArrayList<>();
		// list of activated relations
		List<Relation> relations = new ArrayList<>();
		// list of planning goals
		List<Decision> goals = new ArrayList<>();
		
		// repairing result
		boolean success = true;
		// start contingency handling time
		long now = System.currentTimeMillis();
		try
		{
			// initialize plan database on the given planning domain
			this.pdb = PlanDataBaseBuilder.createAndSet(this.ddl);
			System.out.println("\n\nREPAIRING PROBLEM SPECIFICATION:\n");
			
			// setup the initial fact leveraging goal execution trace
			for (DomainComponent component : this.pdb.getComponents()) 
			{
				// get component execution trace
				for (ExecutionNode node : goal.getExecutionTraceByComponentName(component.getName())) 
				{	
					// get value
					ComponentValue value = component.getValueByName(node.getSignature());
					// create fact 
					Decision fact = component.create(
							value, 
							node.getParameters(), 
							node.getStart(), 
							node.getEnd(), 
							node.getDuration(), 
							node.getStatus());
					
					// activate fact
					component.activate(fact);
					// activated fact
					System.out.println(">>>>> FACT : " + node.getStatus() + " : [" + fact.getId() +"]:" + fact.getComponent().getName() + "." + fact.getValue().getLabel() + " "
							+ "AT [" + fact.getStart()[0]  + ", " + fact.getStart()[1] + "] "
									+ "[" + fact.getEnd()[1] + ", " + fact.getEnd()[1] + "] "
									+ "[" + fact.getDuration()[0] + ", " + fact.getDuration()[1] + "]");
					// add fact
					facts.add(fact);
				}
			}
			
			// check also failure cause and add the related node as fact
			switch (goal.getFailureCause().getType()) 
			{
				// start overflow contingency
				case START_OVERFLOW : {
					// get cause
					StartOverflow cause = (StartOverflow) goal.getFailureCause();
					// get observed start
					long start = cause.getObservedStartTime();
					// get node 
					ExecutionNode node = cause.getInterruptionNode();
					// get component
					DomainComponent comp = this.pdb.getComponentByName(node.getComponent());
					// get value
					// get value
					ComponentValue value = comp.getValueByName(node.getSignature());
					// create fact 
					Decision fact = comp.create(
							value, 
							node.getParameters(), 
							new long[] {start, start}, 
							node.getEnd(), 
							node.getDuration(), 
							ExecutionNodeStatus.EXECUTED);
					
					// activate fact
					comp.activate(fact);
					// activated fact
					System.out.println(">>>>> OBSERVED-FACT : [" + fact.getId() +"]:" + fact.getComponent().getName() + "." + fact.getValue().getLabel() + " "
							+ "AT [" + fact.getStart()[0]  + ", " + fact.getStart()[1] + "] "
									+ "[" + fact.getEnd()[1] + ", " + fact.getEnd()[1] + "] "
									+ "[" + fact.getDuration()[0] + ", " + fact.getDuration()[1] + "]");
					// add fact
					facts.add(fact);
				}
				break;
			
				// duration overflow contingency
				case DURATION_OVERFLOW : {
					// get cause
					DurationOverflow cause = (DurationOverflow) goal.getFailureCause();
					// get observed duration
					long duration = cause.getObservedDuration();
					// get node 
					ExecutionNode node = cause.getInterruptionNode();
					// get component
					DomainComponent comp = this.pdb.getComponentByName(node.getComponent());
					// get value
					// get value
					ComponentValue value = comp.getValueByName(node.getSignature());
					// create fact 
					Decision fact = comp.create(
							value, 
							node.getParameters(), 
							node.getStart(), 
							node.getEnd(), 
							new long[] {duration, duration}, 
							ExecutionNodeStatus.EXECUTED);
					
					// activate fact
					comp.activate(fact);
					// activated fact
					System.out.println(">>>>> OBSERVED-FACT : [" + fact.getId() +"]:" + fact.getComponent().getName() + "." + fact.getValue().getLabel() + " "
							+ "AT [" + fact.getStart()[0]  + ", " + fact.getStart()[1] + "] "
									+ "[" + fact.getEnd()[1] + ", " + fact.getEnd()[1] + "] "
									+ "[" + fact.getDuration()[0] + ", " + fact.getDuration()[1] + "]");
					// add fact
					facts.add(fact);
				}
				break;
				
				default: {
					
					// unknown failure type
					System.err.println("Unknown goal execution failure type : " + goal.getFailureCause().getType() + "\n");
				}
				break;
			}
			
			// check other plan repair information 
			for (PlanRepairInformation info : goal.getFailureCause().getRepairInfo()) 
			{
				// get node 
				ExecutionNode node = info.getNode();
				// get component
				DomainComponent comp = this.pdb.getComponentByName(node.getComponent());
				// get value
				// get value
				ComponentValue value = comp.getValueByName(node.getSignature());
				// create fact 
				Decision fact = comp.create(
						value, 
						node.getParameters(),
						node.getStart(),
						node.getEnd(), 
						new long[] {info.getDuration(), info.getDuration()}, 
						ExecutionNodeStatus.EXECUTED);
				
				// activate fact
				comp.activate(fact);
				// activated fact
				System.out.println(">>>>> REPAIR-INFO-FACT : [" + fact.getId() +"]:" + fact.getComponent().getName() + "." + fact.getValue().getLabel() + " "
						+ "AT [" + fact.getStart()[0]  + ", " + fact.getStart()[1] + "] "
								+ "[" + fact.getEnd()[1] + ", " + fact.getEnd()[1] + "] "
								+ "[" + fact.getDuration()[0] + ", " + fact.getDuration()[1] + "]");
				// add fact
				facts.add(fact);
			}
			
			
			
			// add meets relations to enforce the timeline semantics
			for (DomainComponent component : this.pdb.getComponents()) 
			{
				// enforce timeline semantics through meets relations
				List<Decision> list = component.getActiveDecisions();
				for (int index = 0; index < list.size() -1; index++) 
				{
					// create meet relations between adjacent tokens
					Decision reference = list.get(index);
					Decision target = list.get(index + 1);
					// create meets relation
					MeetsRelation rel = component.create(RelationType.MEETS, reference, target);
					// activate relation
					component.activate(rel);
					// add relation to the list of activated relations
					relations.add(rel);
					
					System.out.println(">>>> RELATION: [" + reference.getId() + "]:" + reference.getComponent().getName() + "." + reference.getValue().getLabel() + " "
							+ "" + rel.getType() + " "
							+ " [" + target.getId() + "]: " + target.getComponent().getName() + "." + target.getValue().getLabel());
				}
			}
			
			
			// get task description
			AgentTaskDescription task = goal.getTaskDescription();
			// set planning goals 
			for (TokenDescription g : task.getGoals()) 
			{
				// get domain component
				DomainComponent component = this.pdb.getComponentByName(g.getComponent());
				// get goal referred value
				ComponentValue value = component.getValueByName(g.getValue());
				// check start time bound
				long[] start = g.getStart();
				if (start == null) {
					start = new long[] {
						this.pdb.getOrigin(),
						this.pdb.getHorizon()
					};
				}
				
				// check end time bound
				long[] end = g.getEnd();
				if (end == null) {
					end = new long[] {
						this.pdb.getOrigin(),
						this.pdb.getHorizon()
					};
				}
				
				// check duration bound
				long[] duration = g.getDuration();
				if (duration == null) {
					duration = new long[] {
						value.getDurationLowerBound(),
						value.getDurationUpperBound()
					};
				}
				
				// check labels
				String[] labels = g.getLabels();
				if (labels == null) {
					labels = new String[] {};
				}
				
				// create goal decision
				Decision decision = component.create(
						value, 
						labels,
						start,
						end,
						duration,
						ExecutionNodeStatus.IN_EXECUTION);
				
				// add decision to goal list
				goals.add(decision);
			}
			
			// deliberate on the current status of the plan database
			SolutionPlan plan = this.contingencyHandler.doHandle(this.pdb);
			// set repaired plan
			goal.setPlan(plan);
			// set goal as repaired
			goal.setRepaired(true);
			// set the tick the execution will start
			goal.setExecutionTick(goal.getFailureCause().getInterruptionTick());
			// clear execution trace
			goal.clearExecutionTrace();
		}
		catch (Exception ex) 
		{
			// error while repairing
			success = false;
			// error message
			System.err.println("Error while trying to repair the plan\n"
					+ "\t- message: " + ex.getMessage() + "\n");
			
			// remove and deactivate facts
			for (Decision f : facts) {
				f.getComponent().deactivate(f);
				f.getComponent().free(f);
			}
			
			// remove and deactivate goals
			for (Decision g : goals) {
				g.getComponent().deactivate(g);
				g.getComponent().free(g);
			}
			
			
			// remove all relations (only local expected)
			for (Relation rel : relations) {
				DomainComponent comp = rel.getReference().getComponent();
				comp.deactivate(rel);
				comp.delete(rel);
			}
		}
		finally 
		{
			// compute actual planning time
			long time = System.currentTimeMillis() - now;
			// add planning time attempt to the goal
			goal.addContingencyHandlingAttempt(time);
			goal.addPlanningAttempt(time);
		}
		
		
		
		synchronized (this.lock) {
			// update status according to the execution results
			if (success) {
				this.status = ActingAgentStatus.READY;
			}
			else {
				this.status = ActingAgentStatus.FAILURE;
			}
			
			// send signal
			this.lock.notifyAll();
		}
		
		// return execution result
		return success;
	}
	
	/**
	 * 
	 * @param status
	 * @throws InterruptedException
	 */
	protected Goal waitGoal(GoalStatus status) 
			throws InterruptedException
	{
		// goal 
		Goal goal = null;
		// wait a selected goal
		synchronized (this.queue) {
			// check selected buffer
			while (this.queue.get(status).isEmpty()) {
				// wait a selected goal
				this.queue.wait();
			}
			
			// remove the first selected goal from the queue
			goal = this.queue.get(status).remove(0);
			// send signal
			this.queue.notifyAll();
		}
		
		// get extracted goal
		return goal;
	}
}
