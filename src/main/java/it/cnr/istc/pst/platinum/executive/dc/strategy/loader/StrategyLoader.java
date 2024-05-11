package it.cnr.istc.pst.platinum.executive.dc.strategy.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.cnr.istc.pst.platinum.executive.dc.strategy.ListStrategy;
import it.cnr.istc.pst.platinum.executive.dc.strategy.State;
import it.cnr.istc.pst.platinum.executive.dc.strategy.Strategy;
import it.cnr.istc.pst.platinum.executive.dc.strategy.clock.ClockSet;
import it.cnr.istc.pst.platinum.executive.dc.strategy.result.Transition;

public class StrategyLoader {
	public final static String MARKER_PRECONDITIONS = "gameInfoPlayInitial state:";
	public final static String MARKER_START = "Strategy to win:";
	public final static String MARKER_STATE = "State:";
	public final static String MARKER_WAIT = "While you are in	";
	public final static String MARKER_TRANSITION = "When you are in ";

	private BufferedReader reader;
	//private PrintWriter writer;
	private String pathPlanXta; 
	private Strategy strategy;
	private Boolean existsStrategy;
	private long tigaTime;
	private long managementStrategyTime;
	//int test = 1;

	//------------------------CONSTRUCTORS---------------------------

	public StrategyLoader(long horizon) {
		System.out.println("Starting Strategy Loader ... :");
		this.strategy = new ListStrategy(horizon); //MODIFIABLE
		this.existsStrategy = false;
		
	}

