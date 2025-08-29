package dev.hilligans.ourcraft.client.rendering.layout;

import org.json.JSONObject;

// to be value class
public record WidgetSize(int minWidth, int minHeight, int maxWidth, int maxHeight, int width, int height) {

    public WidgetSize(JSONObject obj) {
        int width = obj.getInt("width");
        int height = obj.getInt("height");

        this(obj.optInt("minWidth", width), obj.optInt("minHeight", height), obj.optInt("maxWidth", width), obj.optInt("maxHeight", height), width, height);
    }

    public void serialize(JSONObject obj) {
        obj.put("width", width);
        obj.put("height", height);
        obj.put("minWidth", minWidth);
        obj.put("minHeight", minHeight);
        obj.put("maxWidth", maxWidth);
        obj.put("maxHeight", maxHeight);
    }
}
