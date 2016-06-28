package it.uniroma3.epsl2.framework.utils.view;

import it.uniroma3.epsl2.framework.utils.view.gantt.GanttComponentView;

/**
 * 
 * @author anacleto
 *
 */
public enum ComponentViewType {

	/**
	 * 
	 */
	GANTT(GanttComponentView.class.getName());
	
	private String cname;
	
	/**
	 * 
	 * @param cname
	 */
	private ComponentViewType(String cname) {
		this.cname = cname;
	}
	
	public String getComponentViewClassName() {
		return this.cname;
	}
}
