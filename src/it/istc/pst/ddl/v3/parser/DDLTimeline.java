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
public class DDLTimeline extends CommonTree {

    private TimelineType timelineType;
    private String name;
    private final List<String> parameters = new ArrayList<String>();

    public DDLTimeline(Token payload) {
	super(payload);
    }

    void parse() {
	String type = getText();
	if (type.equals("ESTA_LIGHT")) {
	    timelineType = TimelineType.ESTA_LIGHT;
	} else if (type.equals("BOUNDED")) {
	    timelineType = TimelineType.BOUNDED;
	} else if (type.equals("FLEXIBLE")) {
	    timelineType = TimelineType.FLEXIBLE;
	} else if (type.equals("ESTA_LIGHT_MAX_CONSUMPTION")) {
	    timelineType = TimelineType.ESTA_LIGHT_MAX_CONSUMPTION;
	}
	name = getChild(0).getText();
	for (int i = 1; i < getChildCount(); i++) {
	    parameters.add(getChild(i).getText());
	}
    }

    /**
     * @return the timelineType
     */
    public TimelineType getTimelineType() {
	return timelineType;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @return the parameters
     */
    public List<String> getParameters() {
	return Collections.unmodifiableList(parameters);
    }

    public enum TimelineType {

	ESTA_LIGHT, ESTA_LIGHT_MAX_CONSUMPTION, BOUNDED, FLEXIBLE
    }
}
