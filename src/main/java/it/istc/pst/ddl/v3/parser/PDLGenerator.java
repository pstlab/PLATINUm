package it.istc.pst.ddl.v3.parser;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class PDLGenerator {

//    public static String generatePDL(SimpleDomain domain, DecisionNetwork network) {
//	String pdl = new String();
//	HashMap<ComponentDecision, String> decision_ids = new HashMap<ComponentDecision, String>();
//	HashMap<Parameter, String> parameter_ids = new HashMap<Parameter, String>();
//	TemporalModule tm = domain.getTemporalModule();
//	ParameterManager pm = domain.getParameterManager();
//	pdl += "PROBLEM auto_generated_pdl (DOMAIN " + domain.getName() + ") {\n";
//
//	for (Component c : domain.getComponents()) {
//	    for (Assertion a : c.getAssertions()) {
//		String id = c.getName() + "_" + a.getId();
//		decision_ids.put((ComponentDecision) a, id);
//		pdl += "    ";
//		pdl += id + " <fact> " + a.getReasoner().getName();
//		pdl += ".";
//		if (a.getScope().length > 1) {
//		    pdl += "{";
//		    for (Element e : a.getScope()) {
//			pdl += e.getLabel() + ", ";
//		    }
//		    pdl += "}";
//		} else {
//		    pdl += a.getScope()[0].getLabel();
//		}
//		pdl += ".";
//
//		if (a instanceof SimpleGroundValueChoice) {
//		    SVValue value = (SVValue) ((SimpleGroundValueChoice) a).getValue();
//		    pdl += value.getSignature().getName() + "()";
//
//		    TPBound start = (TPBound) tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//		    start.setT(((TimeInterval) ((SimpleGroundValueChoice) a).getTemporalElement()).getFrom());
//		    tm.askQuery(start);
//		    pdl += " AT " + getBound(domain, start.getLb(), start.getUb());
//
//		    TPBound end = (TPBound) tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//		    end.setT(((TimeInterval) ((SimpleGroundValueChoice) a).getTemporalElement()).getTo());
//		    tm.askQuery(end);
//		    pdl += " " + getBound(domain, end.getLb(), end.getUb());
//
//		    TPDistance duration = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//		    duration.setTi(((TimeInterval) ((SimpleGroundValueChoice) a).getTemporalElement()).getFrom());
//		    duration.setTj(((TimeInterval) ((SimpleGroundValueChoice) a).getTemporalElement()).getTo());
//		    tm.askQuery(duration);
//		    pdl += " " + getBound(domain, duration.getMin(), duration.getMax());
//		}
//
//		if (a instanceof SingletonValueChoice) {
//		    SVValue value = (SVValue) ((SingletonValueChoice) a).getValue();
//		    pdl += value.getSignature().getName() + "(";
//		    for (Parameter par : value.getParameters()) {
//			String p_id = "?p" + par.getId();
//			parameter_ids.put(par, p_id);
//			pdl += p_id;
//			ParameterAllowedValues p_query = (ParameterAllowedValues) pm.createElementQuery(ParameterElementQueryType.VALUES);
//			p_query.setP((CSPParameter) par);
//			pm.askQuery(p_query);
//			assert p_query.getLb() == p_query.getUb();
//			pdl += " = " + p_query.getLb() + ", ";
//		    }
//		    pdl += ")";
//
//		    TPBound start = (TPBound) tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//		    start.setT(((TimeInterval) ((SingletonValueChoice) a).getTemporalElement()).getFrom());
//		    tm.askQuery(start);
//		    pdl += " AT " + getBound(domain, start.getLb(), start.getUb());
//
//		    TPBound end = (TPBound) tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//		    end.setT(((TimeInterval) ((SingletonValueChoice) a).getTemporalElement()).getTo());
//		    tm.askQuery(end);
//		    pdl += " " + getBound(domain, end.getLb(), end.getUb());
//
//		    TPDistance duration = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//		    duration.setTi(((TimeInterval) ((SingletonValueChoice) a).getTemporalElement()).getFrom());
//		    duration.setTj(((TimeInterval) ((SingletonValueChoice) a).getTemporalElement()).getTo());
//		    tm.askQuery(duration);
//
//		    if (end.getUb() < ((STPTemporalModule) domain.getTemporalModule()).getH()) {
//			pdl += " " + getBound(domain, duration.getMin(), duration.getMax());
//		    } else {
//			String bound = "[";
//			if (duration.getMin() == Long.MIN_VALUE + 1) {
//			    bound += "-INF";
//			} else {
//			    bound += duration.getMin();
//			}
//			bound += ", ";
//			bound += "+INF";
//			bound += "]";
//			pdl += " " + bound;
//		    }
//		}
//		pdl += ";\n";
//	    }
//	    pdl += "\n";
//	}
//
//	pdl += getRelations(domain, decision_ids, parameter_ids, network);
//
//	pdl += "}";
//
//	pdl = pdl.replace(", }", "}");
//	pdl = pdl.replace(", )", ")");
//	return pdl;
//    }
//
//    private static String getRelations(SimpleDomain domain, HashMap<ComponentDecision, String> decision_ids, HashMap<Parameter, String> parameter_ids, DecisionNetwork network) {
//	String dn = new String();
//
//	Map<Component, Collection<Relation>> meets = new HashMap<Component, Collection<Relation>>();
//	for (Component c : domain.getComponents()) {
//	    meets.put(c, new ArrayList<Relation>());
//	}
//	for (Relation rel : network.getEdges()) {
//	    if (rel instanceof STPTemporalConstraint && ((STPTemporalConstraint) rel).getTemporalType() == TemporalConstraintType.Meets && ((STPTemporalConstraint) rel).getFrom().getReasoner() == ((STPTemporalConstraint) rel).getTo().getReasoner()) {
//		meets.get(((STPTemporalConstraint) rel).getFrom().getReasoner()).add(rel);
//	    }
//	}
//
//	for (Component c : domain.getComponents()) {
//	    Map<Assertion, Relation> cds = new HashMap<Assertion, Relation>();
//	    for (Relation rel : meets.get(c)) {
//		cds.put(((STPTemporalConstraint) rel).getFrom(), rel);
//	    }
//	    List<Assertion> sorted_cds = new ArrayList<Assertion>(cds.keySet());
//	    Collections.sort(sorted_cds, new AssertionComparator(domain));
//	    for (Assertion a : sorted_cds) {
//		dn += "    ";
//		dn += decision_ids.get(((STPTemporalConstraint) cds.get(a)).getFrom()) + " MEETS " + decision_ids.get(((STPTemporalConstraint) cds.get(a)).getTo());
//		dn += ";\n";
//	    }
//	    dn += "\n";
//	}
//
//	for (Relation rel : network.getEdges()) {
//	    if (rel instanceof STPTemporalConstraint) {
//		switch (((STPTemporalConstraint) rel).getTemporalType()) {
//		case Equals:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " EQUALS " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case Before:
//		    if (((STPTemporalConstraint) rel).getFirstLowerBound() != 0) {
//			dn += "    ";
//			dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " BEFORE " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//			dn += ";\n";
//		    }
//		    break;
//		case After:
//		    if (((STPTemporalConstraint) rel).getFirstLowerBound() != 0) {
//			dn += "    ";
//			dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " AFTER " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//			dn += ";\n";
//		    }
//		    break;
//		case Meets:
//		    if (((STPTemporalConstraint) rel).getFrom().getReasoner() != ((STPTemporalConstraint) rel).getTo().getReasoner()) {
//			dn += "    ";
//			dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " MEETS " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//			dn += ";\n";
//		    }
//		    break;
//		case MetBy:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " MET-BY " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case Starts:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " STARTS " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case StartedBy:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " STARTED-BY " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case During:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " DURING " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case Contains:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " CONTAINS " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case Finishes:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " FINISHES " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case FinishedBy:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " FINISHED-BY " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case Overlaps:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " OVERLAPS " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case OverlappedBy:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " OVERLAPPED-BY " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getThirdLowerBound(), ((STPTemporalConstraint) rel).getThirdUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case StartStart:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " START-START " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case AtStart:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " AT-START " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case AtEnd:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " AT-END " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case BeforeStart:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " BEFORE-START " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case AfterEnd:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " AFTER-END " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case StartAt:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " START-AT " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case EndAt:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " END-AT " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case ContainsStart:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " CONTAINS-START " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case ContainsEnd:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " CONTAINS-END " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case StartsDuring:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " STARTS-DURING " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		case EndsDuring:
//		    dn += "    ";
//		    dn += decision_ids.get(((STPTemporalConstraint) rel).getFrom()) + " ENDS-DURING " + getBound(domain, ((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(domain, ((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + decision_ids.get(((STPTemporalConstraint) rel).getTo());
//		    dn += ";\n";
//		    break;
//		}
//	    }
//	    if (rel instanceof ParameterRelation) {
//		ParameterConstraint constraint = ((ParameterRelation) rel).getAssertion();
//		if (constraint instanceof BinaryParameterConstraint) {
//		    if (((BinaryParameterConstraint) constraint).getTargetVar().getParameters().isEmpty()) {
//			// Assignment constraint...
//			continue;
//		    }
//		    dn += "    ";
//		    dn += parameter_ids.get(((BinaryParameterConstraint) constraint).getRefVar());
//		    switch (((BinaryParameterConstraint) constraint).getConstraintType()) {
//		    case Equals:
//			dn += " = ";
//			break;
//		    case Less:
//			dn += " < ";
//			break;
//		    case LessEq:
//			dn += " <= ";
//			break;
//		    case Greater:
//			dn += " > ";
//			break;
//		    case GreaterEq:
//			dn += " >= ";
//			break;
//		    case NotEquals:
//			dn += " != ";
//			break;
//		    }
//		    dn += parameter_ids.get(((BinaryParameterConstraint) constraint).getTargetVar().getParameters().get(0));
//		    dn += ";\n";
//		}
//		if (constraint instanceof LinearParameterConstraint) {
//		    throw new UnsupportedOperationException("Not supported yet...");
//		}
//	    }
//	}
//
//	dn = dn.replace(", }", "}");
//	dn = dn.replace(", )", ")");
//	return dn;
//    }
//
//    private static String getBound(SimpleDomain domain, long lower_bound, long upper_bound) {
//	String bound = "[";
//	if (lower_bound == Long.MIN_VALUE + 1) {
//	    bound += "-INF";
//	} else {
//	    bound += lower_bound;
//	}
//	bound += ", ";
//	if (upper_bound >= ((STPTemporalModule) domain.getTemporalModule()).getH()) {
//	    bound += "+INF";
//	} else {
//	    bound += upper_bound;
//	}
//	bound += "]";
//	return bound;
//    }
//
//    private static final class AssertionComparator implements Comparator<Assertion> {
//
//	private final SimpleDomain domain;
//
//	public AssertionComparator(SimpleDomain domain) {
//	    this.domain = domain;
//	}
//
//	@Override
//	public int compare(Assertion cd1, Assertion cd2) throws RuntimeException {
//	    TemporalModule tm = domain.getTemporalModule();
//
//	    if (cd1 instanceof TRFComponentDecision && cd2 instanceof TRFComponentDecision) {
//		TPDistance bef = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//		bef.setTi(((TimeInterval) ((TRFComponentDecision) cd1).getTemporalElement()).getTo());
//		bef.setTj(((TimeInterval) ((TRFComponentDecision) cd2).getTemporalElement()).getFrom());
//
//		tm.askQuery(bef);
//
//		if (bef.getMin() >= 0) {
//		    return -1;
//		}
//
//		TPDistance aft = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//		aft.setTi(((TimeInterval) ((TRFComponentDecision) cd2).getTemporalElement()).getTo());
//		aft.setTj(((TimeInterval) ((TRFComponentDecision) cd1).getTemporalElement()).getFrom());
//
//		tm.askQuery(aft);
//
//		if (aft.getMin() >= 0) {
//		    return 1;
//		}
//	    }
//
//	    throw new RuntimeException("[PDLGenerator] Not ordered decisions: " + cd1 + " " + cd2 + "\nThis method should be called only among completely ordered decisions");
//	}
//    }
}
