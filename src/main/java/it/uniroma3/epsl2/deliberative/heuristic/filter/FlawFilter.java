package it.uniroma3.epsl2.deliberative.heuristic.filter;

import java.util.Collection;
import java.util.Set;

import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.ApplicationFrameworkObject;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.FrameworkLoggerReference;
import it.uniroma3.epsl2.framework.utils.log.FrameworkLogger;

/**
 * 
 * @author anacleto
 *
 */
public abstract class FlawFilter extends ApplicationFrameworkObject 
{
	private FlawFilterType type;
	
	@FrameworkLoggerReference
	protected FrameworkLogger logger;
	
	/**
	 * 
	 * @param type
	 */
	protected FlawFilter(FlawFilterType type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public FlawFilterType getType() {
		return this.type;
	}
	
	/**
	 * 
	 * @param flaws
	 * @return
	 */
	public abstract Set<Flaw> filter(Collection<Flaw> flaws);
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "[FlawFilter type= " +  this.type + "]";
	}
}
