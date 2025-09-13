package dev.hilligans.ourcraft.client.rendering.layout.parser;

import dev.hilligans.ourcraft.client.rendering.layout.LWidget;
import org.json.JSONObject;

public class SimpleWidgetParser implements IWidgetParser {
    
    @Override
    public LWidget parse(JSONObject obj) {
        return switch (obj.getString("key")) {
            //case "ourcraft:LText" -> new LText.LTextIdentity(obj);
            //case "ourcraft:LList" -> new LList.LListIdentity(obj);
            case "" -> null;
            default -> throw new IllegalStateException("Unexpected value: " + obj.getString("key"));
        };
    }
}
