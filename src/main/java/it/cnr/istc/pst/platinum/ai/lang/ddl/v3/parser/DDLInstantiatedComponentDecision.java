package it.cnr.istc.pst.platinum.ai.lang.ddl.v3.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author Riccardo De Benedictis
 */
public class DDLInstantiatedComponentDecision extends CommonTree {

    private String id;
    private String component;
    private String timeline;
    private DDLComponentDecision componentDecision;
    private final List<String> parameters = new ArrayList<String>();
    private DDLRange start;
    private DDLRange end;
    private DDLRange duration;

    public DDLInstantiatedComponentDecision(Token payload) {
        super(payload);
    }

    void parse() {
        id = getText();
        component = getChild(0).getText();
        timeline = getChild(1).getText();
        componentDecision = (DDLComponentDecision) getChild(2);
        componentDecision.parse();
        for (int i = 3; i < getChildCount(); i++) {
            if (getChild(i) instanceof DDLRange) {
                start = (DDLRange) getChild(i++);
                start.parse();
                end = (DDLRange) getChild(i++);
                end.parse();
                duration = (DDLRange) getChild(i++);
                duration.parse();
            } else {
                parameters.add(getChild(i).getText());
            }
        }
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
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
     * @return the componentDecision
     */
    public DDLComponentDecision getComponentDecision() {
        return componentDecision;
    }

    /**
     * @return the start
     */
    public DDLRange getStart() {
        return start;
    }

    /**
     * @return the end
     */
    public DDLRange getEnd() {
        return end;
    }

    /**
     * @return the duration
     */
    public DDLRange getDuration() {
        return duration;
    }

    /**
     * @return the parameters
     */
    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
