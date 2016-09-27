package it.istc.pst.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLSimpleGroundStateVariableComponentDecisionType extends CommonTree {

    private String name;

    public DDLSimpleGroundStateVariableComponentDecisionType(Token payload) {
        super(payload);
    }

    void parse() {
        name = getText();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
