package it.cnr.istc.pst.platinum.ai.framework.microkernel.lang.flaw;

import java.util.ArrayList;
import java.util.List;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public abstract class Flaw 
{
	private int id;
	private FlawType type;						// the type of flaw
	private DomainComponent component;			// the component the flaw belongs to
	private List<FlawSolution> solutions;		// the list of available solutions
	
	/**
	 * 
	 * @param id
	 * @param component
	 * @param type
	 */
	protected Flaw(int id, DomainComponent component, FlawType type) {
		this.id = id;
		this.type = type;
		this.solutions = new ArrayList<>();
		this.component = component;
	}
	
	/**
	 * 
	 * @return
	 */
	public final int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return
	 */
	public final FlawType getType() {
		return type;
	}
	
	/**
	 * 
	 * @return
	 */
	public final FlawCategoryType getCategory() {
		return this.type.getCategory();
	}
	
	/**
	 * 
	 * @return
	 */
	public DomainComponent getComponent() {
		return component;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSolvable() {
		return !this.solutions.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public final List<FlawSolution> getSolutions() {
		return new ArrayList<>(this.solutions);
	}
	
	/**
	 * 
	 * @param solution
	 */
	public final void addSolution(FlawSolution solution) {
		this.solutions.add(solution);
	}
	
	/**
	 * 
	 * @return
	 */
	public final int getDegree() {
		return this.solutions.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Flaw other = (Flaw) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}
