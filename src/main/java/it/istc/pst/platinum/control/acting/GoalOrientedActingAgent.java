package it.istc.pst.platinum.control.acting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.control.lang.Goal;
import it.istc.pst.platinum.control.lang.GoalStatus;
import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;

/**
 * 
 * @author anacleto
 *
 */
public class GoalOrientedActingAgent
{
	private final Object lock;							// lock state;
	private ActingAgentStatus status;					// agent status

	private final Map<GoalStatus, List<Goal>> queue;	// goal queue
	
	private PlanDataBase pdb;							// internal plan database representation
	
	private Thread listener;							// goal listener thread
	private Thread deliberative;						// goal deliberative thread
	private Thread executive;							// goal executive thread
	private Thread failureHandler;						// goal failure handler
	
	/**
	 * 
	 */
	public GoalOrientedActingAgent() {
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
		// initialize goal listener thread
		this.listener = new Thread(new Runnable() {

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
						Goal goal = waitGoalWithStatus(GoalStatus.BUFFERED);
						// simply select the extracted goal
						select(goal);
					}
					catch (InterruptedException ex) {
						running = false;
					}
				}
			}
		});
		
		// initialize goal deliberative
		this.deliberative = new Thread(new Runnable() {
			
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
						// take a goal to plan for
						Goal goal = waitGoalWithStatus(GoalStatus.SELECTED);
						// deliberate extracted goal
						boolean success = plan(goal);
						// check deliberative result
						if (success) {
							// commit planned goal
							commit(goal);
						}
						else {
							// deliberative failure abort goal
							abort(goal);
						}
					}
					catch (InterruptedException ex) {
						running = false;
					}
				}
			}
		});
	
	
		// initialize goal executive
		this.executive = new Thread(new Runnable() {
			
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
						// take a goal to plan for
						Goal goal = waitGoalWithStatus(GoalStatus.COMMITTED);
						// execute extracted goal
						boolean success = execute(goal);
						// check executive result
						if (success) {
							// goal execution successfully complete
							finish(goal);
						}
						else {
							// goal execution suspended due to some errors
							suspend(goal);
						}
					}
					catch (InterruptedException ex) {
						running = false;
					}
				}
			}
		});
	
	
		// initialize goal failure handler
		this.failureHandler = new Thread(new Runnable() {
			
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
						// take a goal to plan for
						Goal goal = waitGoalWithStatus(GoalStatus.SUSPENDED);
						// try to repair the goal 
						boolean success = repair(goal);
						// check executive result
						if (success) {
							// goal repaired try to execute it again
							commit(goal);
						}
						else {
							// goal aborted due to unsolvable failure during execution 
							abort(goal);
						}
					}
					catch (InterruptedException ex) {
						running = false;
					}
				}
			}
		});
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized ActingAgentStatus getStatus() {
		return status;
	}
	
	/**
	 * Entry point of a goal
	 * 
	 * @param goal
	 */
	public void buffer(Goal goal) {
		// protect access to the queue
		synchronized (this.queue) {
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
			
			// take a goal
			goals.addAll(this.queue.get(GoalStatus.ABORTED));
			goals.addAll(this.queue.get(GoalStatus.FINISHED));
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
	protected void commit(Goal goal) {
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
		
		
		// start buffered goal listener
		this.listener.start();
		// start deliberative 
		this.deliberative.start();
		// start executive 
		this.executive.start();
		// start failure handler
		this.failureHandler.start();

		synchronized (this.lock) {
			// change status
			this.status = ActingAgentStatus.RUNNING;
			// notify all
			this.lock.notifyAll();
		}
	}
	
	
	/**
	 * 
	 * @param ddl
	 * @throws InterruptedException
	 * @throws SynchronizationCycleException
	 */
	public void initialize(String ddl) 
			throws InterruptedException, SynchronizationCycleException
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
		
		// initialize plan database
		this.pdb = PlanDataBaseBuilder.createAndSet(ddl);
		
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
	protected void clear() 
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
		
		/*
		 * TODO : Do something
		 */
		
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

		/*
		 * TODO : Do something
		 */
		
		// setup planner
		Planner planner = PlannerBuilder.createAndSet(this.pdb);
		
		
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
		
		/*
		 * TODO : do something
		 */
		
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
		
		// re-planning result
		boolean success = true;
		
		/*
		 * TODO : do something
		 */
		
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
	protected Goal waitGoalWithStatus(GoalStatus status) 
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
			queue.notifyAll();
		}
		
		// get extracted goal
		return goal;
	}
}
