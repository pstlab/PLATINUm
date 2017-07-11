package it.istc.pst.platinum.executive.est;

import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.pdb.ExecutionNodeStatus;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.ExecutiveDispatcherConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.ExecutiveMonitorConfiguration;
import it.istc.pst.platinum.framework.utils.view.executive.ExecutiveWindow;

/**
 * 
 * @author anacleto
 *
 */
public class EarliestStartTimeExecutive extends Executive
{
	private ExecutiveWindow window;				// executive window
	
	/**
	 * 
	 */
	@ExecutiveMonitorConfiguration(monitor = EarliestStartTimePlanMonitor.class)
	@ExecutiveDispatcherConfiguration(dispatcher = EarliestStartTimePlanDispatcher.class)
	public EarliestStartTimeExecutive() 
	{
		super();
		// create plan monitor
		this.monitor = new EarliestStartTimePlanMonitor(this);
		// create dispatcher
		this.dispatcher = new EarliestStartTimePlanDispatcher(this);
		// create executive window
		this.window = new ExecutiveWindow("Executive Window");
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onTick(long tick)
	{
		boolean complete = false;
		try 
		{
			System.out.println("\n##################################################");
			System.out.println("#### Handle tick = " + tick + " ####");
			System.out.println("#### Synchronization step ####");
			// synchronization step
			this.monitor.handleTick(tick);
			System.out.println("#### Dispatching step ####");
			// dispatching step
			this.dispatcher.handleTick(tick);
			System.out.println("##################################################");
			// display executive window
			this.displayWindow();
			// check if execution is complete
			complete = this.pdb.getNodesByStatus(ExecutionNodeStatus.WAITING).isEmpty() &&
					this.pdb.getNodesByStatus(ExecutionNodeStatus.IN_EXECUTION).isEmpty();
		}
		catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
		return complete;
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	private void displayWindow() 
			throws InterruptedException 
	{
		// get tau
		long tau = this.getTau();
		// set the data-set to show
		this.window.setDataSet(this.pdb.getHorizon(), this.getNodes());
		// display current execution state
		this.window.display(tau);
	}
}
