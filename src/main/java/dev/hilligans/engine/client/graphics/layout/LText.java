package dev.hilligans.engine.client.graphics.layout;

import org.json.JSONObject;

public non-sealed class LText extends LWidget {

    String text;
    boolean richText;
    boolean renderOutline;

    public LText() {
    }

    public LText(String text) {
        this.text = text;
    }

    public String getText() {
        return text == null ? "" : text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void serialize(JSONObject json) {
        super.serialize(json);
        json.put("text", text);
        json.put("richText", richText);
    }

    @Override
    public void recalculate() {
    }
}
