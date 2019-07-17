package it.istc.pst.platinum.control.platform.hrc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.control.platform.PlatformObserver;
import it.istc.pst.platinum.control.platform.PlatformProxy;
import it.istc.pst.platinum.control.platform.lang.PlatformCommand;
import it.istc.pst.platinum.control.platform.lang.PlatformCommandDescription;
import it.istc.pst.platinum.control.platform.lang.ex.PlatformException;
import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class HRCPlatformSimulator extends PlatformProxy implements HRCPlatformAgentObserver 
{
	private static final String DEFAULT_CFG_FILE_PATH = "etc/platform/hrc/config.xml";	// default configuration file
	private Map<String, HRCPlatformAgent> index;										// index agents by commands they can execute
	private List<HRCPlatformAgent> agents;												// list of registered agents
	
	/**
	 * 
	 */
	protected HRCPlatformSimulator() {
		super();
		// initialize data structures
		this.index = new HashMap<>();
		this.agents = new ArrayList<>();
		
	}
	
	/**
	 * 
	 */
	@Override
	public void initialize() 
			throws PlatformException 
	{
		// initialize the plaform on the default configuration file
		this.initialize(DEFAULT_CFG_FILE_PATH);
	}
	
	/**
	 * 
	 */
	@Override
	public void initialize(String cfgFile) 
			throws PlatformException 
	{
		// create configuration loader
		HRCPlatformSimulatorConfigurationLoader loader = new HRCPlatformSimulatorConfigurationLoader(this, cfgFile);
		// load configuration
		loader.load();
	}
	
	
	/**
	 * 
	 * @param agent
	 */
	public void add(HRCPlatformAgent agent) {
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
	public void remove(HRCPlatformAgent agent) {
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
	 * @param cmd
	 */
	@Override
	public void notifySuccess(PlatformCommand cmd) {
		// check platform observers
		synchronized (this.observers) {
			for (PlatformObserver obs : this.observers) {
				// notify success
				obs.success(cmd);
			}
			
			
			// send signals before releasing monitor
			this.observers.notifyAll();
		}
	}
	
	/**
	 * 
	 * @param cmd
	 */
	@Override
	public void notifyFailure(PlatformCommand cmd) {
		// check platform observers
		synchronized (this.observers) {
			for (PlatformObserver obs : this.observers) {
				// notify failure
				obs.failure(cmd);
			}
			
			// send signals before releasing monitor
			this.observers.notifyAll();
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected PlatformCommand doExecuteCommand(ExecutionNode node) 
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
		HRCPlatformAgent agent = this.index.get(name);
		// get associated command description 
		PlatformCommandDescription desc = agent.getCommandDescription(name);
		if (desc == null) {
			throw new PlatformException("No agent can handle requested command:\n\t- cmd-name: " + name + "\n\t- node: " + node + "\n");
		}
		
		// create command with parameters
		PlatformCommand cmd = agent.create(desc, params);
		
		try {
			// ask the agent to execute the node
			agent.execute(cmd);
		}
		catch (InterruptedException ex) {
			// platform exception
			throw new PlatformException(ex.getMessage());
		}
		
		// return executed command
		return cmd;
	}
	
	/**
	 * 
	 */
	@Override
	public PlatformCommand doStartCommand(ExecutionNode node) 
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
		HRCPlatformAgent agent = this.index.get(name);
		// get associated command description 
		PlatformCommandDescription desc = agent.getCommandDescription(name);
		if (desc == null) {
			throw new PlatformException("No agent can handle requested command:\n\t- cmd-name: " + name + "\n\t- node: " + node + "\n");
		}
		
		
		// create command with parameters 
		cmd = agent.create(desc, params);
		try {
			// ask the agent to start the execution of the node
			agent.startCommand(cmd);
			
		}
		catch (InterruptedException ex) {
			throw new PlatformException(ex.getMessage());
		}
		
		// get started command;
		return cmd;
	}
	
	/**
	 * 
	 */
	@Override
	public void doStopCommand(PlatformCommand cmd) 
			throws PlatformException 
	{
		// get the agent 
		HRCPlatformAgent agent = this.index.get(cmd.getName());
		try 
		{
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
		List<HRCPlatformAgent> started = new ArrayList<>();
		try
		{
			// start simulator agents
			for (HRCPlatformAgent agent : this.agents) {
				// start agent
				agent.start();
				started.add(agent);
			}
		}
		catch (InterruptedException ex) 
		{
			// stop started agents
			for (HRCPlatformAgent agent : started) 
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
		for (HRCPlatformAgent agent : this.agents) 
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
