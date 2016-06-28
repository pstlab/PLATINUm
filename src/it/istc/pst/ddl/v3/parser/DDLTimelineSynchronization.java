package it.istc.pst.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLTimelineSynchronization extends CommonTree {

    private String component;
    private String timeline;
    private final List<DDLSynchronization> synchronizations = new ArrayList<DDLSynchronization>();

    public DDLTimelineSynchronization(Token payload) {
        super(payload);
    }

    void parse() {
        component = getChild(0).getText();
        timeline = getChild(1).getText();
        for (int i = 2; i < getChildCount(); i++) {
            DDLSynchronization sync = (DDLSynchronization) getChild(i);
            sync.parse();
            synchronizations.add(sync);
        }
    }

    /**
     * @return the component
     */
    public String getComponent() {
        return component;
    }

    /**
     * @return the timeline
     */
    public String getTimeline() {
        return timeline;
    }

    /**
     * @return the synchronizations
     */
    public List<DDLSynchronization> getSynchronizations() {
        return Collections.unmodifiableList(synchronizations);
    }
}
