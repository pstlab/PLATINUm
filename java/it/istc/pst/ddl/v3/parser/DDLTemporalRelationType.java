package it.istc.pst.ddl.v3.parser;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLTemporalRelationType extends CommonTree {

    private DDLRange firstRange;
    private DDLRange secondRange;
    private TemporalRelationType relationType;

    public DDLTemporalRelationType(Token payload) {
        super(payload);
    }

    void parse() 
    {
        String relation_type = getText();
        if (relation_type.equals("MEETS")) {
            relationType = TemporalRelationType.MEETS;
            return;
        }
        if (relation_type.equals("MET-BY")) {
            relationType = TemporalRelationType.MET_BY;
            return;
        }
        if (relation_type.equals("BEFORE")) {
            relationType = TemporalRelationType.BEFORE;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("AFTER")) {
            relationType = TemporalRelationType.AFTER;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("EQUALS")) {
            relationType = TemporalRelationType.EQUALS;
            return;
        }
        if (relation_type.equals("STARTS")) {
            relationType = TemporalRelationType.STARTS;
            return;
        }
        if (relation_type.equals("STARTED-BY")) {
            relationType = TemporalRelationType.STARTED_BY;
            return;
        }
        if (relation_type.equals("FINISHES")) {
            relationType = TemporalRelationType.FINISHES;
            return;
        }
        if (relation_type.equals("FINISHED-BY")) {
            relationType = TemporalRelationType.FINISHED_BY;
            return;
        }
        if (relation_type.equals("DURING")) {
            relationType = TemporalRelationType.DURING;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            secondRange = (DDLRange) getChild(1);
            secondRange.parse();
            return;
        }
        if (relation_type.equals("CONTAINS")) {
            relationType = TemporalRelationType.CONTAINS;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            secondRange = (DDLRange) getChild(1);
            secondRange.parse();
            return;
        }
        if (relation_type.equals("OVERLAPS")) {
            relationType = TemporalRelationType.OVERLAPS;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("OVERLAPPED-BY")) {
            relationType = TemporalRelationType.OVERLAPPED_BY;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("STARTS-AT")) {
            relationType = TemporalRelationType.STARTS_AT;
            return;
        }
        if (relation_type.equals("ENDS-AT")) {
            relationType = TemporalRelationType.ENDS_AT;
            return;
        }
        if (relation_type.equals("AT-START")) {
            relationType = TemporalRelationType.AT_START;
            return;
        }
        if (relation_type.equals("AT_END")) {
            relationType = TemporalRelationType.AT_END;
            return;
        }
        if (relation_type.equals("BEFORE-START")) {
            relationType = TemporalRelationType.BEFORE_START;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("AFTER-END")) {
            relationType = TemporalRelationType.AFTER_END;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("START-START")) {
            relationType = TemporalRelationType.START_START;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("START-END")) {
            relationType = TemporalRelationType.START_END;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("END-START")) {
            relationType = TemporalRelationType.END_START;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("END-END")) {
            relationType = TemporalRelationType.END_END;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            return;
        }
        if (relation_type.equals("CONTAINS-START")) {
            relationType = TemporalRelationType.CONTAINS_START;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            secondRange = (DDLRange) getChild(1);
            secondRange.parse();
            return;
        }
        if (relation_type.equals("CONTAINS-END")) {
            relationType = TemporalRelationType.CONTAINS_END;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            secondRange = (DDLRange) getChild(1);
            secondRange.parse();
            return;
        }
        if (relation_type.equals("STARTS-DURING")) {
            relationType = TemporalRelationType.STARTS_DURING;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            secondRange = (DDLRange) getChild(1);
            secondRange.parse();
            return;
        }
        if (relation_type.equals("ENDS-DURING")) {
            relationType = TemporalRelationType.ENDS_DURING;
            firstRange = (DDLRange) getChild(0);
            firstRange.parse();
            secondRange = (DDLRange) getChild(1);
            secondRange.parse();
            return;
        }
    }

    /**
     * @return the firstRange
     */
    public DDLRange getFirstRange() {
        return firstRange;
    }

    /**
     * @return the secondRange
     */
    public DDLRange getSecondRange() {
        return secondRange;
    }

    /**
     * @return the relationType
     */
    public TemporalRelationType getRelationType() {
        return relationType;
    }

    public enum TemporalRelationType {

        MEETS,
        MET_BY,
        BEFORE,
        AFTER,
        EQUALS,
        STARTS,
        STARTED_BY,
        FINISHES,
        FINISHED_BY,
        DURING,
        CONTAINS,
        OVERLAPS,
        OVERLAPPED_BY,
        STARTS_AT,
        ENDS_AT,
        AT_START,
        AT_END,
        BEFORE_START,
        AFTER_END,
        START_START,
        START_END,
        END_START,
        END_END,
        CONTAINS_START,
        CONTAINS_END,
        STARTS_DURING,
        ENDS_DURING;
    }
}
