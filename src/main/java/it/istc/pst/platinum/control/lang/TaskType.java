package it.istc.pst.platinum.control.lang;

/**
 * 
 * @author anacleto
 *
 */
public enum TaskType 
{
	/**
	 * 
	 */
	ALFA_ASSEMBLY("ALFA", "use_case", "Assembly"),
	
	/**
	 * 
	 */
	ALFA_REMOVE_TOP_COVER("ALFA", "use_case", "RemoveTopCover"),

	/**
	 * 
	 */
	HRC_ITIA("HRC", "process", "HRC_ITIA"),
	
	/**
	 * 
	 */
	RUN("HRC", "process", "HRC_ITIA");
	
	private String pilot;
	private String timeline;
	private String process;
	
	/**
	 * 
	 * @param pilot
	 * @param timeline
	 * @param process
	 */
	private TaskType(String pilot, String timeline, String process) {
		this.pilot = pilot;
		this.timeline = timeline;
		this.process = process;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPilot() {
		return pilot;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTimeline() {
		return timeline;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProcess() {
		return process;
	}
	 
	/**
	 * 
	 * @param pilot
	 * @param process
	 * @return
	 */
	public static TaskType checkTaskParameters(String pilot, String process) {
		
		// task type
		TaskType type = null;
		// check pilot
		for (int index = 0; index < TaskType.values().length && type == null; index++) {
			
			// get task type
			TaskType existingTypes = TaskType.values()[index];
			// check pilot and process
			if (existingTypes.getPilot().equalsIgnoreCase(pilot) && 
					existingTypes.getProcess().equalsIgnoreCase(process)) {
				
				// found task type
				type = existingTypes;
			}
		}
		// get task type
		return type;
 	}
}
