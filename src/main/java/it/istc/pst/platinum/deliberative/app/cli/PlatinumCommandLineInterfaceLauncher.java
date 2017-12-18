package it.istc.pst.platinum.deliberative.app.cli;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class PlatinumCommandLineInterfaceLauncher 
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			Thread thread = new Thread(new PlatinumDeliberativeProcessInterface());
			thread.start();
			thread.join();
		}catch (InterruptedException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
