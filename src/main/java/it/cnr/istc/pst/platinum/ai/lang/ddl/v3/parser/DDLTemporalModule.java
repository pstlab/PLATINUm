package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLTemporalModule extends CommonTree {

    private String name;
    private DDLRange range;
    private int maxPoints;

    public DDLTemporalModule(Token payload) {
        super(payload);
    }

    void parse() {
        name = getText();
        range = (DDLRange) getChild(0);
        range.parse();
        maxPoints = Integer.parseInt(getChild(1).getText());
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
    public DDLRange getRange() {
        return range;
    }

    /**
     * @return the maxPoints
     */
    public int getMaxPoints() {
        return maxPoints;
    }
}
