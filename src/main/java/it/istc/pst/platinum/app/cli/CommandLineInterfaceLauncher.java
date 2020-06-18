package it.istc.pst.platinum.app.cli;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class CommandLineInterfaceLauncher 
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			Thread thread = new Thread(new CommandLineInterfaceProcess());
			thread.start();
			thread.join();
		}catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
