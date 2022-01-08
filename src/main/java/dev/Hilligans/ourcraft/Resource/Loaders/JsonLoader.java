package dev.Hilligans.ourcraft.Resource.Loaders;

import dev.Hilligans.ourcraft.Resource.ResourceLoader;
import org.json.JSONObject;

import java.nio.ByteBuffer;

public class JsonLoader extends ResourceLoader<JSONObject> {

    public JsonLoader() {
        super("json_loader", "configuration");
        withFileTypes("json");
    }

    @Override
    public JSONObject getResource(ByteBuffer buffer) {
        return new JSONObject(toString(buffer));
    }

    @Override
    public ByteBuffer write(JSONObject jsonObject) {
        return null;
    }
}
