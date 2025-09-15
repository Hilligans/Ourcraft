package dev.hilligans.engine.util;

import java.util.Date;
import java.util.HashMap;

public class Logger {

    public String header;
    public String rawName;

    public Logger(String header, String rawName) {
        this.header = header;
        this.rawName = rawName;
    }

    public Logger getSubCategory(String name) {
        return new Logger(name, rawName + "/" + name);
    }

    public Logger withKey(String key) {
        return getSubCategory(key + getID(key));
    }

    public void debug(String message) {
    }

    public void info(String message) {
    }

    public void warn(String message) {
    }

    public void error(String message) {
    }

    public void addTime(StringBuilder stringBuilder) {
        stringBuilder.append("[").append(new Date()).append("]");
    }

    public void addName(StringBuilder stringBuilder, String type) {
        stringBuilder.append("[").append(rawName).append("/").append(type).append("]");
    }

    public StringBuilder getMessage(String type) {
        StringBuilder stringBuilder = new StringBuilder();
        addTime(stringBuilder);
        stringBuilder.append(" ");
        addName(stringBuilder, type);
        return stringBuilder;
    }

    public static synchronized int getID(String key) {
        IHolder val = ids.computeIfAbsent(key, s -> new IHolder());
        return val.val++;
    }

    public static HashMap<String, IHolder> ids = new HashMap<>();

    static class IHolder {
        public int val = 0;
    }
}
