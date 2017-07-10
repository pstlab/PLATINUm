package it.uniroma3.epsl2.executive.est;

import it.uniroma3.epsl2.executive.Executive;
import it.uniroma3.epsl2.executive.pdb.ExecutionNodeStatus;
import it.uniroma3.epsl2.framework.microkernel.annotation.cfg.executive.ExecutiveConfiguration;
import it.uniroma3.epsl2.framework.utils.view.executive.ExecutiveWindow;

/**
 * 
 * @author anacleto
 *
 */
@ExecutiveConfiguration(

	// set dispatcher
	dispatcher = EarliesStartTimePlanDispatcher.class,
	
	// set monitor
	monitor = EarliestStartTimePlanMonitor.class
)
public class EarliestStartTimeExecutive extends Executive
{
	private ExecutiveWindow window;				// executive window
	
	/**
	 * 
	 */
	public EarliestStartTimeExecutive() 
	{
		super();
		// create plan monitor
		this.monitor = new EarliestStartTimePlanMonitor(this);
		// create dispatcher
		this.dispatcher = new EarliesStartTimePlanDispatcher(this);
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
