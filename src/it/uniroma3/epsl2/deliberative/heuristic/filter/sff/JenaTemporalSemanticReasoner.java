package it.uniroma3.epsl2.deliberative.heuristic.filter.sff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

import it.uniroma3.epsl2.framework.lang.plan.Decision;
import it.uniroma3.epsl2.framework.lang.plan.Relation;
import it.uniroma3.epsl2.framework.lang.plan.RelationType;
import it.uniroma3.epsl2.framework.microkernel.ConstraintCategory;

/**
 * 
 * @author anacleto
 *
 */
public class JenaTemporalSemanticReasoner implements TemporalSemanticReasoner 
{
	private static final String ONTOLOGY_PATH = "etc/ftlp.owl";
	private static final String ONTOLOGY_URL = "http://www.uniroma3.it/ontologies/ftlp"; 
	private static final String ONTOLOGY_NS = ONTOLOGY_URL + "#";
	private static final String ONTOLOGY_RULES = "etc/rules";
	
	private OntModel tbox;
	private Model abox;
	private Map<Resource, Decision> index;
	
	/**
	 * 
	 */
	protected JenaTemporalSemanticReasoner() 
	{
		// setup index
		this.index = new HashMap<>();
		// setup the TBox
		this.tbox = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		this.tbox.getDocumentManager().addAltEntry(ONTOLOGY_URL, "file:" + ONTOLOGY_PATH);
		this.tbox.read(ONTOLOGY_URL, "RDF/XML"); 
		// create a custom rule-based reasoner
		Reasoner reasoner = new GenericRuleReasoner(Rule.rulesFromURL("file:" + ONTOLOGY_RULES));
		reasoner.setDerivationLogging(true);
		// create the related inference model
		this.abox = ModelFactory.createInfModel(reasoner, this.tbox);
	}
	
	/**
	 * 
	 * @param decision
	 */
	public void add(Decision decision) 
	{
		// get decision type
		Resource type = this.abox.getResource(ONTOLOGY_NS + "Decision");
		// check if a resource already exists
		if (this.abox.getResource(ONTOLOGY_NS + decision.getId()) != null) {
			// create individual
			Resource resource = this.abox.createResource(ONTOLOGY_NS + decision.getId(), type);
			// add entry to index
			this.index.put(resource, decision);
		}
	}
	
	/**
	 * 
	 * @param decision
	 */
	@Override
	public void delete(Decision decision)
	{
		// get related resource
		Resource resource = this.abox.getResource(ONTOLOGY_NS + decision.getId());
		// remove all statements concerning the resource
		this.abox.removeAll(resource, null, null);
		this.abox.removeAll(null, null, resource);
		// remove entry from the index
		this.index.remove(resource);
	}
	
	/**
	 * 
	 */
	@Override
	public void add(Relation relation)
	{
		// check category
		if (relation.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT))
		{
			// check relation type
			TemporalRole role = this.checkRelationType(relation.getType());
			// get property from the general knowledge
			Property property = this.tbox.getProperty(ONTOLOGY_NS + role.getLabel());
			// get related resources
			Resource subject, object;
			if (relation.getType().equals(RelationType.AFTER) || 
					relation.getType().equals(RelationType.MET_BY)) {
				// set subject and object
				subject = this.abox.getResource(ONTOLOGY_NS + relation.getTarget().getId());
				object = this.abox.getResource(ONTOLOGY_NS + relation.getReference().getId());
			}
			else {
				// set subject and object
				subject = this.abox.getResource(ONTOLOGY_NS + relation.getReference().getId());
				object = this.abox.getResource(ONTOLOGY_NS + relation.getTarget().getId());
			}
			// check if the related statement already exists
			if (!this.abox.listStatements(subject, property, object).hasNext()) {
				// add statement to the knowledge base
				this.abox.add(subject, property, object);
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void delete(Relation relation)
	{
		// check category
		if (relation.getCategory().equals(ConstraintCategory.TEMPORAL_CONSTRAINT))
		{
			// check relation type
			TemporalRole role = this.checkRelationType(relation.getType());
			// get property from the general knowledge
			Property property = this.tbox.getProperty(ONTOLOGY_NS + role.getLabel());
			// get related resources
			Resource subject, object;
			if (relation.getType().equals(RelationType.AFTER) || 
					relation.getType().equals(RelationType.MET_BY)) {
				// set subject and object
				subject = this.abox.getResource(ONTOLOGY_NS + relation.getTarget().getId());
				object = this.abox.getResource(ONTOLOGY_NS + relation.getReference().getId());
			}
			else {
				// set subject and object
				subject = this.abox.getResource(ONTOLOGY_NS + relation.getReference().getId());
				object = this.abox.getResource(ONTOLOGY_NS + relation.getTarget().getId());
			}
			// delete statement from the knowledge
			this.abox.removeAll(subject, property, object);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public Map<Decision, List<Decision>> getOrderingGraph() 
	{
		// initialize the graph
		Map<Decision, List<Decision>> graph = new HashMap<>();
		// initialize graph
		for (Decision decision : this.index.values()) {
			// add entry to graph
			graph.put(decision, new ArrayList<Decision>());
		}
		
		// check inferred "ordering" properties
		List<Statement> statements = this.read(TemporalRole.ORDERING);
		for (Statement s : statements)
		{
			// get related decisions
			Decision reference = this.index.get(s.getSubject());
			// check graph
			if (!graph.containsKey(reference)) {
				graph.put(reference, new ArrayList<Decision>());
			}
			
			// get target
			Decision target = this.index.get(s.getObject().asResource());
			if (!graph.containsKey(target)) {
				graph.put(target, new ArrayList<Decision>());
			}
			// add edge to graph
			graph.get(reference).add(target);
		}
		
		// get the graph
		return graph;
	}
	
	/**
	 * 
	 * @param rel
	 * @return
	 */
	private List<Statement> read(TemporalRole rel) 
	{
		// get property
		Property p = this.tbox.getProperty(ONTOLOGY_NS + rel.getLabel());
		List<Statement> list = new ArrayList<>();
		Iterator<Statement> it = this.abox.listStatements((Resource) null, p, (RDFNode) null);
		while (it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	private TemporalRole checkRelationType(RelationType type)
	{
		// the role
		TemporalRole role;
		// check relation type
		switch (type)
		{
			case AFTER : 
			case MET_BY :
			case MEETS : 
			case BEFORE : {
				// set role
				role = TemporalRole.BEFORE;
			}
			break;
			
			case CONTAINS : 
			case EQUALS : {
				// set role 
				role = TemporalRole.CONTAINS;
			}
			break;
			
			case DURING : 
			case STARTS_DURING : 
			case ENDS_DURING : {
				// set role 
				role = TemporalRole.DURING;
			}
			break;
			
			default : {
				// unknown semantic of the temporal relation
				throw new RuntimeException("Unknown semantic of temporal relation " + type);
			}
		}
		
		// get role
		return role;
	}
}
