package it.uniroma3.epsl2.framework.parameter.solver.lang.event;

/**
 * 
 * @author anacleto
 *
 */
public enum ParameterNotificationType {

	/**
	 * 
	 */
	ADD_PARAM(AddParameterNotification.class.getName()),
	
	/**
	 * 
	 */
	DEL_PARAM(DelParameterNotification.class.getName()),
	
	/**
	 * 
	 */
	ADD_CONSTRAINT(AddConstraintParameterNotification.class.getName()),
	
	/**
	 * 
	 */
	DEL_CONSTRAINT(DelConstraintParameterNotification.class.getName());
	
	private String cname;
	
	private ParameterNotificationType(String cname) {
		this.cname = cname;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getParameterNotificationClassName() {
		return cname;
	}
}
