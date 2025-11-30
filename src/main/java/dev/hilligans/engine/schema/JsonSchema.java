package dev.hilligans.engine.schema;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.Array;
import dev.hilligans.engine.util.LazyArray;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonSchema implements Schema<JSONObject> {

    public static final JsonSchema instance = new JsonSchema();

    public JsonSchema() {
        track();
    }

    @Override
    public String getResourceName() {
        return "json_schema";
    }

    @Override
    public String getResourceOwner() {
        return Engine.ENGINE_NAME;
    }

    @Override
    public Data getSchema(JSONObject data) {
        return new JsonData(data);
    }

    @Override
    public Class<JSONObject> getSchemaClass() {
        return JSONObject.class;
    }

    static class JsonData implements Schema.Data {

        public JSONObject jsonObject;

        public JsonData(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public String getString(String key) {
            return jsonObject.getString(key);
        }

        @Override
        public String optString(String key, String value) {
            return jsonObject.optString(key, value);
        }

        @Override
        public int getInt(String key) {
            return jsonObject.getInt(key);
        }

        @Override
        public int optInt(String key, int def) {
            return jsonObject.optInt(key, def);
        }

        @Override
        public float getFloat(String key) {
            return jsonObject.getFloat(key);
        }

        @Override
        public float optFloat(String key, float def) {
            return jsonObject.optFloat(key, def);
        }

        @Override
        public boolean getBoolean(String key) {
            return jsonObject.getBoolean(key);
        }

        @Override
        public boolean optBoolean(String key, boolean def) {
            return jsonObject.optBoolean(key, def);
        }

        @Override
        public long getLong(String key) {
            return jsonObject.getLong(key);
        }

        @Override
        public long optLong(String key, long def) {
            return jsonObject.optLong(key, def);
        }

        @Override
        public double getDouble(String key) {
            return jsonObject.getDouble(key);
        }

        @Override
        public double optDouble(String key, double val) {
            return jsonObject.optDouble(key, val);
        }

        @Override
        public Data getObject(String key) {
            return new JsonData(jsonObject.getJSONObject(key));
        }

        @Override
        public Data optObject(String key, Data def) {
            return jsonObject.has(key) ? getObject(key) : def;
        }

        @Override
        public Array<Data> getObjects(String key) {
            JSONArray array = jsonObject.getJSONArray(key);
            return new LazyArray<>(array.length(), (index) -> new JsonData(array.getJSONObject(index)), (integer, jsonData) -> array.put(integer, ((JsonData)jsonData).jsonObject));
        }

        @Override
        public Array<Data> optObjects(String key, Array<Data> def) {
            return jsonObject.has(key) ? getObjects(key) : def;
        }

        @Override
        public List<String> getKeys() {
            return jsonObject.keySet().stream().toList();
        }
    }
}
