package it.istc.pst.platinum.app.control;

import it.istc.pst.platinum.deliberative.Planner;
import it.istc.pst.platinum.executive.Executive;
import it.istc.pst.platinum.framework.domain.component.PlanDataBase;

/**
 * 
 * @author anacleto
 *
 */
public class InteractingAgent implements Runnable 
{
	private PlanDataBase pdb;						// internal model of the agent
	private Planner planner;						// deliberative component of the agent
	private Executive executive;					// executive component of the agent
	
	/**
	 * 
	 */
	public InteractingAgent() {
		this.pdb = null;
		this.planner = null;
		this.executive = null;
	}
	
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
}
