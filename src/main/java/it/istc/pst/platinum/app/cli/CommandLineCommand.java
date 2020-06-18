package it.istc.pst.platinum.app.cli;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum CommandLineCommand 
{
	/**
	 * help command
	 */
	HELP("?", "Get a description of the available commands"),
	
	/**
	 * initialization command
	 */
	INITIALIZE("init", "Specify a valid DDL file path and a valid PDL file path"),
	
	/**
	 * plan command
	 */
	PLAN("plan", "Run the plan on the specifiede DDL and PDL files"),
	
	/**
	 * plan execution command
	 */
	EXEC("exec", "Specify a valid confiugration file path for the simulated (physical) platform"),
	
	/**
	 * get all or single timeline temporal projections 
	 */
	GET("get", "Specify a component name to see the related timelines or the kyeword \"all\" to see the timeline of the plan"),
	
	/**
	 * show domain information
	 */
	SHOW("show", "Show the domain specification of all the components of the domain"),
	
	/**
	 * 
	 */
	DISPLAY("display", "Display a Gantt chart showing the earliest startime instance of the plan"),
	
	/**
	 * 
	 */
	EXPORT("export", "Specify the path of the file to export the current plan"),
	
	/**
	 * exit command
	 */
	EXIT("exit", "");
	
	private String cmd;
	private String usage;
	
	/**
	 * 
	 * @param cmd
	 * @param usage
	 */
	private CommandLineCommand(String cmd, String usage) {
		this.cmd = cmd;
		this.usage = usage;
	}
	
	public String getCmd() {
		return cmd.toUpperCase();
	}
	
	public String getHelp() {
		return usage;
	}
}
