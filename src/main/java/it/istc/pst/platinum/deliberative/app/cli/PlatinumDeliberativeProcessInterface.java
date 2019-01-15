package it.istc.pst.platinum.deliberative.app.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import it.istc.pst.platinum.deliberative.app.cli.ex.DeliberativeCommandLineInterfaceException;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.Token;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.Timeline;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.query.ProtocolQueryType;
import it.istc.pst.platinum.framework.protocol.query.get.GetFlexibleTimelinesProtocolQuery;
import it.istc.pst.platinum.framework.protocol.query.get.GetSingleFlexibleTimelineProtocolQuery;
import it.istc.pst.platinum.framework.protocol.query.show.ShowComponentProtocolQuery;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class PlatinumDeliberativeProcessInterface extends PlatinumDeliberativeAbstractCommandLineInterface implements Runnable
{
	private static final long HORIZON = Long.MAX_VALUE - 1;			// default horizon
	
	/**
	 * 
	 */
	protected PlatinumDeliberativeProcessInterface() {
		super(HORIZON);
	}
	
	/**
	 * 
	 */
	public void run() 
	{
		System.out.println("************************************");
		System.out.println("********** Platinum CLI ************");
		System.out.println("************************************");

		try 
		{
			// get input reader
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			boolean exit = false;
			do {
				
				// display prompt
				System.out.print("platinum-deliberative$ ");
				String input = reader.readLine();
				try 
				{
					// process input
					exit = this.process(input);
					System.out.println();
				} 
				catch (DeliberativeCommandLineInterfaceException ex) {
					System.out.println("> !" + ex.getMessage() + "\n");
					// go on
				}
				catch (Exception ex) {
					System.out.println("> !" + ex.getMessage() + "\n");
					exit = true;
				}
			}
			while (!exit);
			
			// exiting
			System.out.println("\nBye!\n");
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 * @throws DeliberativeCommandLineInterfaceException
	 */
	private boolean process(String input) 
			throws DeliberativeCommandLineInterfaceException {
		
		boolean exit = false;
		// check input string 
		if (input == null) {
			throw new DeliberativeCommandLineInterfaceException("Specify a valid command!");
		}
		
		// check command type
		String[] splits = input.split("\\s");
		// get command
		final String cmd = splits[0].toUpperCase();
		// check command
		if (cmd.toLowerCase().equals(PlatinumDeliberativeCommandLineCommand.HELP.getCmd().toLowerCase())) 
		{
			// help commands
			System.out.println("Available commands");
			for (PlatinumDeliberativeCommandLineCommand cliCommand : PlatinumDeliberativeCommandLineCommand.values()) {
				System.out.println("- " + cliCommand.getCmd() + "\t" + cliCommand.getHelp());
			}
		}
		else if (cmd.toLowerCase().equals(PlatinumDeliberativeCommandLineCommand.EXIT.getCmd().toLowerCase())) {
			// exit command
			exit = true;
		}
		else if (cmd.toLowerCase().equals(PlatinumDeliberativeCommandLineCommand.INITIALIZE.getCmd().toLowerCase())) 
		{
			// check data
			if (splits.length < 3 || splits[1] == null || splits[2] == null) {
				throw new DeliberativeCommandLineInterfaceException("Bad command usage -> " + PlatinumDeliberativeCommandLineCommand.INITIALIZE.getHelp());
			}
			
			// get DDL and PDL files
			File ddl = new File(splits[1]);
			File pdl = new File(splits[2]);
			
			// check files
			if (ddl.exists() && !ddl.isDirectory() && pdl.exists() && !pdl.isDirectory()) {
				// initialize
				this.init(ddl.getAbsolutePath(), pdl.getAbsolutePath());
			}
			else {
				// files not found
				System.out.println("DDL or PDL files not found");
			}
			
			System.out.println();
		}
		else if (cmd.toLowerCase().equals(PlatinumDeliberativeCommandLineCommand.DISPLAY.getCmd().toLowerCase())) 
		{
			if (this.planner != null) {
				try 
				{
					// prepare thread
					Thread t = new Thread(new Runnable() { 
	
							/**
							 * 
							 */
							@Override
							public void run() {
								// print current plan
								System.out.println(currentSolution);
								// display the current plan
								planner.display();
							}
					});
					
					// start and wait execution
					t.start();
					t.join();
				}
				catch (Exception ex) {
					// do not interrupt execution
					System.out.println(ex.getMessage());
				}
			}
			else {
				System.out.println("!No planner has been initialized yet!");
			}
			System.out.println();
		}
		else if (cmd.toLowerCase().equals(PlatinumDeliberativeCommandLineCommand.EXPORT.getCmd().toLowerCase())) 
		{
			// export plan request
			if (splits.length < 2 || splits[1] == null) {
				throw new DeliberativeCommandLineInterfaceException("Bad command usage -> " + PlatinumDeliberativeCommandLineCommand.EXPORT.getHelp());
			}
			
			try 
			{
				// get file path
				String path = splits[1];
				System.out.println("Exporting current plan to file " + path);
				// prepare the file
				try (PrintWriter writer = new PrintWriter(new File(path), "UTF-8")) {
					// export current plan
					PlanProtocolDescriptor plan = this.export();
					// write the plan
					writer.println(plan);
				}
			}
			catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
		else if (cmd.toLowerCase().equals(PlatinumDeliberativeCommandLineCommand.PLAN.getCmd().toLowerCase())) 
		{
			try 
			{
				// plan
				this.plan();
			} 
			catch (NoSolutionFoundException ex) {
				System.out.println("No valid plan found:\n- " + ex.getMessage() + "\n");
			} 
			catch (DeliberativeCommandLineInterfaceException  ex) {
				System.out.println(ex.getMessage());
			}
			System.out.println();
		}
		else if (cmd.toLowerCase().equals(PlatinumDeliberativeCommandLineCommand.GET.getCmd().toLowerCase())) 
		{
			// get command
			if (splits.length < 2 || splits[1] == null) {
				throw new DeliberativeCommandLineInterfaceException("Bad command usage -> " + PlatinumDeliberativeCommandLineCommand.GET.getHelp());
			}
			
			// check current solution
			if (this.currentSolution == null) {
				throw new DeliberativeCommandLineInterfaceException("Bad command usage -> No plan ready to display ");
			}
			
			
			// check command's parameter and create query
			if (splits[1].equals("all")) 
			{
				// plan database projection
				GetFlexibleTimelinesProtocolQuery query = this.queryFactory.createQuery(ProtocolQueryType.GET_FLEXIBLE_TIMELINES);
				// check temporal behaviors
				for (Timeline cTl : this.currentSolution.getTimelines()) 
				{
					// print component information
					System.out.println("\n- " + cTl.getComponent().getName() + " Flexible temporal behavior:");
					for (Token cTk : cTl.getTokens())
					{
						// print token description
						System.out.println("\t- " + cTk.getPredicate() + " AT "
								+ "[" + cTk.getInterval().getStartTime().getLowerBound() + "," + cTk.getInterval().getStartTime().getUpperBound() + "] "
								+ "[" + cTk.getInterval().getEndTime().getLowerBound() + "," + cTk.getInterval().getEndTime().getUpperBound() + "] "
								+ "[" + cTk.getInterval().getDurationLowerBound() + "," + cTk.getInterval().getDurationUpperBound() + "]");
					}
				}
			}
			else 
			{
				// get input specified component
				String[] data = splits[1].split("\\.");
				GetSingleFlexibleTimelineProtocolQuery query = this.queryFactory.createQuery(ProtocolQueryType.GET_SINGLE_FLEXIBLE_TIMELINE);
				query.setQueryComponent(data[0]);
				
				// get timeline
				Timeline cTl = null; 
				for (Timeline t : this.currentSolution.getTimelines()) {
					if (t.getName().contains(query.getComponent())) 
					{
						// found
						cTl = t;
						break;
					}
				}
				
				// check timeline
				if (cTl == null) {
					throw new DeliberativeCommandLineInterfaceException("Bad command usage -> No timeline found with the given name: " + query.getComponent());
				}
				
				System.out.println("\n- " + cTl.getName() + " Flexible temporal behavior:");
				for (Token cTk : cTl.getTokens())
				{
					// print token description
					System.out.println("\t- " + cTk.getPredicate().getValue().getLabel() + " AT "
							+ "[" + cTk.getInterval().getStartTime().getLowerBound() + "," + cTk.getInterval().getStartTime().getUpperBound() + "] "
							+ "[" + cTk.getInterval().getEndTime().getLowerBound() + "," + cTk.getInterval().getEndTime().getUpperBound() + "] "
							+ "[" + cTk.getInterval().getDurationLowerBound() + "," + cTk.getInterval().getDurationUpperBound() + "]");
				}
			}
		}
		else if (cmd.equals(PlatinumDeliberativeCommandLineCommand.SHOW.getCmd())) 
		{
			// check plan database
			if (this.pdb == null) {
				throw new DeliberativeCommandLineInterfaceException("Bad command usage -> No planning domain set");
			}
			
			// show domain components
			ShowComponentProtocolQuery query = this.queryFactory.createQuery(ProtocolQueryType.SHOW_COMPONENTS);
			// check component name if any
			if (splits.length > 1 && splits[1] != null)
			{
				// get component by name
				DomainComponent comp = this.pdb.getComponentByName(splits[1].trim());
				if (comp == null) {
					throw new DeliberativeCommandLineInterfaceException("Bad command usage -> No domain component found with name " + splits[1]);
				}
				
				// check type 
				System.out.println("\n- [" + comp.getType().getLabel() + "] " + comp.getName() + " description:");
				for (ComponentValue v : comp.getValues()) {
					System.out.println("\t- " + v.getLabel() + " [" + v.getDurationLowerBound() + ", " + v.getDurationUpperBound() + "] ");
				}
			}
			else 
			{
				// print component information
				for (DomainComponent comp : this.pdb.getComponents())
				{
					// check type 
					System.out.println("\n- [" + comp.getType().getLabel() + "] " + comp.getName() + " description:");
					for (ComponentValue v : comp.getValues()) {
						System.out.println("\t- " + v.getLabel() + " [" + v.getDurationLowerBound() + ", " + v.getDurationUpperBound() + "] ");
					}
				}
			}
		}
		else 
		{
			// unknown command
			String msg = "Unknown Command!\nAvailable commands:\n";
			for (PlatinumDeliberativeCommandLineCommand cliCommand : PlatinumDeliberativeCommandLineCommand.values()) {
				msg += "- " + cliCommand.getCmd() + "\t" + cliCommand.getHelp() + "\n";
			}
			msg += "\n";
			System.out.println(msg);
		}

		return exit;
	}
	
}
