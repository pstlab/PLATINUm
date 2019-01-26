package it.istc.pst.platinum.framework.utils.view.executive;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import it.istc.pst.platinum.executive.pdb.ExecutionNode;

/**
 * 
 * @author anacleto
 *
 */
public class ExecutiveWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private long horizon;
	private List<ExecutionNode> dataset;
	private ChartPanel panel;
	
	/**
	 * 
	 * @param label
	 */
	public ExecutiveWindow(String label) {
		super(label);
		
		// add the chart to a panel
		this.panel = new ChartPanel(null);
		setContentPane(this.panel);
		
		// display the  graph
		this.pack();
		RefineryUtilities.positionFrameOnScreen(this, 0, 0);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		// set close operation
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * 
	 * @param horizon
	 * @param data
	 */
	public void setDataSet(long horizon, List<ExecutionNode> data) {
		// set horizon
		this.horizon = horizon;
		// set data set
		this.dataset = new ArrayList<>(data);
	}
	
	/**
	 * 
	 * @param units - time units from the execution start
	 */
	public void display(long units) 
	{
		// create the data-set from the plan
		final IntervalCategoryDataset data = this.setupDataSet(units);
		// create the chart
		final JFreeChart chart = ChartFactory.createGanttChart(
				null,			// chart title 
				null, 			// domain axis label
				"Time",			// range axis label 
				data,			// data to display 
				false,			// include legend 
				true, 			// tool-tips
				false			// URLs
		);
		
		// create the plot
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		DateAxis range = new DateAxis("Time");
		// set maximum range value
		range.setMaximumDate(new Date(this.horizon));
		range.setVisible(false);
		// set range to plot
		plot.setRangeAxis(range);
		
		// create and set value marker
		ValueMarker marker = new ValueMarker(units);
		marker.setLabel("Tau = " + units);
		marker.setPaint(Color.BLACK);
		plot.addRangeMarker(marker);
		
		// set chart to display
		this.panel.setChart(chart);
		if (!this.isVisible()) {
			this.setVisible(true);
		}
	}
	
	/**
	 * 
	 * @param units
	 * @return
	 */
	private IntervalCategoryDataset setupDataSet(long units) 
	{
		// create task series collection
		TaskSeriesCollection data = new TaskSeriesCollection();
		// group data by component
		Map<String, Map<String, List<ExecutionNode>>> index = new HashMap<>();
		for (ExecutionNode node : this.dataset) 
		{
			// add entry for component
			if (!index.containsKey(node.getComponent())) {
				index.put(node.getComponent(), new HashMap<String, List<ExecutionNode>>());
			}
			
			// add entry for predicate
			if (!index.get(node.getComponent()).containsKey(node.getGroundSignature())) {
				index.get(node.getComponent()).put(node.getGroundSignature(), new ArrayList<ExecutionNode>());
			}
			
			// add node
			index.get(node.getComponent()).get(node.getGroundSignature()).add(node);
		}
		
		// set data to show
		for (String component : index.keySet()) 
		{
			// create a  task series
			TaskSeries s = new TaskSeries(component);
			// check component's predicates
			for (String predicate : index.get(component).keySet()) 
			{
				// get nodes
				List<ExecutionNode> nodes = index.get(component).get(predicate);
				// create a task
				Task task = new Task(component + "." + predicate,
						new Date(nodes.get(0).getStart()[0]),
						new Date(nodes.get(nodes.size() - 1).getEnd()[0]));
				
				// add sub-tasks if necessary
				if (nodes.size() > 1) 
				{
					// check sub-tasks
					for (ExecutionNode node : nodes) 
					{
						// create a sub-task
						Task subTask = new Task(node.getGroundSignature(),
								new Date(node.getStart()[0]),
								new Date(node.getStart()[0] + node.getDuration()[0]));
						
						// add sub-task
						task.addSubtask(subTask);
					}
				}
				else 
				{
					// get node
					ExecutionNode node = nodes.get(0);
					// check if execution status
					if (units < node.getStart()[0]) {
						// execution not started yet
						task.setPercentComplete(0.0);
					}
					else if (units > node.getEnd()[0]) {
						// execution ended
						task.setPercentComplete(1.0);
					}
					else 
					{
						// in execution
						double execUnits = units - node.getStart()[0];
						double minDuration = node.getDuration()[0];
						// compute execution percentage
						double completion = execUnits / minDuration;
						task.setPercentComplete(completion);
					}
				}
				
				// add task to the series
				s.add(task);
			}
			
			// crate data to show 
			data.add(s);
		}
		
		// return data to show
		return data;
	}
}
