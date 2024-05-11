package it.cnr.istc.pst.platinum.control.lang;

/**
 * 
 * 
 * @author alessandroumbrico
 *
 */
public class PlatformObservation<T extends Object> extends PlatformMessage implements Comparable<PlatformObservation<? extends Object>>
{
	private long time;						// observation generation time
	private T data;							// generic data object containing observed information
	
	/**
	 * 
	 * @param id
	 * @param data
	 */
	public PlatformObservation(long id, T data) {
		super(id);
		this.data = data;
		this.time = System.currentTimeMillis();
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getData() {
		return data;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(PlatformObservation<? extends Object> o) {
		return this.time < o.time ? -1 : this.time > o.time ? 1 : 0;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style description
		return "{\"id\": " + this.id + ", \"data\": " + this.data + "}";
	}
}
