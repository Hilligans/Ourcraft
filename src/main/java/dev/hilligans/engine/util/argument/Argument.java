package dev.hilligans.engine.util.argument;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.util.Ansi;
import dev.hilligans.engine.util.Side;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Argument<T> {
    public Class<T> clazz;
    public T defaultValue;
    public Function<String, T> parseFunction;
    public String[] keys;
    public List<String> acceptedValues;
    public String helpString = "";
    public String acceptedValuesString;

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

    public Argument<T> acceptedValuesString(String defaultValueString) {
        this.acceptedValuesString = defaultValueString;
        return this;
    }

    public Argument<T> acceptedValues(List<String> acceptedValues) {
        this.acceptedValues = acceptedValues;
        if(Ansi.USE_ANSI) {
            ArrayList<String> strings = new ArrayList<>();
            for(String s : acceptedValues) {
                if(s.equals(defaultValue.toString())) {
                    strings.add(Ansi.ANSI_BOLD + s + Ansi.ANSI_RESET);
                } else {
                    strings.add(s);
                }
            }
            acceptedValues = strings;
        }
        acceptedValuesString = acceptedValues.toString();
        return this;
    }

    public Argument<T> acceptedValues(String... acceptedValues) {
        return acceptedValues(List.of(acceptedValues));
    }

    public String getAcceptedValuesString(GameInstance gameInstance) {
        return acceptedValuesString;
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
        return new Argument<>(String.class, defaultValue, (str) -> str, key).acceptedValuesString("<str>");
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
        return new Argument<>(Integer.class, defaultValue, Integer::parseInt, key).acceptedValuesString("<int>");
    }

    public static Argument<Float> floatArg(String key, float defaultValue) {
        return new Argument<>(Float.class, defaultValue, Float::parseFloat, key).acceptedValuesString("<float>");
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

            @Override
            public String getAcceptedValuesString(GameInstance gameInstance) {
                Registry<T> registry = gameInstance.getRegistry(clazz);
                if(registry == null) {
                    return super.getAcceptedValuesString(gameInstance);
                }
                ArrayList<String> list = new ArrayList<>();
                if(Ansi.USE_ANSI) {
                    registry.forEach((val) -> {
                        String str = val.getIdentifierName();
                        if(str.equals(strArg.defaultValue)) {
                            list.add(Ansi.ANSI_BOLD + val.getIdentifierName() + Ansi.ANSI_RESET);
                        } else {
                            list.add(val.getIdentifierName());
                        }
                    });
                } else {
                    registry.forEach((val) -> list.add(val.getIdentifierName()));
                }
                return list.toString();
            }
        }.acceptedValuesString("<mod_id:value>");
    }

    public static <T extends IRegistryElement> Argument<T[]> arrayRegistryArg(String key, Class<T> Clazz, String defaultValue) {
        Argument<String> strArg = stringArg(key, defaultValue);

        return new Argument<>((Class<T[]>) Clazz.arrayType(), null, null, key) {
            @Override
            public T[] get(ArgumentContainer argumentContainer) {
                throw new UnsupportedOperationException("Must use get with GameInstance when looking up registry elements!");
            }

            @Override
            public T[] get(GameInstance gameInstance) {
                String key = strArg.get(gameInstance);
                if (key.equals("all")) {
                    return gameInstance.getRegistry(Clazz).getArray();
                }

                Registry<T> registry = gameInstance.getRegistry(Clazz);
                String[] vals = key.split(",");
                T[] elem = (T[]) Array.newInstance(Clazz, vals.length);
                for(int x = 0; x < vals.length; x++) {
                    elem[x] = registry.getExcept(vals[x]);
                }

                return elem;
            }

            @Override
            public String getAcceptedValuesString(GameInstance gameInstance) {
                Registry<T> registry = gameInstance.getRegistry(Clazz);
                if (registry == null) {
                    return super.getAcceptedValuesString(gameInstance);
                }
                ArrayList<String> list = new ArrayList<>();
                if (Ansi.USE_ANSI) {
                    registry.forEach((val) -> {
                        String str = val.getIdentifierName();
                        if (str.equals(strArg.defaultValue)) {
                            list.add(Ansi.ANSI_BOLD + val.getIdentifierName() + Ansi.ANSI_RESET);
                        } else {
                            list.add(val.getIdentifierName());
                        }
                    });
                } else {
                    registry.forEach((val) -> list.add(val.getIdentifierName()));
                }
                return list.toString();
            }
        }.acceptedValuesString("<mod_id:value,mod_id:value>|<all>");
    }
}
