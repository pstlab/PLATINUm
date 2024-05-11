package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public abstract class DDLComponentType extends CommonTree {

    protected String name;

    DDLComponentType(Token payload) {
        super(payload);
    }

    abstract void parse();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
