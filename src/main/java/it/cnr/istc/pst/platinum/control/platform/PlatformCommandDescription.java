package it.cnr.istc.pst.platinum.control.platform;

/**
 * 
 * @author anacleto
 *
 */
public class PlatformCommandDescription 
{
	private String name;						// command (unique) name
	private String[] paramNames;				// command parameter names
	private float executionTime;				// command (expected) execution duration in seconds
	
	/**
	 * 
	 * @param name
	 * @param parameters
	 * @param duration
	 */
	public PlatformCommandDescription(String name, String[] parameters, float duration) {
		this.name = name;
		this.paramNames = parameters;
		this.executionTime = duration;
	}
	

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}




	public String[] getParameterNames() {
		return paramNames;
	}


	public void setParameterNames(String[] parameters) {
		this.paramNames= parameters;
	}


	public float getExecutionTime() {
		return executionTime;
	}


	public void setExecutionTime(float time) {
		this.executionTime = time;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlatformCommandDescription other = (PlatformCommandDescription) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[CommandDescription name: " + this.name +  ", execution-time: " + this.executionTime + "]";
	}
}
