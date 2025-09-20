package dev.hilligans.engine.util;

import dev.hilligans.engine.command.ICommand;

public class EnumParser {

    public static <T extends Enum<T>> T parse(ICommand command, String val, Class<T> clazz) {
        for(T en : clazz.getEnumConstants()) {
            if(en.name().equals(val)) {
                return en;
            }
        }
        throw new RuntimeException("Could not parse " + val + " in " + clazz);
    }
}
