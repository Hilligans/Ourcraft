package dev.hilligans.engine.schema;

import dev.hilligans.engine.util.Array;
import dev.hilligans.engine.util.registry.IRegistryElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public interface Schema<T> extends IRegistryElement {

    Data getSchema(T data);

    default Data get(Object o) {
        return getSchema((T)o);
    }

    Class<T> getSchemaClass();

    default String getResourceType() {
        return "schema";
    }

    interface Data {
        String getString(String key);
        String optString(String key, String value);

        int getInt(String key);
        int optInt(String key, int def);
        long getLong(String key);
        long optLong(String key, long def);

        float getFloat(String key);
        float optFloat(String key, float def);
        double getDouble(String key);
        double optDouble(String key, double val);

        boolean getBoolean(String key);
        boolean optBoolean(String key, boolean def);

        Data getObject(String key);

        Array<Data> getObjects(String key);
    }
}
