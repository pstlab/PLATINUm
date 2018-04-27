package it.istc.pst.platinum.executive.dc.tga.conditions;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TGARelations {
	private Set<String> relations; 

	public TGARelations(File file) throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file); 
		this.relations = new HashSet<String>(); 
		String line = "";
		
		while(scanner.hasNextLine() && !line.contains("Primitive Relations left")){
			line = scanner.nextLine();
			System.out.println("[DEBUG]: Relation line: " + line + "\n");
		}
			line = scanner.nextLine(); 
			System.out.println("[DEBUG] : second step: " + line + "\n");
			
		while(scanner.hasNextLine() && !line.isEmpty()){
			relations.add(line.substring(0, line.indexOf(':')).trim());
			line = scanner.nextLine(); 
			System.out.println("[DEBUG]: Relation found: " + line + "\n");
		}		
	}
	
	public Set<String> getRelations(){
		return this.relations;
	}
}
