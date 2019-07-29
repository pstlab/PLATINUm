package it.istc.pst.platinum.executive.dc.strategy.loader;

import java.io.BufferedReader;
 
// manca connessione a file o a output di tiga
//manca retrieve dello stato

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.istc.pst.platinum.executive.dc.strategy.clock.ClockSet;
import it.istc.pst.platinum.executive.dc.strategy.State;
import it.istc.pst.platinum.executive.dc.strategy.StateSet;
import it.istc.pst.platinum.executive.dc.strategy.StateStrategy;
import it.istc.pst.platinum.executive.dc.strategy.Strategy;
import it.istc.pst.platinum.executive.dc.strategy.result.Wait;

public class StrategyLoader {
	public final static String MARKER_PRECONDITIONS = "gameInfoPlayInitial state:";
	public final static String MARKER_START = "Strategy to win:";
	public final static String MARKER_STATE = "State:";
	public final static String MARKER_WAIT = "While you are in	";
	public final static String MARKER_TRANSITION = "When you are in ";

	BufferedReader reader;
	Strategy strategy;
	Boolean isValid;

	//------------------------CONSTRUCTORS---------------------------
	
	public StrategyLoader(long horizon) {
		System.out.println("\nStarting Strategy Loader ... : \n");
		this.strategy = new Strategy(horizon);
		this.isValid = false;
	}
	
	// from InputStream
	public StrategyLoader (InputStream file, long horizon) throws FileNotFoundException {
		this(horizon);
		this.reader = new BufferedReader( new InputStreamReader(file));
	}

	//this doesn't work
	public StrategyLoader (String file, long horizon, boolean test, boolean test2) throws FileNotFoundException {
		this(horizon);
		this.reader = new LineNumberReader(new FileReader(file));
	}

	//Generates a strategy from a plan using plan2tiga and UppalTiga, given the absolute path of all of them
	public StrategyLoader (String plan2tiga, String verifytga, String pathPlan, long horizon) throws IOException { //works only for linux
		this(horizon);
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", plan2tiga + " " + pathPlan +
													" && " + verifytga + " -w0 " + pathPlan + ".xta"); 
		builder.redirectErrorStream(true);
		Process process = builder.start();
		this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	}
	
	//Generates a strategy from a plan using plan2tiga and UppalTiga, without being given the absolute path of the first two
	public StrategyLoader (String pathPlan, long horizon) throws IOException { //works only for linux
			this(horizon);
			long time = System.currentTimeMillis();
			ProcessBuilder builder = new ProcessBuilder("bash", "-c", "plan2tiga" + " " + pathPlan +
														" && " + "verifytga" + " -w0 " + pathPlan + ".xta");
			
			builder.redirectErrorStream(true);
			Process process = builder.start();
			this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			time = System.currentTimeMillis() - time;
			System.out.println("\n" + "Plan2Tiga  + VerifyTga = " + time + "ms\n");
		}

	//from a string (test purpose)
	public StrategyLoader(String nomeLettore, long horizon, boolean test) {
		this(horizon);
		this.reader = new BufferedReader(new StringReader(nomeLettore));
	}

	//-------------------------------METHODS-------------------------
	//creates strategy reading lines, validating if a strategy is found and finding states (box 1)
	public void readStrategy() throws IOException, Exception {
			System.out.println("\nReading Strategy..... :\n");
			long time = System.currentTimeMillis();
		try {
			String line = reader.readLine(); 

			while (line != null) { //reading input
				//get local clocks
				if(line.contains(MARKER_PRECONDITIONS)) { //getInitialState(reader.readLine()); getLocalClocks(reader.readLine()); 
					getInitialState(reader.readLine()); getLocalClocks(reader.readLine()); line = reader.readLine();
				}

				//check that the strategy exist
				if(line.startsWith(MARKER_START)) {this.isValid = true; line = reader.readLine();}

				//if strategy exist, search for lines that contain a state
				else if(line.startsWith(MARKER_STATE) && this.isValid) { line = readStateStrategy(line); }
				
				else line = reader.readLine();
			}  
		} catch (IOException e) { //reading error
			e.printStackTrace();
		} 
		try {
			if(this.isValid==false) {
				
				reader.close();
				System.out.println("Il file non contiene una strategia\n");
				throw new Exception("Il file non contiene una strategia\n");
			}
			

		} catch (IOException e) { //closing error
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("\n" + "Reading Strategy Time: " + time + "ms\n");
		System.out.println("\n\n" + this.strategy + "\n\n");
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
		this.strategy.setExpectedState(stateSet);
	} 
	
	//this function's results are clocks with the appendix "_clock", if message passed avoids those it's unnecessary
	private void getLocalClocks(String line) {
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

	//separates case WAIT and TRANSITION and creates StateStrategies for stateset passed
	private String readStateStrategy(String line) throws IOException {
		System.out.println(line);
		StateSet set = readState(line);
		line = reader.readLine();

		while (line!=null) { //if a state is found, search for lines that represent actions and the clocks referred to them

			if(line.startsWith(MARKER_WAIT)) { //wait action
				System.out.println(line + "\n\n");
				for (ClockSet clocks : readClockSets(line)) set.addStateStrategy(new StateStrategy(clocks,new Wait()));
			}
			else if(line.startsWith(MARKER_TRANSITION)) { //transition action
				System.out.println(line + "\n\n");
				for(ClockSet clocks : readClockSets(line)) set.addStateStrategy(new StateStrategy(clocks,line));
			}
			else break; //if not an action, next state to find
			strategy.addState(set);
			line = reader.readLine();	
		}
		return line;
	}
	
	//gets states for the stateset
	public StateSet readState(String line){
		StateSet set = new StateSet();
		String[] states = line.substring(line.indexOf('(')+1, line.indexOf(')')-1).trim().split("\\s");
		for(String state:states) {
			set.addStateSet(new State(state));
		}
		return set;	
	}

	
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
}
