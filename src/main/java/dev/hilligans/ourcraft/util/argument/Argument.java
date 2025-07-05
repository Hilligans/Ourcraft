package dev.hilligans.ourcraft.util.argument;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.Side;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Argument<T> {
    public Class<T> clazz;
    public T defaultValue;
    public String defaultVal;
    public Function<String, T> parseFunction;
    public String[] keys;
    public List<String> acceptedValues;
    public String helpString = "";
    public String defaultAcceptedValuesString;

    public Argument(Class<T> clazz, T defaultValue, Function<String, T> parseFunction, String... keys) {
        this.clazz = clazz;
        this.defaultValue = defaultValue;
        this.parseFunction = parseFunction;
        this.keys = keys;
    }

    public Argument<T> help(String helpString) {
        this.helpString = helpString;
        return this;
    }

    public Argument<T> defaultAcceptedValue(String defaultValueString) {
        this.defaultAcceptedValuesString = defaultValueString;
        return this;
    }

    public Argument<T> acceptedValues(List<String> acceptedValues) {
        this.acceptedValues = acceptedValues;
        defaultAcceptedValuesString = acceptedValues.toString();
        return this;
    }

    public Argument<T> acceptedValues(String... acceptedValues) {
        return acceptedValues(List.of(acceptedValues));
    }

    public T get(ArgumentContainer argumentContainer) {
        for(String key : keys) {
            String arg = argumentContainer.getString(key, null);
            if(arg != null) {
                if(acceptedValues != null && !acceptedValues.contains(arg)) {
                    throw new RuntimeException("Argument " + arg + " is not accepted by " + clazz.getSimpleName() + " " + key);
                }
                return parseFunction.apply(arg);
            }
        }

        return defaultValue;
    }

    public T get(GameInstance gameInstance) {
        return get(gameInstance.getArgumentContainer());
    }

    public static Argument<String> stringArg(String key, String defaultValue) {
        return new Argument<>(String.class, defaultValue, (str) -> str, key).defaultAcceptedValue("<str>");
    }

    public static Argument<Boolean> booleanArg(String key, boolean defaultValue) {
        return new Argument<>(Boolean.class, defaultValue, Boolean::parseBoolean, key).acceptedValues("true", "false");
    }

    public static Argument<Boolean> existArg(String key) {
        return new Argument<>(Boolean.class, false, null, key) {
            @Override
            public Boolean get(ArgumentContainer argumentContainer) {
                for(String s : keys) {
                    if(argumentContainer.exists(s)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Argument<Integer> integerArg(String key, int defaultValue) {
        return new Argument<>(Integer.class, defaultValue, Integer::parseInt, key).defaultAcceptedValue("<int>");
    }

    public static Argument<Float> floatArg(String key, float defaultValue) {
        return new Argument<>(Float.class, defaultValue, Float::parseFloat, key).defaultAcceptedValue("<float>");
    }

    public static Argument<Side> sideArg(String key, Side defaultValue) {
        return new Argument<>(Side.class, defaultValue, Side::parseSide, key).acceptedValues(Arrays.stream(Side.values()).map((s) -> s.name).toList());
    }

    public static <T extends IRegistryElement> Argument<T> registryArg(String key, Class<T> clazz, String defaultValue) {
        Argument<String> strArg = stringArg(key, defaultValue);

        return new Argument<>(clazz, null, null, key) {
            @Override
            public T get(ArgumentContainer argumentContainer) {
                throw new UnsupportedOperationException("Must use get with GameInstance when looking up registry elements!");
            }

            @Override
            public T get(GameInstance gameInstance) {
                return gameInstance.getExcept(strArg.get(gameInstance), clazz);
            }
        }.defaultAcceptedValue("<mod_id:value>");
    }
}
