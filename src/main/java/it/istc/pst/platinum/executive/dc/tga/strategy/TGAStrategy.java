package it.istc.pst.platinum.executive.dc.tga.strategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import it.istc.pst.platinum.executive.dc.FailureDCResult;
import it.istc.pst.platinum.executive.dc.PlanExecutionStatus;
import it.istc.pst.platinum.executive.dc.tga.conditions.TGAConditions;
import it.istc.pst.platinum.executive.dc.tga.conditions.TGARelations;
import it.istc.pst.platinum.executive.dc.tga.helper.StringFinder;
import it.istc.pst.platinum.executive.dc.tga.objects.TGAPlanStatus;
import it.istc.pst.platinum.executive.dc.tga.objects.TGATransition;

public class TGAStrategy {
	private File strategy;
	private long horizon; 
	private TGARelations relations; 

	public TGAStrategy(File strategy, long horizon, TGARelations relations) throws Exception{
		if (strategy.exists()){
			this.strategy = strategy;
			this.horizon = horizon; 
			this.relations = relations; 
		} else 
			throw new FileNotFoundException();

		if (!isToWin()) throw new Exception(); 
	}

	public Set<String> stateVariables(){
		StringFinder sf = new StringFinder(); 
		String state = sf.find(this.strategy, "State:");
		if (state.contains(")"))
			state = state.substring(state.indexOf("(")+1, state.indexOf(")")).trim(); 
		else { 
			int i = 1;
			while (!state.contains(")")){
				try {
					state.concat(sf.lineAtNumber(this.strategy, sf.lineNumber(this.strategy, "State")+i));
				} catch (FileNotFoundException e) {
					System.out.println("File not found!");
				} 
				i++;
			}

		}

		Set<String> stateVariables = new HashSet<String>();
		String[] splits = state.split(" ");
		for (String split : splits) {
			String sv = split.substring(0, split.indexOf(".")).trim();
			stateVariables.add(sv);
		}

		return stateVariables; 
	}


	private int conditionNumber(int line) throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(this.strategy); 
		int i = 0, numero = 0;
		String linea = ""; 

		while (scanner.hasNextLine() && i!=line+1){
			linea = scanner.nextLine(); 
			i++; 
		}

