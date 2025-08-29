package dev.hilligans.ourcraft.client.rendering.layout.parser;

import dev.hilligans.ourcraft.client.rendering.layout.LWidget;
import org.json.JSONObject;

public interface IWidgetParser {

    LWidget parse(JSONObject obj);

}
