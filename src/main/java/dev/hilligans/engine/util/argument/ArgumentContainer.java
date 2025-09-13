package dev.hilligans.engine.util.argument;

import java.util.HashMap;
import java.util.HashSet;

public class ArgumentContainer {

    public HashMap<String, String> arguments = new HashMap<>();
    public HashSet<String> tasks = new HashSet<>();

    public ArgumentContainer(String... args) {
        handle(args);
    }

    public void handle(String... args) {
        for(String string : args) {
            String[] parts = string.split("=",2);
            if(parts.length != 1) {
                arguments.put(parts[0],parts[1]);
            } else {
                tasks.add(parts[0]);
            }
        }
    }

    public boolean exists(String key) {
        return tasks.contains(key);
    }

    public String getString(String key, String defaultValue) {
        return arguments.getOrDefault(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        if(arguments.containsKey(key)) {
            String arg = arguments.get(key);
            try {
                defaultValue = Integer.parseInt(arg);
            } catch (NumberFormatException ignored) {}
        }
        return defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if(arguments.containsKey(key)) {
            String arg = arguments.get(key);
            defaultValue = Boolean.parseBoolean(arg);
        }
        return defaultValue;
    }
}
