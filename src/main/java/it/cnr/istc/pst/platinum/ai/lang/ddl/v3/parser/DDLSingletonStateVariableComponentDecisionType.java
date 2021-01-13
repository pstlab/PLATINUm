package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLSingletonStateVariableComponentDecisionType extends CommonTree {

    private String name;
    private final List<String> parTypes = new ArrayList<String>();

    public DDLSingletonStateVariableComponentDecisionType(Token payload) {
        super(payload);
    }

    void parse() {
        name = getText();
        for (int i = 0; i < getChildCount(); i++) {
            parTypes.add(getChild(i).getText());
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parTypes
     */
    public List<String> getParameterTypes() {
        return Collections.unmodifiableList(parTypes);
    }
}
