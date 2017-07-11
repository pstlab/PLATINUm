package it.istc.pst.platinum.testing.framework.compiler;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.istc.pst.platinum.framework.domain.PlanDataBase;
import it.istc.pst.platinum.framework.domain.PlanDataBaseBuilder;
import it.istc.pst.platinum.framework.domain.component.ComponentValue;
import it.istc.pst.platinum.framework.domain.component.DomainComponent;
import it.istc.pst.platinum.framework.domain.component.DomainComponentType;
import it.istc.pst.platinum.framework.domain.component.ParameterPlaceHolder;
import it.istc.pst.platinum.framework.domain.component.pdb.ParameterSynchronizationConstraint;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationConstraint;
import it.istc.pst.platinum.framework.domain.component.pdb.SynchronizationRule;
import it.istc.pst.platinum.framework.domain.component.pdb.TokenVariable;
import it.istc.pst.platinum.framework.domain.component.resource.DiscreteResource;
import it.istc.pst.platinum.framework.domain.component.resource.ReservoirResource;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceConsumptionValue;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceProductionValue;
import it.istc.pst.platinum.framework.domain.component.resource.ResourceRequirementValue;
import it.istc.pst.platinum.framework.microkernel.lang.ex.ProblemInitializationException;
import it.istc.pst.platinum.framework.microkernel.lang.ex.SynchronizationCycleException;
import it.istc.pst.platinum.framework.microkernel.lang.plan.RelationType;
import it.istc.pst.platinum.framework.parameter.lang.NumericParameterDomain;
import it.istc.pst.platinum.framework.parameter.lang.ParameterDomainType;

/**
 * 
 * @author anacleto
 *
 */
public class DDLv3CompilerTestCase 
{
	private static final String DDL = "src/test/java/it/uniroma3/epsl2/testing/framework/compiler/test.ddl";
	private static final String PDL = "src/test/java/it/uniroma3/epsl2/testing/framework/compiler/test.pdl";
	
