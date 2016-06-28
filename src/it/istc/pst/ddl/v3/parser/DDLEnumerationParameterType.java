package it.istc.pst.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.runtime.Token;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLEnumerationParameterType extends DDLParameterType {

    private final List<String> enums = new ArrayList<String>();

    public DDLEnumerationParameterType(Token payload) {
        super(payload, DDLParameterConstraintType.ENUMERATION);
    }

    @Override
    void parse() {
        name = getText();
        for (int i = 0; i < getChildCount(); i++) {
            enums.add(getChild(i).getText());
        }
    }

    /**
     * @return the enums
     */
    public List<String> getEnums() {
        return Collections.unmodifiableList(enums);
    }
}
