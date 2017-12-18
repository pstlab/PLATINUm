package it.istc.pst.platinum.app.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import it.istc.pst.platinum.app.cli.ex.DeliberativeCommandLineInterfaceException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.NoSolutionFoundException;
import it.istc.pst.platinum.framework.protocol.lang.ParameterTypeDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.PlanProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.TimelineProtocolDescriptor;
import it.istc.pst.platinum.framework.protocol.lang.TokenProtocolDescriptor;
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
				System.out.print("epsl-agent$ ");
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
		if (cmd.equals(PlatinumDeliberativeCommandLineCommand.HELP.getCmd())) 
		{
			// help commands
			System.out.println("Available commands");
			for (PlatinumDeliberativeCommandLineCommand cliCommand : PlatinumDeliberativeCommandLineCommand.values()) {
				System.out.println("- " + cliCommand.getCmd() + "\t" + cliCommand.getHelp());
			}
		}
		else if (cmd.equals(PlatinumDeliberativeCommandLineCommand.EXIT.getCmd())) {
			// exit command
			exit = true;
		}
		else if (cmd.equals(PlatinumDeliberativeCommandLineCommand.INITIALIZE.getCmd())) 
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
		else if (cmd.equals(PlatinumDeliberativeCommandLineCommand.DISPLAY.getCmd())) 
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
		else if (cmd.equals(PlatinumDeliberativeCommandLineCommand.EXPORT.getCmd())) 
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
		else if (cmd.equals(PlatinumDeliberativeCommandLineCommand.PLAN.getCmd())) 
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
		else if (cmd.equals(PlatinumDeliberativeCommandLineCommand.GET.getCmd())) 
		{
			// get command
			if (splits.length < 2 || splits[1] == null) {
				throw new DeliberativeCommandLineInterfaceException("Bad command usage -> " + PlatinumDeliberativeCommandLineCommand.GET.getHelp());
			}
			
			// check command's parameter and create query
			if (splits[1].equals("all")) 
			{
				// plan database projection
				GetFlexibleTimelinesProtocolQuery query = this.queryFactory.createQuery(ProtocolQueryType.GET_FLEXIBLE_TIMELINES);
				
				/*
				 * FIXME : TO IMPLEMENT
				 */
//				// send query
//				this.query(query);
//				// print query result
//				for (TimelineProtocolDescriptor tl : query.getTimelines()) {
//					System.out.println("Flexible Timeline " + tl);
//					for (TokenProtocolDescriptor token : tl.getTokens()) {
//						System.out.println("Flexible Token " + token);
//					}
//					System.out.println();
//				}
			}
			else 
			{
				// get input component and timeline
				String[] data = splits[1].split("\\.");
				// single timeline projection
				GetSingleFlexibleTimelineProtocolQuery query = this.queryFactory.createQuery(ProtocolQueryType.GET_SINGLE_FLEXIBLE_TIMELINE);
				query.setQueryComponent(data[0]);
				query.setQueryTimelineName(data[1]);
				
				/*
				 * FIXME : TO IMPLEMENT
				 */
				
//				// send query
//				this.query(query);
//				// get resulting timeline
//				TimelineProtocolDescriptor timelineDescriptor = query.getTimelineDescriptor();
//				// print timeline's tokens information
//				System.out.println("Flexible Timeline " + timelineDescriptor);
//				for (TokenProtocolDescriptor token : timelineDescriptor.getTokens()) {
//					// print token
//					System.out.println("Flexible Token " + token);
//				}
//				System.out.println();
			}
		}
		else if (cmd.equals(PlatinumDeliberativeCommandLineCommand.SHOW.getCmd())) 
		{
			// show command
			if (splits.length < 2 || splits[1] == null) {
				throw new DeliberativeCommandLineInterfaceException("Bad command usage -> " + PlatinumDeliberativeCommandLineCommand.SHOW.getHelp());
			}
			
			// check command parameter and create query
			if (splits[1].equals("components")) 
			{
				// show domain components
				ShowComponentProtocolQuery query = this.queryFactory.createQuery(ProtocolQueryType.SHOW_COMPONENTS);
				
				/*
				 * FIXME : TO IMPLEMENT
				 */
				
//				// send query
//				this.query(query);
//				// print result
//				System.out.println("Domain Components");
//				for (String comp : query.getComponents()) {
//					System.out.println("- " + comp);
//				}
			}
			else {
				// unknown command option
				System.out.println("Unknown command option");
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
	
	/**
	 * 
	 * @param strings
	 * @return
	 * @throws DeliberativeCommandLineInterfaceException
	 */
	private TokenProtocolDescriptor extractTokenDescription(String[] strings) 
			throws DeliberativeCommandLineInterfaceException 
	{
		// check parameters
		TokenProtocolDescriptor token = null;
		if (strings.length >= 2) 
		{
			// get token name
			String[] tokenName = strings[1].split("\\.");
			String[] temporalParams = strings[2].split(",");
			String[] params = strings.length >= 4 ? strings[3].split(",") : null;
			
			// get token information
			if (tokenName == null || tokenName.length < 3) {
				throw new DeliberativeCommandLineInterfaceException("Wrong \"plan\" command token name information -> component.timeline.predicate");
			}
			
			// create timeline descriptor
			TimelineProtocolDescriptor timeline = this.langFactory.createTimelineDescriptor(tokenName[0], tokenName[1], false);
			String predicate = tokenName[2];
			
			// parse temporal parameters
			if (temporalParams == null || temporalParams.length != 3) {
				throw new DeliberativeCommandLineInterfaceException("Wrong \"plan\" command temporal information -> stLb-stUb,etLb-etUb,dLb-dUb");
			}
			
			// get start time bounds
			String[] startTime = temporalParams[0].split("-");
			long[] startTimeBounds = new long[] {Long.parseLong(startTime[0]), Long.parseLong(startTime[1]) };
			// get end time bounds
			String[] endTime = temporalParams[1].split("-");
			long[] endTimeBounds = new long[] {Long.parseLong(endTime[0]), Long.parseLong(endTime[1])};
			// get duration time bounds
			String[] durationTime = temporalParams[2].split("-");
			long[] durationBounds = new long[] {Long.parseLong(durationTime[0]), Long.parseLong(durationTime[1])};
	
			// prepare parameters
			String[] paramNames = new String[]{};
			ParameterTypeDescriptor[] paramTypes = new ParameterTypeDescriptor[]{};
			long[][] paramBounds = new long[][] {};
			String[][] paramValues = new String[][] {};
			
			// parse parameters
			if (params != null && params.length > 0) 
			{
				// initialize parameters
				paramNames = new String[params.length];
				paramTypes = new ParameterTypeDescriptor[params.length];
				paramBounds = new long[params.length][];
				paramValues = new String[params.length][];
				// get values
				for (int index = 0; index < params.length; index++) {
					// get parameter
					String[] currentParameter = params[index].split("-");
					// parse parameter data
					String name = "?" + currentParameter[0];
					ParameterTypeDescriptor type = currentParameter[1].equals("e") ? ParameterTypeDescriptor.ENUMERATION : ParameterTypeDescriptor.NUMERIC; 
					long[] bounds = new long[] {};
					String[] values = new String[] {};
					// check parameter type
					if (type.equals(ParameterTypeDescriptor.ENUMERATION)) {
						// enumeration parameters
						values = currentParameter[2].split("|");
					}
					else {
						// numeric parameter
						long val = Long.parseLong(currentParameter[2]);
						bounds = new long[] {val, val};
					}
					
					paramNames[index] = name;
					paramTypes[index] = type;
					paramBounds[index] = bounds;
					paramValues[index] = values;
				}
			}
			
			token = this.langFactory.createTokenDescriptor(
					timeline, predicate, 
					startTimeBounds, endTimeBounds, durationBounds, 
					paramNames, paramTypes, paramBounds, paramValues);
		}
		
		// get token descriptor
		return token;
	}
}
