package it.cnr.istc.pst.platinum.control.acting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.cnr.istc.pst.platinum.ai.deliberative.Planner;
import it.cnr.istc.pst.platinum.ai.executive.Executive;
import it.cnr.istc.pst.platinum.ai.executive.lang.failure.ExecutionFailureCause;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNode;
import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNodeStatus;
import it.cnr.istc.pst.platinum.ai.framework.domain.PlanDataBaseBuilder;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ComponentValue;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.Decision;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.PlanDataBase;
import it.cnr.istc.pst.platinum.ai.framework.domain.component.ex.DecisionPropagationException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.plan.SolutionPlan;
import it.cnr.istc.pst.platinum.ai.framework.utils.properties.FilePropertyReader;
import it.cnr.istc.pst.platinum.control.lang.AgentTaskDescription;
import it.cnr.istc.pst.platinum.control.lang.Goal;
import it.cnr.istc.pst.platinum.control.lang.GoalStatus;
import it.cnr.istc.pst.platinum.control.lang.PlatformFeedback;
import it.cnr.istc.pst.platinum.control.lang.PlatformObservation;
import it.cnr.istc.pst.platinum.control.lang.TokenDescription;
import it.cnr.istc.pst.platinum.control.lang.ex.PlatformException;
import it.cnr.istc.pst.platinum.control.platform.PlatformObserver;
import it.cnr.istc.pst.platinum.control.platform.PlatformProxy;
import it.cnr.istc.pst.platinum.control.platform.PlatformProxyBuilder;
import it.cnr.istc.pst.platinum.control.platform.RunnablePlatformProxy;

/**
 * 
 * @author anacleto
 *
 */
public class GoalOrientedActingAgent implements PlatformObserver
{	
	private final Object lock;								// lock state;
	private ActingAgentStatus status;						// agent status

	private final Map<GoalStatus, List<Goal>> queue;		// goal queue
	
	private String ddl;										// path to the domain specification file
	private PlanDataBase pdb;								// internal plan database representation
	
	private List<Thread> processes;							// goal oriented processes
	private DeliberativeProcess deliberative;				// internal deliberative process
	private Class<? extends Planner> pClass;				// planner class
	private boolean displayPlan;							// display plan flag
	
	private ExecutiveProcess executive;						// internal executive process
	private ContingencyHandlerProcess contingencyHandler;	// internal contingency handler process
	private Class<? extends Executive> eClass;				// executive class
	
	protected PlatformProxy proxy;
	private FilePropertyReader properties;
	
