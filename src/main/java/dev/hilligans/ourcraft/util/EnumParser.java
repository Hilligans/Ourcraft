package dev.hilligans.ourcraft.util;

public class EnumParser {

    public static <T extends Enum<T>> T parse(String val, Class<T> clazz) {
        for(T en : clazz.getEnumConstants()) {
            if(en.name().equals(val)) {
                return en;
            }
        }
        throw new RuntimeException("Could not parse " + val + " in " + clazz);
    }
}
