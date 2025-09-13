package dev.hilligans.engine.resource.loaders;

import org.json.JSONObject;

import java.nio.ByteBuffer;

public class TomlLoader extends ResourceLoader<JSONObject> {
    public TomlLoader() {
        super("toml_loader", "configuration");
        withFileTypes("toml");
    }

    @Override
    public JSONObject read(ByteBuffer buffer) {

        return null;
    }

    @Override
    public ByteBuffer write(JSONObject jsonObject) {
        return null;
    }

    public static JSONObject parse(String toParse) {
        return null;
    }
}
