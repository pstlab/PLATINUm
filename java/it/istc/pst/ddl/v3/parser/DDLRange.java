package it.istc.pst.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLRange extends CommonTree {

    private long min;
    private long max;

    public DDLRange(Token payload) {
        super(payload);
    }

    void parse() {
        min = getChild(0).getText().equals("INF") ? Long.MAX_VALUE - 1 : Long.parseLong(getChild(0).getText());
        max = getChild(1).getText().equals("INF") ? Long.MAX_VALUE - 1 : Long.parseLong(getChild(1).getText());
    }

    /**
     * @return the min
     */
    public long getMin() {
        return min;
    }

    /**
     * @return the max
     */
    public long getMax() {
        return max;
    }

    @Override
    public String toString() {
        String lb = min == (Long.MAX_VALUE - 1) ? "+INF" : Long.toString(min);
        String ub = max == (Long.MAX_VALUE - 1) ? "+INF" : Long.toString(max);
        return "[" + lb + ", " + ub + ']';
    }
}
