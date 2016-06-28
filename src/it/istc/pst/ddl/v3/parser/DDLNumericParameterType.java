package it.istc.pst.ddl.v3.parser;

import org.antlr.runtime.Token;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLNumericParameterType extends DDLParameterType {

//    private long lowerBound;
//    private long upperBound;
	private int lowerBound;
	private int upperBound;

    public DDLNumericParameterType(Token payload) {
        super(payload, DDLParameterConstraintType.NUMERIC);
    }

    @Override
    void parse() {
        name = getText();
//        lowerBound = Long.parseLong(getChild(0).getText());
        lowerBound = Integer.parseInt(getChild(0).getText());
        if (getChild(0).getChildCount() > 0 && getChild(0).getChild(0).getText().equals("-")) {
            lowerBound = -lowerBound;
        }
//        upperBound = Long.parseLong(getChild(1).getText());
        upperBound = Integer.parseInt(getChild(1).getText());
        if (getChild(1).getChildCount() > 0 && getChild(1).getChild(0).getText().equals("-")) {
            upperBound = -upperBound;
        }
    }

    /**
     * @return the lowerBound
     */
    public int getLowerBound() {
        return lowerBound;
    }

    /**
     * @return the upperBound
     */
    public int getUpperBound() {
        return upperBound;
    }
}
