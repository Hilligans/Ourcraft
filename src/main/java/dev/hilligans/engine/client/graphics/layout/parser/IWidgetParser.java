package dev.hilligans.engine.client.graphics.layout.parser;

import dev.hilligans.engine.client.graphics.layout.LWidget;
import org.json.JSONObject;

public interface IWidgetParser {

    LWidget parse(JSONObject obj);

}