	/**
	 * 
	 */
	@Test
	public void buildTest() 
	{
		try
		{
			// parse plan database
			PlanDataBase pdb = PlanDataBaseBuilder.build(DDL, PDL);
			Assert.assertNotNull(pdb);
			
			// check parsed data
			List<DomainComponent> components = pdb.getComponents();
			Assert.assertNotNull(components);
			Assert.assertTrue(!components.isEmpty());
			Assert.assertTrue(components.size() == 5);
			
			// check functional variable
			DomainComponent component = pdb.getComponentByName("RoverController");
			Assert.assertNotNull(component);
			Assert.assertTrue(component.getType().equals(DomainComponentType.SV_FUNCTIONAL));
			
			// check external variable
			component = pdb.getComponentByName("Window");
			Assert.assertNotNull(component);
			Assert.assertTrue(component.getType().equals(DomainComponentType.SV_EXTERNAL));
			// check external values
			for (ComponentValue val : component.getValues()) {
				// check controllability type
				Assert.assertFalse(val.isControllable());
			}
			
			// check primitive variable 
			component = pdb.getComponentByName("BaseController");
			Assert.assertNotNull(component);
			Assert.assertTrue(component.getType().equals(DomainComponentType.SV_PRIMITIVE));
			
			
			// check reservoir resource
			component = pdb.getComponentByName("Battery");
			Assert.assertNotNull(component);
			Assert.assertTrue(component.getType().equals(DomainComponentType.RESOURCE_RESERVOIR));
			
			// get reservoir resource
			ReservoirResource rr = (ReservoirResource) component;
			Assert.assertTrue(rr.getMinCapacity() == 5);
			Assert.assertTrue(rr.getMaxCapacity() == 35);
			Assert.assertTrue(rr.getInitialCapacity() == 35);
			Assert.assertTrue(rr.getValues().size() == 2);
			
			// check production value
			ResourceProductionValue prod = rr.getProductionValue();
			Assert.assertNotNull(prod);
			Assert.assertTrue(prod.getLabel().equals("PRODUCTION"));
			Assert.assertTrue(prod.getParameterPlaceHolders().size() == 1);
			// check parameter domain
			NumericParameterDomain dom = (NumericParameterDomain) prod.getParameterPlaceHolderByIndex(0).getDomain();
			Assert.assertNotNull(dom);
			Assert.assertTrue(dom.getLowerBound() == 0);
			Assert.assertTrue(dom.getUpperBound() == rr.getMaxCapacity());
			
			// check consumption value
			ResourceConsumptionValue cons = rr.getConsumptionValue();
			Assert.assertNotNull(cons);
			Assert.assertTrue(cons.getLabel().equals("CONSUMPTION"));
			Assert.assertTrue(cons.getParameterPlaceHolders().size() == 1);
			// check parameter domain 
			dom = (NumericParameterDomain) cons.getParameterPlaceHolderByIndex(0).getDomain();
			Assert.assertNotNull(dom);
			Assert.assertTrue(dom.getLowerBound() == 0);
			Assert.assertTrue(dom.getUpperBound() == rr.getMaxCapacity());
			
			// check discrete resource
			component = pdb.getComponentByName("Energy");
			Assert.assertNotNull(component);
			Assert.assertTrue(component.getType().equals(DomainComponentType.RESOURCE_DISCRETE));
			
			// cast component
			DiscreteResource resource = (DiscreteResource) component;
			// check parameters
			Assert.assertTrue(resource.getMinCapacity() == 0);
			Assert.assertTrue(resource.getMaxCapacity() == 10);
			Assert.assertTrue(resource.getInitialCapacity() == 10);
			Assert.assertTrue(resource.getInitialCapacity() == 10);
			Assert.assertTrue(resource.getValues().size() == 1);
			
			// check requirement value
			ResourceRequirementValue req = resource.getRequirementValue();
			Assert.assertNotNull(req);
			Assert.assertTrue(req.getLabel().equals("REQUIREMENT"));
			Assert.assertTrue(req.getParameterPlaceHolders().size() == 1);
			
			// check parameter place holder
			ParameterPlaceHolder ph = req.getParameterPlaceHolderByIndex(0);
			Assert.assertNotNull(ph);
			Assert.assertTrue(ph.getDomain().getType().equals(ParameterDomainType.NUMERIC_DOMAIN_PARAMETER_TYPE));
			dom = (NumericParameterDomain) ph.getDomain();
			// check allowed bounds
			Assert.assertTrue(dom.getLowerBound() == 0);
			Assert.assertTrue(dom.getUpperBound() == resource.getMaxCapacity());
			
			// check synchronization
			List<SynchronizationRule> rules = pdb.getSynchronizationRules();
			Assert.assertNotNull(rules);
			Assert.assertTrue(rules.size() == 3);
			
			// get a rule
			SynchronizationRule rule = rules.get(0);
			Assert.assertTrue(rule.getTokenVariables().size() == 2);
			// check constraints 
			List<SynchronizationConstraint> constraints = rule.getConstraints();
			Assert.assertTrue(constraints.size() == 3);
			// check constraints
			for (SynchronizationConstraint constraint : constraints) 
			{
				// check type 
				if (constraint.getType().equals(RelationType.BIND_PARAMETER))
				{
					// cast constraint
					ParameterSynchronizationConstraint psc = (ParameterSynchronizationConstraint) constraint;
					// check token variable
					TokenVariable reference = psc.getTarget();
					if (reference.getValue().equals(rr.getConsumptionValue()))
					{
						// check expected parameter constraint
						Assert.assertTrue(psc.getReferenceLabel().equals("?cons"));
						Assert.assertTrue(psc.getTargetLabel().equals("3"));
					}
					
					if (reference.getValue().equals(resource.getRequirementValue()))
					{
						// check expected parameter constraint
						Assert.assertTrue(psc.getReferenceLabel().equals("?amount"));
						Assert.assertTrue(psc.getTargetLabel().equals("7"));
					}
				}
			}
		}
		catch (ProblemInitializationException | SynchronizationCycleException ex) {
			System.err.println(ex.getMessage());
			Assert.assertTrue(false);
		}
	}

}
