package it.istc.pst.platinum.control.platform.sim;

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
public class PlatformSimulator implements PlatformProxy, PlatformAgentObserver 
{
	private final List<PlatformObserver> observers;								// list of observers
	private Map<String, PlatformAgent> index;							// index agents by commands they can execute
	private List<PlatformAgent> agents;									// list of registered agents
	private Map<ExecutionNode, PlatformCommand> trace;							// cache of executed commands
	
	/**
	 * 
	 */
	protected PlatformSimulator() {
		super();
		// initialize data structures
		this.observers = new ArrayList<>();
		this.index = new HashMap<>();
		this.agents = new ArrayList<>();
		this.trace = new HashMap<>();
	}
	
	/**
	 * 
	 */
	@Override
	public void register(PlatformObserver observer) {
		// register a new observer
		synchronized (this.observers) {
			// add the observer
			this.observers.add(observer);
			// send signal
			this.observers.notifyAll();
		}
	}
	
	/**
	 * Unregister a (previously) registered observer to a particular (physical) platform.
	 * 
	 * 
	 * @param observer
	 */
	@Override
	public void unregister(PlatformObserver observer) {
		// unregister observers
		synchronized (this.observers) {
			// remove observer
			this.observers.remove(observer);
			// send signal
			this.observers.notifyAll();
		}
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
	public PlatformCommand executedCommand(ExecutionNode node) 
			throws PlatformException
	{
		// platform command
		PlatformCommand cmd = null;
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
		cmd = agent.create(desc, params);
		try {
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
	public PlatformCommand startCommand(ExecutionNode node) 
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
		cmd = agent.create(desc, params);
		try {
			// ask the agent to start the execution of the node
			agent.startCommand(cmd);
			// add entry to the execution trace
			this.trace.put(node, cmd);
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
	public PlatformCommand stopCommand(ExecutionNode node) 
			throws PlatformException 
	{
		// check execution trace
		if (!this.trace.containsKey(node)) {
			throw new PlatformException("No command associated to the requested node:\n\t- node: " + node + "\n");
		}
		
		// get associated command
		PlatformCommand cmd = this.trace.get(node);
		// get the agent 
		PlatformAgent agent = this.index.get(cmd.getName());
		try {
			// ask the agent to stop the execution of the command
			agent.stopCommand();
		}
		catch (InterruptedException ex) {
			throw new PlatformException(ex.getMessage());
		}
		
		// get stopped command
		return cmd;
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
	 * @throws InterruptedException
	 */
	public void start() 
			throws InterruptedException
	{
		// start simulator agents
		for (PlatformAgent agent : this.agents) {
			// start agent
			agent.start();
		}
	}
	
	/**
	 * Stop simulator 
	 * 
	 * @throws InterruptedException
	 */
	public void stop() 
			throws InterruptedException
	{
		// stop agents
		for (PlatformAgent agent : this.agents) {
			// stop and wait agent
			agent.stop();
		}
	}
}
