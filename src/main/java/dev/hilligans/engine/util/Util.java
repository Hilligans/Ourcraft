package dev.hilligans.engine.util;

public class Util {

    public static <X> X identity(X val) {
        return val;
    }

    public static String toString(String[] strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String string : strings) {
            stringBuilder.append(string).append(" ");
        }
        return stringBuilder.toString();
    }
}
