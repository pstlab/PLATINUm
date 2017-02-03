package it.uniroma3.epsl2.framework.lang.plan;

import java.util.concurrent.atomic.AtomicInteger;

import it.uniroma3.epsl2.framework.domain.component.ComponentValue;
import it.uniroma3.epsl2.framework.domain.component.DomainComponent;
import it.uniroma3.epsl2.framework.domain.component.PlanElementStatus;
import it.uniroma3.epsl2.framework.domain.component.Token;
import it.uniroma3.epsl2.framework.parameter.lang.Parameter;

/**
 * 
 * @author anacleto
 *
 */
public class Decision 
{
	private static AtomicInteger ID_COUNTER = new AtomicInteger(0);
	private int id;
	private Decision causalLink;		// set "generator" decision
	private ComponentValue value;
	private PlanElementStatus status;
	private String[] labels;
	private long[] start;
	private long[] end;
	private long[] nominalDuration;
	private Token token;
	
	// solving knowledge
	private boolean mandatoryExpansion;
	private boolean mandatoryUnification;
	
	/**
	 * 
	 * @param value
	 * @param labels
	 * @param start
	 * @param end
	 * @param nominalDuration
	 */
	public Decision(ComponentValue value, String[] labels, long[] start, long[] end, long[] nominalDuration) 
	{
		// set id
		this.id = ID_COUNTER.getAndIncrement();
		// set parameter labels
		this.labels = labels;
		// set causal link
		this.causalLink = null;
		// set related value
		this.value = value;
		this.start = start;
		this.end = end;
		this.nominalDuration = nominalDuration;
		// set initial status
		this.status = PlanElementStatus.PENDING;
		this.token = null;
		
		// set solving knowledge
		this.mandatoryExpansion = false;
		this.mandatoryUnification = false;
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
	 * @return
	 */
	public boolean isActive() {
		return this.status.equals(PlanElementStatus.ACTIVE);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isPending() {
		return this.status.equals(PlanElementStatus.PENDING);
	}
	
	/**
	 * 
	 * @param token
	 */
	public void setToken(Token token) {
		this.token = token;
		this.status = PlanElementStatus.ACTIVE;
	}
	
	/**
	 * 
	 */
	public void clear() {
		this.token = null;
		this.status = PlanElementStatus.PENDING;
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
	 * @return
	 */
	@Override
	public String toString() {
		return "[Decision <" + (this.value.isControllable() ? "c" : "u") + "> "
				+ "id= " + this.id + " "
				+ "value= " + this.value.getLabel() +" "
				+ "status= " + this.status + " "
				+ "start= [" + this.getStart()[0] + ", " + this.getStart()[1] + "] "
				+ "duration= [" + this.getDuration()[0] + ", " + this.getDuration()[1] + "] "
				+ "nominal-duration= [" + this.getNominalDuration()[0] + ", " + this.getNominalDuration()[1] + "] "
				+ "end= [" + this.getEnd()[0] + ", " + this.getEnd()[1] + "]]";
	}
}
