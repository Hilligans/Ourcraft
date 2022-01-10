package dev.Hilligans.ourcraft.Resource.Loaders;

import dev.Hilligans.ourcraft.Resource.ResourceLoader;
import groovy.toml.TomlBuilder;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
