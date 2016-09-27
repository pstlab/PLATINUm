package it.istc.pst.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLParameterSet extends CommonTree {

    private final List<String> parameters = new ArrayList<String>();

    public DDLParameterSet(int t_type) {
    }

    void parse() {
        for (int i = 3; i < getChildCount(); i++) {
            if (getChild(i).getType() == ddl3Lexer.ID) {
                parameters.add(getChild(i).getText());
            }
        }
    }

    /**
     * @return the parameters
     */
    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
