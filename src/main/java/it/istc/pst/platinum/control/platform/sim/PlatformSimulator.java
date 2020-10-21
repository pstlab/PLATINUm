package it.istc.pst.platinum.control.platform.sim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.control.platform.PlatformCommand;
import it.istc.pst.platinum.control.platform.PlatformCommandDescription;
import it.istc.pst.platinum.control.platform.RunnablePlatformProxy;
import it.istc.pst.platinum.control.platform.ex.PlatformException;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class PlatformSimulator extends RunnablePlatformProxy implements PlatformAgentObserver 
{
	private Map<String, PlatformAgent> index;											// index agents by commands they can execute
	private List<PlatformAgent> agents;													// list of registered agents
	protected Map<ExecutionNode, PlatformCommand> trace;								// cache of executed commands
	
	/**
	 * 
	 */
	protected PlatformSimulator() {
		super();
		// set data structures
		this.index = new HashMap<>();
		this.agents = new ArrayList<>();
		this.trace = new HashMap<>();
	}
	
	/**
	 * 
	 */
	@Override
	public void initialize(String cfgFile) 
			throws PlatformException 
	{
		// create configuration loader
		PlatformSimulatorConfigurationLoader loader = new PlatformSimulatorConfigurationLoader(this, cfgFile);
		// load configuration
		loader.load();
	}
	
	/**
	 * 
	 * @param agent
	 */
	public void add(PlatformAgent agent) {
		// add the agent to the platform
		this.agents.add(agent);
		// index the agent 
		for (PlatformCommandDescription desc : agent.getCommandDescriptions()) {
			// add entry to the index
			this.index.put(desc.getName(), agent);
		}
		
		// start observing the agent
		agent.register(this);
	}
	
	/**
	 * 
	 * @param agent
	 */
	public void remove(PlatformAgent agent) {
		// stop observing the agent
		agent.unregister(this);
		// remove the agent from the platform
		this.agents.remove(agent);
		// index the agent 
		for (PlatformCommandDescription desc : agent.getCommandDescriptions()) {
			// remove entry from the index
			this.index.remove(desc.getName());
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void notifyFailure(PlatformCommand cmd) {
		this.failure(cmd);
	}
	
	/**
	 * 
	 */
	@Override
	public void notifySuccess(PlatformCommand cmd) {
		this.success(cmd);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isPlatformCommand(ExecutionNode node) {
		// check name flags
		return node.getGroundSignature().startsWith("_") || node.getGroundSignature().startsWith("r");
	}
	
	/**
	 * 
	 */
	@Override
	public PlatformCommand executeNode(ExecutionNode node) 
			throws PlatformException 
	{
		// extract command name 
		String name = this.extractCommandName(node);
		// extract command parameter values
		String[] params = this.extractCommandParameters(node);
		
		// check if there exists an agent that can handle the execution of this command
		if (!this.index.containsKey(name)) {
			throw new PlatformException("No agent can handle requested command:\n\t- cmd-name: " + name + "\n\t- node: " + node + "\n");
		}
		
		// get the simulator that can handle the execution of this command
		PlatformAgent agent = this.index.get(name);
		// get associated command description 
		PlatformCommandDescription desc = agent.getCommandDescription(name);
		if (desc == null) {
			throw new PlatformException("No agent can handle requested command:\n\t- cmd-name: " + name + "\n\t- node: " + node + "\n");
		}
		
		// create command with parameters
		PlatformCommand cmd = this.create(name, params);
		try 
		{
			// ask the agent to execute the node
			agent.execute(cmd);
			// add entry to the cache
			this.trace.put(node, cmd);
		}
		catch (InterruptedException ex) {
			// platform exception
			throw new PlatformException(ex.getMessage());
		}
		
		// get command
		return cmd;
	}
	
	/**
	 * 
	 */
	@Override
	public PlatformCommand startNode(ExecutionNode node) 
			throws PlatformException
	{
		// platform command
		PlatformCommand cmd = null;
		// extract command name 
		String name = this.extractCommandName(node);
		// get command parameter values
		String[] params = this.extractCommandParameters(node);
		
		// check if there exists an agent that can handle the execution of this command
		if (!this.index.containsKey(name)) {
			throw new PlatformException("No agent can handle requested command:\n\t- cmd-name: " + name + "\n\t- node: " + node + "\n");
		}
		
		// get the simulator that can handle the execution of this command
		PlatformAgent agent = this.index.get(name);
		// get associated command description 
		PlatformCommandDescription desc = agent.getCommandDescription(name);
		if (desc == null) {
			throw new PlatformException("No agent can handle requested command:\n\t- cmd-name: " + name + "\n\t- node: " + node + "\n");
		}
		
		
		// create command with parameters 
		cmd = this.create(name, params);
		try 
		{
			// ask the agent to start the execution of the node
			agent.startCommand(cmd);
			this.trace.put(node, cmd);
		}
		catch (InterruptedException ex) {
			throw new PlatformException(ex.getMessage());
		}
		
		// get command
		return cmd;
	}
	
	/**
	 * 
	 */
	@Override
	public void stopNode(ExecutionNode node) 
			throws PlatformException 
	{
		// check execution trace
		if (!this.trace.containsKey(node)) {
			throw new PlatformException("No command associated to the requested node:\n\t- node: " + node + "\n");
		}
		
		try 
		{
			// get associated command
			PlatformCommand cmd = this.trace.get(node);
			// get the agent 
			PlatformAgent agent = this.index.get(cmd.getName());
			// ask the agent to stop the execution of the command
			agent.stopCommand();
		}
		catch (InterruptedException ex) {
			throw new PlatformException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	@Override
	protected String extractCommandName(ExecutionNode node) { 
		// extract command name from node to execute
		String[] splits = node.getGroundSignature().split("-");
		// command name
		String name = splits[0];
		
		// "normalize" command name 
		name = name.replaceFirst("_", "");
		if (name.startsWith("r")) {
			// remove flag
			name = name.replaceFirst("r", "");
		}
		
		// get command name
		return name.toLowerCase();
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	@Override
	protected String[] extractCommandParameters(ExecutionNode node) {
		// extract command parameter from node to execute
		String[] splits = node.getGroundSignature().split("-");
		// get parameters
		String[] parameters = Arrays.copyOfRange(splits, 1, splits.length);
		// get parameters		
		return parameters;
	}
	
	/**
	 * 
	 */
	@Override
	public void start() 
			throws PlatformException
	{
		// list of started agents
		List<PlatformAgent> started = new ArrayList<>();
		try
		{
			// start simulator agents
			for (PlatformAgent agent : this.agents) {
				// start agent
				agent.start();
				started.add(agent);
			}
		}
		catch (InterruptedException ex) 
		{
			// stop started agents
			for (PlatformAgent agent : started) 
			{
				try
				{
					agent.stop();
				}
				catch (Exception exx) {
					System.err.println(exx.getMessage());
				}
			}
			
			throw new PlatformException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 */
	public void stop() 
			throws PlatformException
	{
		// stop agents
		for (PlatformAgent agent : this.agents) 
		{
			try
			{
				// stop and wait agent
				agent.stop();
			}
			catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
}
