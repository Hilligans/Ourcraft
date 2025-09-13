package dev.hilligans.engine.util.argument;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.content.ModList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ArgumentSearcher {

    public static String findAllArguments(GameInstance gameInstance) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            ModList list = gameInstance.MOD_LIST.load();
            for(Class<?> clazz : list.classList) {
                boolean spaced = false;
                for(Field field : clazz.getFields()) {
                    if(!Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    if(Argument.class.isAssignableFrom(field.getType())) {
                        spaced = true;
                        Argument<?> arg = (Argument<?>) field.get(null);
                        for(String s : arg.keys) {
                            stringBuilder.append('\t').append(s);
                            String acceptedValues = arg.getAcceptedValuesString(gameInstance);
                            if(acceptedValues != null) {
                                stringBuilder.append("=").append(acceptedValues);
                            }
                            stringBuilder.append("\n\t\t").append(arg.helpString.replace("\n", "\n\t\t")).append('\n');
                        }
                    }
                }
                if(spaced) {
                    stringBuilder.append('\n');
                }
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
