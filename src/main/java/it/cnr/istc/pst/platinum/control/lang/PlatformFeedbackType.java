package it.cnr.istc.pst.platinum.control.lang;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum PlatformFeedbackType 
{
	/**
	 * Unknown feedback type
	 */
	UNKNOWN(-1),
	
	/**
	 * Feedback concerning the successful execution of an action
	 */
	SUCCESS(0),
	
	/**
	 * Feedback concerning the failure of the execution of an action
	 */
	FAILURE(1),

	/**
	 * Feedback concerning the interrupted execution of an action
	 */
	INTERRUPTED(2);
	
	
	private int value;
	
	/**
	 * 
	 * @param value
	 */
	private PlatformFeedbackType(int value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static PlatformFeedbackType get(int value) {
		// check feedback type
		for (PlatformFeedbackType type : PlatformFeedbackType.values()) {
			if (type.value == value) {
				return type;
			}
		}
		
		// return unknown feedback type as default
		return UNKNOWN;
	}
	
}
