package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

/**
 * 
 * @author Riccardo De Benedictis
 */
public class DDLGenerator {

//    public static String getPDLRepresentation(SimpleDomain domain, DecisionNetwork network, RepresentationStyle style) {
//        HashMap<ComponentDecision, String> ids = new HashMap<ComponentDecision, String>();
//        TemporalModule tm = domain.getTemporalModule();
//        ParameterManager pm = domain.getParameterManager();
//        String pdl = new String();
//        pdl += "PROBLEM auto_generated_pdl (DOMAIN " + domain.getName() + ") {\n";
//        for (Assertion a : network.getNodes()) {
//            String id = a.getReasoner().getName() + "_" + a.getId();
//            ids.put((ComponentDecision) a, id);
//            pdl += "    ";
//            pdl += id + " <fact> " + a.getReasoner().getName();
//            pdl += ".";
//            if (a.getScope().length > 1) {
//                pdl += "{";
//                for (Element e : a.getScope()) {
//                    pdl += e.getLabel() + ", ";
//                }
//                pdl += "}";
//            } else {
//                pdl += a.getScope()[0].getLabel();
//            }
//            pdl += ".";
//
//            if (a instanceof SimpleGroundValueChoice) {
//                SVValue value = (SVValue) ((SimpleGroundValueChoice) a).getValue();
//                pdl += value.getSignature().getName() + "()";
//
//                TPBound start = (TPBound) tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//                start.setT(((TimeInterval) ((SimpleGroundValueChoice) a).getTemporalElement()).getFrom());
//                tm.askQuery(start);
//                pdl += " AT " + DDLGenerator.getBound(start.getLb(), start.getUb());
//
//                TPBound end = (TPBound) tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//                end.setT(((TimeInterval) ((SimpleGroundValueChoice) a).getTemporalElement()).getTo());
//                tm.askQuery(end);
//                pdl += " " + getBound(end.getLb(), end.getUb());
//
//                TPDistance duration = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//                duration.setTi(((TimeInterval) ((SimpleGroundValueChoice) a).getTemporalElement()).getFrom());
//                duration.setTj(((TimeInterval) ((SimpleGroundValueChoice) a).getTemporalElement()).getTo());
//                tm.askQuery(duration);
//                pdl += " " + getBound(duration.getMin(), duration.getMax());
//            }
//
//            if (a instanceof SingletonValueChoice) {
//                SVValue value = (SVValue) ((SingletonValueChoice) a).getValue();
//                pdl += value.getSignature().getName() + "(";
//                for (Parameter par : value.getParameters()) {
//                    pdl += par.getName();
//            		ParameterAllowedValues p_query = (ParameterAllowedValues) pm.createElementQuery(ParameterElementQueryType.VALUES);
//            		p_query.setP((CSPParameter) par);
//            		pm.askQuery(p_query);
//            		assert p_query.getLb() == p_query.getUb();
//            		pdl += " = " + p_query.getLb() + ", ";
//                }
//                pdl += ")";
//
//                TPBound start = (TPBound) tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//                start.setT(((TimeInterval) ((SingletonValueChoice) a).getTemporalElement()).getFrom());
//                tm.askQuery(start);
//                pdl += " AT " + DDLGenerator.getBound(start.getLb(), start.getUb());
//
//                TPBound end = (TPBound) tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//                end.setT(((TimeInterval) ((SingletonValueChoice) a).getTemporalElement()).getTo());
//                tm.askQuery(end);
//                pdl += " " + getBound(end.getLb(), end.getUb());
//
//                TPDistance duration = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//                duration.setTi(((TimeInterval) ((SingletonValueChoice) a).getTemporalElement()).getFrom());
//                duration.setTj(((TimeInterval) ((SingletonValueChoice) a).getTemporalElement()).getTo());
//                tm.askQuery(duration);
//                pdl += " " + getBound(duration.getMin(), duration.getMax());
//            }
//            /*
//             * if (a instanceof Activity) { dn += "A(" + ((Activity)
//             * a).getAmount() + ")";
//             *
//             * TPBound start = (TPBound)
//             * tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//             * start.setT(((TimeInterval) ((SimpleGroundValueChoice)
//             * a).getTemporalElement()).getFrom()); tm.askQuery(start); dn +=
//             * " AT " + DDLGenerator.getBound(start.getLb(), start.getUb());
//             *
//             * TPDistance duration = (TPDistance)
//             * tm.createElementQuery(TemporalElementQueryTypeEnhanced
//             * .TIME_POINT_DISTANCE); duration.setTi(((TimeInterval)
//             * ((SimpleGroundValueChoice) a).getTemporalElement()).getFrom());
//             * duration.setTj(((TimeInterval) ((SimpleGroundValueChoice)
//             * a).getTemporalElement()).getTo()); tm.askQuery(duration); dn +=
//             * " " + getBound(duration.getMin(), duration.getMax());
//             *
//             * TPBound end = (TPBound)
//             * tm.createElementQuery(TemporalElementQueryType.TIME_POINT_BOUND);
//             * end.setT(((TimeInterval) ((SimpleGroundValueChoice)
//             * a).getTemporalElement()).getFrom()); tm.askQuery(end); dn += " "
//             * + getBound(end.getLb(), end.getUb()); }
//             */
//            pdl += ";\n";
//        }
//
//	switch (style) {
//	    case Timeline:
//		pdl += getTimelineRepresentation(domain, ids);
//		break;
//	    case DecisionNetwork:
//		pdl += getDecisionNetworkRelations(ids, network);
//		break;
//	}
//
//        pdl += "}";
//
//        pdl = pdl.replace(", }", "}");
//        pdl = pdl.replace(", )", ")");
//        return pdl;
//    }
//
//    @SuppressWarnings("unchecked")
//    private static String getTimelineRepresentation(SimpleDomain domain, HashMap<ComponentDecision, String> ids) {
//        AssertionComparator comparator = new AssertionComparator(domain);
//        Map<Component, Set<ComponentDecision>> decisions = new HashMap<Component, Set<ComponentDecision>>();
//        for (ComponentDecision cd : ids.keySet()) {
//            if (!decisions.containsKey((Component) cd.getReasoner())) {
//                decisions.put((Component) cd.getReasoner(), new HashSet<ComponentDecision>());
//            }
//            decisions.get((Component) cd.getReasoner()).add(cd);
//        }
//
//        Map<Component, List<ComponentDecision>> ordered_decisions = new HashMap<Component, List<ComponentDecision>>();
//        for (Component c : decisions.keySet()) {
//            List<ComponentDecision> c_decisions = new ArrayList<ComponentDecision>(decisions.get(c));
//            Collections.sort(c_decisions, comparator);
//            ordered_decisions.put(c, c_decisions);
//        }
//
//        String representation = new String();
//        TemporalModule tm = domain.getTemporalModule();
//        TPDistance distance = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//        Set<Entry<Component, List<ComponentDecision>>> entrySet = ordered_decisions.entrySet();
//
//        Entry[] entries = entrySet.toArray(new Entry[entrySet.size()]);
//
//        // Generate MEETS constraints...
//        for (int i = 0; i < entries.length; i++) {
//            Entry<Component, List<ComponentDecision>> entry = entries[i];
//            for (ComponentDecision cd1 : entry.getValue()) {
//                for (ComponentDecision cd2 : entry.getValue()) {
//                    if(cd1 instanceof SimpleGroundValueChoice) {
//                	distance.setTi(((TimeInterval) ((SimpleGroundValueChoice) cd1).getTemporalElement()).getTo());
//                    }
//                    if(cd1 instanceof SingletonValueChoice) {
//                	distance.setTi(((TimeInterval) ((SingletonValueChoice) cd1).getTemporalElement()).getTo());
//                    }
//                    if(cd2 instanceof SimpleGroundValueChoice) {
//                        distance.setTj(((TimeInterval) ((SimpleGroundValueChoice) cd2).getTemporalElement()).getFrom());
//                    }
//                    if(cd2 instanceof SingletonValueChoice) {
//                        distance.setTj(((TimeInterval) ((SingletonValueChoice) cd2).getTemporalElement()).getFrom());
//                    }
//                    tm.askQuery(distance);
//                    if (distance.getMin() == distance.getMax() && distance.getMax() == 0) {
//                        representation += ids.get(cd1) + " MEETS " + ids.get(cd2);
//                        representation += ";\n";
//                        break;
//                    }
//                }
//            }
//        }
//
//        // Generate BEFORE constraints...
//        for (int i = 0; i < entries.length - 1; i++) {
//            Entry<Component, List<ComponentDecision>> entry1 = entries[i];
//            List<ComponentDecision> cds1 = entry1.getValue();
//            for (int j = 0; j < cds1.size() - 1; j++) {
//                for (int k = 1; k < entries.length; k++) {
//                    Entry<Component, List<ComponentDecision>> entry2 = entries[i + 1];
//                    List<ComponentDecision> cds2 = entry2.getValue();
//                    for (int l = 1; l < cds2.size(); l++) {
//                        ComponentDecision cd1 = cds1.get(j);
//                        ComponentDecision cd2 = cds2.get(l);
//                        if(cd1 instanceof SimpleGroundValueChoice) {
//                            distance.setTi(((TimeInterval) ((SimpleGroundValueChoice) cd1).getTemporalElement()).getTo());
//                        }
//                        if(cd1 instanceof SingletonValueChoice) {
//                            distance.setTi(((TimeInterval) ((SingletonValueChoice) cd1).getTemporalElement()).getTo());
//                        }
//                        if(cd2 instanceof SimpleGroundValueChoice) {
//                            distance.setTj(((TimeInterval) ((SimpleGroundValueChoice) cd2).getTemporalElement()).getFrom());
//                        }
//                        if(cd2 instanceof SingletonValueChoice) {
//                            distance.setTj(((TimeInterval) ((SingletonValueChoice) cd2).getTemporalElement()).getFrom());
//                        }
//                        tm.askQuery(distance);
//                        representation += ids.get(cd1) + " BEFORE [" + distance.getMin() + ", " + distance.getMax() + "]" + ids.get(cd2);
//                        representation += ";\n";
//                        break;
//                    }
//                }
//            }
//        }
//
//        return representation;
//    }
//
//    private static String getDecisionNetworkRelations(HashMap<ComponentDecision, String> ids, DecisionNetwork network) {
//        String dn = new String();
//        for (Relation rel : network.getEdges()) {
//            dn += "    ";
//            if (rel instanceof STPTemporalConstraint) {
//                switch (((STPTemporalConstraint) rel).getTemporalType()) {
//                    case Equals:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " EQUALS " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case Before:
//                    	if(((STPTemporalConstraint) rel).getFrom().getReasoner() != ((STPTemporalConstraint) rel).getTo().getReasoner() || ((STPTemporalConstraint) rel).getFirstLowerBound() != 0) {
//                            dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " BEFORE " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                    	}
//                        break;
//                    case After:
//                    	if(((STPTemporalConstraint) rel).getFrom().getReasoner() != ((STPTemporalConstraint) rel).getTo().getReasoner() || ((STPTemporalConstraint) rel).getFirstLowerBound() != 0) {
//                    		dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " AFTER " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                    	}
//                        break;
//                    case Meets:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " MEETS " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case MetBy:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " MET-BY " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case Starts:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " STARTS " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case StartedBy:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " STARTED-BY " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case During:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " DURING " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case Contains:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " CONTAINS " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case Finishes:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " FINISHES " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case FinishedBy:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " FINISHED-BY " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case Overlaps:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " OVERLAPS " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case OverlappedBy:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " OVERLAPPED-BY " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getThirdLowerBound(), ((STPTemporalConstraint) rel).getThirdUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case StartStart:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " START-START " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case AtStart:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " AT-START " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case AtEnd:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " AT-END " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case BeforeStart:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " BEFORE-START " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case AfterEnd:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " AFTER-END " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case StartAt:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " START-AT " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case EndAt:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " END-AT " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case ContainsStart:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " CONTAINS-START " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case ContainsEnd:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " CONTAINS-END " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case StartsDuring:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " STARTS-DURING " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                    case EndsDuring:
//                        dn += ids.get(((STPTemporalConstraint) rel).getFrom()) + " ENDS-DURING " + getBound(((STPTemporalConstraint) rel).getFirstLowerBound(), ((STPTemporalConstraint) rel).getFirstUpperBound()) + " " + getBound(((STPTemporalConstraint) rel).getSecondLowerBound(), ((STPTemporalConstraint) rel).getSecondUpperBound()) + " " + ids.get(((STPTemporalConstraint) rel).getTo());
//                        break;
//                }
//            }
//            if (rel instanceof ParameterRelation) {
//                ParameterConstraint constraint = ((ParameterRelation) rel).getAssertion();
//                if (constraint instanceof BinaryParameterConstraint) {
//                    dn += ((BinaryParameterConstraint) constraint).getRefVar().getName();
//                    switch (((BinaryParameterConstraint) constraint).getConstraintType()) {
//                        case Equals:
//                            dn += " = ";
//                            break;
//                        case Less:
//                            dn += " < ";
//                            break;
//                        case LessEq:
//                            dn += " <= ";
//                            break;
//                        case Greater:
//                            dn += " > ";
//                            break;
//                        case GreaterEq:
//                            dn += " >= ";
//                            break;
//                        case NotEquals:
//                            dn += " != ";
//                            break;
//                    }
//                    dn += ((BinaryParameterConstraint) constraint).getTargetVar().getParameters().get(0).getName();
//                }
//                if (constraint instanceof LinearParameterConstraint) {
//                    throw new UnsupportedOperationException("Not supported yet...");
//                }
//            }
//            dn += ";\n";
//        }
//        dn = dn.replace(", }", "}");
//        dn = dn.replace(", )", ")");
//        return dn;
//    }
//
//    private static String getBound(long lower_bound, long upper_bound) {
//        String bound = "[";
//        if (lower_bound == Long.MIN_VALUE + 1) {
//            bound += "-INF";
//        } else {
//            bound += lower_bound;
//        }
//        bound += ", ";
//        if (upper_bound == Long.MAX_VALUE - 1) {
//            bound += "+INF";
//        } else {
//            bound += upper_bound;
//        }
//        bound += "]";
//        return bound;
//    }
//
//    private static final class AssertionComparator implements Comparator<Assertion> {
//
//        private final SimpleDomain domain;
//
//        public AssertionComparator(SimpleDomain domain) {
//            this.domain = domain;
//        }
//
//        @Override
//        public int compare(Assertion cd1, Assertion cd2) throws RuntimeException {
//            TemporalModule tm = domain.getTemporalModule();
//
//            if (cd1 instanceof TRFComponentDecision && cd2 instanceof TRFComponentDecision) {
//                TPDistance bef = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//                bef.setTi(((TimeInterval) ((TRFComponentDecision) cd1).getTemporalElement()).getTo());
//                bef.setTj(((TimeInterval) ((TRFComponentDecision) cd2).getTemporalElement()).getFrom());
//
//                tm.askQuery(bef);
//
//                if (bef.getMin() >= 0) {
//                    return -1;
//                }
//
//                TPDistance aft = (TPDistance) tm.createElementQuery(TemporalElementQueryTypeEnhanced.TIME_POINT_DISTANCE);
//                aft.setTi(((TimeInterval) ((TRFComponentDecision) cd2).getTemporalElement()).getTo());
//                aft.setTj(((TimeInterval) ((TRFComponentDecision) cd1).getTemporalElement()).getFrom());
//
//                tm.askQuery(aft);
//
//                if (aft.getMin() >= 0) {
//                    return 1;
//                }
//            }
//
//            throw new RuntimeException("[OMPSBinaryScheduler] Not ordered decisions: " + cd1 + " " + cd2 + "\nThis method should be called only among completely ordered decisions");
//        }
//    }
//
//    public enum RepresentationStyle {
//	
//	Timeline,
//	DecisionNetwork;
//    }
}