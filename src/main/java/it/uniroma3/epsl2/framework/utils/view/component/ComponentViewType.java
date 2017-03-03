package it.uniroma3.epsl2.framework.utils.view.component;

import it.uniroma3.epsl2.framework.utils.view.component.gantt.GanttComponentView;

/**
 * 
 * @author anacleto
 *
 */
public enum ComponentViewType 
{
	/**
	 * This element is responsible for providing a Gantt chart representation of 
	 * the partial-plan concerning a component of the domain 
	 */
	GANTT(GanttComponentView.class.getName()),
	
	/**
	 * This element is responsible for proving a 2-dimensional representation of 
	 * the partial-plan concerning a component of the domain 
	 */
	CARTESIAN(null);
	
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
