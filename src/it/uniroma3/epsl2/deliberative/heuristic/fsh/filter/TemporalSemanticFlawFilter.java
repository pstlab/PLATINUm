package it.uniroma3.epsl2.deliberative.heuristic.fsh.filter;

import java.util.Collection;
import java.util.Set;

import org.apache.jena.ontology.OntModel;

import it.uniroma3.epsl2.framework.domain.PlanDataBase;
import it.uniroma3.epsl2.framework.lang.flaw.Flaw;
import it.uniroma3.epsl2.framework.microkernel.annotation.framework.inject.PlanDataBaseReference;

/**
 * 
 * @author anacleto
 *
 */
public class TemporalSemanticFlawFilter extends FlawFilter
{
	@PlanDataBaseReference
	private PlanDataBase pdb;
	
	private OntModel tbox;
	
	private static final String FTLP_ONTOLOGY_PATH = "etc/ftlp.owl";
	private static final String FTLP_ONTOLOGY_URL = "http://www.uniroma3.it/ontologies/ftlp"; 
	private static final String FTLP_ONTOLOGY_NS = FTLP_ONTOLOGY_URL + "#";
	private static final String FTLP_ONTOLOGY_RULES = "etc/rules";
	
	/**
	 * 
	 */
	protected TemporalSemanticFlawFilter() {
		super(FlawFilterType.TSFF);
	}
	
	/**
	 * 
	 */
	@Override
	public Set<Flaw> filter(Collection<Flaw> flaws) {
		// TODO Auto-generated method stub
		return null;
	}
}
