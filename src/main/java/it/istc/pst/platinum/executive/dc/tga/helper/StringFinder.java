package it.istc.pst.platinum.executive.dc.tga.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class StringFinder {

	@SuppressWarnings("resource")
	public String find(File file, String stringToFound) {
		String finalLine= "";
		BufferedReader br;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null && finalLine.equals(""))
			{
				if (line.contains(stringToFound))
					finalLine = line; 
			}  
		} catch (Exception exc) {
			return ""; 
		} 
		return finalLine; 
	}
	
	public int lineNumber(File file, String stringToFound) throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file); 
		int lineNumber = 0; 
		String line = ""; 
		
		while(scanner.hasNextLine() && !line.contains(stringToFound)){
			line = scanner.nextLine();
			lineNumber ++; 
		}
		
		return lineNumber; 
			
	}
	
	public String lineAtNumber(File file, int lineNumber) throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file); 
		int index = 1; 
		String line = ""; 
		
		while(scanner.hasNextLine() && index!=lineNumber+1){
			line = scanner.nextLine(); 
			index++;
		}
			
		return line; 
	}

	
	public boolean isThere(File file, String stringToFound) throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file);
		boolean isThere = false; 

		while (!isThere && scanner.hasNextLine()){
			String line = scanner.nextLine(); 
			if (line.contains(stringToFound)){
				isThere = true; 
			}
		}
		return isThere; 
	}
}

