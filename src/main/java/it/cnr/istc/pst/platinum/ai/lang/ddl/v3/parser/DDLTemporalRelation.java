package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLTemporalRelation extends CommonTree {

    private String from;
    private String to;
    private DDLTemporalRelationType relationType;

    public DDLTemporalRelation(Token payload) {
        super(payload);
    }

    void parse() {
        relationType = (DDLTemporalRelationType) getChild(0);
        relationType.parse();
        to = getChild(1).getText();
        if (getChildCount() > 2) {
            from = getChild(2).getText();
        }
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * @return the relationType
     */
    public DDLTemporalRelationType getRelationType() {
        return relationType;
    }
}