	//Generates a strategy from a plan using plan2tiga and UppalTiga, given the absolute path of all of them
	public StrategyLoader (String plan2tiga, String verifytga, String pathPlan, long horizon) throws IOException { //works only for linux
		this(horizon);
		this.pathPlanXta = pathPlan + ".xta";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", plan2tiga + " " + pathPlan +
				" && " + verifytga + " -w0 " + pathPlan + ".xta"); 
		builder.redirectErrorStream(true);
		Process process = builder.start();
		this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	}

	//Generates a strategy from a plan using plan2tiga and UppalTiga, without being given the absolute path of the first two
	public StrategyLoader (String pathPlan, long horizon) throws IOException { //works only for linux
		this(horizon);
		this.pathPlanXta = pathPlan + ".xta";
		/* File file = new File(pathPlan + "_" + test + ".txt");
        FileWriter w = new FileWriter(pathPlan + "_" + test + ".txt");
		this.writer = new PrintWriter(w); */
		this.tigaTime = System.currentTimeMillis();
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", "plan2tiga" + " " + pathPlan +
				" && " + "verifytga" + " -w0 " + pathPlan + ".xta");

		builder.redirectErrorStream(true);
		Process process = builder.start();
		this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		this.tigaTime = System.currentTimeMillis() - this.tigaTime;
		System.out.println("Plan2Tiga  + VerifyTga = " + this.tigaTime + "ms");
		//writer.println("\n" + "Plan2Tiga  + VerifyTga = " + time + "ms\n");
		//this.strategy.setWriter(writer);
	}

	//from a string (test purpose)
	public StrategyLoader(String nomeLettore, String planXta, long horizon, boolean test) {
		this(horizon);
		this.reader = new BufferedReader(new StringReader(nomeLettore));
	}

	//-------------------------------METHODS-------------------------
	//creates strategy reading lines, validating if a strategy is found and finding states (box 1)
	public void readStrategy() throws IOException, Exception {
		System.out.println("Reading Strategy..... :");
		this.managementStrategyTime = System.currentTimeMillis();
		try {
			String line = reader.readLine(); 

			while (line != null) { //reading input 
				//System.out.println(line + "\n");
				//get local clocks
				if(line.contains(MARKER_PRECONDITIONS)) { //getInitialState(reader.readLine()); getLocalClocks(reader.readLine()); 
					getInitialState(reader.readLine()); 
					getLocalClocks(reader.readLine()); 
					line = reader.readLine();
				}

				//check that the strategy exist
				if(line.startsWith(MARKER_START)) {this.existsStrategy = true; line = reader.readLine();}

				//if strategy exist, search for lines that contain a state
				else if(line.startsWith(MARKER_STATE) && this.existsStrategy) { line = readStateStrategy(line); }

				else line = reader.readLine();
			}  
		} catch (IOException e) { //reading error
			e.printStackTrace();
		} 
		try {
			if(this.existsStrategy==false) {

				reader.close();
				System.out.println("Il file non contiene una strategia\n");
				throw new Exception("Il file non contiene una strategia\n");
			}
			else {
				System.out.println("Reading uncontrollable planning variables...");
				this.reader = new BufferedReader(new FileReader(this.pathPlanXta));
				this.strategy.setuPostConditions(extractUPostConditions());	
				System.out.println("Building strategy...");
				this.strategy.buildStrategyStructure();
				System.out.println("Strategy ready!\n");
			}

		} catch (IOException e) { //closing error
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		this.managementStrategyTime = System.currentTimeMillis() - this.managementStrategyTime;
		System.out.println("\n" + "Reading Strategy Time: " + this.managementStrategyTime + "ms\n");
		//System.out.println("\n\n" + this.strategy + "\n\n");
	}

	private Map<Transition,Map<String,String>> extractUPostConditions() throws Exception {
		Map<Transition,Map<String,String>> result = new HashMap<>();
		boolean uncontrollable;
		Set<String> uncontrollables = new HashSet<>();
		String line = this.reader.readLine();
		//System.out.println(line);
		String tl;
		while(line!=null) {
			if(line.contains("process")) {
				//System.out.println("process:" + line);
				tl = line.replace("process","");
				tl = tl.substring(0,tl.indexOf("(")).trim();
				uncontrollable = true;
				line = this.reader.readLine();
				
				while(line!=null && !line.contains("process")) {
					//System.out.println(line);
					
					if(line.contains(" -> ")) { uncontrollable = false;} //check for all uncontrollable transitions -> uncontrollable token
					if(line.contains("-u->")) {
						Map<String,String> postCond = new HashMap<>();
						String from = line.substring(0,line.indexOf("-")).trim();
						String to = line.substring(line.indexOf(">")+1,line.indexOf("{")).trim();
						line = this.reader.readLine();
						while(!line.contains("assign")) {line = this.reader.readLine();}
						while(line.contains(":=")) {
							//System.out.println(line);
							String clock = "none";
							String[] split = line.split(":=");
							String[] containClock  = split[0].split("\\s");
							for(String s : containClock) { if(s.contains("_clock")) clock = s.replace("_clock", "").trim();}
							if(split[1].contains("0")) postCond.put(clock,"0");
							else if (split[1].contains("H")) postCond.put(clock, "H");
					
							line = this.reader.readLine();
						}
						result.put(new Transition(tl,from,to), postCond);	
						//System.out.println(result);
						//System.out.println(tl + uncontrollable);
					}
					if(line!=null) {line = this.reader.readLine();}
				}
				if(uncontrollable) { uncontrollables.add(tl);}
				//System.out.println(uncontrollables);
			}
			else if(line!=null) {line = this.reader.readLine();}
		}	
		//System.out.println(">>>>>>>>>>>>>>> POST CONDITIONS ON UNCONTROLLABLES" + "\n" + result + "<<<<<<<<<<<<\n");
		this.strategy.setuStates(uncontrollables);
		return result;
	}

	//extract only initial state of strategy ()
	private void getInitialState(String line) {
		Map<String,String> stateSet = new HashMap<>();
		line = line.substring(1,line.length()-3).trim();
		String[] split = line.split(" ");
		for(String state : split) {
			State temp = new State(state.trim());
			stateSet.put(temp.getTimeline(), temp.getToken());
		}
		this.strategy.setInitialState(stateSet);
	} 

	//this function's results are clocks with the appendix "_clock", if message passed avoids those it's unnecessary
	private void getLocalClocks(String line) {
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + line + "\n");
		Map<String,Long> timelineClocks = new HashMap<>();
		line = line.substring(1,line.length()-1).trim();
		String[] split = line.split(" ");
		for(String words : split) {
			String[] clocks = words.split("=="); 
			for(String c : clocks) { 
				if(c.length()>2) 
					timelineClocks.put(cleanClock(c),0l);}
		}
		this.strategy.setTimelineClocks(timelineClocks);

	}

	private String cleanClock(String c) {
		if(c.contains("_")) c = c.substring(0,c.indexOf("_"));
		if(c.contains(".")) c = c.substring(0,c.indexOf("."));
		return c;
	}

	//splits strings that represent states and strategies and passes 
	// them to the strategy to be processed
	private String readStateStrategy(String line) throws IOException{
		List<String> accumulatedStates = new ArrayList<>();
		String stateAssociatedToStrategies = line;
		line = reader.readLine();

		while (line!=null) { //if a state is found, search for lines that represent actions and the clocks referred to them

			if(line.startsWith(MARKER_WAIT) || line.startsWith(MARKER_TRANSITION)) { //wait action
				//System.out.println(line + "\n\n");
				accumulatedStates.add(line);
			}
			else break; //if not an action, next state to find
			line = reader.readLine();	
		}
		strategy.getStrategyAsStrings(stateAssociatedToStrategies, accumulatedStates);
		return line;
	}
	//may be obsolete
