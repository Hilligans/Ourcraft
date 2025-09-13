package dev.hilligans.engine.resource.loaders;

import org.json.JSONObject;

import java.nio.ByteBuffer;

public class JsonLoader extends ResourceLoader<JSONObject> {

    public JsonLoader() {
        super("json_loader", "configuration");
        withFileTypes("json");
    }

    @Override
    public JSONObject read(ByteBuffer buffer) {
        return new JSONObject(toString(buffer));
    }

    @Override
    public ByteBuffer write(JSONObject jsonObject) {
        return toByteBuffer(jsonObject.toString());
    }
}
