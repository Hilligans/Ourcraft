package dev.hilligans.ourcraft.client.rendering.layout;

import org.json.JSONObject;

// to be value class
public record WidgetPosition(int x, int y) {

    public WidgetPosition(JSONObject obj) {
        this(obj.getInt("x"), obj.getInt("y"));
    }

    public void serialize(JSONObject obj) {
        obj.put("x", x);
        obj.put("y", y);
    }
}
