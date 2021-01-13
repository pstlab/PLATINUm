package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLDomain extends CommonTree {

    private String name;
    private DDLTemporalModule temporalModule;
    private final Map<String, DDLParameterType> parTypes = new LinkedHashMap<String, DDLParameterType>();
    private final Map<String, DDLComponentType> compTypes = new LinkedHashMap<String, DDLComponentType>();
    private final Map<String, DDLComponent> components = new LinkedHashMap<String, DDLComponent>();
    private final List<DDLTimelineSynchronization> timelineSynchronizations = new ArrayList<DDLTimelineSynchronization>();

    public DDLDomain(Token payload) {
        super(payload);
    }

    public void parse() {
        name = getText();
        for (int i = 0; i < getChildCount(); i++) {
            Tree child = getChild(i);
            if (child instanceof DDLTemporalModule) {
                temporalModule = (DDLTemporalModule) getChild(0);
                temporalModule.parse();
                continue;
            }
            if (child instanceof DDLParameterType) {
                DDLParameterType par_type = ((DDLParameterType) child);
                par_type.parse();
                if (parTypes.containsKey(par_type.getName())) {
                    throw new RuntimeException("Duplicate parameter type name in domain definition");
                }
                parTypes.put(par_type.getName(), par_type);
                continue;
            }
            if (child instanceof DDLComponentType) {
                DDLComponentType comp_type = ((DDLComponentType) child);
                comp_type.parse();
                if (parTypes.containsKey(comp_type.getName())) {
                    throw new RuntimeException("Duplicate component type name in domain definition");
                }
                compTypes.put(comp_type.getName(), comp_type);
                continue;
            }
            if (child instanceof DDLComponent) {
                DDLComponent comp = ((DDLComponent) child);
                comp.parse();
                if (parTypes.containsKey(comp.getName())) {
                    throw new RuntimeException("Duplicate component name in domain definition");
                }
                components.put(comp.getName(), comp);
                continue;
            }
            if (child instanceof DDLTimelineSynchronization) {
                DDLTimelineSynchronization syncs = ((DDLTimelineSynchronization) child);
                syncs.parse();
                timelineSynchronizations.add(syncs);
                continue;
            }
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the range
     */
    public DDLTemporalModule getTemporalModule() {
        return temporalModule;
    }

    /**
     * @return the parTypes
     */
    public Map<String, DDLParameterType> getParameterTypes() {
        return Collections.unmodifiableMap(parTypes);
    }

    /**
     * @return the compTypes
     */
    public Map<String, DDLComponentType> getComponentTypes() {
        return Collections.unmodifiableMap(compTypes);
    }

    /**
     * @return the components
     */
    public Map<String, DDLComponent> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    /**
     * @return the synchronizations
     */
    public List<DDLTimelineSynchronization> getTimelineSynchronizations() {
        return Collections.unmodifiableList(timelineSynchronizations);
    }
}
