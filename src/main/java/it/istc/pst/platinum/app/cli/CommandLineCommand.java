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
	HELP("?", ""),
	
	/**
	 * initialization command
	 */
	INITIALIZE("init", "init file.ddl file.pdl"),
	
	/**
	 * plan command
	 */
	PLAN("plan", "solve the loaded timeline-based problem"),
	
	/**
	 * plan execution command
	 */
	EXEC("exec", "execute a previously generated plan through the internal simulator"),
	
	/**
	 * get all or single timeline temporal projections 
	 */
	GET("get", "get all/Component.timeline"),
	
	/**
	 * show domain information
	 */
	SHOW("show", "show information about domain components"),
	
	/**
	 * 
	 */
	DISPLAY("display", "display current plan"),
	
	/**
	 * 
	 */
	EXPORT("export", "export <file-path> to export the last created plan into the file"),
	
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
