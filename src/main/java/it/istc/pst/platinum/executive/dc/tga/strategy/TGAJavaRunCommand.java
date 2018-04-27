package it.istc.pst.platinum.executive.dc.tga.strategy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class TGAJavaRunCommand {
	
	
	public File runCommandVTGA(File file) throws FileNotFoundException {
		File f = new File("strategy.txt");

		try 
		{
			
			Process p = Runtime.getRuntime().exec("/usr/local/bin/verifytga -t0 "+ file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(new FileWriter (f));
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line = null;
			while((line = br.readLine()) != null){
				bw.write(line + "\n"); // You can also use append.
			}
			
			line = null;
			while ((line = err.readLine()) != null) {
				System.err.println(line);
			}

			p.waitFor();
			
			bw.close(); 

		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

		return f;
	}
}
