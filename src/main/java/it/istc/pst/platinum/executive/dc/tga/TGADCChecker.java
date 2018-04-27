package it.istc.pst.platinum.executive.dc.tga;

import java.io.FileNotFoundException;
import java.util.HashMap;

import it.istc.pst.platinum.executive.dc.DCChecker;
import it.istc.pst.platinum.executive.dc.DCResult;
import it.istc.pst.platinum.executive.dc.DCResultType;
import it.istc.pst.platinum.executive.dc.DispatchDCResult;
import it.istc.pst.platinum.executive.dc.FailureDCResult;
import it.istc.pst.platinum.executive.dc.PlanExecutionStatus;
import it.istc.pst.platinum.executive.dc.WaitDCResult;
import it.istc.pst.platinum.executive.dc.tga.strategy.TGAStrategy;
import it.istc.pst.platinum.executive.dc.tga.strategy.TGAStrategyCreator;

/**
 * 
 * @author anacleto
 *
 */
public class TGADCChecker implements DCChecker 
{
	private TGAStrategy strategy;
	private TGAManager manager;
	/**
	 * 
	 * 
	 */

	public TGADCChecker(){

	}

	public TGADCChecker(long horizon) {
		try {
			this.strategy = (new TGAStrategyCreator()).createStrategy(horizon*2);
			this.manager = new TGAManager(this.strategy, null); 

			if (this.strategy ==  null) {
				this.manager.getIstant().setMessage(new FailureDCResult());
			}

		} catch (FileNotFoundException a) {
			System.out.println("File not found!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());		
		}
	}

	/**
	 *
	 * 
	 */
	@Override
	public DCResult evaluate(PlanExecutionStatus status) {
		if (this.manager.getNotified() 
				|| this.manager.getIstant().getPlanExecutionStatus() == null 
				|| this.manager.getIstant().getAction().getFinalStates().equals(status.getStatus())
				|| this.manager.getIstant().getMessage().getType()!= DCResultType.FAILURE) {
			this.manager.getIstant().setPlanExecutionStatus(status); 
			this.manager.setNotified(false);
			this.manager.run(this.strategy);
		} 

		return this.manager.getIstant().getMessage(); 

	}

	/**
	 *
	 * 
	 */
	@Override
	public boolean notify(PlanExecutionStatus status){
		if (this.manager.getIstant().getMessage().getType()!= DCResultType.FAILURE){
			this.manager.getIstant().uncontrollable(status.getStatus());
			this.manager.setNotified(true);
			return this.strategy.canIMove(this.manager.getIstant());
		}
		return false;
	}
	
	public static void main(String [] args)
	{
		
		TGADCChecker checker = new TGADCChecker(100);
		
		PlanExecutionStatus status = new PlanExecutionStatus(11);
		status.time = 11;
		status.status = new HashMap<>();
		status.status.put("visibilitywindow", "visibilitywindow1");
		status.status.put("pointingmode", "pointingmode24");
		
		for (int i = 0; i < 20; i++) {
			
			DCResult result = checker.evaluate(status);
			switch (result.getType())
			{
				case DISPATCH: {
					DispatchDCResult disp = (DispatchDCResult) result;
					
					System.out.println("\nDISPATCH: " + disp.getTokens() + "\n--------------------------");
				}
				break;
				
				case FAILURE: {
					
					System.out.println("\n FAILURE!!!\n\n--------------------------------");
					
				}
				break;
				
				case WAIT: {
					WaitDCResult w = (WaitDCResult) result;
					
					System.out.println("\n WAIT!\n until: " + w.getUntil() + "\n--------------------------");
					
				}
				
				break;
			}
			
			try {
				Thread.sleep(1000);
			}
			catch (Exception ex) {
				System.err.println(ex.getLocalizedMessage());
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
