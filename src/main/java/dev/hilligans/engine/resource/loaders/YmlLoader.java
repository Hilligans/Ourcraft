package dev.hilligans.engine.resource.loaders;

import org.json.JSONObject;

import java.nio.ByteBuffer;

public class YmlLoader extends ResourceLoader<JSONObject> {

    public YmlLoader() {
        super("yml_loader", "configuration");
    }

    @Override
    public JSONObject read(ByteBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer write(JSONObject jsonObject) {
        return null;
    }
}
