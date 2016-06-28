package it.uniroma3.epsl2.framework.utils.view.gantt;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.Token;
import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.time.TemporalInterval;
import it.uniroma3.epsl2.framework.utils.view.ComponentView;

/**
 * 
 * @author anacleto
 *
 */
public class GanttComponentView extends ApplicationFrame implements ComponentView, Comparator<Token> {
	private static final long serialVersionUID = 1L;

	private DomainComponent component;
	
	/**
	 * 
	 * @param component
	 */
	public GanttComponentView(DomainComponent component) {
		super(component.getName() + " Component View");
		this.component = component;
	}
	
	/**
	 * 
	 */
	@Override
	public void display() {
		// create the data-set from the plan
		final IntervalCategoryDataset dataset = this.createDataset();

		// create the chart
		final JFreeChart chart = ChartFactory.createGanttChart(
				null,				// chart title 
				null, 			// domain axis label
				"Time",				// range axis label 
				dataset,			// data to display 
				false,				// include legend 
				true, 				// tool-tips
				false				// URLs
		);
		
		// create the plot
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		DateAxis range = new DateAxis("Time");
		// range unit in seconds
		range.setDateFormatOverride(new SimpleDateFormat("ss"));
		// set tick range every second
		range.setTickUnit(new DateTickUnit(DateTickUnitType.MILLISECOND, 1));
		// set maximum to plan horizon
		range.setMaximumDate(new Date(this.component.getHorizon()));
		range.setVisible(false);
		plot.setRangeAxis(range);
		// get renderer
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator(
				"value= \"{1}\", start= \"{2}\"", NumberFormat.getInstance()));
		
		// add the chart to a panel
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
		
		// display the Gantt graph
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}
	
	/**
	 * 
	 * @return
	 */
	private IntervalCategoryDataset createDataset() {

		// create task series collection
		TaskSeriesCollection dataset = new TaskSeriesCollection();
		
		// setup data for components and sub-tasks
		Map<DomainComponent, Map<ComponentValue, List<Token>>> index = new HashMap<>();
		
		// create a task series for each time-line
	 	for (Decision dec : this.component.getActiveDecisions()) 
	 	{
	 		// create a task series if necessary
	 		if (!index.containsKey(dec.getComponent())) {
	 			// initialize component data
	 			index.put(dec.getComponent(), new HashMap<>());
	 		}
	 		
	 		if (!index.get(dec.getComponent()).containsKey(dec.getValue())) {
	 			// initialize value data
	 			index.get(dec.getComponent()).put(dec.getValue(), new ArrayList<>());
	 		}
	 		
	 		// add token for value
	 		index.get(dec.getComponent()).get(dec.getValue()).add(dec.getToken());
	 	}

	 	// create data to display
	 	for (DomainComponent c : index.keySet()) {
	 		// create series 
	 		TaskSeries s = new TaskSeries(c.getName());
	 		// create tasks
	 		for (ComponentValue v : index.get(c).keySet()) {
	 			// get value related tokens
	 			List<Token> tokens = index.get(c).get(v);
	 			// sort tokens
	 			Collections.sort(tokens, this);
	 			
 				// create task
 				Task task = new Task(v.getComponent().getName() + "." + v.getLabel(),
 						new Date(tokens.get(0).getInterval().getStartTime().getLowerBound()),
 						new Date(tokens.get(tokens.size()-1).getInterval().getEndTime().getLowerBound()));
 				// create sub-tasks if necessary
 				for (Token t : tokens) {
 					// create sub task
 					Task subTask = new Task(v.getLabel(),
 							new Date(t.getInterval().getStartTime().getLowerBound()),
 							new Date(t.getInterval().getEndTime().getLowerBound()));
 					
 					// check controllability of the value
 					subTask.setPercentComplete(t.isControllable() ? 1.0 : 0.0);
 					task.addSubtask(subTask);
 				}	
	 			
	 			// add task to the series
		 		s.add(task);
	 		}
	 		// add series to data
	 		dataset.add(s);
	 		
	 	}
	 	
	 	// return data-set
		return dataset;
	}
	
	/**
	 * 
	 */
	@Override
	public int compare(Token o1, Token o2) {
		// get intervals
		TemporalInterval i1 = o1.getInterval();
		TemporalInterval i2 = o2.getInterval();
		return i1.getStartTime().getLowerBound() < i2.getStartTime().getLowerBound() ? -1 :
			i1.getStartTime().getLowerBound() == i2.getStartTime().getLowerBound() && 
			i1.getStartTime().getUpperBound() < i2.getStartTime().getUpperBound() ? -1 : 1;
	}
}
