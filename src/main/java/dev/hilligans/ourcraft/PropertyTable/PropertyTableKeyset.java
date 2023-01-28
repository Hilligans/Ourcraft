package dev.hilligans.ourcraft.PropertyTable;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.ArrayList;

public class PropertyTableKeyset {

    public int length;
    public ArrayList<String> methodProperties = new ArrayList<>();
    public ArrayList<String> methods = new ArrayList<>();
    public Object2IntOpenHashMap<String> keyset = new Object2IntOpenHashMap<>();

    public void addProperty(String property) {
        this.methodProperties.add(property);
    }

    public void addMethod(String name) {
        methods.add(name);
    }

    public PropertyTableKeyset build() {
        methods.sort(String::compareTo);
        methodProperties.sort(String::compareTo);
        length = 0;
        for(String string : methods) {
            for(String property : methodProperties) {
                keyset.put(string + ":" + property, length++);
            }
        }
        return this;
    }

    public int getOffset(String key) {
        return keyset.getOrDefault(key,-1);
    }

    public int getArrayLength() {
        return (int) Math.ceil(length / 64.0);
    }
}
