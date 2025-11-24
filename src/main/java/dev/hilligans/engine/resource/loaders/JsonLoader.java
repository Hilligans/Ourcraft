package dev.hilligans.engine.resource.loaders;

import dev.hilligans.engine.optimization.AsciiBufferReader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.nio.ByteBuffer;

public class JsonLoader extends ResourceLoader<JSONObject> {

    public JsonLoader() {
        super("json_loader", "configuration");
        withFileTypes("json");
    }

    @Override
    public JSONObject read(ByteBuffer buffer) {
        return new JSONObject(new JSONTokener(new AsciiBufferReader(buffer)));
    }

    @Override
    public ByteBuffer write(JSONObject jsonObject) {
        return toByteBuffer(jsonObject.toString());
    }
}
