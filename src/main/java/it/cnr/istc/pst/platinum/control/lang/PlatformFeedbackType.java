package it.cnr.istc.pst.platinum.control.lang;

/**
 * 
 * @author alessandroumbrico
 *
 */
public enum PlatformFeedbackType 
{
	/**
	 * Feedback concerning the successful execution of an action
	 */
	SUCCESS(1),
	
	/**
	 * Feedback concerning execution failure of an action
	 */
	FAILURE(0),
	
	/**
	 * Unknown feedback type
	 */
	UNKNOWN(-1);
	
	
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
