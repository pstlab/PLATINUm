package it.istc.pst.platinum.executive.dc.tga.strategy;
import java.io.File;
import java.io.IOException;

import it.istc.pst.platinum.executive.dc.tga.conditions.TGARelations;

public class TGAStrategyCreator {
	private TGAStrategy strategy;  

	/*this method create the file strategy if the input file has .xta extension.*/
	public TGAStrategy createStrategy(long horizon) throws Exception{
//		ClassLoader classLoader = getClass().getClassLoader();
//		File file = new File(classLoader.getResource("etc/plan.txt.xta").getFile());
		File file = new File("etc/plan.txt.xta");
		
		if (okExtension(file.getName(), ".xta")){ 
			this.strategy = new TGAStrategy(verifytga(file), horizon, 
					new TGARelations(file));
			return this.strategy;
		}
		return null; 
	}


	/*The method verifytga call the instruction  verifytga on the terminal and save the output in a file.*/
	private static File verifytga(File file) throws IOException, InterruptedException {
		TGAJavaRunCommand jav = new TGAJavaRunCommand();
		File output = jav.runCommandVTGA(file);
		return output; 
	}

	/*The method controls the extension of the input file.*/
	private boolean okExtension(String filePath, String ok){
		return filePath.substring(filePath.lastIndexOf('.')).equals(ok);  //we need an .xta file
	}

	public TGAStrategy getStrategy(){
		return this.strategy; 
	}
}

