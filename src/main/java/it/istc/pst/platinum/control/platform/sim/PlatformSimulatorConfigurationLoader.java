package it.istc.pst.platinum.control.platform.sim;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.istc.pst.platinum.control.platform.ex.PlatformException;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class PlatformSimulatorConfigurationLoader 
{
	private String xmlFilePath;							// configuration file path
	private PlatformSimulator simulator;				// HRC platform simulator
	
	/**
	 * 
	 * @param simulator
	 * @param path
	 */
	protected PlatformSimulatorConfigurationLoader(PlatformSimulator simulator, String path) {
		this.simulator = simulator;
		this.xmlFilePath = path;
	}
	
	/**
	 * 
	 * @throws PlatformException
	 */
	public void load() 
			throws PlatformException
	{
		try
		{
			// parse the XML file through XPath expressions 
			DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = bf.newDocumentBuilder();
			
			// get document
			Document doc = db.parse(new File(this.xmlFilePath));
			doc.getDocumentElement().normalize();
			
			// create XPath
			XPath xpath = XPathFactory.newInstance().newXPath();
			// expression to retrieve agents
			String expr = "//agent";
			NodeList list = (NodeList) xpath.compile(expr).evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++)
			{
				// get node 
				Node an = list.item(i);
				// check node type
				if (an.getNodeType() == Node.ELEMENT_NODE)
				{
					// get element
					Element el = (Element) an;
					// get agent id
					String id = el.getAttribute("id");
					// get agent label
					String label = el.getAttribute("label");
					// get uncertainty
					String uncertainty = el.getAttribute("uncertainty");
					
					// create agent
					PlatformAgent agent = new PlatformAgent(id, label, Long.parseLong(uncertainty));
					
					// parse agent commands
					XPath cmdXpath = XPathFactory.newInstance().newXPath();
					String cmdExpression = "//agent[@id= '" + id +"']//command";
					NodeList cmdList = (NodeList) cmdXpath.compile(cmdExpression).evaluate(doc, XPathConstants.NODESET);
					for (int j = 0; j < cmdList.getLength(); j++)
					{
						// get node 
						Node cmd = cmdList.item(j);
						if (cmd.getNodeType() == Node.ELEMENT_NODE)
						{
							// get command information
							Element cl = (Element) cmd;
							// get command name
							String cmdName = cl.getElementsByTagName("name").item(0).getTextContent();
							// get command duration
							String cmdDuration = cl.getElementsByTagName("duration").item(0).getTextContent();
							
							
							// add command description to agent
							agent.addCommandDescription(cmdName, new String[] {}, Float.parseFloat(cmdDuration));
						}
					}
					
					// register agent to the simulator
					this.simulator.add(agent);
				}
			}
		}
		catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
			throw new PlatformException("Platform initialization error:\n\t- path: " + this.xmlFilePath + "\n\t- msg: " + ex.getMessage() + "\n");
		}
	}
}
