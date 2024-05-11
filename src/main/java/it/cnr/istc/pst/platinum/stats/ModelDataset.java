package it.cnr.istc.pst.platinum.stats;

import java.util.List;

/**
 * 
 * @author alessandroumbrico
 *
 */
public interface ModelDataset 
{
	/**
	 * 
	 * @param model
	 * @return
	 */
	public List<TokenExecutionData> getTokenExecutionData();
	
	/**
	 * Select a data-set name for statistics
	 * 
	 * @param name
	 */
	public void select(String name);
	
	/**
	 * 
	 * @param model
	 * @param component
	 * @return
	 */
	public List<TokenExecutionData> getTokenExecutionDataByComponent(String component);
	
	/**
	 * 
	 * @param token
	 */
	public void save(TokenExecutionData token);
	
	/**
	 * 
	 */
	public void clear();
}
