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
public class DDLComponent extends CommonTree {

    private String name;
    private String componentType;
    private final List<DDLTimeline> timelines = new ArrayList<DDLTimeline>();

    public DDLComponent(Token payload) {
        super(payload);
    }

    void parse() {
        name = getText();
        componentType = getChild(0).getText();
        for (int i = 1; i < getChildCount(); i++) {
            DDLTimeline timeline = (DDLTimeline) getChild(i);
            timeline.parse();
            timelines.add(timeline);
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the componentType
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * @return the timelines
     */
    public List<DDLTimeline> getTimelines() {
        return Collections.unmodifiableList(timelines);
    }
}