		while(!linea.startsWith("State:")){
			if (linea.startsWith("When") || linea.startsWith("While"))
				numero ++; 
			linea = scanner.nextLine(); 
		}
		return numero;
	}

	/*The method finds the state in the document and returns the line of the document containing the state.*/
	public int findState(PlanExecutionStatus status) throws FileNotFoundException{
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(this.strategy); 
		String line = "";
		String stringState = ""; 
		int lineNumber = 1; 

		while (scanner.hasNextLine()){

			line = scanner.nextLine(); 
			if (line.startsWith("State:")){
				stringState = getStateFromLine(lineNumber);
				if (isState(status, stringState)) {
					return lineNumber; 
				}
			}
			lineNumber ++; 
		} return lineNumber; 

	}

	private boolean isState(PlanExecutionStatus status, String stringState) {
		boolean itIs = true;
		for (String key : status.getStatus().keySet()){
			itIs = itIs && stringState.contains(((key.concat(".")).concat(status.getStatus().get(key))));
		}
		return itIs; 
	}

	/*the method controls if the strategy is a strategy to win.*/
	private boolean isToWin() throws FileNotFoundException{
		StringFinder sf = new StringFinder(); 
		return sf.isThere(this.strategy, "Strategy to win");
	}

	public TGAPlanStatus goOn(TGAPlanStatus istant)  {

		TGAConditions condition = new TGAConditions("");
		TGATransition transition = new TGATransition(stateVariables());

		try {

			int line = findState(istant.getPlanExecutionStatus());

			if (conditionNumber(line)==2){
				condition.setCondition(getConditionFromLine(line));
				if (condition.getCondition().contains("While") && istant.isVerified(condition))
					istant.evaluateCondition(condition);
				else { 
					int i = line; 
					while (!getConditionFromLine(i).contains("When")) i++;
					condition.setCondition(getConditionFromLine(i));
					transition.setInitialAndFinalStates(getActionFromLine(i));
					transition.setGuardsAndNewValues(getGuardsFromLine(i));

					istant.setAction(transition);
					istant.evaluateCondition(condition); 
				}
			} else { 
				condition.setCondition(getConditionFromLine(line)); 
				if (condition.isWhen()) {
					transition.setInitialAndFinalStates(getActionFromLine(line+1));
					transition.setGuardsAndNewValues(getGuardsFromLine(line+1));
				}
				istant.setAction(transition);
				istant.evaluateCondition(condition);

			}
		} catch (Exception e) { e.printStackTrace();}
		return istant; 
	}

	private String getStateFromLine(int i) throws FileNotFoundException{
		StringFinder sf = new StringFinder(); 
		String state = ""; 
		String line = sf.lineAtNumber(this.strategy, i); 

		do {
			state = state.concat(line); 
			i++; 
			line = sf.lineAtNumber(this.strategy, i); 

		} while (!line.contains("you are in")); 

		return state.substring(state.indexOf("(")+1, state.indexOf(")")).trim();
	}


	public String getGuardsFromLine(int i) throws FileNotFoundException{
		StringFinder sf = new StringFinder(); 
		String guards = ""; 
		String line = sf.lineAtNumber(this.strategy, i);

		while(!sf.lineAtNumber(this.strategy, i).contains("{")){
			i ++; 
			line = sf.lineAtNumber(this.strategy, i);
		}

		do {
			guards = guards.concat(line); 
			line = sf.lineAtNumber(this.strategy, i);
			i++; 
		} while (!line.contains("}")); 

		return guards.substring(guards.indexOf("{")+1, guards.indexOf("}")).trim();
	}

	/*The method getActionFromLine saves a string containing the action.*/	
	public String getActionFromLine(int i) throws FileNotFoundException {
		StringFinder sf = new StringFinder(); 
		String action = ""; 
		String line = sf.lineAtNumber(this.strategy, i);

		while(!sf.lineAtNumber(this.strategy, i).contains("take transition")){
			i ++; 
			line = sf.lineAtNumber(this.strategy, i);
		}

		do {
			action = action.concat(line); 
			line = sf.lineAtNumber(this.strategy, i);
			i++; 
		} while (!line.contains("")); 

		return action.substring(action.indexOf("take transition")+("take transition").length(), 
				action.indexOf('{')).trim();
	}



	/*The method getConditionFromLine saves a string containing the condition.*/
	public String getConditionFromLine(int i) throws FileNotFoundException {
		StringFinder sf = new StringFinder(); 
		String condition = ""; 
		String line = sf.lineAtNumber(this.strategy, i); 

		while (!line.contains("When") && !line.contains("While")) {
			i++; 
			line = sf.lineAtNumber(this.strategy, i);
		} 

		do {
			condition = condition.concat(line); 
			line = sf.lineAtNumber(this.strategy, i);
			i++; 
		} while (!line.contains(","));

		return condition.substring(0, condition.indexOf(')')+1); 

	}

	public boolean canIMove(TGAPlanStatus status){
		boolean iCan = false;  
		int i = 0; 
		try {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(this.strategy);
			while(scanner.hasNextLine() && !iCan) {
				if ((scanner.nextLine()).startsWith("State"))
					iCan = isState(status.getPlanExecutionStatus(), getStateFromLine(i));
				i++; 
			}
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} 

		if (!iCan) status.setMessage(new FailureDCResult());
		return iCan; 
	}

	public File getStrategy(){
		return this.strategy; 
	}

	public long getHorizon(){
		return this.horizon;
	}

	public TGARelations getRelations(){
		return this.relations; 
	}

}