//	private String readStateStrategy_old(String line) throws IOException {
//		//System.out.println(line);
//		Set<StateSet> accumulatedStates = new HashSet<>();
//		StateSet set = readState(line);
//		line = reader.readLine();
//
//		while (line!=null) { //if a state is found, search for lines that represent actions and the clocks referred to them
//
//			if(line.startsWith(MARKER_WAIT)) { //wait action
//				//System.out.println(line + "\n\n");
//				for (ClockSet clocks : readClockSets(line)) set.addStateStrategy(new StateStrategy(clocks,new Wait()));
//			}
//			else if(line.startsWith(MARKER_TRANSITION)) { //transition action
//				//System.out.println(line + "\n\n");
//				for(ClockSet clocks : readClockSets(line)) set.addStateStrategy(new StateStrategy(clocks,line));
//			}
//			else break; //if not an action, next state to find
//			accumulatedStates.add(set);
//			line = reader.readLine();	
//		}
//		strategy.getStrategyAsList(accumulatedStates);
//		return line;
//	}

	//gets states for the stateset, may be obsolete
//	public StateSet readState(String line){
//		StateSet set = new StateSet();
//		String[] states = line.substring(line.indexOf('(')+1, line.indexOf(')')-1).trim().split("\\s");
//		for(String state:states) {
//			set.addStateSet(new State(state));
//		}
//		return set;	
//	}


	//splits case of multiple clocksets separated by ||
	public List<ClockSet> readClockSets(String line) {
		List<ClockSet> set = new ArrayList<>();
		String[] splitClockSets = line.split("[||]");
		for(String clockSet : splitClockSets) { 
			if (clockSet.length()>2) {
				String[] clocks = clockSet.substring(clockSet.indexOf('(')+1, clockSet.indexOf(')')).trim().split("\\s");
				ClockSet cSet = new ClockSet(clocks);
				set.add(cSet); 
			}
		}
		return set;	
	}

	//-------------------------- GETTERS -----------------------------
	public Strategy getStrategy() {
		return this.strategy;
	}

	public long getTigaTime() {
		return tigaTime;
	}
	
	public long getManagementStrategyTime() {
		return managementStrategyTime;
	}
	
	public Boolean getExistsStrategy() {
		return existsStrategy;
	}
	
}
