package it.cnr.istc.pst.platinum.ai.framework.domain.component;

import it.cnr.istc.pst.platinum.ai.executive.pdb.ExecutionNodeStatus;
import it.cnr.istc.pst.platinum.ai.framework.parameter.lang.Parameter;

/**
 * 
 * @author alessandro
 *
 */
public class Decision implements Comparable<Decision>
{
	private int id;
	private Decision causalLink;		// set "generator" decision
	private ComponentValue value;
	private String[] labels;
	private long[] start;
	private long[] end;
	private long[] nominalDuration;
	private Token token;
	
	// solving knowledge
	private boolean mandatoryExpansion;
	private boolean mandatoryUnification;
	
	// execution knowledge
	private ExecutionNodeStatus startExecutionState;
	
	/**
	 * 
	 * @param id
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param nominalDuration
	 */
	protected Decision(int id, ComponentValue value, String[] labels, long[] start, long[] end, long[] nominalDuration) 
	{
		// set id
		this.id = id;
		// set parameter labels
		this.labels = labels;
		// set causal link
		this.causalLink = null;
		// set related value
		this.value = value;
		this.start = start;
		this.end = end;
		this.nominalDuration = nominalDuration;
		this.token = null;
		
		// set solving knowledge
		this.mandatoryExpansion = false;
		this.mandatoryUnification = false;

		// set default start execution state
		this.startExecutionState = ExecutionNodeStatus.WAITING;
	}
	
	/**
	 * 
	 * @param id
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param nominalDuration
	 * @param status
	 */
	protected Decision(int id, ComponentValue value, String[] labels, long[] start, long[] end, long[] nominalDuration, ExecutionNodeStatus status) 
	{
		// set id
		this.id = id;
		// set parameter labels
		this.labels = labels;
		// set causal link
		this.causalLink = null;
		// set related value
		this.value = value;
		this.start = start;
		this.end = end;
		this.nominalDuration = nominalDuration;
		this.token = null;
		
		// set solving knowledge
		this.mandatoryExpansion = false;
		this.mandatoryUnification = false;

		// set default start execution state
		this.startExecutionState = status;
	}

	/**
	 * 
	 * @return
	 */
	public ExecutionNodeStatus getStartExecutionState() {
		return startExecutionState;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param dec
	 */
	public void setCausalLink(Decision dec) {
		this.causalLink = dec;
	}
	
	/**
	 * 
	 * @return
	 */
	public Decision getCausalLink() {
		return this.causalLink;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMandatoryExpansion() {
		return mandatoryExpansion;
	}
	
	/**
	 * 
	 */
	public void setMandatoryExpansion() {
		this.mandatoryExpansion = true;
		this.mandatoryUnification = false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isMandatoryUnification() {
		return mandatoryUnification;
	}
	
	/**
	 * 
	 */
	public void setMandatoryUnification() {
		this.mandatoryUnification = true;
		this.mandatoryExpansion = false;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getParameterLabels() {
		return this.labels;
	}
	
	/**
	 * 
	 * @param label
	 * @return
	 */
	public int getParameterIndexByLabel(String label) {
		int index = 0;
		boolean found = false;
		while (index < this.labels.length && !found) {
			if (this.labels[index].equals(label)) {
				found = true;
			}
			else {
				index++;
			}
		}
		
		// check 
		if (!found) {
			throw new RuntimeException("Parameter label not found label= " + label);
		}
		
		// get index
		return index;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getParameterLabelByIndex(int index) {
		return this.labels[index];
	}
	
	/**
	 * 
	 * @param index
	 * @param label
	 */
	public void setParameterLabelByIndex(int index, String label) {
		this.labels[index] = label;
		Parameter<?> param = this.getParameterByIndex(index);
		param.setLabel(label);
	}
	
	/**
	 * 
	 * @return
	 */
	public ComponentValue getValue() {
		return value;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getStart() {
		// set "required" start bound
		long[] s = this.start;
		if (this.token != null) {
			// set scheduled start time
			s = new long[] {
				this.token.getInterval().getStartTime().getLowerBound(),
				this.token.getInterval().getStartTime().getUpperBound()
			};
		}
		// get start time
		return s;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getEnd() {
		// set "required" end bound
		long[] e = end;
		if (this.token != null) {
			// set scheduled end time
			e = new long[] {
				this.token.getInterval().getEndTime().getLowerBound(),
				this.token.getInterval().getEndTime().getUpperBound()
			};
		}
		
		// get end time
		return e;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getDuration() {
		// set "required" duration
		long[] d = this.nominalDuration;
		if (this.token != null) {
			// set scheduled duration
			d = new long[] {
				this.token.getInterval().getDurationLowerBound(),
				this.token.getInterval().getDurationUpperBound()
			};
		}
		// get duration
		return d;
	}
	
	/**
	 * 
	 * @return
	 */
	public long[] getNominalDuration() {
		// set "nominal" duration
		return this.nominalDuration;
	}
	
	/**
	 * 
	 * @return
	 */
	public DomainComponent getComponent() {
		return this.value.getComponent();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isControllable() {
		return this.value.isControllable();
	}
	
	/**
	 * 
	 * @param token
	 */
	public void setToken(Token token) {
		this.token = token;
	}
	
	/**
	 * 
	 */
	public void clear() {
		this.token = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public Parameter<?> getParameterByIndex(int index) {
		return this.token.getPredicate().getParameterByIndex(index);
	}
	
	/**
	 * 
	 */
	@Override
	public int compareTo(Decision o) {
		// check tokens if not null, the IDs otherwise
		return this.token != null && o.token != null ? this.token.compareTo(o.token) :
			this.id < o.id ? -1 : this.id > o.id ? 1 : 0;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Decision other = (Decision) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		// JSON style object description
		return "{ "
				+ "id: " + this.id + ", "
				+ "component: \"" + this.value.getComponent().getName() + "\", "
				+ (this.token == null ? "value: \"" + this.value + "\"" : "token: \"" +  this.token + "\"") + ", " 
				+ " }";
	}
}