	/**
	 * 
	 * @param agentPropertyFile
	 */
	@SuppressWarnings("unchecked")
	public GoalOrientedActingAgent(String agentPropertyFile) 
	{
		try
		{
			// set lock and status
			this.lock = new Object();
			// set status
			this.status = ActingAgentStatus.OFFLINE;
			// set goal buffer
			this.queue = new HashMap<>();
			// set goal queue
			for (GoalStatus s : GoalStatus.values()) {
				this.queue.put(s, new LinkedList<>());
			}
			
			// set internal plan database representation
			this.pdb = null;
			// set platform
			this.processes = null;
			
			
			// get agent property file
			this.properties = new FilePropertyReader(agentPropertyFile);
			
			// get DDL file 
			String ddlFile = this.properties.getProperty("model");
			// check if null
			if (ddlFile == null || ddlFile.equals("")) {
				throw new RuntimeException("You need to specify an acting model of the agent in \"etc/agent.properties\"!");
			}
			
			// set the model
			this.ddl = ddlFile;
			
			// read the class name of the planner
			String plannerClassName = this.properties.getProperty("planner");
			// set planner class
			this.pClass = (Class<? extends Planner>) Class.forName(plannerClassName);
			// set display plan flag
			this.displayPlan = this.properties.getProperty("display_plan").equals("1") ? true : false;

			// read the class name of the executive
			String executiveClassName = this.properties.getProperty("executive");
			// set executive class
			this.eClass = (Class<? extends Executive>) Class.forName(executiveClassName);
			
			
			
			// read the class of the platform 
			String platformClassName = this.properties.getProperty("platform");
			// check if a platform is necessary
			if (platformClassName != null && !platformClassName.equals("")) 
			{
				// print agent configuration
				System.out.println("Configuration of the Goal-Oriented Acting Agent:\n"
						+ "- Deliberative: " + plannerClassName + "\n"
						+ "- Executive: " + executiveClassName + "\n"
						+ "- Platform: " + platformClassName + "\n");
				
				// get platform configuration file 
				String configFile = this.properties.getProperty("platform_config_file");
				// check platform configuration file 
				if (configFile == null || configFile.equals("")) {
					throw new RuntimeException("Specify a configuration file for the platform in \"" + agentPropertyFile + "\"!");
				}
				
				
				// create platform PROXY
				Class<? extends PlatformProxy> clazz = (Class<? extends PlatformProxy>) Class.forName(platformClassName); 
				// create PROXY
				this.proxy = PlatformProxyBuilder.build(clazz, configFile);
			}
			else 
			{
				// print agent configuration
				System.out.println("Configuration of the Goal-Oriented Acting Agent:\n"
						+ "- deliberative: " + plannerClassName + "\n"
						+ "- executive: " + executiveClassName + "\n");
			}
			
			
			// setup deliberative and executive processes
			this.setupProcesses();
			
			
			
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	
	/**
	 * 
	 */
	private void setupProcesses() 
	{
		// set the list of processes
		this.processes = new ArrayList<>();
		// set goal listener thread
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
		
		
		// set goal deliberative
		this.deliberative = new DeliberativeProcess(this.pClass, this.displayPlan, this);
		this.processes.add(new Thread(this.deliberative));
	
		// set goal executive
		this.executive = new ExecutiveProcess(this.eClass, this);
		this.processes.add(new Thread(this.executive));
	
		// set goal failure handler
		this.contingencyHandler = new ContingencyHandlerProcess(this);
		this.processes.add(new Thread(this.contingencyHandler));
		
		// finally register to platform events
		if (this.proxy != null) {
			this.proxy.register(this);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public synchronized ActingAgentStatus getStatus() {
		return status;
	}
	
	/**
	 * 
	 */
	@Override
	public void task(AgentTaskDescription task) {
		// buffer task planning request
		this.buffer(task);
	}
	
	/**
	 * 
	 */
	@Override
	public void feedback(PlatformFeedback feedback) {
		// nothing to do
	}
	
	/**
	 * 
	 */
	@Override
	public void observation(PlatformObservation<? extends Object> obs) {
		// nothing to do
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
		synchronized (this.queue) 
		{
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
	 * @throws PlatformException
	 */
	public void start() 
			throws InterruptedException, PlatformException
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
		
		// start PROXY if necessary
		if (this.proxy instanceof RunnablePlatformProxy) {
			// start runnable PROXY
			((RunnablePlatformProxy) this.proxy).start();
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
		
		
		// stop platform PROXY
		if (this.proxy instanceof RunnablePlatformProxy) {
			// stop platform PROXY
			((RunnablePlatformProxy) this.proxy).stop();
		}

		
		synchronized (this.lock) {
			// change status
			this.status = ActingAgentStatus.OFFLINE;
			// notify all
			this.lock.notifyAll();
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 * @throws SynchronizationCycleException
	 * @throws PlatformException
	 */
	public void initialize() 
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
		
		// set plan database on the given planning domain
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
		// clear PROXY
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
				SolutionPlan plan = this.deliberative.doHandle(this.pdb);
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
			// execute the plan
			this.executive.doHandle(goal);
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
		
		// repairing result
		boolean success = true;
		// start contingency handling time
		long now = System.currentTimeMillis();
		try
		{
			// repair plan data
			System.out.println("\n\nPLAN REPAIR\n");
			
			// list of kept decisions 
			List<Decision> kept = new ArrayList<>();
			// clear domain components
			for (DomainComponent comp : this.pdb.getComponents())
			{
				// clear component 
				System.out.println("CLEAR COMPONENT : " + comp.getName() + "\n");
				
				// remove all pending decisions
				System.out.println("\nREMOVE ALL PENDING DECISIONS\n");
				// list of pending decisions
				List<Decision> pendings = comp.getPendingDecisions();
				for (Decision pending : pendings) 
				{
					// completely remove decision and related relations
					System.out.println("\nCLEAR DECISION " + pending + " AND RELATED RELATIONS");
					comp.deactivate(pending);
					comp.free(pending);
				}
				
				// get execution trace 
				List<ExecutionNode> trace = goal.getExecutionTraceByComponentName(comp.getName());
				// remove active decisions that have not been executed
				System.out.println("\nREMOVE ALL ACTIVE DECISIONS THAT HAVE NOT BEEN EXECUTED\n");
				// list of active decisions
				List<Decision> actives = comp.getActiveDecisions();
				for (Decision active : actives)
				{
					// check if the token has been executed
					System.out.println("\nACTIVE DECISION " + active + "\n");
					boolean executed = false;
					for (ExecutionNode node : trace) {
						// check if the temporal interval has been executed
						if (node.getInterval().equals(active.getToken().getInterval())){
							executed = true;
							break;
						}
					}
					
					// check flag
					if (executed) {
						// keep the decision as active
						System.out.println("\nKEEP DECISION AS ACTIVE SINCE ALREADY EXECUTED");
						kept.add(active);
					}
					else {
						// clear and remove decision and related relations
						System.out.println("\nREMOVE DECISION AND RELATED RELATIONS SINCE NOT EXECUTED");
						comp.deactivate(active);
						comp.free(active);
					}
				}
			}
			
			
			// check execution failure cause
			ExecutionFailureCause cause = goal.getFailureCause();
			// check type
			switch (cause.getType())
			{
				case NODE_DURATION_OVERFLOW : {
					// keep the decision as active and consider it as executed
					System.out.println("\nHANDLE DURATION OVERFLOW FAILURE\n");
					ExecutionNode node = cause.getInterruptionNode();
					// find the related decision
					for (DomainComponent comp : this.pdb.getComponents()) {
						// get active decisions
						List<Decision> actives = comp.getActiveDecisions();
						for (Decision active : actives) {
							// check temporal intervals
							if (node.getInterval().equals(active.getToken().getInterval())) {
								// keep the decision as active 
								System.out.println("\nKEEP DECISION " + active + "\n");
								kept.add(active);
							}
						}
					}
				}
				break;
				
				case NODE_EXECUTION_ERROR :
				case NODE_START_OVERFLOW : {
					// remove decisions they are going to be re-planned
					System.out.println("\nHANDLE START OVERFLOW FAILURE / EXECUTION ERRROR FAILURE\n");
					ExecutionNode node = cause.getInterruptionNode();
					// find the related decision
					for (DomainComponent comp : this.pdb.getComponents()) {
						// get active decisions
						List<Decision> actives = comp.getActiveDecisions();
						for (Decision active : actives) {
							// check temporal intervals
							if (node.getInterval().equals(active.getToken().getInterval())) {
								// keep the decision as active 
								System.out.println("\nREMOVE DECISION " + active + "\n");
								comp.deactivate(active);
								comp.free(active);
							}
						}
					}
				}
				break;
				
				default:
					throw new RuntimeException("Unknown Execution Failure Cause : " + cause.getType());
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
				
				/*
				 * TODO : check parameter relations
				 */
				
				// create goal decision
				Decision decision = component.create(
						value, 
						labels,
						start,
						end,
						duration,
						ExecutionNodeStatus.IN_EXECUTION);
				
				// add decision to goal list
				System.out.println("REPAIR GOAL : [" + decision.getId() +"]:" + decision.getComponent().getName() + "." + decision.getValue().getLabel() + " "
						+ "AT [" + decision.getStart()[0]  + ", " + decision.getStart()[1] + "] "
						+ "[" + decision.getEnd()[0] + ", " + decision.getEnd()[1] + "] "
						+ "[" + decision.getDuration()[0] + ", " + decision.getDuration()[1] + "]");
			}
			
			
			// deliberate on the current status of the plan database
			SolutionPlan plan = this.contingencyHandler.doHandle(
					this.pClass, 
					this.pdb);
			
			
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
			
			// completely clear all the plan database
			for (DomainComponent comp : this.pdb.getComponents()) {
				// remove all pending decisions
				List<Decision> pendings = comp.getPendingDecisions();
				for (Decision pending : pendings) {
					comp.deactivate(pending);
					comp.free(pending);
					
				}
				
				// remove all active decisions
				List<Decision> actives = comp.getActiveDecisions();
				for (Decision active : actives) {
					comp.deactivate(active);
					comp.free(active);
				}
				
				// finally completely clear component
				comp.clear();
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
