package it.istc.pst.platinum.executive.dc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.deliberative.PlannerBuilder;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.executive.ExecutiveBuilder;
import it.istc.pst.platinum.executive.dc.tga.TGADCChecker;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.DispatcherConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.cfg.executive.MonitorConfiguration;
import it.istc.pst.platinum.framework.microkernel.annotation.lifecycle.PostConstruct;
import it.istc.pst.platinum.framework.microkernel.lang.plan.SolutionPlan;

/**
 * 
 * @author anacleto
 *
 */
@MonitorConfiguration(monitor = DCMonitor.class)
@DispatcherConfiguration(dispatcher = DCDispatcher.class)
public class DCExecutive extends Executive 
{
	protected DCChecker checker;						// dynamic controllability checker
	private final BlockingQueue<String> feedbacks;		// input queue containing execution feedbacks
	
	/**
	 * 
	 */
	protected DCExecutive() {
		super();
		// initialize blocking queue
		this.feedbacks = new LinkedBlockingDeque<String>();
	}
	
	/**
	 * 
	 * @param feedback
	 * @throws InterruptedException
	 */
	public void addFeedback(String feedback) 
			throws InterruptedException 
	{
		// mutually access the queue
		synchronized (this.feedbacks) {
			// add a feedback
			this.feedbacks.put(feedback);
			// notify 
			this.feedbacks.notifyAll();
		}
	}
	
	/**
	 * 
	 * @param feedbacks
	 */
	public void addFeedbacks(List<String> feedbacks) {
		// mutually access the queue
		synchronized (this.feedbacks) {
			// add all feedbacks
			this.feedbacks.addAll(feedbacks);
			// notify 
			this.feedbacks.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	public List<String> getAndClearFeedbacks() 
	{
		// list of feedbacks
		List<String> feeds = new ArrayList<>();
		// mutually access feedbacks 
		synchronized (this.feedbacks) {
			// add all feedbacks to the list 
			feeds.addAll(this.feedbacks);
			// clear queue
			this.feedbacks.clear();
			// notify 
			this.feedbacks.notifyAll();
		}
		
		// get feedbacks
		return feeds;
	}
	
	/**
	 * 
	 */
	@PostConstruct
	protected void init() 
	{
		// create DC checker
		this.checker = new TGADCChecker();
	}
	
	/**
	 * 
	 */
	@Override
	public void doPreExecute() 
	{
		// start feedback generators
		Thread t1 = new Thread(new FeedbackGenerator(0, 5));
		Thread t2 = new Thread(new FeedbackGenerator(1, 5));
		Thread t3 = new Thread(new FeedbackGenerator(2, 7));
		Thread t4 = new Thread(new FeedbackGenerator(3, 11));
		// start threads
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			// create plan database
			PlanDataBase pdb = PlanDataBaseBuilder.createAndSet("domains/satellite.ddl", "domains/satellite.pdl");
			// create planner
			Planner planner = PlannerBuilder.createAndSet(pdb);
			// start deliberative process 
			SolutionPlan plan = planner.plan();
			System.out.println();
			System.out.println("... solution found after " + plan.getSolvingTime() + " msecs\n");
			System.out.println(plan.export());
			System.out.println();
			
			// display solution plan
			planner.display();
			
			
			// Dynamic controllability aware executive
			DCExecutive dce = ExecutiveBuilder.createAndSet(DCExecutive.class);
			// setup the executive
			dce.initialize(planner.export(plan));
			// start execution 
			dce.execute();
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
 	}
	
	
	/**
	 * 
	 * @author anacleto
	 *
	 */
	class FeedbackGenerator implements Runnable {
		
		private long id;
		private long wait;
		
		protected FeedbackGenerator(long id, long wait) {
			this.id = id;
			this.wait = wait;
		}
		
		/**
		 * 
		 */
		@Override
		public void run() {
			try
			{
				System.out.println("\nStarting feedback generation [wait: " + wait + " seconds]\n");
				
				// sleep for a bit
				Thread.sleep(wait * 1000);
				
				// generate feedback
				addFeedback("FB_" + this.id + " at " + wait + " seconds");
			}
			catch (InterruptedException ex) {
				System.err.println(ex.getMessage());
			}
		}
		
	}
}
