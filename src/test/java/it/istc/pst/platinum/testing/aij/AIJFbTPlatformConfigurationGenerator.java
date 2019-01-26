package it.istc.pst.platinum.testing.aij;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 
 * @author anacleto
 *
 */
public class AIJFbTPlatformConfigurationGenerator extends AIJFbT 
{
	// domain name
	private String configurationFolder;
	private int[] platformUncertainty;
	
	/**
	 * 
	 * @param configurationFolder
	 * @param uncertainty
	 */
	protected AIJFbTPlatformConfigurationGenerator(
			String configurationFolder,
			int[] uncertainty)
	{
		// set generator fields
		this.configurationFolder = configurationFolder;
		this.platformUncertainty = uncertainty;
	}
	
	/**
	 * 
	 */
	public void generate()
	{
		// iterate over domain parameters
		for (int uncertainty : this.platformUncertainty) 
		{
			try
			{
				// set configuration file name
				String configFileName = "AIJ_EXP_PLATFORM_CONFIG_U" + uncertainty;
				// prepare the XML configuration file 
				String xml = "<platform>";
				
				// add human agent specification 
				xml += this.prepareHumanAgentConfiguration(uncertainty);
				xml += this.prepareRobotAgentConfiguration(uncertainty);
				
				
				xml +="</platform>";
				
				
				// write the XML configuration file 
				File cfgFile = new File(this.configurationFolder + "/" + configFileName + ".xml");
				try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cfgFile), "UTF-8"))) {
					// write file
					writer.write(xml);
				}
			}
			catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
	
	
	/**
	 * 
	 * @param uncertainty
	 * @return
	 */
	private String prepareHumanAgentConfiguration(int uncertainty)
	{
		// prepare agent configuration description
		String xml = "\t<agent id=\"a01\" label=\"Human\" type=\"acting\" uncertainty=\"" + uncertainty +"\">\n\n";
		// TASK: SetWorkPiece
		xml += "\t\t<command type=\"uc\">\n"
				+ "\t\t\t<name>setworkpiece</name>\n"
				+ "\t\t\t<duration>22</duration>\n"
				+ "\t\t</command>\n\n";
		// TASK: RemoveWaxPart 
		xml += "\t\t<command type=\"uc\">\n"
				+ "\t\t\t<name>removewaxpart</name>\n"
				+ "\t\t\t<duration>31</duration>\n"
				+ "\t\t</command>\n\n";
		
		// compute possible top and bottom commands
		int topUnscrew = TASKS[TASKS.length -1] / 2;
		int bottomUnscrew = TASKS[TASKS.length -1] - topUnscrew;
		// add all possible top bolt screwing commands
		for (int i = 1; i <= topUnscrew; i++)
		{
			// TASK: UnscrewTopBolt(i)
			xml += "\t\t<command type=\"uc\">\n"
					+ "\t\t\t<name>unscrewtopbolt" + i + "</name>\n"
					+ "\t\t\t<duration>26</duration>\n"
					+ "\t\t</command>\n\n";
		}
		
		// add all possible bottom bolt screwing commands
		for (int i = 1; i <= bottomUnscrew; i++)
		{
			// TASK: UnscrewBottomBolt(i)
			xml += "\t\t<command type=\"uc\">\n"
					+ "\t\t\t<name>unscrewbottombolt" + i + "</name>\n"
					+ "\t\t\t<duration>28</duration>\n"
					+ "\t\t</command>\n\n";
		}
		
		// close agent description
		xml += "\t</agent>\n\n";
		// return configuration description
		return xml;
	}
	
	/**
	 * 
	 * @param uncertainty
	 * @return
	 */
	private String prepareRobotAgentConfiguration(int uncertainty)
	{
		// prepare agent configuration description
		String xml = "\t<agent id=\"a02\" label=\"Robot\" type=\"acting\" uncertainty=\"" + uncertainty + "\">\n\n";
		
		// TASK: Tool.UnscrewBolt
		xml += "\t\t<command type=\"c\">\n"
				+ "\t\t\t<name>unscrewbolt</name>\n"
				+ "\t\t\t<duration>6</duration>\n"
				+ "\t\t</command>\n\n";
		
		// TASK: Arm.Moving
		xml += "\t\t<command type=\"pc\">\n"
				+ "\t\t\t<name>moving</name>\n"
				+ "\t\t\t<duration>18</duration>\n"
				+ "\t\t</command>\n\n";
		
		// close agent description
		xml += "\t</agent>\n\n";
		// return configuration description
		return xml;
	}
	
	
	
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// experiment generator
		AIJFbTPlatformConfigurationGenerator generator = new AIJFbTPlatformConfigurationGenerator(
						PLATFORM_CONFIGURATION_FOLDER,
						UNCERTAINTY);
		// generate experiment domains
		generator.generate();
	}
}

